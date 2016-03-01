package com.example.sistemas.centralactuarios;

/**
 * Created by Sistemas on 29/02/2016.
 */
        import android.annotation.SuppressLint;
        import android.os.StrictMode;
        import android.util.Log;
        import java.sql.SQLException;
        import java.sql.Connection;
        import java.sql.DriverManager;
/**
 * Created by h-pc on 16-Oct-15.
 */
public class ConnectionClass {
    String ip = "10.12.12.101";
    String classs = "net.sourceforge.jtds.jdbc.Driver";
    String db = "Andro";
    String un = "sa";
    String password = "masterkey";


    @SuppressLint("NewApi")
    public Connection CONN() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String ConnURL = null;
        try {
            Class.forName(classs);
            ConnURL = "jdbc:jtds:sqlserver://" + ip + ";"
                    + "databaseName=" + db + ";user=" + un + ";password="
                    + password + ";";
            conn = DriverManager.getConnection(ConnURL);
        } catch (SQLException se) {
            Log.e("ERRO", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERRO", e.getMessage());
        } catch (Exception e) {
            Log.e("ERRO", e.getMessage());
        }
        return conn;
    }
}