package com.example.petme;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class LogInFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View contentView =  inflater.inflate(R.layout.fragment_log_in, container, false);

        Button registerBtn = (Button) contentView.findViewById(R.id.signUpButton);
        registerBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Fragment detailsFragment = new PetDetailsFragment ();
                FragmentTransaction tran = getFragmentManager().beginTransaction();
                tran.replace(R.id.main_container, detailsFragment);
                //tran.addToBackStack("");
                tran.commit();
            }
        });

        Button logInBtn = (Button) contentView.findViewById(R.id.signInButton);
        logInBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                PetListFragment petListFragment = PetListFragment.newInstance();
                //Fragment petListFragment = new PetListFragment();
                FragmentTransaction tran = getFragmentManager().beginTransaction();
                tran.replace(R.id.main_container, petListFragment);
                tran.commit();
            }
        });
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_log_in, container, false);
        return contentView;
    }
}
