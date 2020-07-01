package com.sanjaydevtech.firechat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sanjaydevtech.firechat.databinding.ListChatsItemBinding;

import java.util.ArrayList;

public class MyChatsAdapter extends RecyclerView.Adapter<MyChatsAdapter.MyViewHolder>{

    private Context context;
    private ArrayList<Chats> chats;

    public MyChatsAdapter(Context context, ArrayList<Chats> chats) {
        this.context = context;
        this.chats = chats;
    }

    public void setChats(ArrayList<Chats> chats) {
        this.chats = chats;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(ListChatsItemBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.msgTxt.setText(chats.get(position).getMsg());
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ListChatsItemBinding binding;

        public MyViewHolder(@NonNull ListChatsItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
