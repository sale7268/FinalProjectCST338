package com.hamza.cst338.finalprojectcst338;

import android.database.Cursor;

import java.util.ArrayList;

public class Flight {

    public Flight() {
    }

    public ArrayList<String> flightSearch(int tickets, String dLoc, String aLoc, Database db) {

        String s = "SELECT * FROM flights WHERE (flightCap - claimedSeats - " + tickets + ") > 0 AND departLoc = '" + dLoc + "' AND destinLoc = '" + aLoc + "';";
        try {
            if (db.lookup(s) == null) {
                throw new Exception("Found no flights");
            }
           
            Cursor c = db.lookup(s);
            ArrayList<String> list = new ArrayList<>();

            StringBuilder sb = new StringBuilder();
            int col = c.getColumnCount();
            int row = c.getCount();

            for (int i = 0; i < row; i++) {
                c.moveToNext();
                for (int j = 0; j < col; j++) {
                    try {
                        sb.append(c.getString(j) + ",");
                    } catch (Exception e) {
                        sb.append(Integer.toString(c.getInt(j)) + ",");
                    }
                }
                list.add(sb.toString());
                sb = new StringBuilder();
            }

            c.close();
            db.close();
            return list;

        } catch (Exception e) {
            System.exit(2);
            return null;
        }
    }

    public boolean addFlight(String name, String dep, String arriv, int hour, int min, int cap, Float price, Database db){

        String s = "INSERT INTO flights (name, departLoc, destinLoc, departHour, departMin, flightCap, price, claimedSeats)" +
                " VALUES ('"+ name + "', '" + dep +"', '"+ arriv + "', " + hour + ", " + min + ", " + cap + ", " + price + ", 0);";
        try {
            return db.insert(s);
        }catch(Exception e){
            return false;
        }
    }
}
