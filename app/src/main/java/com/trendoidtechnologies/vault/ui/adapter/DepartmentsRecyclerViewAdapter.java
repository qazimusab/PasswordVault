package com.trendoidtechnologies.vault.ui.adapter;

/**
 * Created by qazimusab on 2/26/16.
 */

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trendoidtechnologies.vault.R;

import java.util.ArrayList;
import java.util.List;

public class DepartmentsRecyclerViewAdapter extends RecyclerView.Adapter<DepartmentsRecyclerViewAdapter.ItemHolder> {

    private List<String> itemsName;
    private OnItemClickListener onItemClickListener;
    private LayoutInflater layoutInflater;

    public DepartmentsRecyclerViewAdapter(Context context){
        layoutInflater = LayoutInflater.from(context);
        itemsName = new ArrayList<>();
    }

    @Override
    public DepartmentsRecyclerViewAdapter.ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.layout_item, parent, false);
        return new ItemHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(DepartmentsRecyclerViewAdapter.ItemHolder holder, int position) {
        holder.setItemName(itemsName.get(position));
    }

    @Override
    public int getItemCount() {
        return itemsName.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        onItemClickListener = listener;
    }

    public OnItemClickListener getOnItemClickListener(){
        return onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(ItemHolder item, int position);
    }

    public void add(String iName){
        itemsName.add(iName);
        notifyItemInserted(itemsName.size() - 1);
    }

    public void clear() {
        itemsName.clear();
    }

    public String getItemAtPosition(int position){
        return itemsName.get(position);
    }

    public void add(int location, String iName){
        itemsName.add(location, iName);
        notifyItemInserted(location);
    }

    public void remove(int location){
        if(location >= itemsName.size())
            return;

        itemsName.remove(location);
        notifyItemRemoved(location);
    }

    public static class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private DepartmentsRecyclerViewAdapter parent;
        public TextView textItemName;
//        public TextView background;

        public ItemHolder(View itemView, DepartmentsRecyclerViewAdapter parent) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.parent = parent;
            textItemName = (TextView) itemView.findViewById(R.id.item_name);
//            background = (TextView) itemView.findViewById(R.id.background);
        }

        public void setItemName(CharSequence name){
            textItemName.setText(name);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                textItemName.setTransitionName(name.toString());
            }
        }

        public CharSequence getItemName(){
            return textItemName.getText();
        }

        @Override
        public void onClick(View v) {
            final OnItemClickListener listener = parent.getOnItemClickListener();
            if(listener != null){
                listener.onItemClick(this, getPosition());
            }
        }
    }
}
