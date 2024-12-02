package BLL;

import java.sql.ResultSet;
import java.util.ArrayList;

import DAL.Mail_DAL;
import DTO.Mail;
import javafx.scene.layout.HBox;

public class Mail_BLL {
    public static ArrayList<Mail> getSharedItem() {
        ArrayList<Mail> result = new ArrayList<>();
        try {
            ResultSet rs = Mail_DAL.loadSharedItem();
            while (rs.next()) {
                result.add(new Mail(
                        rs.getString("username_send"),
                        rs.getString("username_receive"),
                        rs.getString("date"),
                        rs.getString("item_name"),
                        rs.getString("path"),
                        rs.getBoolean("seen"),
                        rs.getString("access_modifier")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static ArrayList<Mail> getShareItem() {
        ArrayList<Mail> result = new ArrayList<>();
        try {
            ResultSet rs = Mail_DAL.loadShareItem();
            while (rs.next()) {
                result.add(new Mail(
                        rs.getString("username_send"),
                        rs.getString("username_receive"),
                        rs.getString("date"),
                        rs.getString("item_name"),
                        rs.getString("path"),
                        rs.getBoolean("seen"),
                        rs.getString("access_modifier")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
