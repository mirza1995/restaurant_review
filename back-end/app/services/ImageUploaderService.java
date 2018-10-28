package services;

import com.fasterxml.jackson.databind.JsonNode;
import helpers.UploadObject;
import models.Photo;
import models.Restaurant;
import play.libs.Json;
import repositories.PhotoRepository;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;


public class ImageUploaderService {
    @Inject private PhotoRepository photoRepository;

    public String uploadImage(String imageType, String base64String){
        BufferedImage image = decodeImage(base64String);
        if(image == null){
            return "Image null";
        }

        File outputFile = getFileFromImage(image,imageType);
        if(outputFile == null){
            return null;
        }
        try{
            String url = UploadObject.uploadImage(outputFile);
            if(url == null){
                return "Upload Failed";
            }
            return url;
        } catch(Exception e){
            e.printStackTrace();
        }
        return "Exception thrown";
    }

    public String uploadGallery(JsonNode json){
        for(int i = 0; i < json.size(); i++){
            JsonNode photoInformation = json.get(i);
            String imageType = photoInformation.get("imageType").textValue();
            String base64String = photoInformation.get("photo").textValue();

            BufferedImage image = decodeImage(base64String);
            if(image == null){
                return "Image null";
            }

            File outputFile = getFileFromImage(image,imageType);
            if(outputFile == null){
                return null;
            }
            try{
                String url = UploadObject.uploadImage(outputFile);
                if(url == null){
                    return "Upload Failed";
                }
                Photo photoToSave = new Photo();
                photoToSave.setRestaurant(Json.fromJson(photoInformation.get("restaurant"), Restaurant.class));
                photoToSave.setUrl(url);
                photoRepository.createPhoto(photoToSave);

            } catch(Exception e){
                e.printStackTrace();
            }
        }
        return "Exception thrown";
    }

    public BufferedImage decodeImage(String base64String){
        BufferedImage image = null;
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] imageByte = decoder.decodeBuffer(base64String);
            image = ImageIO.read(new ByteArrayInputStream(imageByte));
        } catch(Exception e){
            e.printStackTrace();
        }
        return image;
    }

    public File getFileFromImage(BufferedImage image, String imageType){
        File outputFile = new File("image."+imageType);
        try{
            ImageIO.write(image, imageType, outputFile);
        } catch (Exception e){
            e.printStackTrace();
        }
        if(outputFile == null){
            return null;
        }
        return outputFile;
    }
}
