package com.example.jean.testcontact.adapter;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import com.example.jean.testcontact.R;
import com.example.jean.testcontact.modele.Contact;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by JEAN on 17/05/2017.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder> implements LocationListener{
    private ArrayList<Contact> lesContacts;
    private Context context;
    private int origin; // origin = 1 alors contacts local sinon contact sim
    private LocationManager locationManager; //Service de localisation
    private String longi;
    private String lati;

    public ContactAdapter(ArrayList<Contact> lesContacts, Context context, int origin) {
        this.lesContacts = lesContacts;
        this.context = context;
        this.origin = origin;
    }

    @Override
    public ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ligne_content, parent, false);
        ContactHolder contactHolder = new ContactHolder(view);
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        return contactHolder;
    }

    @Override
    public void onBindViewHolder(ContactHolder holder, int position) {
        holder.nom.setText(lesContacts.get(position).getName());
        holder.tel.setText(lesContacts.get(position).getTel());
        //new QuickContactHelper(context, holder.quickContact, holder.tel.getText().toString());

        if(origin == 1) {
            holder.quickContact.setImageBitmap(retrieveContactPhoto(context, holder.tel.getText().toString()));
        }
    }

    @Override
    public int getItemCount() {
        return lesContacts.size();
    }

    @Override
    public void onLocationChanged(Location location) {
        longi = ""+location.getLongitude();
        lati = ""+location.getLatitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    class ContactHolder extends RecyclerView.ViewHolder {
        TextView nom;
        TextView tel;
        QuickContactBadge quickContact;
        ImageButton btnCall;

        public ContactHolder(View itemView) {
            super(itemView);
            nom = (TextView) itemView.findViewById(R.id.idNom);
            tel = (TextView) itemView.findViewById(R.id.idTel);
            quickContact = (QuickContactBadge) itemView.findViewById(R.id.idBadge);
            btnCall = (ImageButton) itemView.findViewById(R.id.btnCall);

            btnCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + tel.getText().toString()));
                    context.startActivity(callIntent);
                }
            });

            quickContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("SMS SOS")
                            .setMessage("Envoyer un message d'urgence ainsi que vos coordonnées GPS à ce destinataire ?")
                            .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    sendSMS(tel.getText().toString(), lati, longi, nom.getText().toString());
                                }
                            })
                            .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    return;
                                }
                            });
                    Dialog dialog = builder.create();
                    dialog.show();
                }
            });
        }
    }

    private void sendSMS(String s, String lati, String longi, String nom) {
        SmsManager smsManager = SmsManager.getDefault();
        StringBuffer sms = new StringBuffer();
        sms.append(nom+" j'ai besoin de ton aide au plus vite ! ");
        sms.append("envoie-moi des secours ou retrouve moi à cette position : ");
        sms.append("latitude "+lati+" et longitude "+longi);
        smsManager.sendTextMessage(s, null, sms.toString(), null,null);
    }

    public static Bitmap retrieveContactPhoto(Context context, String number) {
        if(number == null){
            return null;
        }
        ContentResolver contentResolver = context.getContentResolver();
        String contactId = null;
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));

        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID};

        Cursor cursor =
                contentResolver.query(
                        uri,
                        projection,
                        null,
                        null,
                        null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID));
            }
            cursor.close();
        }

        Bitmap photo = null;

        try {
            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(),
                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contactId)));

            if (inputStream != null) {
                photo = BitmapFactory.decodeStream(inputStream);
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
        return photo;
    }
}