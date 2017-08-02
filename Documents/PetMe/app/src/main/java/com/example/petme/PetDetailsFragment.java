package com.example.petme;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.petme.model.Model;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class PetDetailsFragment extends Fragment {
    ImageView userImageView;
    Bitmap userImageBitmap;
    long usersCounter;
    int whichPhoto;

    ImageView petImageView;
    Bitmap petImageBitmap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View contentView =  inflater.inflate(R.layout.fragment_pet_details, container, false);
        final ProgressBar savingProfile = (ProgressBar) contentView.findViewById(R.id.progressBar_details);
        savingProfile.setVisibility(View.GONE);

        final String currentUserMail = Model.instance.GetMailOfCurrentUser();

        Model.instance.countUsers(new Model.GetUsersCountCallback(){
            @Override
            public void onComplete(long count) {
                usersCounter = count;
                Log.d("TAG", "There are " +usersCounter+ " elements in database");
            }
            @Override
            public void onCancel() {}
        });

        Button saveBtn = (Button) contentView.findViewById(R.id.DetailsSaveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final EditText petName = (EditText) contentView.findViewById(R.id.pet_name);
                final EditText petType = (EditText) contentView.findViewById(R.id.pet_type);
                final EditText petAge = (EditText) contentView.findViewById(R.id.pet_age);
                final EditText petInfo = (EditText) contentView.findViewById(R.id.extra_info_details);
                final EditText ownerName = (EditText) contentView.findViewById(R.id.owner_name);
                final EditText ownerPhone = (EditText) contentView.findViewById(R.id.owner_phone);
                final EditText ownerAddress = (EditText) contentView.findViewById(R.id.owner_address);

                boolean error = false;

                if (petName.getText().toString().trim().equals("")) {
                    petName.setError("Pet name is required");
                    error = true;
                } else {
                    if (petType.getText().toString().trim().equals("")) {
                        petType.setError("Pet Type is required");
                        error = true;
                    } else {
                        if ( (petAge.getText().toString().trim().equals(""))){
                            petAge.setError("Pet age is required");
                            error = true;
                        }
                        else
                        {
                            if (ownerName.getText().toString().trim().equals("")) {
                                ownerName.setError("User name is required");
                                error = true;
                            } else if (ownerAddress.getText().toString().trim().equals("")) {
                                ownerAddress.setError("User address is required");
                                error = true;
                            }
                        }
                    }
                }

                if (ownerPhone.getText().toString().trim().equals(""))
                    ownerPhone.setText(null);
                if (petInfo.getText().toString().trim().equals(""))
                    petInfo.setText(null);

                if (error==false){
                    savingProfile.setVisibility(View.VISIBLE);
                    if (userImageBitmap!=null) {
                        Model.instance.saveImage(userImageBitmap, ownerName.getText().toString() + ".jpeg", new Model.SaveImageListener() {
                            @Override
                            public void complete(final String urlOwner) {
                                Log.d("TAG","User image saved");
                                if (petImageBitmap!=null) {
                                    Model.instance.saveImage(petImageBitmap, petName.getText().toString() + ".jpeg", new Model.SaveImageListener() {
                                        @Override
                                        public void complete(String urlPet) {
                                            Log.d("TAG", "Pet image saved");
                                            createNewUser(petName.getText().toString(), petType.getText().toString(), Integer.parseInt(petAge.getText().toString()), petInfo.getText().toString() ,ownerName.getText().toString(), ownerPhone.getText().toString(), currentUserMail, ownerAddress.getText().toString(), urlOwner, urlPet);
                                        }

                                        @Override
                                        public void fail() {
                                            Log.d("TAG", "Error saving Image");
                                        }
                                    });
                                }
                                else{
                                    createNewUser(petName.getText().toString(), petType.getText().toString(), Integer.parseInt(petAge.getText().toString()),  petInfo.getText().toString(), ownerName.getText().toString(), ownerPhone.getText().toString(), currentUserMail, ownerAddress.getText().toString(), urlOwner, null);
                                }
                            }
                            @Override
                            public void fail() {
                                Log.d("TAG","Error saving Image");
                            }
                        });
                    }
                    else
                    {
                        if (petImageBitmap!=null) {
                            Model.instance.saveImage(petImageBitmap, petName.getText().toString() + ".jpeg", new Model.SaveImageListener() {
                                @Override
                                public void complete(String urlPet) {
                                    Log.d("TAG", "Pet image saved");
                                    createNewUser(petName.getText().toString(), petType.getText().toString(), Integer.parseInt(petAge.getText().toString()),  petInfo.getText().toString(), ownerName.getText().toString(), ownerPhone.getText().toString(), currentUserMail, ownerAddress.getText().toString(), null, urlPet);
                                }

                                @Override
                                public void fail() {
                                    Log.d("TAG", "Error saving Image");
                                }
                            });
                        }
                        else {
                            createNewUser(petName.getText().toString(), petType.getText().toString(), Integer.parseInt(petAge.getText().toString()),  petInfo.getText().toString(), ownerName.getText().toString(), ownerPhone.getText().toString(), currentUserMail, ownerAddress.getText().toString(), null, null);
                        }
                    }
                }
            }
        });

        userImageView = (ImageView) contentView.findViewById(R.id.owner_image_details);
        userImageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dispatchTakePictureIntent();
                Log.d("TAG", "Take user picture");
            }
        });

        petImageView = (ImageView) contentView.findViewById(R.id.pet_image_details);
        petImageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dispatchTakePictureIntent2();
                Log.d("TAG", "Take pet picture");
            }
        });

        return contentView;
        //return inflater.inflate(R.layout.fragment_pet_details, container, false);

    }

    private void createNewUser (String petName, String petType, int petAge, String petInfo ,String ownerName, String ownerPhone, String userMail, String ownerAddress, String urlOwner, String urlPet){
        Model.instance.CreateUser(usersCounter, petName, petType, petAge, petInfo, ownerName, ownerPhone, userMail, ownerAddress, urlOwner, urlPet);
        PetListFragment petList = PetListFragment.newInstance();
        FragmentTransaction tran = getFragmentManager().beginTransaction();
        tran.replace(R.id.main_container, petList);
        tran.commit();
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;


    // open camera
    private void dispatchTakePictureIntent() {
        whichPhoto=1;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    // open camera
    private void dispatchTakePictureIntent2() {
        whichPhoto=2;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    // take the photo out of the intent
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();

            if (whichPhoto==1) {
                userImageBitmap = (Bitmap) extras.get("data");
                userImageView.setImageBitmap(userImageBitmap);
            }
            if (whichPhoto==2) {
                petImageBitmap = (Bitmap) extras.get("data");
                petImageView.setImageBitmap(petImageBitmap);
            }
        }
    }
}
