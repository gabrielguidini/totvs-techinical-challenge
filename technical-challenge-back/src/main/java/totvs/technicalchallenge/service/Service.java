package totvs.technicalchallenge.service;

import java.time.format.DateTimeFormatter;
import org.springframework.validation.annotation.Validated;
import totvs.technicalchallenge.model.Entity;

@Validated
public interface Service<T extends Entity<T>> {

  DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
  DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
  DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");


  T saveEntity(T entity);
}
