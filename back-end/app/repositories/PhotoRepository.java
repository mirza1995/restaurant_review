package repositories;

import models.Photo;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class PhotoRepository {
    @Inject
    private JPAApi api;

    public void createPhoto(Photo photo){
        api.em().persist(photo);
    }

    public void update(Photo photo){
        api.em().unwrap(Session.class).update(photo);
    }


    public Photo get(int id){
        return (Photo) api.em().unwrap(Session.class).createCriteria(Photo.class).add(Restrictions.eq("id", id)).uniqueResult();
    }

    public List<Photo> getRestaurantPhotos(int id){
        return api.em().unwrap(Session.class).createCriteria(Photo.class).add(Restrictions.eq("restaurant.id", id)).list();
    }

    public boolean delete(int id){
        Photo Photo = (Photo) api.em().unwrap(Session.class).createCriteria(Photo.class).add(Restrictions.eq("id", id)).uniqueResult();
        api.em().unwrap(Session.class).delete(Photo);
        return true;
    }
}
