package tads.ufrn.eaj.loja.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tads.ufrn.eaj.loja.model.Joia;
import tads.ufrn.eaj.loja.repository.JoiaRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class JoiaService {

    JoiaRepository repository;
    @Autowired
    public void setJoia(JoiaRepository repository) {
        this.repository = repository;
    }

    public void save(Joia joia){
        repository.save(joia);

    }
    public Joia findById(Long id){
        return this.repository.getById(id);
    }

    public List<Joia> listAll(){
        return repository.findAllByDeletedIsNull();
    }

    public List<Joia> listByCategory(String category){
        return repository.findAllByCategory(category);

    }

    public void deletar(Long id){
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Joia joia = repository.getById(id);
        Date data  = new Date();
        joia.setDeleted(data);
        repository.save(joia);

    }

}
