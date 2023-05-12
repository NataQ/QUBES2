package id.co.qualitas.qubes.helper;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import java.util.ArrayList;
import java.util.Random;

import id.co.qualitas.qubes.model.OutletResponse;

public class MyDiffUtilCallback extends DiffUtil.Callback {
    ArrayList<OutletResponse> newList;
    ArrayList<OutletResponse> oldList;

    public MyDiffUtilCallback(ArrayList<OutletResponse> newList, ArrayList<OutletResponse> oldList) {
        this.newList = newList;
        this.oldList = oldList;
    }

    @Override
    public int getOldListSize() {
        return oldList != null ? oldList.size() : 0 ;
    }

    @Override
    public int getNewListSize() {
        // Simulate a really long running diff calculation.
        try {
            Thread.sleep(new Random().nextInt(3000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return newList != null ? newList.size() : 0 ;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return true;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        int result = newList.get(newItemPosition).compareTo(oldList.get(oldItemPosition));
        if (result==0){
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        OutletResponse newContact = newList.get(newItemPosition);
        OutletResponse oldContact = oldList.get(oldItemPosition);

        Bundle diff = new Bundle();
        if(newContact.isColor() != (oldContact.isColor())){
            diff.putBoolean("color", newContact.isColor());
        }
        if (diff.size()==0){
            return null;
        }
        return diff;
    }
}