package com.sanjaydevtech.firechat;

import androidx.recyclerview.widget.DiffUtil;

import java.util.ArrayList;

public class MyDiffUtil extends DiffUtil.Callback {

    private ArrayList<Chats> oldList;
    private ArrayList<Chats> newList;

    public MyDiffUtil(ArrayList<Chats> oldList, ArrayList<Chats> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {

        // Compare the uniqueness... here the key
        return oldList.get(oldItemPosition).getKey().equals(newList.get(newItemPosition).getKey());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {

        // Check whole object
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }
}
