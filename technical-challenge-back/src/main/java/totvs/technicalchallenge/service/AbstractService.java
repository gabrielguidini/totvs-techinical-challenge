package totvs.technicalchallenge.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import totvs.technicalchallenge.model.Entity;
import totvs.technicalchallenge.repository.AbstractRepository;

@Validated
@RequiredArgsConstructor
@Slf4j
public class AbstractService<T extends Entity<T>> implements Service<T> {

  @Getter
  protected final Class<T> clazz;
  protected final AbstractRepository<T> repository;

  @Override
  public T saveEntity(T entity) {
    return this.repository.save(entity);
  }
}
