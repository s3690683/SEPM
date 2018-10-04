package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.*;

public class Driver {

    Statement st;
    Connection conn;
    PreparedStatement pr_st;
    ResultSet rs;
    DBConnection db1;
    Scanner scnr = new Scanner ( System.in );

    Driver() throws SQLException, ClassNotFoundException {
        db1 = new DBConnection ();
        st = db1.DB ();
        conn = db1.DBConnect ();
    }

    public void login() throws SQLException, IOException, InterruptedException, RuntimeException {

        ArrayList db_usrname = new ArrayList ();

        System.out.println ( "Please enter valid credentials below:" );
        System.out.println ();
        String db_password = null;
        boolean usr_prsnt = false;
        System.out.println ( "Enter Username : " );
        String username = scnr.nextLine ().toUpperCase ();
        System.out.println ();
        String qry1 = "select USERNAME from LOGIN_MASTER";
        rs = st.executeQuery ( qry1 );
        while (rs.next ()) {
            db_usrname.add ( rs.getString ( "USERNAME" ) );
        }

        for (int i = 0; i < db_usrname.size (); i++) {
            if (db_usrname.get ( i ).toString ().equals ( username )) {
                usr_prsnt = true;
            }
        }

        if (usr_prsnt) {
            String qry2 = "select PASSWORD from LOGIN_MASTER WHERE USERNAME='" + username + "'";
            rs = st.executeQuery ( qry2 );

            while (rs.next ()) {
                db_password = rs.getString ( "PASSWORD" );
            }

            System.out.println ( "Enter Password: " );
            String pass_word = scnr.nextLine ().toUpperCase ();

            if (db_password.equals ( pass_word )) {
                menu_list ();
            } else {
                System.out.println ( "Incorrect password!!!" );
                login ();
            }
        } else {
            System.out.println ( "Invalid username!!!" );
            System.out.println ();
            login ();
        }

    }

    public void menu_list() throws SQLException, IOException, InterruptedException {
        System.out.println ();
        System.out.println ( "Course Management System" );
        System.out.println ( "===================================" );
        System.out.println ( " 1 . Add a student" );
        System.out.println ( " 2 . Withdraw a student" );
        System.out.println ( " 3 . Display a student list for a course" );
        System.out.println ( " 4 . Display the course figures" );
        System.out.println ( " 5 . Quit" );
        System.out.println ();
        System.out.println ( "Enter an option:" );
        String choice = scnr.nextLine ();

        while (!choice.matches ( "\\d+?" )) {
            System.out.println ( "Please enter a valid input" );
            choice = scnr.nextLine ();
        }

        int input = Integer.parseInt ( choice );

        if (input <= 5 && input != 0) {
            switch (input) {
                case 1: {
                    //add_stud ();
                    break;
                }
                case 2: {
                    //withdraw_stud ();
                    break;
                }
                case 3: {
                    //display_stud_list ();
                    break;
                }
                case 4: {
                    //display_crse_fig ();
                    break;
                }
                case 5: {
                    System.out.println ( "Exiting Program..." );
                    System.exit ( 0 );
                    break;
                }
            }
        } else {
            System.out.println ( "Please check the options entered." );
            System.out.println ();
            Thread.sleep ( 1000 );
            menu_list ();
        }

    }
}