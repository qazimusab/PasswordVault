package com.trendoidtechnologies.vault.ui.adapter;

/**
 * Created by qazimusab on 2/26/16.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trendoidtechnologies.vault.datacontract.Credential;
import com.trendoidtechnologies.vault.R;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class CredentialsRecyclerViewAdapter extends RecyclerView.Adapter<CredentialsRecyclerViewAdapter.ItemHolder> {

    private List<Credential> credentialList;
    private OnItemClickListener onItemClickListener;
    private LayoutInflater layoutInflater;

    public CredentialsRecyclerViewAdapter(Context context){
        layoutInflater = LayoutInflater.from(context);
        credentialList = new ArrayList<>();
    }

    @Override
    public CredentialsRecyclerViewAdapter.ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.layout_credential_item, parent, false);
        return new ItemHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(CredentialsRecyclerViewAdapter.ItemHolder holder, int position) {
        holder.setCredentials(credentialList.get(position));
    }

    @Override
    public int getItemCount() {
        return credentialList.size();
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

    public void add(Credential credential){
        credentialList.add(credential);
        notifyItemInserted(credentialList.size() - 1);
    }

    public Credential getItemAtPosition(int position){
        return credentialList.get(position);
    }

    public void add(int location, Credential credential){
        credentialList.add(location, credential);
        notifyItemInserted(location);
    }

    public void remove(int location){
        if(location >= credentialList.size())
            return;

        credentialList.remove(location);
        notifyItemRemoved(location);
    }

    public static class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private CredentialsRecyclerViewAdapter parent;
        private Credential credential;
        public TextView username;
        public TextView password;

        public ItemHolder(View itemView, CredentialsRecyclerViewAdapter parent) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.parent = parent;
            username = (TextView) itemView.findViewById(R.id.username);
            password = (TextView) itemView.findViewById(R.id.password);
        }

        @SuppressLint("SetTextI18n")
        public void setCredentials(Credential credential){
            username.setText("Username: " + credential.getUserName());
            password.setText("Password: " + credential.getPassword());
            this.credential = credential;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                username.setTransitionName(credential.getUserName());
                password.setTransitionName(credential.getPassword());
            }
        }

        public Credential getCredential(){
            return credential;
        }

        @SuppressWarnings("deprecation")
        @Override
        public void onClick(View v) {
            final OnItemClickListener listener = parent.getOnItemClickListener();
            if(listener != null){
                listener.onItemClick(this, getPosition());
            }
        }
    }
}
