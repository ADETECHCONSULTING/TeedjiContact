package com.example.jean.testcontact.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jean.testcontact.R;
import com.example.jean.testcontact.modele.Contact;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by JEAN on 17/05/2017.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder>{
    private ArrayList<Contact> lesContacts;
    private Context context;

    public ContactAdapter(ArrayList<Contact> lesContacts, Context context){
        this.lesContacts = lesContacts;
        this.context = context;
    }
    @Override
    public ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ligne_content, parent, false);
        ContactHolder contactHolder = new ContactHolder(view);
        return contactHolder;
    }

    @Override
    public void onBindViewHolder(ContactHolder holder, int position) {
        holder.nom.setText(lesContacts.get(position).getName());
        holder.tel.setText(lesContacts.get(position).getTel());
        Picasso.with(context).load(R.drawable.male).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return lesContacts.size();
    }

    class ContactHolder extends RecyclerView.ViewHolder{
        TextView nom;
        TextView tel;
        ImageView img;

        public ContactHolder(View itemView) {
            super(itemView);
            nom = (TextView) itemView.findViewById(R.id.idNom);
            tel = (TextView) itemView.findViewById(R.id.idTel);
            img = (ImageView) itemView.findViewById(R.id.idPhoto);
        }
    }

}