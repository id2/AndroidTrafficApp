package com.example.tyler.trafficapp;
import com.example.tyler.trafficapp.R;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {
    String msg = "Android : "; //Added to implement logging functionality

    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // TextView text = findViewById(R.id.textView2);
      //  text.setText(SQLConnect()); //to prove sql server connected
    }

    public void viewCamList(View view) {
        Intent viewCameras = new Intent(this, CameraList.class);
        startActivity(viewCameras);
    }

    public void viewMap(View view){
        Intent mapView = new Intent(this, MapsActivity.class);
        startActivity(mapView);
    }

    public void viewJSON(View view){
        Intent ViewJSON = new Intent(this, Camera_JSON.class);
        startActivity(ViewJSON);
    }
}
