package totvs.technicalchallenge.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class InstallmentResponseDto implements Dto<InstallmentResponseDto> {

  private LocalDate competenceDate;
  private BigDecimal loanAmount;
  private BigDecimal outstandingBalance;
  private String installmentNumber;
  private BigDecimal total;
  private BigDecimal amortization;
  private BigDecimal remainingBalance;
  private BigDecimal provision;
  private BigDecimal accumulated;
  private BigDecimal paidInterest;

}
