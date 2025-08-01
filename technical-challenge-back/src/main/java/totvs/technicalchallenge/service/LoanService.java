package totvs.technicalchallenge.service;

import ch.obermuhlner.math.big.BigDecimalMath;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import totvs.technicalchallenge.dto.LoanResponseDto;
import totvs.technicalchallenge.dto.SimluationDto;
import totvs.technicalchallenge.model.Installment;
import totvs.technicalchallenge.model.Loan;
import totvs.technicalchallenge.repository.AbstractRepository;
import totvs.technicalchallenge.repository.LoanRepository;
import totvs.technicalchallenge.utils.DateUtils;
import totvs.technicalchallenge.utils.LoanUtils;
import totvs.technicalchallenge.utils.MonetaryUtils;

@Validated
@Service
@Slf4j
public class LoanService extends AbstractService<Loan> {

  private final LoanRepository loanRepository;

  private static final int BASE_DAYS = 360;
  private static final int TOTAL_INSTALLMENTS = 120;

  public LoanService(final AbstractRepository<Loan> repository,
                     final LoanRepository loanRepository) {
    super(Loan.class, repository);
    this.loanRepository = loanRepository;
  }

  public LoanResponseDto simulateLoan(final @NotNull SimluationDto simulationDto) {
    log.info("LoanService.simulateLoan() - init_process - simulationDto: {}", simulationDto);
    List<LocalDate> dates = DateUtils.generateDates(
        simulationDto.getInitialDate(), simulationDto.getFinalDate(),
        simulationDto.getFirstPaymentDate(), TOTAL_INSTALLMENTS
    );

    final List<Installment> installments =
        this.calculateInstallments(dates, simulationDto.getTotalAmount(),
            simulationDto.getInterestRate());

    log.info("LoanService.simulateLoan() - init_saving_process");

    final Loan loan = loanRepository.saveAndFlush(this.buildLoan(
        simulationDto.getInitialDate(), simulationDto.getFinalDate(),
        simulationDto.getFirstPaymentDate(), simulationDto.getTotalAmount(),
        simulationDto.getInterestRate(), installments
    ));

    log.info("LoanService.simulateLoan() - finish_saving_process");

    final LoanResponseDto loanResponseDto = LoanUtils.convertoEntityToDto(loan);

    log.info("LoanService.simulateLoan() - end_process - loan: {}", loanResponseDto);

    return loanResponseDto;
  }

  private List<Installment> calculateInstallments(List<LocalDate> dates,
                                                  final @Positive BigDecimal totalAmount,
                                                  final @Positive BigDecimal annualInterestRate) {

    log.info(
        "LoanService.calculateInstallments() - init_process - dates {} - totalAmount {} - annualInterestRate {}",
        dates, totalAmount, annualInterestRate);
    List<Installment> installments = new ArrayList<>();

    // Correção 1: Usar HALF_UP e mais casas decimais na amortização
    BigDecimal fixedAmortization = totalAmount.divide(
        BigDecimal.valueOf(TOTAL_INSTALLMENTS),
        10,
        RoundingMode.HALF_UP
    );

    log.info("LoanService.calculateInstallments() - building_grid");
    installments.add(this.buildInstallment(dates.getFirst(),
        totalAmount, BigDecimal.ZERO, BigDecimal.ZERO,
        BigDecimal.ZERO, BigDecimal.ZERO, totalAmount,
        BigDecimal.ZERO, false, 0));

    BigDecimal previousBalance = totalAmount;
    BigDecimal previousAccumulated = BigDecimal.ZERO;

    for (int i = 1; i < dates.size(); i++) {
      LocalDate currentDate = dates.get(i);
      LocalDate previousDate = dates.get(i - 1);

      long daysBetween = ChronoUnit.DAYS.between(previousDate, currentDate);
      BigDecimal baseValue = BigDecimal.ONE.add(annualInterestRate);
      BigDecimal exponentValue = new BigDecimal(daysBetween).divide(
          new BigDecimal(BASE_DAYS), 10, RoundingMode.HALF_UP
      );
      BigDecimal factor = calculatePower(baseValue, exponentValue);
      BigDecimal provision = factor.subtract(BigDecimal.ONE)
          .multiply(previousBalance.add(previousAccumulated));

      boolean isPaymentDate = i % 2 == 0 || i == dates.size() - 1;
      int installmentIndex = isPaymentDate ? (i / 2) : 0;

      BigDecimal amortization = BigDecimal.ZERO;
      BigDecimal paidInterest = BigDecimal.ZERO;
      BigDecimal currentBalance = previousBalance;
      BigDecimal currentAccumulated = previousAccumulated.add(provision);

      if (isPaymentDate) {
        // Correção 2: Ajustar última amortização
        if (i == dates.size() - 1) {
          amortization = previousBalance; // Amortiza o saldo residual
        } else {
          amortization = fixedAmortization;
        }

        paidInterest = currentAccumulated;
        currentAccumulated = BigDecimal.ZERO;
        currentBalance = previousBalance.subtract(amortization);
      }

      BigDecimal total = MonetaryUtils.round(amortization.add(paidInterest));
      BigDecimal outstandingBalance = MonetaryUtils.round(currentBalance.add(currentAccumulated));

      installments.add(this.buildInstallment(currentDate,
          MonetaryUtils.round(currentBalance),
          MonetaryUtils.round(currentAccumulated),
          MonetaryUtils.round(provision),
          MonetaryUtils.round(paidInterest),
          MonetaryUtils.round(amortization),
          MonetaryUtils.round(outstandingBalance),
          MonetaryUtils.round(total),
          isPaymentDate, installmentIndex));

      previousBalance = currentBalance;
      previousAccumulated = currentAccumulated;
    }

    log.info("LoanService.calculateInstallments() - end_process - installments: {}",
        installments.size());

    return installments;
  }

  private Loan buildLoan(final LocalDate initialDate, final LocalDate finalDate,
                         final LocalDate firstPaymentDate, final BigDecimal totalAmount,
                         final BigDecimal annualInterestRate,
                         final List<Installment> installments) {
    return Loan.builder()
        .initialDate(initialDate)
        .finalDate(finalDate)
        .firstPaymentDate(firstPaymentDate)
        .totalAmount(MonetaryUtils.round(totalAmount))
        .interestRate(MonetaryUtils.round(annualInterestRate))
        .installments(installments)
        .createdAt(OffsetDateTime.now(ZoneOffset.UTC))
        .build();
  }

  private Installment buildInstallment(final LocalDate currentDate,
                                       final BigDecimal currentBalance,
                                       final BigDecimal currentAccumulated,
                                       final BigDecimal provision,
                                       final BigDecimal paidInterest,
                                       final BigDecimal amortization,
                                       final BigDecimal outstandingBalance,
                                       final BigDecimal total,
                                       final boolean isPaymentDate,
                                       final int installmentIndex) {
    return Installment.builder()
        .competenceDate(currentDate)
        .loanAmount(BigDecimal.ZERO)
        .balance(MonetaryUtils.round(currentBalance))
        .accumulated(MonetaryUtils.round(currentAccumulated))
        .provision(MonetaryUtils.round(provision))
        .paidInterest(MonetaryUtils.round(paidInterest))
        .amortization(MonetaryUtils.round(amortization))
        .outstandingBalance(MonetaryUtils.round(outstandingBalance))
        .total(MonetaryUtils.round(total))
        .installmentNumber(isPaymentDate ? (installmentIndex) + "/" + TOTAL_INSTALLMENTS : "")
        .build();
  }

  private BigDecimal calculatePower(BigDecimal base, BigDecimal exponent) {
    MathContext mc = new MathContext(20, RoundingMode.HALF_UP);
    return BigDecimalMath.pow(base, exponent, mc);
  }

}
