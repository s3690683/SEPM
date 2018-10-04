package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.*;


public class CourseManagement {

    public static void main(String args[]) throws SQLException, ClassNotFoundException, IOException, InterruptedException {
        Driver dr = new Driver();
        try {
            System.out.println ("Welcome to Course Management System......");
            dr.login ();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    }


