package totvs.technicalchallenge.utils;

import jakarta.validation.Valid;
import lombok.experimental.UtilityClass;
import totvs.technicalchallenge.dto.LoanResponseDto;
import totvs.technicalchallenge.model.Loan;

@UtilityClass
public class LoanUtils {

  public static LoanResponseDto convertoEntityToDto(final @Valid Loan loan) {
    return LoanResponseDto.builder()
        .initialDate(loan.getInitialDate())
        .finalDate(loan.getFinalDate())
        .firstPaymentDate(loan.getFirstPaymentDate())
        .totalAmount(loan.getTotalAmount())
        .interestRate(loan.getInterestRate())
        .installments(loan.getInstallments()
            .stream()
            .map(InstallmentUtils::convertToDto).toList())
        .build();
  }
}
