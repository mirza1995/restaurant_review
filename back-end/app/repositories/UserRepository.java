package repositories;
import helpers.Helper;
import models.Location;
import models.Restaurant;
import models.User;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import play.db.jpa.JPAApi;
import org.hibernate.Session;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Singleton
public class UserRepository {
    @Inject private JPAApi api;

    public void createUser(User user){
        api.em().persist(user);
    }

    public void update(User user){
        api.em().unwrap(Session.class).update(user);
    }

    public List<User> getAllUsers(){
        return api.em().unwrap(Session.class).createCriteria(User.class).list();
    }

    public User checkEmail(String email){
        return (User) api.em().unwrap(Session.class).createCriteria(User.class).add(Restrictions.eq("email", email)).uniqueResult();
    }

    public User login(User user){
        String password = Helper.hashPassword(user);
        if(password == null){
            return null;
        }
        return (User) api.em().unwrap(Session.class).createCriteria(User.class).add(Restrictions.eq("email", user.getEmail())).add(Restrictions.eq("password", password)).uniqueResult();
    }

    public User getUser(int id){
        return (User) api.em().unwrap(Session.class).createCriteria(User.class).add(Restrictions.eq("id", id)).uniqueResult();
    }

    public boolean deleteUser(int id){
        User User = (User) api.em().unwrap(Session.class).createCriteria(User.class).add(Restrictions.eq("id", id)).uniqueResult();
        api.em().unwrap(Session.class).delete(User);
        return true;
    }

    public Number getNumberOfUsers(){
        return (Number) api.em().unwrap(Session.class).createCriteria(User.class)
                .setProjection(Projections.rowCount())
                .uniqueResult();
    }

    public List<User> searchUser(String search){
        String[] searchArray = search.split(" ");
        if(searchArray.length == 1){
            return api.em().unwrap(Session.class).createCriteria(User.class)
                    .add( Restrictions.or(
                            Restrictions.eq( "firstName", search ).ignoreCase(),
                            Restrictions.eq("lastName", search).ignoreCase(),
                            Restrictions.eq("email", search).ignoreCase()
                    )).list();
        } else {
            return api.em().unwrap(Session.class).createCriteria(User.class)
                    .add( Restrictions.and(
                            Restrictions.eq( "firstName", searchArray[0]).ignoreCase(),
                            Restrictions.eq("lastName", searchArray[1]).ignoreCase()
                    )).list();
        }
    }

    public List<User> getUsersPage(int page, int numberPerPage){
        return  api.em().unwrap(Session.class).createCriteria(User.class)
                .setMaxResults(numberPerPage)
                .setFirstResult((page-1)*numberPerPage)
                .list();
    }
}
