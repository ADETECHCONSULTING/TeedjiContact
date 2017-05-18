package com.example.jean.testcontact.vue;


import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Contacts;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jean.testcontact.R;
import com.example.jean.testcontact.adapter.ContactAdapter;
import com.example.jean.testcontact.modele.Contact;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSim extends Fragment {
    private ArrayList<Contact> mesContacts;
    private RecyclerView rv;
    private ContactAdapter contactAdapter;
    private Cursor cursorSim;
    private EditText search;
    private static final int PERMISSIONS_REQUEST = 100;

    public FragmentSim() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sim, container, false);

        mesContacts = new ArrayList<>();
        //Appel de la méthode qui gère les findViewByID
        retrieveView(view);

        //Méthode gère l'affichage des contacts du téléphone
        affichContact();

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {
                query = query.toString().toLowerCase();
                final ArrayList<Contact> filteredList = new ArrayList<Contact>();
                for(int i=0; i < mesContacts.size(); i++){
                    final String text = mesContacts.get(i).getName().toLowerCase();
                    if(text.contains(query)){
                        filteredList.add(mesContacts.get(i));
                    }
                }
                rv.setLayoutManager(new LinearLayoutManager(getActivity()));
                contactAdapter = new ContactAdapter(filteredList,getActivity(),2);
                rv.setAdapter(contactAdapter);
                contactAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    /**
     * Méthoe qui gère la création et l'affichage de la liste déroulante
     */
    private void affichContact() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST);
        }else {
            mesContacts = recupContactsSim();
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            rv.setLayoutManager(layoutManager);
            contactAdapter = new ContactAdapter(mesContacts, getActivity(), 2);
            rv.setAdapter(contactAdapter);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission accordée
                affichContact();
            } else {
                Toast.makeText(getActivity(), "Sans votre permission la liste des contacts ne peut être affichée", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Methode qui gère les findViewByID
     */
    private void retrieveView(View view) {
        rv = (RecyclerView) view.findViewById(R.id.idRecyclerSim);
        search = (EditText) view.findViewById(R.id.searchSim);
    }

    private ArrayList<Contact> recupContactsSim() {
        ArrayList<Contact> listeContact = new ArrayList();

        ContentResolver connectApp = getActivity().getContentResolver();
        Uri uri = Uri.parse("content://icc/adn");
        cursorSim = connectApp.query(uri, null, null, null, null);
        getActivity().startManagingCursor(cursorSim);

        if(cursorSim.moveToFirst()){
            do {
                String name = cursorSim.getString(cursorSim.getColumnIndex(Contacts.People.NAME));
                String tel = cursorSim.getString(cursorSim.getColumnIndex(Contacts.People.NUMBER));
                String id = cursorSim.getString(cursorSim.getColumnIndex(Contacts.People._ID));
                listeContact.add(new Contact(id, name, null, tel));
            } while (cursorSim.moveToNext());
        }
        return listeContact;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(cursorSim != null){
            cursorSim.close();
        }
    }
}
