package totvs.technicalchallenge.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Installment extends AbstractEntity<Installment> {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id = UUID.randomUUID();
  private LocalDate competenceDate;
  private BigDecimal loanAmount;
  private BigDecimal outstandingBalance;
  private String installmentNumber;
  private BigDecimal total;
  private BigDecimal amortization;
  private BigDecimal balance;
  private BigDecimal provision;
  private BigDecimal accumulated;
  private BigDecimal paidInterest;
}
