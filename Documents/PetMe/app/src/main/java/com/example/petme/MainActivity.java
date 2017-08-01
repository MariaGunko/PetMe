package com.example.petme;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

import com.example.petme.model.Model;
import com.example.petme.model.User;

public class MainActivity extends Activity implements PetListFragment.OnFragmentInteractionListener, UserPageFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new CountDownTimer(2000, 1000) {

           public void onTick(long millisUntilFinished) { }

            public void onFinish() {
                Bundle b = getIntent().getExtras();
                int value = b.getInt("key");

                if (value == 2) {
                    Fragment detailsFragment = new PetDetailsFragment();
                    FragmentTransaction tran = getFragmentManager().beginTransaction();
                    tran.replace(R.id.main_container, detailsFragment);
                    //tran.addToBackStack("");
                    tran.commit();
                } else {
                    if (value == 1) {
                        PetListFragment petListFragment = PetListFragment.newInstance();
                        FragmentTransaction tran = getFragmentManager().beginTransaction();
                        tran.replace(R.id.main_container, petListFragment);
                        tran.commit();
                    }
                }
            }
        }.start();
    }

    @Override
    public void onItemSelected(String itemId) {
        Log.d("TAG","List frag onItemSelected" + itemId);
        UserPageFragment userPageFragment = UserPageFragment.newInstance(itemId);
        FragmentTransaction tran = getFragmentManager().beginTransaction();
        tran.replace(R.id.main_container, userPageFragment);
        tran.addToBackStack("");
        tran.commit();
    }

    @Override
    public void onEdit(String itemId) {
        Log.d("TAG","Menu Item Selected " + itemId);
        UpdatePetFragment updatePetFragment = UpdatePetFragment.newInstance(itemId);
        FragmentTransaction tran = getFragmentManager().beginTransaction();
        tran.replace(R.id.main_container, updatePetFragment);
        tran.commit();
    }

    @Override
    public void onDelete(String itemId) {
        Model.instance.getUser(itemId, new Model.GetUserCallback(){
            @Override
            public void onComplete(User pet) {
                Model.instance.RemoveUser(pet);
            }

            @Override
            public void onCancel() {
            }
        });

        PetListFragment petListFragment = PetListFragment.newInstance();
        FragmentTransaction tran = getFragmentManager().beginTransaction();
        tran.replace(R.id.main_container, petListFragment);
        tran.commit();
    }
}
