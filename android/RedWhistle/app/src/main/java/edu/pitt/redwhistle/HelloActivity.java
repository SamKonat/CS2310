package edu.pitt.redwhistle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class HelloActivity extends AppCompatActivity {
    public static Properties prop = new Properties();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);

        FileInputStream fin = null;
        try {
            fin = new FileInputStream(new File(getFilesDir(), MainActivity.USER_PROPS));
        } catch (FileNotFoundException e) {
            System.out.println("User data not found.");
        }

        try {
            prop.load(new InputStreamReader(fin));
            fin.close();
        } catch (IOException e) {

        }
        TextView userName = findViewById(R.id.user_name);
        userName.setText(prop.getProperty("username"));

        File f = new File(getFilesDir(), MainActivity.USER_PROPS);
        f.delete();

        Intent intent = new Intent(this, ShakeService.class);
        startService(intent);
    }
}
