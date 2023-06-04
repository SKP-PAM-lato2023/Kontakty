package com.example.kontakty;


import android.app.ListActivity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.Manifest;
import android.provider.ContactsContract;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.loader.content.CursorLoader;

public class MainActivity extends ListActivity {

    private final int code = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uzyskajZgode();
    }

    private void uzyskajZgode(){
        if(checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
            }
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, code);
        }
        else{
            pobierzKontakty();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == code){
            if(grantResults.length ==1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                pobierzKontakty();
            }else{
                Toast.makeText(this, "brak uprawnień do odczytania kontaktów", Toast.LENGTH_SHORT).show();
            }
        }else{
            super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        }

    }

    private void pobierzKontakty() {
        Uri urKontakty = ContactsContract.Contacts.CONTENT_URI;
        Cursor c;
        String[] polaProjekcji = new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME};
        String sortowanie = ContactsContract.Contacts.DISPLAY_NAME + " desc";

        CursorLoader cursorLoader = new CursorLoader(this,
                urKontakty, polaProjekcji, null, null, sortowanie);
        c = cursorLoader.loadInBackground();

        int[] kontrolki = new int[]{R.id.txtID, R.id.txtNazwa};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.activity_main, c, polaProjekcji, kontrolki,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        setListAdapter(adapter);
    }
}