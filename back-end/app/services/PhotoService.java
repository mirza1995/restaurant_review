package services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import models.Photo;
import play.libs.Json;
import repositories.PhotoRepository;
import repositories.RestaurantRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class PhotoService {
    @Inject private PhotoRepository repository;

    private final PhotoRepository photoRepository;

    public PhotoService(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    public PhotoService() {
        this.photoRepository = null;
    }

    public Photo addPhoto(Photo item){
        repository.createPhoto(item);
        return item;
    }
    public Photo updatePhoto(Photo item){
        repository.update(item);
        return item;
    }

    public Photo getPhoto(int id){
        return repository.get(id);
    }

    public JsonNode getRestaurantPhotos(int id,int gallerySize){
        PhotoRepository photoRepositoryUsed;
        if(this.photoRepository == null){
            photoRepositoryUsed = repository;
        } else {
            photoRepositoryUsed = photoRepository;
        }
        List<Photo> photos = photoRepositoryUsed.getRestaurantPhotos(id);
        if(photos== null || photos.size() == 0){
            return null;
        } else if(gallerySize > photos.size()){
            gallerySize = photos.size();
        }
        if(gallerySize == 0){
            gallerySize = photos.size();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode photosJson = objectMapper.createArrayNode();
        //Converting to match the template of gallery addon
        for(int i = 0; i < gallerySize; i++){
            JsonNode photo = Json.parse("{\"src\":\"" + photos.get(i).getUrl() + "\",\"w\":1260,\"h\":750}");
            photosJson.add(photo);
        }
        return photosJson;
    }

    public List<Photo> getAllPhotos(int id){
        return repository.getRestaurantPhotos(id);
    }
    public boolean deletePhoto(int id){
        return repository.delete(id);
    }
}
