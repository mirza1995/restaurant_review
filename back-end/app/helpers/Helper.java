package helpers;

import models.User;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

public class Helper {
    public static int findIndexOfMaximum(List<Double> array){
        int indexOfMax=0;
        for(int i=0;i<array.size();i++){
            if(array.get(i)>array.get(indexOfMax)){
                indexOfMax=i;
            }
        }
        return indexOfMax;
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public static String hashPassword(User user){
        byte[] emailBytes = user.getEmail().getBytes();
        byte[] saltBytes = "atlantbh".getBytes();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] emailOfDigest = md.digest(emailBytes);
            byte[] saltOfDigest = md.digest(saltBytes);
            String emailDigested = DatatypeConverter.printHexBinary(emailOfDigest).toLowerCase();
            String saltDigested= DatatypeConverter.printHexBinary(saltOfDigest).toLowerCase();
            String finalString = emailDigested+saltDigested+user.getPassword();
            byte[] finalPasswordBytes = md.digest(finalString.getBytes());
            String finalpassword = DatatypeConverter.printHexBinary(finalPasswordBytes).toLowerCase();
            return finalpassword;
        }
        catch (NoSuchAlgorithmException e) {
            System.err.println("I'm sorry, but MD5 is not a valid message digest algorithm");
            return null;
        }
    }

    public static String[] convertToUpperCase(String[] string){
        for(int i = 0; i < string.length; i++){
            String[] stringArray = string[i].split(" ");
            String stringTemp = "";
            for(int j = 0; j < stringArray.length; j++){
                stringArray[j] = stringArray[j].toLowerCase().substring(0, 1).toUpperCase() + stringArray[j].toLowerCase().substring(1);
                stringTemp += stringArray[j];
                if(j != stringArray.length - 1){
                    stringTemp += " ";
                }
            }
            string[i] = stringTemp;
        }
        return string;
    }

    public static String randomString(){
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 15;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }
}
