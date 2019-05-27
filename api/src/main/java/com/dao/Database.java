package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.ConfigReader;
import com.constants.Constants;
import com.mysql.cj.jdbc.MysqlDataSource;

/**
 * 
 * @author Shreyas
 *
 */
public class Database {
    private static final Logger LOG = LoggerFactory.getLogger(Database.class);
    public Connection connection;
    PreparedStatement pStmtBatch;
    ResultSet cRowSet;

    public Database() throws Exception {
	super();
	getConnection();
    }

    public boolean getConnection() throws Exception {
	DataSource ds = getDataSource();
	if (ds == null) {
	    return false;
	}
	connection = ds.getConnection();
	return true;
    }

    public void setAutoCommitFalse() throws SQLException {
	if (connection != null) {
	    connection.setAutoCommit(false);
	}
    }

    public void commit() throws SQLException {
	if (connection != null) {
	    connection.commit();
	}
    }

    public void rollBack() throws SQLException {
	if (connection != null) {
	    connection.rollback();
	}
    }

    public void createBatch(String query) throws Exception {
	if (connection == null) {
	    getConnection();
	    if (connection == null) {
		return;
	    }
	}
	if (connection != null) {
	    setAutoCommitFalse();
	    pStmtBatch = connection.prepareStatement(query);
	}

    }

    public void executeQuery(String query, LinkedList<Object> parameters) throws Exception {
	// Check if the connection is not obtained, if obtained then proceed, if
	// not get a new connection.
	if (connection == null) {
	    getConnection();
	    if (connection == null) {
		return;
	    }
	}
	PreparedStatement ps = connection.prepareStatement(query);
	if (parameters != null && parameters.size() > 0) {
	    int counter = 1;
	    for (Object object : parameters) {
		ps.setObject(counter++, object);
	    }
	}
	cRowSet = ps.executeQuery();
    }

    public void addBatch(String query, LinkedList<Object> parameters) throws Exception {
	// Check if the connection is not obtained, if obtained then proceed, if
	// not get a new connection.
	if (connection == null) {
	    createBatch(query);
	}

	if (parameters != null && parameters.size() > 0) {
	    int counter = 1;
	    for (Object object : parameters) {
		pStmtBatch.setObject(counter++, object);
	    }
	}
	pStmtBatch.addBatch();
    }

    public int[] executeBatch() throws SQLException {
	if (pStmtBatch != null) {
	    return pStmtBatch.executeBatch();
	}
	return null;
    }

    public int executeUpdate(String query, LinkedList<Object> parameters) throws Exception {
	// Check if the connection is not obtained, if obtained then proceed, if
	// not get a new connection.
	if (connection == null) {
	    getConnection();
	    if (connection == null) {
		return 0;
	    }
	}
	PreparedStatement ps = connection.prepareStatement(query);
	if (parameters != null && parameters.size() > 0) {
	    int counter = 1;
	    for (Object object : parameters) {
		ps.setObject(counter++, object);
	    }
	}
	return ps.executeUpdate();
    }

    public void closeConnection() throws SQLException {
	if (pStmtBatch != null) {
	    pStmtBatch.close();
	}
	if (cRowSet != null) {
	    cRowSet.close();
	}
	if (connection != null) {
	    connection.close();
	}
    }

    private static DataSource ds = null;

    static {
	LOG.info("Inside Database() static method... ");
	Context context = null;
	try {
	    context = new InitialContext();

	    // ds = (DataSource) context.lookup("java:comp/env/jdbc/bon");
	    MysqlDataSource mysqlDS = new MysqlDataSource();
	    try {
		mysqlDS.setURL(ConfigReader.getObject().getAppConfig(Constants.MYSQL_DB_URL));
		mysqlDS.setUser(ConfigReader.getObject().getAppConfig(Constants.MYSQL_DB_USERNAME));
		mysqlDS.setPassword(ConfigReader.getObject().getAppConfig(Constants.MYSQL_DB_PASSWORD));
		ds = mysqlDS;
	    } catch (Exception e) {
		e.printStackTrace();
		LOG.error("Error : " + e.getMessage());
	    }

	} catch (NamingException e) {
	    LOG.error("Error : " + e.getMessage());
	    LOG.error("Unable to get Datasource...");
	    e.printStackTrace();
	}
    }

    public static DataSource getDataSource() {
	return ds;
    }
}
