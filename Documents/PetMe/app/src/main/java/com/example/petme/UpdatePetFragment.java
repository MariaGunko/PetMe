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
import com.example.petme.model.User;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpdatePetFragment extends Fragment {

    int whichPhoto; // index of image pressed

    ImageView userImageView;
    Bitmap userImageBitmap;

    ImageView petImageView;
    Bitmap petImageBitmap;

    // current images set for profile
    String ImageForPet;
    String ImageForUser;

    private static final String ARG_PARAM1 = "itemId";
    private String itemId;

    User pet;


    public UpdatePetFragment() {
        // Required empty public constructor
    }

    public static UpdatePetFragment newInstance(String param1) {
        UpdatePetFragment fragment = new UpdatePetFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            itemId = getArguments().getString(ARG_PARAM1);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View contentView =  inflater.inflate(R.layout.fragment_update_pet, container, false);
        final ProgressBar savingProfile = (ProgressBar) contentView.findViewById(R.id.update_progressBar);
        final String currentUser = Model.instance.GetMailOfCurrentUser();
        savingProfile.setVisibility(View.GONE);

        final EditText petName = (EditText) contentView.findViewById(R.id.update_pet_name);
        final EditText petType = (EditText) contentView.findViewById(R.id.update_pet_type);
        final EditText petAge = (EditText) contentView.findViewById(R.id.update_pet_age);
        final EditText petInfo = (EditText) contentView.findViewById(R.id.update_info);
        final EditText ownerName = (EditText) contentView.findViewById(R.id.update_owner_name);
        final EditText ownerPhone = (EditText) contentView.findViewById(R.id.update_owner_phone);
        final EditText ownerAddress = (EditText) contentView.findViewById(R.id.update_owner_address);
        final ImageView userPic = (ImageView) contentView.findViewById(R.id.update_owner_image) ;
        final ImageView petPic = (ImageView) contentView.findViewById(R.id.update_pet_image) ;


        savingProfile.setVisibility(View.VISIBLE);
        pet = Model.instance.getUser(itemId);

        if (pet==null){
            Model.instance.getUser(itemId, new Model.GetUserCallback(){
                @Override
                public void onComplete(User previousPet) {
                    pet=previousPet;
                    Log.d("TAG", "Taken from Firebase");
                    petName.setText(pet.getPetName());
                    petType.setText(pet.getPetType());
                    petAge.setText(pet.getPetAge() + "");
                    petInfo.setText(pet.getInfo());
                    ownerName.setText(pet.getUserName());
                    ownerAddress.setText(pet.getUserAddress());
                    ownerPhone.setText(pet.getUserPhone());

                    ImageForPet = pet.getImagePetUrl();
                    ImageForUser = pet.getImageUserUrl();

                    savingProfile.setVisibility(View.GONE);
                    if (pet.getImageUserUrl() != null && pet.getImageUserUrl().isEmpty() == false) {
                        Model.instance.getImage(pet.getImageUserUrl(), new Model.GetImageListener() {
                            @Override
                            public void onSuccess(Bitmap image) {
                                userPic.setImageBitmap(image);
                                savingProfile.setVisibility(View.GONE);
                            }

                            @Override
                            public void onFail() {

                            }
                        });
                    }

                    if (pet.getImagePetUrl() != null && pet.getImagePetUrl().isEmpty() == false) {

                        Model.instance.getImage(pet.getImagePetUrl(), new Model.GetImageListener() {
                            @Override
                            public void onSuccess(Bitmap image) {
                                petPic.setImageBitmap(image);
                                savingProfile.setVisibility(View.GONE);
                            }

                            @Override
                            public void onFail() {

                            }
                        });
                    }
                }

                @Override
                public void onCancel() {
                    Log.d("TAG", "get pet cancel");
                }
            });
        }
        else {

            Log.d("TAG", "Taken from SQLITE");

            petName.setText(pet.getPetName());
            petType.setText(pet.getPetType());
            petAge.setText(pet.getPetAge() + "");
            petInfo.setText(pet.getInfo());
            ownerName.setText(pet.getUserName());
            ownerAddress.setText(pet.getUserAddress());
            ownerPhone.setText(pet.getUserPhone());

            ImageForPet = pet.getImagePetUrl();
            ImageForUser = pet.getImageUserUrl();

            savingProfile.setVisibility(View.GONE);
            if (pet.getImageUserUrl() != null && pet.getImageUserUrl().isEmpty() == false) {
                Model.instance.getImage(pet.getImageUserUrl(), new Model.GetImageListener() {
                    @Override
                    public void onSuccess(Bitmap image) {
                        userPic.setImageBitmap(image);
                        savingProfile.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFail() {

                    }
                });
            }

            if (pet.getImagePetUrl() != null && pet.getImagePetUrl().isEmpty() == false) {

                Model.instance.getImage(pet.getImagePetUrl(), new Model.GetImageListener() {
                    @Override
                    public void onSuccess(Bitmap image) {
                        petPic.setImageBitmap(image);
                        savingProfile.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFail() {

                    }
                });
            }
        }

        Button cancelBtn = (Button) contentView.findViewById(R.id.update_CancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                PetListFragment listFragment = PetListFragment.newInstance();
                FragmentTransaction tran = getFragmentManager().beginTransaction();
                tran.replace(R.id.main_container, listFragment);
                tran.commit();
            }
        });

        Button saveBtn = (Button) contentView.findViewById(R.id.update_SaveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

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
                                            updateUser(pet, petName.getText().toString(), petType.getText().toString(), Integer.parseInt(petAge.getText().toString()), petInfo.getText().toString(), ownerName.getText().toString(), ownerPhone.getText().toString(), currentUser, ownerAddress.getText().toString(), urlOwner, urlPet);
                                        }

                                        @Override
                                        public void fail() {
                                            Log.d("TAG", "Error saving Image");
                                        }
                                    });
                                }
                                else{
                                    updateUser(pet, petName.getText().toString(), petType.getText().toString(), Integer.parseInt(petAge.getText().toString()), petInfo.getText().toString(), ownerName.getText().toString(), ownerPhone.getText().toString(), currentUser, ownerAddress.getText().toString(), urlOwner, ImageForPet);
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
                                    updateUser(pet, petName.getText().toString(), petType.getText().toString(), Integer.parseInt(petAge.getText().toString()), petInfo.getText().toString(), ownerName.getText().toString(), ownerPhone.getText().toString(), currentUser, ownerAddress.getText().toString(), ImageForUser, urlPet);
                                }

                                @Override
                                public void fail() {
                                    Log.d("TAG", "Error saving Image");
                                }
                            });
                        }
                        else {
                            updateUser(pet, petName.getText().toString(), petType.getText().toString(), Integer.parseInt(petAge.getText().toString()), petInfo.getText().toString(), ownerName.getText().toString(), ownerPhone.getText().toString(), currentUser, ownerAddress.getText().toString(), ImageForUser, ImageForPet);
                        }
                    }
                }
            }
        });

        userImageView = (ImageView) contentView.findViewById(R.id.update_owner_image);
        if (ImageForUser==null) {
            userImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dispatchTakePictureIntent();
                }
            });
        }

        petImageView = (ImageView) contentView.findViewById(R.id.update_pet_image);
        if (ImageForPet==null) {
            petImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dispatchTakePictureIntent2();
                }
            });
        }

        return contentView;
    }

    private void updateUser (User pet, String petName, String petType, int petAge, String petInfo, String ownerName, String ownerPhone, String userMail, String ownerAddress, String urlOwner, String urlPet){
        Model.instance.UpdateUser(pet, petName, petType, petAge, petInfo, ownerName, ownerPhone, userMail, ownerAddress, urlOwner, urlPet);
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
