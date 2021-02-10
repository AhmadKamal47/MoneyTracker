package com.example.moneytracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import Model.Data;

public
class dsIncomeAapter extends FirebaseRecyclerAdapter<Data,dsIncomeAapter.MyViewHolder> {

    public
    dsIncomeAapter(@NonNull FirebaseRecyclerOptions<Data> options) {
        super(options);
    }

    @Override
    protected
    void onBindViewHolder(@NonNull dsIncomeAapter.MyViewHolder holder, int position, @NonNull Data model) {
        holder.mamt.setText(String.valueOf(model.getAmount()));
        holder.mdate.setText(model.getDate());
        holder.mnote.setText(model.getNote());
        holder.mtype.setText(model.getType());
    }

    @NonNull
    @Override
    public
    dsIncomeAapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_income_item,parent,false);
        return  new dsIncomeAapter.MyViewHolder(view);
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mtype, mnote, mdate, mamt;
        public
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mtype = itemView.findViewById(R.id.type_income_ds);
            mnote = itemView.findViewById(R.id.note_income_ds);
            mdate = itemView.findViewById(R.id.date_income_ds);
            mamt = itemView.findViewById(R.id.amount_income_ds);

        }
    }
}
