package com.example.petme;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.petme.model.Model;
import com.example.petme.model.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserPageFragment extends Fragment {

    User pet;
    final String userMail = Model.instance.GetMailOfCurrentUser();

    private OnFragmentInteractionListener mListener;
    private static final String ARG_PARAM1 = "itemId";
    private String itemId;

    public UserPageFragment() {
        // Required empty public constructor
    }

    public static UserPageFragment newInstance(String param1) {
        UserPageFragment fragment = new UserPageFragment();
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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_user_page, container, false);
        setHasOptionsMenu(true);
        final View contentView = inflater.inflate(R.layout.fragment_user_page, container, false);


        Model.instance.getUser(itemId, new Model.GetUserCallback(){
            @Override
            public void onComplete(User pet) {

                UserPageFragment.this.pet = pet;
                Log.d("TAG", "get pet name" + pet.getPetName());

                if (userMail.equals(pet.getUserMail())) {
                }

                TextView name = (TextView) contentView.findViewById (R.id.petName_view);
                TextView type = (TextView) contentView.findViewById(R.id.type_view);
                TextView age = (TextView) contentView.findViewById(R.id.age_view);
                TextView userName = (TextView) contentView.findViewById(R.id.userName_view);
                TextView address = (TextView) contentView.findViewById(R.id.userAddress_view);
                TextView phone = (TextView) contentView.findViewById(R.id.userPhone_view);
                TextView mail = (TextView) contentView.findViewById(R.id.userMail_view);
                final ImageView userPic = (ImageView) contentView.findViewById(R.id.ownerImage_view) ;
                final ImageView petPic = (ImageView) contentView.findViewById(R.id.petImage_view) ;


                name.setText(pet.getPetName());
                type.setText("Type: " + pet.getPetType());
                age.setText ("Age: " + pet.getPetAge());
                userName.setText(pet.getUserName());
                address.setText(pet.getUserAddress());
                phone.setText(pet.getUserPhone());
                mail.setText(pet.getUserMail());

                if (pet.getImageUserUrl()!=null && pet.getImageUserUrl().isEmpty()==false) {

                    Model.instance.getImage(pet.getImageUserUrl(), new Model.GetImageListener() {
                        @Override
                        public void onSuccess(Bitmap image) {
                            userPic.setImageBitmap(image);
                        }

                        @Override
                        public void onFail() {

                        }
                    });
                }

                if (pet.getImagePetUrl()!=null && pet.getImagePetUrl().isEmpty()==false) {

                    Model.instance.getImage(pet.getImagePetUrl(), new Model.GetImageListener() {
                        @Override
                        public void onSuccess(Bitmap image) {
                            petPic.setImageBitmap(image);
                        }

                        @Override
                        public void onFail() {}
                    });
                }
            }

            @Override
            public void onCancel() {
                Log.d("TAG", "get pet cancel");
            }
        });

        return contentView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_edit:
                    if (userMail.equals(pet.getUserMail()))
                        mListener.onEdit(pet.getID());
                    else
                    {
                        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this.getActivity());
                        dlgAlert.setMessage("You are not permitted to edit this section");
                        dlgAlert.setTitle("Edit pet");
                        dlgAlert.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {}
                                });

                        dlgAlert.setCancelable(true);
                        dlgAlert.create().show();
                    }
                    break;
                case R.id.menu_delete:
                    if (userMail.equals(pet.getUserMail()))
                        mListener.onDelete(pet.getID());
                    else
                    {
                        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this.getActivity());
                        dlgAlert.setMessage("You are not permitted to delete this content");
                        dlgAlert.setTitle("Delete pet");
                        dlgAlert.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {}
                                });

                        dlgAlert.setCancelable(true);
                        dlgAlert.create().show();
                    }
                    break;
            }
            return true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PetListFragment.OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onEdit(String itemId);
        void onDelete(String itemId);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_edit, menu);
        inflater.inflate(R.menu.menu_delete, menu);
    }

}
