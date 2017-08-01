package com.example.petme.model;

import com.example.petme.MyApplication;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Maria on 30/07/2017.
 */

public class ModelMem {

    private List<User> data = new LinkedList<User>();

    ModelMem(){
        for (int i=0;i<10;i++){
            User pet = new User();
            pet.setID(SetDynamicID());
            pet.setPetName("Michael");;
            pet.setPetType("Exotic");
            pet.setPetAge(3);
            pet.setUserName("Maria");
            pet.setUserAddress("Ashdod");
            pet.setUserPhone("0526682600");
            pet.setUserMail("masha.gonko@gmail.com");
            data.add(pet);
        }
    }

    public String SetDynamicID (){
        return data.size()+1+"";
    }

    public void deleteUser (User pet){
        data.remove(pet);
    }

    public void addUser (User pet){
        data.add(pet);
    }

    public List<User> getAllUsers(){
        return data;
    }

    public User getUser (String petID){
        for (User pet : data){
            if (pet.getID().equals(petID)){
                return pet;
            }
        }
        return null;
    }
}
