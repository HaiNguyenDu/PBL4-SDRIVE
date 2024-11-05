package BLL;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileSync {
    public static String calculateMD5(File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] dataBytes = new byte[1024];
            int nread;
            while ((nread = fis.read(dataBytes)) != -1) {
                md.update(dataBytes, 0, nread);
            }
            byte[] mdbytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : mdbytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void syncFile(String sourcePath, String destPath) {
        File sourceFile = new File(sourcePath);
        File destFile = new File(destPath);
        if (!sourceFile.exists()) {
            System.out.println("Tệp nguồn không tồn tại: " + sourcePath);
            return;
        }
        String sourceMD5 = calculateMD5(sourceFile);
        String destMD5 = destFile.exists() ? calculateMD5(destFile) : null;
        if (destMD5 == null || !sourceMD5.equals(destMD5)) {
            try {
                Files.copy(sourceFile.toPath(), destFile.toPath());
                System.out.println("Tệp đã được đồng bộ hóa từ " + sourcePath + " đến " + destPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Tệp đã đồng bộ, không cần thay đổi: " + destPath);
        }
    }
}