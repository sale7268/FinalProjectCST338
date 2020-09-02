package com.hamza.cst338.finalprojectcst338;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class CreateActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        Button checkButton = findViewById(R.id.checkButton);
        checkButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        if(v.getId() == R.id.checkButton){
            EditText user = findViewById(R.id.usernameEditText);
            EditText pass = findViewById(R.id.passwordEditText);
            String username = user.getText().toString();
            String password = pass.getText().toString();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final Intent intent = new Intent(this, MainActivity.class);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(intent);
                }
            });

            Database db = new Database(getBaseContext());
            Account account = new Account();

            if(!account.createAccount(username, password, db)){
                builder.setTitle("Fail");
                builder.setMessage("Unable to create account.");
            }else{
                builder.setTitle("Success");
                builder.setMessage("Your account was created.");
            }

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}
