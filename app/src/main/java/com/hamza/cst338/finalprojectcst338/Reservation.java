package com.hamza.cst338.finalprojectcst338;

import android.database.Cursor;
import android.util.Log;

public class Reservation {

    public Reservation() {
    }

    public Cursor getReservations(Database db, String uname, String pass)
    {
        Account a = new Account();
        int cid = a.getCustomerID(uname, db);
        String s = "SELECT id, flight_name FROM reservations WHERE customer_id = " + cid + ";";
        return db.lookup(s);
    }

    public boolean newReservation(Database db, String uname, String pass, int numSeats, String flightName) {

        Account a = new Account();

        if (a.verifyCust(uname, pass, db)) {

            int cID = a.getCustomerID(uname, db);
            String s = "INSERT INTO reservations (seatsReq, flight_name, customer_id) VALUES (" +
                    Integer.toString(numSeats) + ", '" + flightName + "', " + Integer.toString(cID) + ");";

            try{
                String st = "UPDATE flights SET claimedSeats = (select claimedSeats from flights where name = '" + flightName + "'; + " + Integer.toString(numSeats) + ") WHERE name = '" + flightName + "';";
                db.update(st);
            }catch (Exception e){
                Log.d("Error: ", e.getLocalizedMessage());
            }

            return db.insert(s);

        } else {
            return false;
        }
    }

    public boolean deleteReservation(Database db, String uname, String pass, int reservationNumber)
    {
        String s = "DELETE FROM reservations WHERE id = " + reservationNumber;
        return db.delete(s);
    }

    public int getLastReservation(Database db, String uname, String pass){
        String s = "SELECT id FROM reservations order by id desc limit 1;";

        Cursor c = db.lookup(s);
        c.moveToFirst();
        int i = c.getInt(0);
        c.close();
        return i;
    }

}
