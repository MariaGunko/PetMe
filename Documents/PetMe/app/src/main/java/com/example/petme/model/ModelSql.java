package com.example.petme.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Maria on 30/07/2017.
 */

public class ModelSql {
    Helper helper;

    static final String PET_TABLE = "pets";
    static final String PET_ID = "petId";
    static final String PET_NAME = "petName";
    static final String PET_TYPE = "petType";
    static final String PET_AGE = "petAge";
    static final String USER_NAME = "userName";
    static final String USER_ADDRESS = "userAddress";
    static final String USER_PHONE = "userPhone";
    static final String USER_MAIL = "userMail";
    static final String PET_IMAGE = "imagePetUrl";
    static final String USER_IMAGE = "imageUserUrl";
    static final String PET_INFO = "petInfo";

    ModelSql(Context context){
        helper = new Helper(context);
    }

    class Helper extends SQLiteOpenHelper { // DB Version manager
        public Helper(Context context) {
            super(context, "database.db", null, 4);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table " + PET_TABLE + " ("+PET_ID+" TEXT PRIMARY KEY, "+PET_NAME+" TEXT, "+PET_TYPE+" TEXT, "+PET_AGE+" NUMBER, "+PET_INFO+" TEXT, "+USER_NAME+" TEXT, "+USER_PHONE+" TEXT, "+USER_MAIL+" TEXT, "+USER_ADDRESS+" TEXT, "+PET_IMAGE+" TEXT, "+USER_IMAGE+" TEXT);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + PET_TABLE );
            onCreate(db);
        }
    }

    public void addUser (User pet){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put (PET_ID, pet.getID());
        values.put (PET_NAME, pet.getPetName());
        values.put (PET_TYPE, pet.getPetType());
        values.put (PET_AGE, pet.getPetAge());
        values.put (PET_INFO,pet.getInfo());
        values.put (USER_NAME, pet.getUserName());
        values.put (USER_ADDRESS, pet.getUserAddress());
        values.put (USER_MAIL, pet.getUserMail());
        values.put (USER_PHONE, pet.getUserPhone());
        values.put (PET_IMAGE, pet.getImagePetUrl());
        values.put (USER_IMAGE, pet.getImageUserUrl());

        Log.d("TAG","Added user " +pet.getID());
        db.insert(PET_TABLE, PET_ID, values);
    }

    public List<User> getAllUsers(){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(PET_TABLE, null,null,null,null,null, null);
        List<User> list= new LinkedList<User>();
        if (cursor.moveToFirst()){
            int idIndex = cursor.getColumnIndex(PET_ID);
            int petNameIndex = cursor.getColumnIndex(PET_NAME);
            int petTypeIndex = cursor.getColumnIndex(PET_TYPE);
            int petAgeIndex = cursor.getColumnIndex(PET_AGE);
            int petInfoIndex = cursor.getColumnIndex(PET_INFO);
            int userNameIndex = cursor.getColumnIndex(USER_NAME);
            int userAddressIndex = cursor.getColumnIndex(USER_ADDRESS);
            int userPhoneIndex = cursor.getColumnIndex(USER_PHONE);
            int userMailIndex = cursor.getColumnIndex(USER_MAIL);
            int petImageIndex = cursor.getColumnIndex(PET_IMAGE);
            int userImageIndex = cursor.getColumnIndex(USER_IMAGE);

            do {
                User pet = new User();
                pet.setID(cursor.getString(idIndex));
                pet.setPetName(cursor.getString(petNameIndex));
                pet.setPetType(cursor.getString(petTypeIndex));
                pet.setPetAge(cursor.getInt(petAgeIndex));
                pet.setInfo(cursor.getString(petInfoIndex));
                pet.setUserName(cursor.getString(userNameIndex));
                pet.setUserAddress(cursor.getString(userAddressIndex));
                pet.setUserPhone(cursor.getString(userPhoneIndex));
                pet.setUserMail(cursor.getString(userMailIndex));
                pet.setImagePetUrl(cursor.getString(petImageIndex));
                pet.setImageUserUrl(cursor.getString(userImageIndex));
                list.add(pet);
            }while(cursor.moveToNext());
        }
        return list;
    }

    public void updateUser (User pet){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put (PET_NAME, pet.getPetName());
        values.put (PET_TYPE, pet.getPetType());
        values.put (PET_AGE, pet.getPetAge());
        values.put (PET_INFO, pet.getInfo());
        values.put (USER_NAME, pet.getUserName());
        values.put (USER_ADDRESS, pet.getUserAddress());
        values.put (USER_MAIL, pet.getUserMail());
        values.put (USER_PHONE, pet.getUserPhone());
        values.put (PET_IMAGE, pet.getImagePetUrl());
        values.put (USER_IMAGE, pet.getImageUserUrl());

        Log.d("TAG","Added user " +pet.getID());
        db.update(PET_TABLE, values, PET_ID+"="+pet.getID(), null);
    }

    public User getUser(String userId){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + PET_TABLE + " WHERE "+PET_ID+" = "+userId+";",null);
        cursor.moveToFirst();

        User pet = new User();
        pet.setID(cursor.getString(cursor.getColumnIndex(PET_ID)));
        pet.setPetName(cursor.getString(cursor.getColumnIndex(PET_NAME)));
        pet.setPetType(cursor.getString(cursor.getColumnIndex(PET_TYPE)));
        pet.setPetAge(cursor.getInt(cursor.getColumnIndex(PET_AGE)));
        pet.setInfo(cursor.getString(cursor.getColumnIndex(PET_INFO)));
        pet.setUserName(cursor.getString(cursor.getColumnIndex(USER_NAME)));
        pet.setUserAddress(cursor.getString(cursor.getColumnIndex(USER_ADDRESS)));
        pet.setUserPhone(cursor.getString(cursor.getColumnIndex(USER_PHONE)));
        pet.setUserMail(cursor.getString(cursor.getColumnIndex(USER_MAIL)));
        pet.setImagePetUrl(cursor.getString(cursor.getColumnIndex(PET_IMAGE)));
        pet.setImageUserUrl(cursor.getString(cursor.getColumnIndex(USER_IMAGE)));

        return pet;
    }

    public void RemoveUser(String userId){
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("DELETE FROM " + PET_TABLE + " WHERE " + PET_ID + "= '" + userId + "'");
        Log.d("TAG","Removed user " + userId);
    }
}
