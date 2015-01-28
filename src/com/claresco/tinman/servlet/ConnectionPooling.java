/**
 * Copyright (c) 1999, 2014 Claresco Corporation, Berkeley, California. All rights reserved.
 *
 *
 * ConnectionPooling.java	May 20, 2014
 *
 * Copyright 2014 Claresco Corporation, Berkeley, CA 94704. All Rights Reserved.
 *
 * This software is the proprietary information of Claresco Corporation.
 * Use is subject to license terms.
 *
 * Author : Rheza
 *
 */
package com.claresco.tinman.servlet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnection;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDriver;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;


/**
 * ConnectionPooling
 * @author Rheza
 *
 * Description: 
 *	Copied from apache commons website
 *
 * Status: 
 *
 *
 */
public class ConnectionPooling {
	
	private String myPoolName = "example";
	private static final String myPoolingDriverName = "jdbc:apache:commons:dbcp:";
	
	/**
	 * Constructor 
	 *
	 * Params:
	 *
	 *
	 */
	public ConnectionPooling(String connectionURL, String userName, String password, String driverName) throws ClassNotFoundException, SQLException{
		Class.forName(driverName);
		
		Properties props = new Properties();
		props.setProperty("user", userName);
		props.setProperty("password", password);
		
		ObjectPool connectionPool = new GenericObjectPool(null);
		
		ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(connectionURL, props);
		
		PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, 
				connectionPool, null, null, false, true);
		
		
		Class.forName("org.apache.commons.dbcp.PoolingDriver");
        PoolingDriver driver = (PoolingDriver) DriverManager.getDriver(myPoolingDriverName);
        driver.registerPool(myPoolName,connectionPool);
	}
	
	
	
	protected Connection getConnection() throws SQLException{
		return DriverManager.getConnection(myPoolingDriverName+myPoolName);
	}
	
	/**
	public static void main(String[] args) {
		try {
			Class.forName("org.postgresql.Driver");
			ConnectionPooling cp = new ConnectionPooling();
			Connection conn = DriverManager.getConnection("jdbc:apache:commons:dbcp:example");
			String s = "select statementid from statement where statementid = ?;";
			PreparedStatement statement = conn.prepareStatement(s);
			statement.setInt(1, 10881);
			java.sql.ResultSet rs = statement.executeQuery();
			rs.next();
			System.out.println(rs.getInt("statementid"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	**/
}
