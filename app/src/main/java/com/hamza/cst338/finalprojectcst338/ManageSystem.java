package com.hamza.cst338.finalprojectcst338;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class ManageSystem extends AppCompatActivity implements View.OnClickListener {

    //Linking up buttons
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_system);

        Button checkButton = findViewById(R.id.checkButton);
        checkButton.setOnClickListener(this);
    }

    //Deciding what button will do
    @Override
    public void onClick(View v){
        if(v.getId() == R.id.checkButton){
            EditText username = findViewById(R.id.usernameEditText);
            EditText password = findViewById(R.id.passwordEditText);
            String user = username.getText().toString();
            String pass = password.getText().toString();

            //Verification of admin
            Account a = new Account();
            if(a.adminVerify(user, pass))
            {
                Intent i = new Intent(this, SystemLogs.class);
                startActivity(i);
            }
            else{           //Pop up for error
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final Intent intent = new Intent(this, MainActivity.class);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(intent);
                    }
                });
                builder.setTitle("Wrong Credentials");
                builder.setMessage("Check username and password and try again.");
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }
}
