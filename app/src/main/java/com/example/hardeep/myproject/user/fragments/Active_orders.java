package com.example.hardeep.myproject.user.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.hardeep.myproject.R;
import com.example.hardeep.myproject.get_details;
import com.example.hardeep.myproject.user.OrderAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Active_orders extends Fragment {

    FirebaseAuth firebaseAuth;
    String userid,uid;
    RecyclerView recyclerView;
    ArrayList<String> orders,costs,statuses;
    RecyclerView.Adapter adapter;
    String order_id,amount;
    ProgressBar progressBar;
    DatabaseReference databaseReference,dataref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {

        orders.clear();
        costs.clear();
        statuses.clear();


        dataref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d:dataSnapshot.getChildren())
                {
                    uid=d.child("userid").getValue(String.class);

                    if(userid.equals(uid))
                    {
                        order_id=d.getRef().getKey();
                        amount=d.child("totalamount").getValue(String.class);
                        orders.add("#"+order_id);
                        costs.add("INR "+amount);
                        statuses.add(d.child("status").getValue(String.class));
                        adapter.notifyDataSetChanged();
                    }
                }
                if(orders.size()==0)
                {
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        firebaseAuth=FirebaseAuth.getInstance();
        userid=firebaseAuth.getCurrentUser().getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("1");
        dataref=FirebaseDatabase.getInstance().getReference().child("2").child("Accepted Orders");

        orders=new ArrayList<>();
        costs=new ArrayList<>();
        statuses=new ArrayList<>();



        View v= inflater.inflate(R.layout.fragment_active_orders, container, false);
        recyclerView=v.findViewById(R.id.orderrecyclerview);
        progressBar=v.findViewById(R.id.progressbaractive);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter=new OrderAdapter(orders,costs,statuses,getActivity(),progressBar);
        recyclerView.setAdapter(adapter);


        return v;
    }

}