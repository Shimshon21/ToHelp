package com.example.fifol.tohelp.DeliveriesActivities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.fifol.tohelp.DonatorActivity.ScanBarCode;
import com.example.fifol.tohelp.R;
import com.example.fifol.tohelp.Utils.MyOrdersData;


public class MissionDetails extends Fragment {

    Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mission_details, container, false);
    }

    //Todo set views texts!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        MyOrdersData myOrdersData = (MyOrdersData) bundle.get("missionDetails");
        System.out.println(myOrdersData._id);
        button=view.findViewById(R.id.closeDetailsFrag);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(MissionDetails.this);
                fragmentTransaction.commit();
            }
        });
    }
}
