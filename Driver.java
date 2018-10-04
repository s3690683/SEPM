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
        System.out.println ( " 5 . Update course fees" );
        System.out.println ( " 6 . View course details" );
        System.out.println ( " 7 . View student details" );
        System.out.println ( " 8 . Quit" );
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
                    add_stud ();
                    break;
                }
                case 2: {
                    withdraw_stud ();
                    break;
                }
                case 3: {
                    display_stud_list ();
                    break;
                }
                case 4: {
                    display_crse_fig ();
                    break;
                }
                case 5: {
                    update_crse_fees ();
                    break;
                }
                case 6: {
                    crse_dtls ();
                    break;
                }
                case 7: {
                    stdt_dtls ();
                    break;
                }
                case 8: {
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


            if (name.equals ( db_stud )) {          //ALREADY ENROLLED
                String id_1;
                System.out.println ( "Student already enrolled in some course. So, gets 20% discount on the fees to be paid for this course." );
                System.out.println ( "Do you want to proceed with enrollment?" );
                System.out.println ( "Enter Y to continue or Press any key except Y to go back to main menu." );
                String opt = scnr.nextLine ().toUpperCase ();
                if (opt.equals ( "Y" )) {
                    String qry4 = "select ID from STUDENT WHERE STUD_NAME='" + name + "'";
                    rs = st.executeQuery ( qry4 );

                    while (rs.next ()) {
                        id_1 = rs.getString ( "ID" );                      //Fetching ID of student from the database.
                        String chk3[] = id_1.split ( "," );
                        int chk4[] = Arrays.stream ( chk3 ).mapToInt ( Integer::parseInt ).toArray ();
                        for (int i = 0; i < chk4.length; i++) {
                            if (chk4[i] == crse_no) {
                                System.out.println ( "Already enrolled in this course." );
                                Thread.sleep ( 1000 );
                                menu_list ();
                            }
                        }

                        String a = Integer.toString ( crse_no );
                        String final_id = id_1 + ',' + a;
                        pr_st = conn.prepareStatement ( "update STUDENT set ID='" + final_id + "'where STUD_NAME='" + name + "'" );
                        pr_st.executeUpdate ();

                        double discount = 0.2 * fees_of_course;
                        fees_of_stud = fees_of_course - discount;


                        income_in_DB = income_in_DB + fees_of_stud;

                        pr_st = conn.prepareStatement ( "update COURSE set INCOME='" + income_in_DB + "'WHERE ID='" + crse_no + "'" );
                        pr_st.executeUpdate ();

                        stud_enrolled = stud_enrolled + 1;

                        pr_st = conn.prepareStatement ( "update COURSE set NO_OF_STUDENTS_ENROLLED='" + stud_enrolled + "'WHERE ID='" + crse_no + "'" );
                        pr_st.executeUpdate ();

                        System.out.println ( "Student has been enrolled succesfully in " + getMappedCrse_Name ( crse_no ) + " Course." );
                        menu_list ();

                    }
                } else {
                    menu_list ();
                }
            } else {                   //New Enrollment

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
        String str = scnr.next ();
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

    public void withdraw_stud() throws SQLException, IOException, InterruptedException {
        int crse_no = Course ();
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

        String name = ask_name ();
        String id_1;
        int stud_enrolled = 0;


        String qry2 = "select NO_OF_STUDENTS_ENROLLED from COURSE WHERE ID='" + crse_no + "'";
        rs = st.executeQuery ( qry2 );

        while (rs.next ()) {
            stud_enrolled = rs.getInt ( "NO_OF_STUDENTS_ENROLLED" );
        }

        if (stud_enrolled == 0) {
            System.out.println ( "No student enrolled." );
            Thread.sleep ( 1000 );
            menu_list ();
        } else {

            String qry3 = "select STUD_NAME from STUDENT WHERE STUD_NAME='" + name + "'";
            rs = st.executeQuery ( qry3 );
            String db_stud = "";
            while (rs.next ()) {
                db_stud = rs.getString ( "STUD_NAME" );
            }

            if (name.equals ( db_stud )) {
                String qry4 = "select ID from STUDENT WHERE STUD_NAME='" + name + "'";
                rs = st.executeQuery ( qry4 );
                while (rs.next ()) {
                    id_1 = rs.getString ( "ID" );                      //Fetching ID of student from the database.
                    String final_id;

                    int id_3 = 0;
                    String qry7 = "select first_course from student where stud_name='" + name + "'";
                    rs = st.executeQuery ( qry7 );
                    while (rs.next ()) {
                        id_3 = rs.getInt ( "first_course" );
                    }

                    if (id_1.contains ( "," )) {
                        String chk3[] = id_1.split ( "," );
                        int chk4[] = Arrays.stream ( chk3 ).mapToInt ( Integer::parseInt ).toArray ();
                        int chk5[] = Arrays.stream ( chk4 ).filter ( x -> crse_no != x ).toArray ();
                        String strArray[] = Arrays.stream ( chk5 ).mapToObj ( String::valueOf ).toArray ( String[]::new );
                        String joined2 = String.join ( ",", strArray );

                        pr_st = conn.prepareStatement ( "update STUDENT set ID='" + joined2 + "'WHERE STUD_NAME ='" + name + "'" );
                        pr_st.executeUpdate ();

                        stud_enrolled = stud_enrolled - 1;

                        pr_st = conn.prepareStatement ( "update COURSE set NO_OF_STUDENTS_ENROLLED='" + stud_enrolled + "'WHERE ID='" + crse_no + "'" );
                        pr_st.executeUpdate ();

                        income_updt_with ( income_in_DB, fees_of_course, crse_no, id_3, name );

                        System.out.println ( "Student has been withdraw succesfully from " + getMappedCrse_Name ( crse_no ) + " Course." );

                        menu_list ();

                    } else {
                        pr_st = conn.prepareStatement ( "DELETE FROM STUDENT WHERE STUD_NAME ='" + name + "'" );
                        pr_st.executeUpdate ();

                        stud_enrolled = stud_enrolled - 1;

                        pr_st = conn.prepareStatement ( "update COURSE set NO_OF_STUDENTS_ENROLLED='" + stud_enrolled + "'WHERE ID='" + crse_no + "'" );
                        pr_st.executeUpdate ();
                        income_updt_with ( income_in_DB, fees_of_course, crse_no, id_3, name );
                        System.out.println ( "Student has been withdraw succesfully from " + getMappedCrse_Name ( crse_no ) + " Course." );
                        menu_list ();
                    }
                }

            } else {
                System.out.println ( "Student not enrolled in any course." );
                menu_list ();
            }
        }
    }

    public void income_updt_with(double income_in_DB, int fees_of_course, int crse_no, int id_3, String name) throws SQLException, IOException {
        double income;
        if (crse_no != id_3) {
            double fees_of_course_1 = 0.2 * fees_of_course;
            double final_fees = fees_of_course - fees_of_course_1;
            income = income_in_DB - final_fees;
        } else {
            income = income_in_DB - fees_of_course;

            pr_st = conn.prepareStatement ( "update STUDENT set FIRST_COURSE=0 WHERE STUD_NAME ='" + name + "'" );
            pr_st.executeUpdate ();
        }
        pr_st = conn.prepareStatement ( "update COURSE set INCOME='" + income + "'WHERE ID='" + crse_no + "'" );
        pr_st.executeUpdate ();
    }

    public void display_stud_list() throws SQLException, IOException, InterruptedException {
        int crse_no = Course ();
        String crse_name = "";
        String sql1 = "select COURSE_NAME from COURSE WHERE ID='" + crse_no + "'";
        rs = st.executeQuery ( sql1 );
        while (rs.next ()) {
            crse_name = rs.getString ( "COURSE_NAME" );
        }

        int stud_enrolled1 = 0;
        String qry8 = "select NO_OF_STUDENTS_ENROLLED from COURSE WHERE ID='" + crse_no + "'";
        rs = st.executeQuery ( qry8 );

        while (rs.next ()) {
            stud_enrolled1 = rs.getInt ( "NO_OF_STUDENTS_ENROLLED" );
            if (stud_enrolled1 == 0) {
                System.out.println ( "No student enrolled." );
                menu_list ();
            }
        }

        System.out.println ( "== Students in " + getMappedCrse_Name ( crse_no ) + " ==" );
        String sql = "select * from student";
        rs = st.executeQuery ( sql );
        while (rs.next ()) {
            String id = rs.getString ( "ID" );
            String name = rs.getString ( "STUD_NAME" );
            String address = rs.getString ( "ADDRESS" );
            int age = rs.getInt ( "AGE" );

            if(address==null){
                address="Unknown";
            }

            String chk3[] = id.split ( "," );
            int chk4[] = Arrays.stream ( chk3 ).mapToInt ( Integer::parseInt ).toArray ();

            if (id.contains ( "," )) {

                for (int i = 0; i < chk4.length; i++) {
                    if (chk4[i] == crse_no)                                        // Checking whether they are already friends or not.
                    {

                        System.out.println ( "" + name + ", " + address + ", " + age );
                    }
                }
                //menu_list ();
            } else {
                for (int i = 0; i < chk4.length; i++) {
                    if (chk4[i] == crse_no) {
                        System.out.println ( "" + name + ", " + address + ", " + age );
                    }
                    //menu_list ();
                }
            }
        }
        menu_list ();
    }

    public void display_crse_fig() throws SQLException, IOException, InterruptedException {
        String sql = "select * from COURSE";
        rs = st.executeQuery ( sql );
        while (rs.next ()) {
            String crse_name = rs.getString ( "COURSE_NAME" );
            int stud_enrolled = rs.getInt ( "NO_OF_STUDENTS_ENROLLED" );
            int income = rs.getInt ( "INCOME" );
            int cst_of_rng = rs.getInt ( "COST_OF_RUNNING" );
            int profit = income - cst_of_rng;

            System.out.println ( "" + crse_name + " : Students " + stud_enrolled + ", Income $" + income + ", Cost $" + cst_of_rng + ", Profit $" + profit );

        }
        Thread.sleep ( 3000 );
        menu_list ();
    }

    public void update_crse_fees() throws SQLException, IOException, InterruptedException {
        int crse_no = Course ();

        System.out.println ( "Enter new fees to be updated:" );
        String new_fees = scnr.nextLine ();
        int updt_fees = containsOnlyNumbers ( new_fees );

        pr_st = conn.prepareStatement ( "update COURSE set FEES='" + updt_fees + "'WHERE ID='" + crse_no + "'" );
        pr_st.executeUpdate ();

        System.out.println ( getMappedCrse_Name ( crse_no ) + " course fees updated successfully." );
        Thread.sleep ( 1000 );
        menu_list ();
    }

    public void crse_dtls() throws SQLException, IOException, InterruptedException {
        int crse_no = Course ();

        String sql = "select * from COURSE where ID='" + crse_no + "'";
        rs = st.executeQuery ( sql );
        while (rs.next ()) {
            String crse_name = rs.getString ( "COURSE_NAME" );
            String tchr_name = rs.getString ( "TEACHER_NAME" );
            int max_stud_enrld = rs.getInt ( "MAX_STUDENTS" );
            int fees = rs.getInt ( "FEES" );
            int stud_enrolled = rs.getInt ( "NO_OF_STUDENTS_ENROLLED" );
            int income = rs.getInt ( "INCOME" );
            int cst_of_rng = rs.getInt ( "COST_OF_RUNNING" );
            int profit = income - cst_of_rng;
            System.out.println ( "***** " + crse_name + " *****" );
            System.out.println ( "Teacher name : " + tchr_name );
            System.out.println ( "Fees : $" + fees );
            System.out.println ( "Students enrolled : " + stud_enrolled );
            System.out.println ( "Total class capacity : " + max_stud_enrld );
            System.out.println ( "Income : $" + income );
            System.out.println ( "Cost of running : $" + cst_of_rng );
        }

        Thread.sleep ( 3000 );
        menu_list ();
    }

    public void stdt_dtls() throws SQLException, IOException, InterruptedException {
        String name = ask_name ();
        String crse = null;

        String sql = "select * from STUDENT where STUD_NAME='" + name + "'";
        rs = st.executeQuery ( sql );
        while (rs.next ()) {
            String crse_id = rs.getString ( "ID" );
            String stud_name = rs.getString ( "STUD_NAME" );
            String addr = rs.getString ( "ADDRESS" );
            int age = rs.getInt ( "AGE" );
            int frst_crse = rs.getInt ( "FIRST_COURSE" );

            String chk3[] = crse_id.split ( "," );
            int chk4[] = Arrays.stream ( chk3 ).mapToInt ( Integer::parseInt ).toArray ();
            System.out.println ();
            System.out.println ( "Name : " + name );
            System.out.println ();
            System.out.println ( "Course enrolled : " );
            for (int i = 0; i < chk4.length; i++) {
                System.out.println ( getMappedCrse_Name ( chk4[i] ) );
            }
            System.out.println ();
            if (addr == null) {
                System.out.println ( "Address : Unknown" );
            } else {
                System.out.println ( "Address : " + addr );
            }

            System.out.println ();
            System.out.println ( "Age : " + age );
            System.out.println ();
            System.out.println ( "20% discount in following courses : " );
            String chk2[] = crse_id.split ( "," );
            int chk5[] = Arrays.stream ( chk3 ).mapToInt ( Integer::parseInt ).toArray ();
            for (int i = 0; i < chk4.length; i++) {
                if (chk5[i] != frst_crse) {
                    System.out.println ( getMappedCrse_Name ( chk5[i] ) );
                }
            }

            Thread.sleep ( 3000 );
            menu_list ();
        }
    }
}