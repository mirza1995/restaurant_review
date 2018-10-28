package helpers;

import java.io.File;
import java.io.IOException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;

public class UploadObject {

    public static String uploadImage(File file) throws IOException {
        String bucketName = "abhpraksa201807";
        String keyName = Helper.randomString();

        try {
            BasicAWSCredentials awsCreds = new BasicAWSCredentials("", "");
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    //.withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                    .withCredentials(new EnvironmentVariableCredentialsProvider())
                    .withRegion(Regions.EU_CENTRAL_1)
                    .build();
            TransferManager tm = TransferManagerBuilder.standard()
                    .withS3Client(s3Client)
                    .build();

            Upload upload = tm.upload(new PutObjectRequest(bucketName, keyName, file).withCannedAcl(CannedAccessControlList.PublicRead));
            System.out.println("Object upload started");

            try {
                upload.waitForCompletion();
            } catch(InterruptedException e){
                e.printStackTrace();
            }
            System.out.println("Object upload complete");
            return s3Client.getUrl(bucketName, keyName).toString();
        }
        catch(AmazonServiceException e) {
            e.printStackTrace();
        }
        catch(SdkClientException e) {
            e.printStackTrace();
        }
        return null;
    }
}