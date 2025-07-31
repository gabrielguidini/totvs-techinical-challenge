package totvs.technicalchallenge.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
public class SimluationDto implements Dto<SimluationDto> {

  private LocalDate initialDate;
  private LocalDate finalDate;
  private LocalDate firstPaymentDate;
  private BigDecimal totalAmount;
  private BigDecimal interestRate;
}
