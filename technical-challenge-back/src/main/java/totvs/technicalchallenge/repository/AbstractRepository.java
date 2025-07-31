package totvs.technicalchallenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import totvs.technicalchallenge.model.Entity;

@NoRepositoryBean
public interface AbstractRepository<T extends Entity<T>> extends JpaRepository<T, Object>,
    JpaSpecificationExecutor<T> {

}
