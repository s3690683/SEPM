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
        System.out.println ( " 2 . Quit" );
        System.out.println ();
        System.out.println ( "Enter an option:" );
        String choice = scnr.nextLine ();

        while (!choice.matches ( "\\d+?" )) {
            System.out.println ( "Please enter a valid input" );
            choice = scnr.nextLine ();
        }

        int input = Integer.parseInt ( choice );

        if (input <= 1 && input != 0) {
            switch (input) {
                case 1: {
                    add_stud ();
                    break;
                }
                case 2: {
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

    public void add_stud() throws SQLException, IOException, InterruptedException {

        double fees_of_stud;

        int max_stud = 0;
        int stud_enrolled = 0;

        int crse_no = Course ();

        //Maximum students allowed to enroll
        String qry1 = "select MAX_STUDENTS from COURSE WHERE ID='" + crse_no + "'";
        rs = st.executeQuery ( qry1 );

        while (rs.next ()) {
            max_stud = rs.getInt ( "MAX_STUDENTS" );
        }
        System.out.println ( "Maximum enrollment capacity:" + max_stud );


        //Number of students enrolled
        String qry2 = "select NO_OF_STUDENTS_ENROLLED from COURSE WHERE ID='" + crse_no + "'";
        rs = st.executeQuery ( qry2 );

        while (rs.next ()) {
            stud_enrolled = rs.getInt ( "NO_OF_STUDENTS_ENROLLED" );
        }
        System.out.println ( "Students enrolled:" + stud_enrolled );


        //Check whether the course if full or not
        int rem_enroll = max_stud - stud_enrolled;
        if (rem_enroll <= 0) {
            System.out.println ( "Sorry the course is full!!!!" );
            Thread.sleep ( 1000 );
            menu_list ();
        } else {
            String name = ask_name ();
            double income_in_DB = 0;
            int fees_of_course = 0;

            String qry6 = "select INCOME from COURSE WHERE ID='" + crse_no + "'";
            rs = st.executeQuery ( qry6 );
            while (rs.next ()) {
                income_in_DB = rs.getDouble ( "INCOME" );
            }

            String qry5 = "select FEES from COURSE WHERE ID='" + crse_no + "'";
            rs = st.executeQuery ( qry5 );
            while (rs.next ()) {
                fees_of_course = rs.getInt ( "FEES" );
            }

            String qry3 = "select STUD_NAME from STUDENT WHERE STUD_NAME='" + name + "'";
            rs = st.executeQuery ( qry3 );
            String db_stud = "";
            while (rs.next ()) {
                db_stud = rs.getString ( "STUD_NAME" );
            }


            BufferedReader br = new BufferedReader ( new InputStreamReader ( System.in ) );

            System.out.println ( "Enter Address:" );
            String address = br.readLine ();

            int age = age ();


            pr_st = conn.prepareStatement ( "insert into student (ID,STUD_NAME,ADDRESS,AGE,FIRST_COURSE) values ('" + crse_no + "','" + name + "','" + address + "','" + age + "','" + crse_no + "')" );   //Adding name into the netwrok with the help of Database.
            pr_st.executeUpdate ();


            fees_of_stud = fees_of_course;
            income_in_DB = income_in_DB + fees_of_stud;
            pr_st = conn.prepareStatement ( "update COURSE set INCOME='" + income_in_DB + "'WHERE ID='" + crse_no + "'" );
            pr_st.executeUpdate ();

            stud_enrolled = stud_enrolled + 1;
            pr_st = conn.prepareStatement ( "update COURSE set NO_OF_STUDENTS_ENROLLED='" + stud_enrolled + "'WHERE ID='" + crse_no + "'" );
            pr_st.executeUpdate ();

            System.out.println ( "Student has been enrolled succesfully in " + getMappedCrse_Name ( crse_no ) + " Course." );
            Thread.sleep ( 3000 );
            menu_list ();

        }
    }

        public int Course() throws SQLException, IOException, InterruptedException {
            System.out.println ( "Please enter course option from the following: " );
            System.out.println ( " 1 . Italian Cooking" );
            System.out.println ( " 2 . Seafood Cooking" );
            System.out.println ( " 3 . Sewing" );
            System.out.println ( " 4 . Creative Writing" );
            System.out.println ( " 5 . Business Writing" );
            String choice = scnr.nextLine ();
            while (!choice.matches ( "\\d+?" )) {
                System.out.println ( "Please enter a valid input." );
                choice = scnr.nextLine ();
            }


            int input = Integer.parseInt ( choice );

            System.out.println ( input );
            if (input <= 5 && input != 0) {
                return input;
            } else {
                System.out.println ( "Please check the options entered." );
                Thread.sleep ( 1000 );
                menu_list ();
            }
            return input;
        }

        public String ask_name() throws SQLException {
            String full_name = "";
            try {
                System.out.println ( "Enter First name of the student:" );
                String firstname = scnr.nextLine ().toUpperCase ();
                String a1 = namevalidation ( firstname );

                System.out.println ( "Enter Last name of the student:" );
                String lastname = scnr.nextLine ().toUpperCase ();
                String a2 = namevalidation ( lastname );

                full_name = firstname + " " + lastname;

                return full_name;
            } catch (Exception e) {
                System.out.println ( "Please enter proper name." );
            }

            return full_name;
        }

        public String namevalidation(String name) throws SQLException, IOException, InterruptedException {

            if (name.isEmpty ()) {
                System.out.println ( "Name should not be empty." );
                ask_name ();
            }
            if (name.matches ( "\\d+?" )) {
                System.out.println ( "Please enter a proper name." );
                System.out.println ( "Do you wish to continue?" );
                System.out.println ( "Press Y to enter the name again or Press any key except Y to go back to main menu." );
                String opt = scnr.nextLine ().toUpperCase ();
                if (opt.equalsIgnoreCase ( "Y" )) {
                    ask_name ();
                } else {
                    Thread.sleep ( 3000 );
                    menu_list ();
                }

            } else {
                return name;
            }
            return name;
        }

        public String getMappedCrse_Name(int crse_no) {

            Map<Integer, String> dictionary = new HashMap<Integer, String> ();

            dictionary.put ( 1, "Italian Cooking" );
            dictionary.put ( 2, "Seafood Cooking" );
            dictionary.put ( 3, "Sewing" );
            dictionary.put ( 4, "Creative Writing" );
            dictionary.put ( 5, "Business Writing" );

            return dictionary.get ( crse_no );
        }

        public int age() throws IOException, SQLException, InterruptedException {
            System.out.println ( "Enter Age:" );
            String str = scnr.nextLine ();
            int age = containsOnlyNumbers ( str );
            return age;
        }

        public int containsOnlyNumbers(String str) throws IOException, SQLException, InterruptedException {
            try {
                Integer num = Integer.valueOf ( str );
                if (num > 0) {
                    return num;
                } else {
                    System.out.println ( "Please enter positive number" );
                    System.out.println ();
                    menu_list ();
                    return 0;
                }
            } catch (NumberFormatException e) {
                System.out.println ( "Please enter a number" );
                System.out.println ();
                menu_list ();
                return 0;
            }
        }
    }