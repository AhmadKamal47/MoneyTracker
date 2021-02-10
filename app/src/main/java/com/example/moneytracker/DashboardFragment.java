package com.example.moneytracker;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;

import Model.Data;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public
class DashboardFragment extends Fragment {

    dsIncomeAapter adapter;
    dsExpenseAdapter adp;
    TextView incomeSumResult;
    TextView expenseSumResult;
    private
    RecyclerView mRecyclerIncome,mRecyclerExpense;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FloatingActionButton fab_main;
    private FloatingActionButton fab_income;
    private FloatingActionButton fab_expense;
    private TextView fab_income_txt;
    private TextView fab_expense_txt;
    private boolean isopen=false;
    private Animation fadeopen,fadeclose;
    private FirebaseAuth mAuth;
    private DatabaseReference mIncomedatabase,mExpensedatabase;




    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static
    DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
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
        View myview= inflater.inflate(R.layout.fragment_dashboard, container, false);
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser mUser=mAuth.getCurrentUser();
        String uid=mUser.getUid();
        mIncomedatabase= FirebaseDatabase.getInstance().getReference().child("Income Data").child(uid);
        mExpensedatabase= FirebaseDatabase.getInstance().getReference().child("Expense Data").child(uid);
        fab_main=myview.findViewById(R.id.main_plus_button);
        fab_income=myview.findViewById(R.id.income_ft_button);
        fab_expense=myview.findViewById(R.id.expense_ft_button);
        fab_income_txt=myview.findViewById(R.id.income_ft_text);
        fab_expense_txt=myview.findViewById(R.id.expense_ft_text);
        incomeSumResult=(TextView)myview.findViewById(R.id.dashboard_income);
        expenseSumResult=(TextView)myview.findViewById(R.id.dashboard_expense);
        mRecyclerIncome=myview.findViewById(R.id.dashbaord_recycler_income);
        mRecyclerExpense=myview.findViewById(R.id.dashboard_recycler_expense);
        fadeopen= AnimationUtils.loadAnimation(getActivity(),R.anim.fade_open);
        fadeclose= AnimationUtils.loadAnimation(getActivity(),R.anim.fade_close);
        LinearLayoutManager layoutManagerincome=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        layoutManagerincome.setStackFromEnd(true);
        layoutManagerincome.setReverseLayout(true);
        mRecyclerIncome.setHasFixedSize(true);
        mRecyclerIncome.setLayoutManager(layoutManagerincome);
        FirebaseRecyclerOptions<Data> options = new FirebaseRecyclerOptions.Builder<Data>()
                .setQuery(mIncomedatabase, Data.class)
               .build();
        adapter = new dsIncomeAapter(options);
        mRecyclerIncome.setAdapter(adapter);
        adapter.startListening();
        LinearLayoutManager layoutManagerexpense=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        layoutManagerexpense.setStackFromEnd(true);
        layoutManagerexpense.setReverseLayout(true);
        mRecyclerExpense.setHasFixedSize(true);
        mRecyclerExpense.setLayoutManager(layoutManagerexpense);
        FirebaseRecyclerOptions<Data> option = new FirebaseRecyclerOptions.Builder<Data>()
                .setQuery(mExpensedatabase, Data.class)
                .build();
        adp = new dsExpenseAdapter(option);
        mRecyclerExpense.setAdapter(adp);
        adp.startListening();
        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View v) {
                if(isopen)
                {
                    fab_income.setAnimation(fadeclose);
                    fab_expense.setAnimation(fadeclose);
                    fab_income.setClickable(false);
                    fab_expense.setClickable(false);
                    fab_income_txt.startAnimation(fadeclose);
                    fab_expense_txt.startAnimation(fadeclose);
                    fab_income_txt.setClickable(false);
                    fab_expense_txt.setClickable(false);
                    isopen=false;
                }
                else{
                    fab_income.setAnimation(fadeopen);
                    fab_expense.setAnimation(fadeopen);
                    fab_income.setClickable(true);
                    fab_expense.setClickable(true);
                    fab_income_txt.startAnimation(fadeopen);
                    fab_expense_txt.startAnimation(fadeopen);
                    fab_income_txt.setClickable(true);
                    fab_expense_txt.setClickable(true);
                    isopen=true;
                }

            }
        });
        addData();
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
        mExpensedatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public
            void onDataChange(@NonNull DataSnapshot snapshot) {
                int expenseSum=0;
                for(DataSnapshot mySnapshot:snapshot.getChildren()){
                    Data data=mySnapshot.getValue(Data.class);
                    expenseSum=expenseSum+data.getAmount();
                    String strExpenseSum=String.valueOf(expenseSum);
                    expenseSumResult.setText(strExpenseSum);
                }
            }

            @Override
            public
            void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return myview;
    }


    public void addData(){
        fab_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View v) {
                incomeInsertdata();
            }
        });
        fab_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View v) {
                ExpenseInsertdata();
            }
        });
    }
    public void incomeInsertdata(){
        AlertDialog.Builder mydailoge=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        View myviewm=inflater.inflate(R.layout.layout_insertdata,null);
        mydailoge.setView(myviewm);
        AlertDialog dialoge=mydailoge.create();
        EditText edtAmount=myviewm.findViewById(R.id.amount_edt);
        EditText edtType=myviewm.findViewById(R.id.type_edt);
        EditText edtNote=myviewm.findViewById(R.id.Note_edt);

        Button btnSave=myviewm.findViewById(R.id.btnSave);
        Button btnCancel=myviewm.findViewById(R.id.btncancel);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type=edtType.getText().toString().trim();
                String amount=edtAmount.getText().toString().trim();
                String note=edtNote.getText().toString().trim();

                if(TextUtils.isEmpty(type)){
                    edtType.setError("Required Field..");
                    return;
                }
                if(TextUtils.isEmpty(amount)){
                    edtType.setError("Required Field..");
                    return;
                }

                String id =mIncomedatabase.push().getKey();
                String mDate= DateFormat.getDateInstance().format(new Date());
                int ouramount=Integer.parseInt(amount);
                Data data=new Data(ouramount,mDate,id,note,type);
                mIncomedatabase.child(id).setValue(data);
                Toast.makeText(getActivity(),"Data Added..",Toast.LENGTH_SHORT).show();
                dialoge.dismiss();

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View v) {
                dialoge.dismiss();
            }
        });
        dialoge.show();

    }
    public void ExpenseInsertdata(){
        AlertDialog.Builder mydailoge=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        View myviewm=inflater.inflate(R.layout.layout_insertdata,null);
        mydailoge.setView(myviewm);
        AlertDialog dialoge=mydailoge.create();
        EditText edtAmount=myviewm.findViewById(R.id.amount_edt);
        EditText edtType=myviewm.findViewById(R.id.type_edt);
        EditText edtNote=myviewm.findViewById(R.id.Note_edt);

        Button btnSave=myviewm.findViewById(R.id.btnSave);
        Button btnCancel=myviewm.findViewById(R.id.btncancel);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type=edtType.getText().toString().trim();
                String amount=edtAmount.getText().toString().trim();
                String note=edtNote.getText().toString().trim();

                if(TextUtils.isEmpty(type)){
                    edtType.setError("Required Field..");
                    return;
                }
                if(TextUtils.isEmpty(amount)){
                    edtType.setError("Required Field..");
                    return;
                }

                String id =mExpensedatabase.push().getKey();
                String mDate= DateFormat.getDateInstance().format(new Date());
                int ouramount=Integer.parseInt(amount);
                Data data=new Data(ouramount,mDate,id,note,type);
                mExpensedatabase.child(id).setValue(data);
                Toast.makeText(getActivity(),"Data Added..",Toast.LENGTH_SHORT).show();
                dialoge.dismiss();

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View v) {
                dialoge.dismiss();
            }
        });
        dialoge.show();

    }
}