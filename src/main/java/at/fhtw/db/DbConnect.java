package at.fhtw.db;

import java.sql.*;


public class DbConnect {
        public DbConnect() {
    }


    public Connection connect() {
        Connection connection = null;

        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/MCTG_db?user=postgres&password=fij3tas";
            connection = DriverManager.getConnection(url);
         //   String driver = DriverManager.getDrivers().getClass().getName(); //debug
          //  System.out.println("Connected to DB" + connection);
        } catch(SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }
    public void close(Connection connection) throws SQLException {
        connection.close();
        System.out.println("DB Connection closed.");
    }
}
