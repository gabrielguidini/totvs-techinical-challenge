package totvs.technicalchallenge.model;


import jakarta.persistence.MappedSuperclass;
import java.io.Serial;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@NoArgsConstructor
@SuperBuilder(builderMethodName = "superbuilder")
@Getter
@ToString
@MappedSuperclass
@DynamicInsert
@DynamicUpdate
public abstract class AbstractEntity<T extends Entity<T>> implements Entity<T> {

  @Serial
  private static final long serialVersionUID = 1L;

}