package com.example.suresh.mapsample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showMap(View view){
        Intent iMap = new Intent(this, MapsActivity.class);
        startActivity(iMap);
    }

    public void showSQLite(View view) {
        Intent iSQLite = new Intent(this, SQLiteActivity.class);
        startActivity(iSQLite);
    }

    public void showLocationActivity(View view){
        Intent iLocation = new Intent(this, LocationActivity.class);
        startActivity(iLocation);
    }
}
