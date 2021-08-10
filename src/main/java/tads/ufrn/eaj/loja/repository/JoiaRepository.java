package tads.ufrn.eaj.loja.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tads.ufrn.eaj.loja.model.Joia;

import java.util.List;

public interface JoiaRepository extends JpaRepository<Joia, Long> {
   List<Joia> findAllByCategory(String category);
   List<Joia> findAllByDeletedIsNotNull();
    List<Joia> findAllByDeletedIsNull();

}
