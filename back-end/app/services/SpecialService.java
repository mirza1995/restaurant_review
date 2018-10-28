package services;

import models.Special;
import repositories.SpecialRepository;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class SpecialService {
    @Inject private SpecialRepository repository;

    public Special addSpecial(Special special){
        repository.createSpecial(special);
        return special;
    }
    public Special updateSpecial(Special special){
        repository.update(special);
        return special;
    }

    public Special getSpecial(int id){
        return repository.get(id);
    }

    public List<Special> getAllSpecials(){
        return repository.getAllSpecials();
    }

    public boolean deleteSpecial(int id){
        return repository.delete(id);
    }
}
