package com.example.petme;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.petme.model.Model;
import com.example.petme.model.User;

import java.util.LinkedList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PetListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PetListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PetListFragment extends Fragment {

    static final int REQUEST_WRITE_STORAGE=3;

    private OnFragmentInteractionListener mListener;
    ListView list;
    List<User> data = new LinkedList<>();
    UserListAdapter adapter;

    public PetListFragment() {
        // Required empty public constructor
    }

    public static PetListFragment newInstance() {
        PetListFragment fragment = new PetListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Model.instance.getCurrentlyConnectedUser();
        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_pet_list, container, false);
        final ProgressBar loadingList = (ProgressBar) contentView.findViewById(R.id.progressBar_list);
        final ImageButton addBtn = (ImageButton) contentView.findViewById(R.id.addPet_button);
        loadingList.setVisibility(View.GONE);
        adapter = new UserListAdapter();


        list = (ListView) contentView.findViewById(R.id.pet_list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d ("TAG", "row item was clicked at position " + position);
                String itemId = data.get(position).getID();
                mListener.onItemSelected(itemId);
            }
        });

        loadingList.setVisibility(View.VISIBLE);
        //data = Model.instance.getAllUsers();
        //if (data==null){
            Model.instance.getAllUsers(new Model.GetAllUsersAndObserveCallback(){
                @Override
                public void onComplete(List<User> petsList) {
                    data = petsList;
//                    for (int j=0;j<data.size();j++)
//                        Model.instance.deleteUser(String.valueOf(j));
                    for (int i=0;i<data.size();i++)
                        Model.instance.addToSQL(data.get(i));
                    adapter.notifyDataSetChanged();
                    loadingList.setVisibility(View.GONE);
                    addBtn.setVisibility (View.VISIBLE);
                }

                @Override
                public void onCancel() { }
            });
//        }
//        else
//        {
//            adapter.notifyDataSetChanged();
//            loadingList.setVisibility(View.GONE);
//            addBtn.setVisibility (View.VISIBLE);
//        }


        boolean hasPermission = (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(getActivity(),new String[]{
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
        }

            addBtn.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View w){
                    Log.d("TAG","Image button clicked");
                    Fragment detailsFragment = new PetDetailsFragment();
                    FragmentTransaction tran = getFragmentManager().beginTransaction();
                    tran.replace(R.id.main_container, detailsFragment);
                    tran.addToBackStack("");
                    tran.commit();
                }
            });

        return contentView;

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_pet_list, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
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
        void onItemSelected(String itemId);
    }

    class UserListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView==null){
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.pet_list_row, null);
            }

            TextView petName = (TextView) convertView.findViewById(R.id.pet_row_name);
            TextView userName = (TextView) convertView.findViewById(R.id.owner_row_name);
            final ImageView imageProfile = (ImageView) convertView.findViewById(R.id.pet_row_image);

            int id = getResources().getIdentifier("com.example.petme:drawable/peticon", null, null);
            imageProfile.setImageResource(id);

            final ProgressBar imageLoading = (ProgressBar) convertView.findViewById(R.id.pet_row_progress_bar);
            imageLoading.setVisibility(View.GONE);
            final User pet = data.get(position);

            imageProfile.setTag(pet.getImagePetUrl());
            if (pet.getImagePetUrl()!=null && pet.getImagePetUrl().isEmpty()==false)
            {
                imageLoading.setVisibility(View.VISIBLE);
                Model.instance.getImage(pet.getImagePetUrl(), new Model.GetImageListener() {
                    @Override
                    public void onSuccess(Bitmap image) {
                        String imURL = imageProfile.getTag().toString();
                        if (imURL.equals(pet.getImagePetUrl())) {
                            imageProfile.setImageBitmap(image);
                            imageLoading.setVisibility(View.GONE);
                        }
                    }
                    @Override
                    public void onFail() { }
                });
            }

            petName.setText(pet.getPetName());
            userName.setText(pet.getUserName());
            return convertView;
        }
    }
}
