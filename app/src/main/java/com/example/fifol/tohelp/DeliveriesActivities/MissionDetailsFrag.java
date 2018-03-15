package com.example.fifol.tohelp.DeliveriesActivities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.example.fifol.tohelp.R;
import com.example.fifol.tohelp.Utils.MyOrdersData;
import android.widget.ArrayAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MissionDetailsFrag extends Fragment {
    TextView name ,address , phone;
    ListView listView ;
    Button closeFrag;

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
        setView(view);
        MyOrdersData myOrdersData = (MyOrdersData) bundle.get("missionDetails");
        if(myOrdersData!=null) {
            name.setText(myOrdersData._id);
            address.setText(myOrdersData.address);
            phone.setText(myOrdersData.phone);
            setProductsAdapter(myOrdersData);
        }
        onClickCloseFragemnt();
    }


    //Find views by id.
    private void setView(View view) {
        name = view.findViewById(R.id.name);
        address = view.findViewById(R.id.address);
        phone = view.findViewById(R.id.phone);
        closeFrag =view.findViewById(R.id.closeDetailsFrag);
        listView = view.findViewById(R.id.products);
    }

    //Set adapter for order products.
    public void setProductsAdapter(MyOrdersData myOrdersData){
        Map<String,Integer> productsMap = myOrdersData.products;
        List<String> productsList = new ArrayList<>();
        for(String key :productsMap.keySet()){
            productsList.add(key.replace("_"," ") +"  כמות: " + productsMap.get(key));
        }
        listView.setAdapter( new ArrayAdapter(getContext(),R.layout.array_textview_adapter,productsList));
    }


    //Close fragment.
    private void onClickCloseFragemnt() {
        closeFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(MissionDetailsFrag.this);
                fragmentTransaction.commit();
            }
        });
    }




}
