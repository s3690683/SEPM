package com.company;/*
 * Class Name           : DBConnection
 * Version Info.        : 1.0
 * Author               : Dipesh Dinesh Dadhania

 |-----------------------------------PROGRAM HISTORY-------------------------------------------------|
 |----------------------------------------------------------------------------------------------------------|
 |                  |                      |                                |                               |
 |                  |                      |                                |                               |
 |     Version      |         Date         |        Resource                |         DESCRIPTION           |
 |                  |                      |                                |                               |
 |------------------|----------------------|--------------------------------|-------------------------------|
 |      1.0         |       28-Mar-2018    |     Dipesh Dinesh Dadhania     |      DBConnection.java        |
 |----------------------------------------------------------------------------------------------------------|
 */


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

    public Statement DB() throws SQLException, ClassNotFoundException {

        Class.forName("oracle.jdbc.driver.OracleDriver");               //Driver Class
        //Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:9922/CSAMPR1.ITS.RMIT.EDU.AU", "s3690683", "WI5T5bpR");         //Established the database connection.
        Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@//emu.cs.rmit.edu.au:1521/CSAMPR1.ITS.RMIT.EDU.AU", "s3690683", "WI5T5bpR");         //Established the database connection.

        Statement st = conn.createStatement();
        return st;
    }

    public Connection DBConnect() throws ClassNotFoundException, SQLException {
        Class.forName("oracle.jdbc.driver.OracleDriver");               //Driver Class

        //Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:9922/CSAMPR1.ITS.RMIT.EDU.AU", "s3690683", "WI5T5bpR");         //Established the DB connection.
        Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@//emu.cs.rmit.edu.au:1521/CSAMPR1.ITS.RMIT.EDU.AU", "s3690683", "WI5T5bpR");
        //Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:9922:CSAMPR1.ITS.RMIT.EDU.AU", "s3690683", "WI5T5bpR");         //Established the DB connection.
        return conn;
    }
}