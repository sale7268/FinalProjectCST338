package com.hamza.cst338.finalprojectcst338;

import android.database.Cursor;

public class Account {

    public Account() {}

    private String specialChar = "~`!@#$%^&*()-_=+\\\\|[{]};:'\\\",<.>/?";
    private String nums = "0123456789";

    public boolean createAccount(String uname, String pass, Database db)
    {
        int count = 0, counter = 0;
        boolean unum = false, pnum = false;

        for(int i = 0; i < nums.length(); i++)
        {
            if(uname.contains(Character.toString(nums.charAt(i)))){
                unum = true;
                break;
            }
        }

        for(int i = 0; i < nums.length(); i++)
        {
            if(pass.contains(Character.toString(nums.charAt(i)))){
                pnum = true;
                break;
            }
        }
        for(int i = 0; i < specialChar.length(); i++)
        {
            if(uname.contains(Character.toString(specialChar.charAt(i)))){
                count += 1;
                break;
            }
        }
        for(int i = 0; i < specialChar.length(); i++)
        {
            if(pass.contains(Character.toString(specialChar.charAt(i)))){
                counter += 1;
                break;
            }
        }
        if (count == 0 || counter == 0 || !unum || !pnum)
        {
            return false;
        }
        String s = "INSERT INTO customers (password, username) VALUES('" + pass + "', '" + uname +"');";
        return db.insert(s);
    }

    public boolean adminVerify(String uname, String pass) {
        return uname.equals("admin2") && pass.equals("admin2");
    }
    public Cursor dispLog(Database db) {

        String s = "SELECT * FROM log order by timestamp desc;";

        return db.logLookUp(s);
    }

    public boolean verifyCust(String uname, String pass, Database db)
    {
        String s = "SELECT password FROM customers WHERE username = '" + uname + "';";
        Cursor c = db.lookup(s);
        c.moveToFirst();
        if (c.getCount() == 0)
        {
            return false;
        }
        String r = c.getString(0);
        c.close();
        return r.equals(pass);
    }

    public int getCustomerID(String uname, Database db)
    {
        String s = "SELECT id FROM customers WHERE username = '" + uname + "';";
        Cursor c = db.lookup(s);
        if (c.getCount() == 0)
        {
            return -1;
        } else {
            c.moveToFirst();
            int temp = c.getInt(0);
            c.close();
            return temp;
        }
    }
}