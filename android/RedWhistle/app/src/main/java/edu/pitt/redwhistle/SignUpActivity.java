package edu.pitt.redwhistle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    /**
     * Collects user data from the user
     * @param view
     */
    public void signUp(View view) {
        System.out.println("clicked");
        EditText userName = (EditText) findViewById(R.id.user_name);
        EditText userToken = (EditText) findViewById(R.id.user_token);

        // Stores user data as a property file in the device
        Properties prop = new Properties();
        prop.setProperty("username", userName.getText().toString());
        prop.setProperty("userToken", userToken.getText().toString());
        try {
            FileOutputStream fout = new FileOutputStream(new File(getFilesDir(), MainActivity.USER_PROPS));
            prop.store(fout, "");
            fout.close();
            TextView textView = findViewById(R.id.user_name);
            textView.setText(prop.getProperty("username"));

            // Redirects to welcome screen
            Intent intent = new Intent(this, HelloActivity.class);
            startActivity(intent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
