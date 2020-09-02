package com.hamza.cst338.finalprojectcst338;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.hamza.cst338.finalprojectcst338.Database;
import com.hamza.cst338.finalprojectcst338.MainActivity;
import com.hamza.cst338.finalprojectcst338.R;
import com.hamza.cst338.finalprojectcst338.Reservation;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.StringTokenizer;

public class ConfirmSeatReservation extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_seat_reservation);

        String a = getIntent().getExtras().getString("Flight Number");

        TextView title = findViewById(R.id.label);
        title.setText("Flight:\n" + a);

        Button checkButton = findViewById(R.id.checkButton);
        checkButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        if(v.getId() == R.id.checkButton){
            Reservation reservation = new Reservation();
            Database db = new Database(this);

            EditText user = findViewById(R.id.usernameEditText);
            String username = user.getText().toString();

            EditText pass = findViewById(R.id.passwordEditText);
            String password = pass.getText().toString();

            String flightInfo = getIntent().getExtras().getString("Flight Info");
            String delim = ",";
            StringTokenizer st = new StringTokenizer(flightInfo, delim);
            String flightNum = st.nextToken();
            String departure = st.nextToken();
            String arrival = st.nextToken();
            String departHour = st.nextToken();
            String departMin = st.nextToken();
            st.nextToken();
            String price = st.nextToken();
            String numOfTickets = getIntent().getExtras().getString("Tickets");
            int claimed = Integer.parseInt(st.nextToken());
            float totalPrice = Float.parseFloat(price) * Integer.parseInt(numOfTickets);

            NumberFormat formatter = new DecimalFormat("0.00");
            StringBuilder sb = new StringBuilder();
            if(departHour.length() == 1)
            {
                sb.append("0" + departHour + ":");
            }
            else{
                sb.append(departHour + ".");
            }
            if(departMin.length() == 1)
            {
                sb.append(departMin + "0");
            }else{
                sb.append(departMin);
            }

            float departTime = Float.parseFloat(sb.toString());

            boolean reserveSucccesful = reservation.newReservation(db, username, password, Integer.parseInt(numOfTickets), flightNum);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final Intent intent = new Intent(this, MainActivity.class);

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(intent);
                }
            });

            int reservationNum = -1;
            if(reserveSucccesful){
                reservationNum = reservation.getLastReservation(db, username, password);
            }

            String message = "Username: " + username +"\n"+
                    "Flight Number: " + flightNum + "\n"+
                    "Departure: " + departure + ", " + formatter.format(departTime) + "\n"+
                    "Arrival: " + arrival + "\n"+
                    "Number Of Tickets: " + numOfTickets + "\n"+
                    "Reservation Number: " + Integer.toString(reservationNum) + "\n"+
                    "Total amount: $" + formatter.format(totalPrice);

            if (reserveSucccesful){
                builder.setMessage(message);
                String str = "UPDATE flights set claimedSeats = " + claimed + Integer.valueOf(numOfTickets) + " where name = '" + flightNum + "';";
                db.update(str);
            }else{
                builder.setMessage("Wrong username or password");
            }

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}