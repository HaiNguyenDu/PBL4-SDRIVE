package DAL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Mail_DAL {
    public static Connection connectToDatabase() throws Exception {
        // URL, user, và password cần thay đổi theo cơ sở dữ liệu của bạn
        String url = "jdbc:mysql://192.168.1.29:3306/PBL4?useSSL=false&allowPublicKeyRetrieval=true&maxPoolSize=50";
        String user = "pbl4_user";
        String password = "12345";

        // String url =
        // "jdbc:mysql://localhost:3306/PBL4?useSSL=false&allowPublicKeyRetrieval=true";
        // String user = "root";
        // String password = "Thanhan@2004";

        // Tạo kết nối
        return DriverManager.getConnection(url, user, password);
    }

    public static ResultSet fetchData() throws Exception {
        Connection conn = connectToDatabase();
        Statement stmt = conn.createStatement();
        return stmt.executeQuery("SELECT * FROM logfile");
    }

    public static ResultSet loadSharedItem() throws Exception {
        Connection conn = connectToDatabase();
        Statement stmt = conn.createStatement();
        return stmt.executeQuery("SELECT * FROM logfile WHERE username_receive = '" + ConnectWindowServer.user + "'");
    }

    public static ResultSet loadShareItem() throws Exception {
        Connection conn = connectToDatabase();
        Statement stmt = conn.createStatement();
        return stmt.executeQuery("SELECT * FROM logfile WHERE username_send = '" + ConnectWindowServer.user + "'");
    }
}