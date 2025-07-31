package totvs.technicalchallenge.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class LoanResponseDto implements Dto<LoanResponseDto> {

  private LocalDate initialDate;
  private LocalDate finalDate;
  private LocalDate firstPaymentDate;
  private BigDecimal totalAmount;
  private BigDecimal interestRate;
  private int totalInstallments;
  private List<InstallmentResponseDto> installments;
}
