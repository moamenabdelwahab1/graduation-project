package com.example.shoppingapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.ReportViewHolder> {

    private List<Transaction> transactions;

    public ReportsAdapter(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void updateData(List<Transaction> newTransactions) {
        this.transactions = newTransactions;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);

        // Bind transaction details to the TextViews
        holder.textViewTransactionId.setText("Transaction ID: " + transaction.getId());
        holder.textViewUserId.setText("User ID: " + transaction.getUserId());
        holder.textViewProfit.setText("Profit: " + transaction.getProfit());
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public static class ReportViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTransactionId, textViewUserId, textViewProfit;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTransactionId = itemView.findViewById(R.id.text_view_transaction_id);
            textViewUserId = itemView.findViewById(R.id.text_view_user_id);
            textViewProfit = itemView.findViewById(R.id.text_view_profit);
        }
    }
}
