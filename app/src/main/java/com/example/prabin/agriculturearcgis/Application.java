package com.example.prabin.agriculturearcgis;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Prabin on 6/30/2018.
 */

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
