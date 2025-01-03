package DAL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import DTO.Mail;

public class Mail_DAL {
    public static Connection connectToDatabase() throws Exception {
        // URL, user, và password cần thay đổi theo cơ sở dữ liệu của bạn
        // String url = "jdbc:mysql://192.168.1.35:3306/PBL4?useSSL=false&allowPublicKeyRetrieval=true&maxPoolSize=50";
        // String user = "pbl4_user";
        // String password = "12345";

        String url =        "jdbc:mysql://localhost:3306/PBL4?useSSL=false&allowPublicKeyRetrieval=true";
        String user = "root";
        String password = "Thanhan@2004";

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
        String query = "SELECT * FROM logfile WHERE username_receive = '" + ConnectWindowServer.user
                + "' order by id DESC";
        return stmt.executeQuery(query);
    }

    public static ResultSet loadShareItem() throws Exception {
        Connection conn = connectToDatabase();
        Statement stmt = conn.createStatement();
        return stmt.executeQuery("SELECT * FROM logfile WHERE username_send = '" + ConnectWindowServer.user + "'");
    }

    public static void updateMail_Seen(Mail mail) throws Exception {
        Connection conn = connectToDatabase();
        String sql = "UPDATE logFile SET seen = ? WHERE username_send = ? and username_receive = ? and item_name = ? and path = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, mail.getSeen());
            pstmt.setString(2, mail.getUsername_send());
            pstmt.setString(3, mail.getUsername_receive());
            pstmt.setString(4, mail.getItem_name());
            pstmt.setString(5, mail.getPath());
            System.out.println(pstmt.toString());
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Số hàng bị ảnh hưởng: " + rowsAffected);
        }
        conn.close();
    }
}