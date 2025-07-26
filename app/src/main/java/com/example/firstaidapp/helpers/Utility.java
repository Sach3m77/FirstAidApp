package com.example.firstaidapp.helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Utility {
    static public void showAlertDialog(Context context, String message) {

        String[] instructions = {
                "1. Co się stało?",
                "2. Czy są poszkodowani.",
                "3. Miejsce zdarzenia (adres, nazwa obiektu, charakterystyczne cechy miejsca).",
                "4. Swoje imię i nazwisko, numer telefonu.",
                "Pamiętaj! Dyspozytor zawsze odkłada słuchawkę jako pierwszy!"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, instructions);

        ListView listView = new ListView(context);
        listView.setAdapter(adapter);

        new AlertDialog.Builder(context)
                .setMessage("Po wykręceniu numeru alarmowego i zgłoszeniu się dyspozytora spokojnie i wyraźnie powiedz:")
                .setView(listView)
                .setPositiveButton("Rozumiem", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                        dialIntent.setData(Uri.parse("tel:" + message));
                        context.startActivity(dialIntent);
                    }
                })
                .create()
                .show();
    }

}
