package com.example.moneytracker;

import android.appwidget.AppWidgetHost;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Model.Data;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link incomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public
class incomeFragment extends Fragment {
    private RecyclerView recyclerView;
    FirebaseAuth mAuth;
    DatabaseReference mIncomedatabase;
    myadapter adapter;
    TextView incomeSumResult;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private AppWidgetHost firebaseRecyclerAdapter;

    public
    incomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment incomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static
    incomeFragment newInstance(String param1, String param2) {
        incomeFragment fragment = new incomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public
    View onCreateView(LayoutInflater inflater, ViewGroup container,
                      Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview = inflater.inflate(R.layout.fragment_income, container, false);
        recyclerView =(RecyclerView) myview.findViewById(R.id.recycler_id_income);
        incomeSumResult=(TextView) myview.findViewById(R.id.income_text_result);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser mUser=mAuth.getCurrentUser();
        String uid=mUser.getUid();
        mIncomedatabase= FirebaseDatabase.getInstance().getReference().child("Income Data").child(uid);



        FirebaseRecyclerOptions<Data> options = new FirebaseRecyclerOptions.Builder<Data>()
                .setQuery(mIncomedatabase, Data.class)
                .build();
        adapter = new myadapter(options);
        recyclerView.setAdapter(adapter);
        mIncomedatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public
            void onDataChange(@NonNull DataSnapshot snapshot) {
                int incomeSum=0;
                for(DataSnapshot mySnapshot:snapshot.getChildren()){
                    Data data=mySnapshot.getValue(Data.class);
                    incomeSum=incomeSum+data.getAmount();
                    String strIncomeSum=String.valueOf(incomeSum);
                    incomeSumResult.setText(strIncomeSum);
                }
            }

            @Override
            public
            void onCancelled(@NonNull DatabaseError error) {

            }
        });




        return myview;
    }

    @Override
    public
    void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public
    void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}


