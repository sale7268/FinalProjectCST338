package com.hamza.cst338.finalprojectcst338;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

    SQLiteDatabase db;

    public Database(Context context) {
        super(context, "database", null, 1);
    }

    //Delete from database
    public boolean delete(String s) {
        db = getWritableDatabase();
        if (s.contains("\"; ") || s.contains("'; ")) {
            db.close();
            return false;
        }
        try {
            db.execSQL(s);
            db.close();
            return true;
        } catch (Exception e) {
            db.close();
            return false;
        }
    }

    //Insert value into data base
    public boolean insert(String s) {
        db = getWritableDatabase();
        try {
            if (s.contains("\"; ")) {
                db.close();
                return false;
            }
            db.execSQL(s);
            db.close();
            return true;
        } catch (Exception e) {
            db.close();
            return false;
        }
    }

    public boolean update(String s) {
        db = getWritableDatabase();
        if (s.contains("\"; ")) {
            db.close();
            return false;
        }
        try {
            db.execSQL(s);
            db.close();
            return true;
        } catch (SQLException e) {
            db.close();
            return false;
        }
    }

    //Look up (find) functions
    public Cursor lookup(String s) {

        db = getReadableDatabase();

        if (s.contains("\"; ")) {
            db.close();
            return null;
        }
        Cursor c = db.rawQuery(s, null);
        return c;
    }
    public Cursor logLookUp(String s) {

        db = getReadableDatabase();
        return db.rawQuery(s, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        /**Log Table Schema**/

        String s = "CREATE TABLE log (" +
                "timestamp    datetime not null,\n" +
                "event        varchar(48) not null,\n" +
                "entry_id     integer primary key autoincrement);";

        db.execSQL(s);

        /**Customer Table Schema**/
        s = "CREATE TABLE customers (\n" +
                "id          integer primary key autoincrement,\n" +
                "password    varchar(16) not null,\n" +
                "username    varchar(16) not null unique);";

        db.execSQL(s);

        /**Project spec wants default admin generated...**/
        s = "INSERT INTO customers (password, username) VALUES('!admiM2', '!admiM2');";

        db.execSQL(s);

        /**Project spec wants pre-generated accounts added**/
        s = "INSERT INTO customers (password, username) VALUES('@cSit100', 'A@lice5');";

        db.execSQL(s);

        s = "INSERT INTO customers (password, username) VALUES('123aBc##', '$BriAn7');";

        db.execSQL(s);

        s = "INSERT INTO customers (password, username) VALUES('CHrIS12!!', '!chriS12!');";

        db.execSQL(s);

        /**Flight Table Schema**/
        s = "CREATE TABLE flights (\n" +
                "name         varchar(20) not null unique,\n" +
                "departLoc    varchar(20) not null,\n" +
                "destinLoc    varchar(20) not null,\n" +
                "departHour   integer check (departHour<24) check(departHour>=0),\n" +
                "departMin    integer check (departMin <60) check(departMin >=0),\n" +
                "flightCap    integer not null,\n" +
                "price        decimal not null,\n" +
                "claimedSeats integer not null,\n" +
                "primary key (name));";

        db.execSQL(s);

        /**Project spec wants default generated flights**/
        s = "INSERT INTO flights (name, departLoc, destinLoc, departHour, departMin, flightCap, price, claimedSeats)" +
                " VALUES ('Otter101', 'Monterey', 'Los Angeles', 10, 30, 10, 150.00, 0);";

        db.execSQL(s);

        s = "INSERT INTO flights (name, departLoc, destinLoc, departHour, departMin, flightCap, price, claimedSeats)" +
                " VALUES ('Otter102', 'Los Angeles', 'Monterey', 13, 00, 10, 150.00, 0);";

        db.execSQL(s);

        s = "INSERT INTO flights (name, departLoc, destinLoc, departHour, departMin, flightCap, price, claimedSeats)" +
                " VALUES ('Otter201', 'Monterey', 'Seattle', 11, 00, 5, 200.50, 0);";

        db.execSQL(s);

        s = "INSERT INTO flights (name, departLoc, destinLoc, departHour, departMin, flightCap, price, claimedSeats)" +
                " VALUES ('Otter205', 'Monterey', 'Seattle', 15, 45, 15, 150.00, 0);";

        db.execSQL(s);

        s = "INSERT INTO flights (name, departLoc, destinLoc, departHour, departMin, flightCap, price, claimedSeats)" +
                " VALUES ('Otter202', 'Seattle', 'Monterey', 14, 10, 5, 200.00, 0);";

        db.execSQL(s);

        /**Reservation Table Schema**/
        s = "CREATE TABLE reservations(\n" +
                "id          integer primary key autoincrement,\n" +
                "seatsReq    integer not null," +
                "flight_name varchar(20),\n" +
                "customer_id integer);"; //+

        db.execSQL(s);

        /** Getting T R I G G E R E D **/

        s = "CREATE TRIGGER cust_mod AFTER UPDATE ON customers BEGIN INSERT INTO log(event, timestamp) VALUES('update customers', datetime('NOW')); END;";

        db.execSQL(s);

        s = "CREATE TRIGGER flight_mod AFTER UPDATE ON flights BEGIN INSERT INTO log(event, timestamp) VALUES(old.name || ' seats reserved changed from: ' || old.claimedSeats || ' to: ' || new.claimedSeats, datetime('NOW')); END;";
        db.execSQL(s);

        s = "CREATE TRIGGER res_mod AFTER UPDATE ON reservations BEGIN INSERT INTO log(event, timestamp) VALUES('update reservation', datetime('NOW')); END;";

        db.execSQL(s);

        s = "CREATE TRIGGER new_cust AFTER INSERT ON customers BEGIN INSERT INTO log(event, timestamp) VALUES ('new customer: ' || new.username, datetime('NOW')); END;";
        db.execSQL(s);

        s = "CREATE TRIGGER new_res AFTER INSERT ON reservations BEGIN INSERT INTO log(event, timestamp) VALUES( 'Customer: ' || CAST(new.customer_id AS VARCHAR) || ' booked flight: ' || new.flight_name || ' for: ' || CAST(new.seatsReq as VARCHAR) || ' tickets.', datetime('NOW')); END;";
        db.execSQL(s);

        s = "CREATE TRIGGER new_flight AFTER INSERT ON flights BEGIN INSERT INTO log(event, timestamp) VALUES ('New flight: ' || new.name || 'Depart from: ' || new.departLoc || ' Destination: ' || new.destinLoc || ' Flight time: ' || CAST(new.departHour AS VARCHAR) || CAST(new.departMin AS VARCHAR) || ' max capacity: ' || CAST(new.flightCap AS VARCHAR), datetime('NOW')); END;";
        db.execSQL(s);

        s = "CREATE TRIGGER del_cust AFTER DELETE ON customers BEGIN INSERT INTO log(event, timestamp) VALUES ('customer deleted', datetime('NOW')); END;";

        db.execSQL(s);

        s = "CREATE TRIGGER del_flight AFTER DELETE ON flights BEGIN INSERT INTO log(event, timestamp) VALUES ('flight deleted', datetime('NOW')); END;";

        db.execSQL(s);

        s = "CREATE TRIGGER del_res AFTER DELETE ON reservations BEGIN INSERT INTO log(event, timestamp) VALUES('Customer: ' || CAST(old.customer_id AS VARCHAR) || ' deleted reservation number: ' || CAST(old.id AS VARCHAR), datetime('NOW')); END;";
        db.execSQL(s);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
