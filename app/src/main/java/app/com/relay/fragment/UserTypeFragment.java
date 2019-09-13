package app.com.relay.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import app.com.relay.R;

public class UserTypeFragment extends Fragment {

    private LinearLayout lyt_parent_client, lyt_parent_store, lyt_parent_deliverer;
    private boolean lytParentClient, lytParentStore, lytParentDeliverer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateUserTypeRegistrationChoice("NULL");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_type, container, false);
        initToolbar();
        initComponent(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Who are you ");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private void initComponent(View view) {

        lyt_parent_client = view.findViewById(R.id.lyt_parent_client);
        lyt_parent_store = view.findViewById(R.id.lyt_parent_store);
        lyt_parent_deliverer = view.findViewById(R.id.lyt_parent_deliverer);

        lyt_parent_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lytParentClient = changeState(true, lyt_parent_client);
                lytParentStore = changeState(false, lyt_parent_store);
                lytParentDeliverer = changeState(false, lyt_parent_deliverer);
                updateUserTypeRegistrationChoice("CLIENT");


            }
        });

        lyt_parent_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lytParentStore = changeState(true, lyt_parent_store);
                lytParentClient = changeState(false, lyt_parent_client);
                lytParentDeliverer = changeState(false, lyt_parent_deliverer);
                updateUserTypeRegistrationChoice("STORE");


            }
        });

        lyt_parent_deliverer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lytParentDeliverer = changeState(true, lyt_parent_deliverer);
                lytParentStore = changeState(false, lyt_parent_store);
                lytParentClient = changeState(false, lyt_parent_client);
                updateUserTypeRegistrationChoice("DELIVERER");

            }
        });
    }

    private boolean changeState(boolean lytParentState, LinearLayout lytParent) {
        if (lytParentState) {
            changeLayoutColor(true, lytParent);
            return false;
        } else {
            changeLayoutColor(false, lytParent);
            return true;
        }
    }

    private void changeLayoutColor(boolean state, LinearLayout linearLayout) {
        if (state) {
            linearLayout.setBackgroundColor(getResources().getColor(R.color.blue_100));
        } else {
            linearLayout.setBackgroundColor(Color.WHITE);
        }
    }

    private void updateUserTypeRegistrationChoice(String userType){
        SharedPreferences preferences =
                getContext().getSharedPreferences("REGISTRATION",
                        Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("USER_TYPE", userType);
        editor.apply();
    }


}
