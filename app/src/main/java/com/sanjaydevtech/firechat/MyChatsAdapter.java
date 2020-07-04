package com.sanjaydevtech.firechat;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sanjaydevtech.firechat.databinding.ListChatsItemBinding;

import java.util.ArrayList;

public class MyChatsAdapter extends RecyclerView.Adapter<MyChatsAdapter.MyViewHolder>{

    private Context context;
    private ArrayList<Chats> chats;

    public MyChatsAdapter(Context context, ArrayList<Chats> chats) {
        this.context = context;
        this.chats = chats;
    }

    public void setChats(ArrayList<Chats> newList) {
        MyDiffUtil diffUtil = new MyDiffUtil(chats, newList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtil);
        chats.clear();
        chats.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(ListChatsItemBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.binding.msgTxt.setText(chats.get(position).getMsg());
        holder.binding.baseLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setMessage("Edit or Delete Message")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseReference ref = FirebaseDatabase.getInstance()
                                        .getReference("chats").child(chats.get(position).getKey());
                                ref.removeValue();
                            }
                        })
                        .setNeutralButton("Edit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Here launch another dialog to edit....

                                // We will do that in next tutorial
                            }
                        })
                        .setNegativeButton("Nope", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                        dialog.show();
                return true;
            }
        });
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
