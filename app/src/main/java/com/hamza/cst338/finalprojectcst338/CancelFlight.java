package com.hamza.cst338.finalprojectcst338;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class CancelFlight extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout());
    }

    @Override
    public void onClick(View v){
        ArrayList<String> list = getIntent().getExtras().getStringArrayList("list");
        for(int i = 0; i < list.size(); i++)
        {
            if(v.getId() == i)
            {
                //Searching for reservation
                Database db = new Database(getBaseContext());
                StringTokenizer st = new StringTokenizer(list.get(i), ",");
                Reservation reservation = new Reservation();
                boolean didDelete = reservation.deleteReservation(
                        db,
                        getIntent().getExtras().getString("username"),
                        getIntent().getExtras().getString("password"),
                        Integer.parseInt(st.nextToken()));

                //Alert pop up
                final Intent intent = new Intent(this, MainActivity.class);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(intent);
                    }
                });
                if(didDelete){
                    builder.setTitle("Success");
                    builder.setMessage("Your reservation " + Integer.parseInt(st.nextToken()) + " was deleted");
                }else{
                    builder.setTitle("Fail");
                    builder.setMessage("Reservation cancellation failed");
                }
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }

    private LinearLayout layout(){
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layout.setLayoutParams(params);

        ArrayList<String> list = getIntent().getExtras().getStringArrayList("list");
        for(int i = 0; i < list.size(); i++){

            Button b = new Button(this);

            StringTokenizer st = new StringTokenizer(list.get(i), ",");
            st.nextToken();
            b.setText(st.nextToken());
            b.setId(i);
            b.setOnClickListener(this);
            layout.addView(b);
        }

        layout.setOnClickListener(this);

        return layout;
    }
}
