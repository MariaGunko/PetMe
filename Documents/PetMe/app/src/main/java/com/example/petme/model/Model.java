package com.example.petme.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.webkit.URLUtil;

import com.example.petme.MyApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Maria on 15/07/2017.
 */

public class Model {
    public static Model instance = new Model ();
    private ModelFirebase modelFirebase;
    private ModelMem modelMem;
    private ModelSql modelSql;

    private List<User> data = new LinkedList<User>();

    private Model(){
        modelMem = new ModelMem();
        modelSql = new ModelSql(MyApplication.context);
        modelFirebase = new ModelFirebase();

//        for (int i=0;i<10;i++){
//            User pet = new User();
//            pet.setID(i+"");
//            pet.setPetName("Michael");;
//            pet.setPetType("Exotic");
//            pet.setPetAge(3);
//            pet.setUserName("Maria");
//            pet.setUserAddress("Ashdod");
//            pet.setUserPhone("0526682600");
//            pet.setUserMail("masha.gonko@gmail.com");
//            modelFirebase.addUser(pet);
//        }
    }

    public interface GetAllUsersAndObserveCallback{
        void onComplete(List <User> list);
        void onCancel ();
    }

    public List<User> getAllUsers (){
        return modelSql.getAllUsers();
    }

    public void getAllUsers(final GetAllUsersAndObserveCallback callback){
        modelFirebase.getAllUsersAndObserve(new ModelFirebase.GetAllUsersAndObserveCallback() {
            @Override
            public void onComplete(List<User> list) {
                callback.onComplete(list);
                data = list;
            }

            @Override
            public void onCancel() {
                callback.onCancel();
            }
        });
    }

    public void addToSQL (User pet){
        modelSql.addUser(pet);
    }

    public void addUser (User pet){
        modelFirebase.addUser(pet);
        //modelSql.addUser(pet);
    }

    public void countUsers(final GetUsersCountCallback callback){
       modelFirebase.getCountOfUsers(new ModelFirebase.GetUsersCountCallback(){
           @Override
           public void onComplete(long count) {
               callback.onComplete(count);
           }
           @Override
           public void onCancel() {
               callback.onCancel();
           }
       });
    }

    public interface GetUsersCountCallback{
        void onComplete(long count);
        void onCancel ();
    }

    public void deleteUser (String id){
        modelSql.RemoveUser(id);
    }

    public interface GetUserCallback{
        void onComplete(User pet);
        void onCancel ();
    }

    public User getUser (String petId){
        return modelSql.getUser(petId);
    }

    public void getUser(String petID, final GetUserCallback callback) {
        modelFirebase.getUser(petID, new ModelFirebase.GetUserCallback(){
            @Override
            public void onComplete(User pet) {
                callback.onComplete(pet);
            }

            @Override
            public void onCancel() {
                callback.onCancel();
            }
        });

    }

    public void UpdateUser(User pet){
        modelSql.updateUser(pet);
    }

    public void UpdateUser (User pet, String namePet, String type, int age, String name, String phone ,String mail, String add, String imageUser, String imagePet ){
        pet.setPetName(namePet);;
        pet.setPetType(type);
        pet.setPetAge(age);
        pet.setUserName(name);
        pet.setUserAddress(add);
        pet.setUserPhone(phone);
        pet.setUserMail(mail);
        pet.setImageUserUrl(imageUser);
        pet.setImagePetUrl(imagePet);
        modelFirebase.addUser(pet);
        modelSql.updateUser(pet);
    }

    public void RemoveUser (User pet){
        modelFirebase.DeleteUser(pet);
    }

    public String GetMailOfCurrentUser (){
        return modelFirebase.getUserMail();
    }

    public void CreateUser (long prevId, String namePet, String type, int age, String name, String phone ,String mail, String add, String imageUser, String imagePet ){
        User pet = new User();
        pet.setID(prevId+1+"");
        pet.setPetName(namePet);;
        pet.setPetType(type);
        pet.setPetAge(age);
        pet.setUserName(name);
        pet.setUserAddress(add);
        pet.setUserPhone(phone);
        pet.setUserMail(mail);
        pet.setImageUserUrl(imageUser);
        pet.setImagePetUrl(imagePet);
        addUser(pet);
    }

    public interface SaveImageListener {
        void complete (String url);
        void fail ();
    }

    public void getCurrentlyConnectedUser(){
        modelFirebase.getLoggedInUser();
    }
    // Firebase storage - upload
    public void saveImage(final Bitmap imageBmp, String name, final SaveImageListener listener) {
        modelFirebase.saveImage(imageBmp, name, new SaveImageListener() {
            @Override
            public void complete(String url) {
                String fileName = URLUtil.guessFileName(url, null, null);
                saveImageToFile(imageBmp, fileName);
                listener.complete(url);
            }

            @Override
            public void fail() {
                listener.fail();
            }
        });
    }

    public interface GetImageListener {
        void onSuccess (Bitmap image);
        void onFail ();
    }

    public void getImage(String url, final GetImageListener listener){
        // first check if the image exist locally
        final String fileName = URLUtil.guessFileName(url, null, null); // retrieve the file name from the url
        Bitmap bitmap = loadImageFromFile(fileName);

        if (bitmap!=null){
            Log.d("TAG", "Got image from local storage: " + fileName);
          listener.onSuccess(bitmap);
        }
        else{  // if not, go to firebase
            modelFirebase.getImage(url, new GetImageListener() {
                @Override
                public void onSuccess(Bitmap image) {
                    Log.d("TAG", "Got image from firebase: " + fileName);
                    saveImageToFile(image, fileName);         // save the image locally
                    listener.onSuccess(image);
                }

                @Override
                public void onFail() {
                    listener.onFail();
                }
            });
        }
    }

    // manage files in device
    private void saveImageToFile(Bitmap imageBitmap, String imageFileName){
        try {
            File dir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File imageFile = new File(dir,imageFileName);
            imageFile.createNewFile();
            OutputStream out = new FileOutputStream(imageFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
            //addPicureToGallery(imageFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap loadImageFromFile(String imageFileName){
        Bitmap bitmap = null;
        try {
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File imageFile = new File(dir,imageFileName);
            InputStream inputStream = new FileInputStream(imageFile);
            bitmap = BitmapFactory.decodeStream(inputStream);
            Log.d("tag","got image from cache: " + imageFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
