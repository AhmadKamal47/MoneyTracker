package com.example.moneytracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import Model.Data;

public class myadapter extends FirebaseRecyclerAdapter<Data,myadapter.MyViewHolder> {

    public
    myadapter(@NonNull FirebaseRecyclerOptions<Data> options) {
        super(options);
    }

    @Override
    protected
    void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Data model) {
        holder.mamt.setText(String.valueOf(model.getAmount()));
        holder.mdate.setText(model.getDate());
        holder.mnote.setText(model.getNote());
        holder.mtype.setText(model.getType());



    }

    @NonNull
    @Override
    public
    MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.income_recycler_data,parent,false);
       return  new MyViewHolder(view);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mtype, mnote, mdate, mamt;
        public
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mtype = itemView.findViewById(R.id.type_text_income);
            mnote = itemView.findViewById(R.id.note_text_income);
            mdate = itemView.findViewById(R.id.date_text_income);
            mamt = itemView.findViewById(R.id.amount_text_income);

        }
    }
}