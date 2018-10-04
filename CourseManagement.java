package com.company;

import java.io.IOException;
import java.sql.*;

public class CourseManagement {

    public static void main(String args[]) throws SQLException, ClassNotFoundException, IOException, InterruptedException {
        Driver dr = new Driver();
        try {
            System.out.println ("Welcome to Course Management System......");
            System.out.println ();
            dr.login ();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    }


