package com.example.cofex_project_ba26_group6.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cofex_project_ba26_group6.Database.DatabaseHelper;
import com.example.cofex_project_ba26_group6.R;

public class TransactionListAdapter extends RecyclerView.Adapter<TransactionListAdapter.CustomHolder> {
    Cursor cursorTransaction;
    DatabaseHelper dbHelper;
    int userId;

    public TransactionListAdapter(Context context, int userId) {
        dbHelper = new DatabaseHelper(context, DatabaseHelper.DB_NAME, null, DatabaseHelper.DB_VERSION);
        this.cursorTransaction = dbHelper.getDatabaseData("`Transaction`", "userId = " + userId);
        this.userId = userId;
    }

    @NonNull
    @Override
    public TransactionListAdapter.CustomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_transaction, parent, false);
        return new TransactionListAdapter.CustomHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TransactionListAdapter.CustomHolder holder, final int position) {

        if (cursorTransaction.moveToPosition(position)) {
            holder.itemView.setTag(cursorTransaction.getInt(0));
            Cursor coffeeData = dbHelper.getDatabaseData("Coffee", "ID = " + cursorTransaction.getString(2));
            if (coffeeData.moveToFirst()) {
                holder.tvCoffeeName.setText(coffeeData.getString(1));
                holder.tvTotalPrice.setText("Rp. " + Integer.parseInt(cursorTransaction.getString(3)) * Integer.parseInt(coffeeData.getString(2)));
                holder.tvDate.setText(cursorTransaction.getString(4));
            }
        }
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.deleteDialog.setContentView(R.layout.alert_delete_transaction);
                holder.deleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                holder.btnDeleteYes = holder.deleteDialog.findViewById(R.id.btn_DeleteConfirmationYes);
                holder.btnDeleteNo = holder.deleteDialog.findViewById(R.id.btn_DeleteConfirmationNo);

                holder.deleteDialog.show();
                holder.btnDeleteYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (cursorTransaction.moveToPosition(position)) {
                            dbHelper.dbDelete(cursorTransaction.getInt(0));
                        }
                        cursorTransaction = dbHelper.getDatabaseData("`Transaction`", "userId = " + userId);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, cursorTransaction.getCount());
                        Toast.makeText(v.getContext(), "Transaction deleted", Toast.LENGTH_SHORT).show();
                        holder.deleteDialog.dismiss();
                    }
                });
                holder.btnDeleteNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.deleteDialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return cursorTransaction.getCount();
    }

    public void dataChange() {
        cursorTransaction = dbHelper.getDatabaseData("`Transaction`", "userId = " + userId);
        notifyDataSetChanged();
    }

    public class CustomHolder extends RecyclerView.ViewHolder {
        TextView tvCoffeeName, tvTotalPrice, tvDate;
        ImageButton btnDelete;
        Dialog deleteDialog;
        Button btnDeleteYes, btnDeleteNo;

        public CustomHolder(@NonNull View itemView) {
            super(itemView);
            tvCoffeeName = itemView.findViewById(R.id.tv_TransactionCoffeeName);
            tvTotalPrice = itemView.findViewById(R.id.tv_TransactionTotalPrice);
            tvDate = itemView.findViewById(R.id.tv_TransactionDate);
            btnDelete = itemView.findViewById(R.id.btn_Delete);
            deleteDialog = new Dialog(itemView.getContext());
        }
    }
}
