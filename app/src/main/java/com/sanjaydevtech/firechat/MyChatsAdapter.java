package com.sanjaydevtech.firechat;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sanjaydevtech.firechat.databinding.ListChatsItemBinding;

import java.util.ArrayList;
import java.util.HashMap;

public class MyChatsAdapter extends RecyclerView.Adapter<MyChatsAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Chats> chats;
    private String Uid = FirebaseAuth.getInstance().getUid();

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

        if (chats.get(position).getUid().equals(Uid)) {
            holder.binding.receiverLayout.setVisibility(View.GONE);
            holder.binding.sendMsgTxt.setText(chats.get(position).getMsg());
        } else {
            holder.binding.senderLayout.setVisibility(View.GONE);
            holder.binding.msgTxt.setText(chats.get(position).getMsg());
        }

        holder.binding.baseLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (chats.get(position).getUid().equals(Uid)) {
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
                            .setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    dialogAlertForEdit(context, position);
                                }
                            })
                            .setNeutralButton("Nope", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create();
                    dialog.show();
                    return true;
                } else {
                    return false;
                }

            }
        });
    }

    private void dialogAlertForEdit(final Context context, final int position) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context)
                .setMessage("Edit Message");
        final EditText editMsgTxt = new EditText(context);
        editMsgTxt.setHint("Edit Msg");
        editMsgTxt.setText(chats.get(position).getMsg());
        editMsgTxt.setImeOptions(EditorInfo.IME_ACTION_DONE);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        editMsgTxt.setLayoutParams(layoutParams);
        dialog.setView(editMsgTxt);
        dialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseReference ref = FirebaseDatabase.getInstance()
                        .getReference("chats").child(chats.get(position).getKey());
                // Or we can do that in another way
                ref.child("msg").setValue(editMsgTxt.getText().toString());
            }
        })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
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
