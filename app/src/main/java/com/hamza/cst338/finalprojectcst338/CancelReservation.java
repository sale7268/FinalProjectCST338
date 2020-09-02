package com.hamza.cst338.finalprojectcst338;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CancelReservation extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cancel_reservation);

        Button checkButton = findViewById(R.id.checkButton);
        checkButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        if(v.getId() == R.id.checkButton){
            final Intent intent = new Intent(this, MainActivity.class);
            AlertDialog.Builder builder = new  AlertDialog.Builder(this);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(intent);
                }
            });

            EditText user = findViewById(R.id.usernameEditText);
            String username = user.getText().toString();
            EditText pass = findViewById(R.id.passwordEditText);
            String password = pass.getText().toString();

            Database db = new Database(getBaseContext());
            Reservation reservation = new Reservation();
            Cursor c = reservation.getReservations(db, username, password);

            int column = c.getColumnCount();
            int row = c.getCount();
            StringBuilder sb = new StringBuilder();
            ArrayList<String> list = new ArrayList<>();
            for(int i = 0; i < row; i++)
            {
                c.moveToNext();
                for(int j = 0; j < column; j++)
                {
                    try{
                       sb.append(c.getString(j) + ",");
                    }catch (Exception e){
                        sb.append(Integer.toString(c.getInt(j)) + ",");
                    }
                }
                list.add(sb.toString());
                sb = new StringBuilder();
            }

            if(list.size() == 0)
            {
                builder.setTitle("Fail");
                builder.setMessage("Sorry, no reservation with the entered credentials.");
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            else{
                Intent i = new Intent(this, CancelFlight.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("list", list);
                bundle.putString("username", username);
                bundle.putString("password", password);
                i.putExtras(bundle);
                startActivity(i);
            }
        }
    }
}
