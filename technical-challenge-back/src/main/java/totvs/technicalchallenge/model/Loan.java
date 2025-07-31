package totvs.technicalchallenge.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
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
public class Loan extends AbstractEntity<Loan> {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id = UUID.randomUUID();
  private LocalDate initialDate;
  private LocalDate finalDate;
  private LocalDate firstPaymentDate;
  private BigDecimal totalAmount;
  private BigDecimal interestRate;
  @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true)
  private List<Installment> installments;
  private OffsetDateTime createdAt;

}
