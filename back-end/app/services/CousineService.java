package services;

import models.Cousine;
import repositories.CousineRepository;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class CousineService {
    @Inject private CousineRepository repository;

    public Cousine addCousine(Cousine cousine){
        repository.createCousine(cousine);
        return cousine;
    }
    public Cousine updateCousine(Cousine cousine){
        repository.update(cousine);
        return cousine;
    }

    public Cousine getCousineByName(String name){
        return repository.getCousineByName(name);
    }

    public List<Cousine> getAllCousines(){
        return repository.getAllCousines();
    }

    public boolean deleteCousine(int id){
        return repository.delete(id);
    }
}
