package totvs.technicalchallenge.utils;

import lombok.experimental.UtilityClass;
import totvs.technicalchallenge.dto.InstallmentResponseDto;
import totvs.technicalchallenge.model.Installment;

@UtilityClass
public class InstallmentUtils {

  public static InstallmentResponseDto convertToDto(Installment installment) {
    return InstallmentResponseDto.builder()
        .competenceDate(installment.getCompetenceDate())
        .loanAmount(MonetaryUtils.round(installment.getLoanAmount()))
        .outstandingBalance(MonetaryUtils.round(installment.getOutstandingBalance()))
        .installmentNumber(installment.getInstallmentNumber())
        .total(MonetaryUtils.round(installment.getTotal()))
        .amortization(MonetaryUtils.round(installment.getAmortization()))
        .remainingBalance(MonetaryUtils.round(installment.getBalance()))
        .provision(MonetaryUtils.round(installment.getProvision()))
        .accumulated(MonetaryUtils.round(installment.getAccumulated()))
        .paidInterest(MonetaryUtils.round(installment.getPaidInterest()))
        .build();
  }
}
