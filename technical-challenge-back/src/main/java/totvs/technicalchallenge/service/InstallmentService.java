package totvs.technicalchallenge.service;

import org.springframework.stereotype.Service;
import totvs.technicalchallenge.model.Installment;
import totvs.technicalchallenge.repository.AbstractRepository;

@Service
public class InstallmentService extends AbstractService<Installment> {
  public InstallmentService(final AbstractRepository<Installment> repository) {
    super(Installment.class, repository);
  }

  
}
