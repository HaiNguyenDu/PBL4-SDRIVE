package BLL;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

public class Caculateor {
    public synchronized static String calculateMD5(File file) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                md.update(buffer, 0, bytesRead);
            }
        }
        byte[] messageDigest = md.digest();
        BigInteger no = new BigInteger(1, messageDigest);
        String hashText = no.toString(16);
        while (hashText.length() < 32) {
            hashText = "0" + hashText;
        }
        return hashText;
    }

    public synchronized static String calculateMD5(String content) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(content.getBytes());
        BigInteger no = new BigInteger(1, messageDigest);
        String hashText = no.toString(16);

        // Bổ sung 0 nếu MD5 không đủ 32 ký tự
        while (hashText.length() < 32) {
            hashText = "0" + hashText;
        }
        return hashText;
    }

    public static List<String> readDocxContentLines(File docxFile) throws Exception {
        List<String> lines = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(docxFile); XWPFDocument document = new XWPFDocument(fis)) {
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                String text = paragraph.getText().trim();
                if (!text.isEmpty()) {
                    lines.add(text); // Thêm đoạn văn bản không rỗng vào danh sách
                }
            }
        }
        return lines;
    }

    public static String readDocxContent(File file) throws Exception {
        while (true) {
            try (FileInputStream fis = new FileInputStream(file);
                    FileChannel channel = fis.getChannel();
                    FileLock lock = channel.tryLock(0, Long.MAX_VALUE, true)) {

                if (lock == null) {
                    // File is locked by another process, wait and try again
                    Thread.sleep(100); // Adjust delay as needed
                    continue;
                }

                XWPFDocument document = new XWPFDocument(OPCPackage.open(fis));
                StringBuilder content = new StringBuilder();
                for (XWPFParagraph paragraph : document.getParagraphs()) {
                    content.append(paragraph.getText()).append("\n");
                }
                return content.toString();
            } catch (IOException e) {
                throw new IOException("Error reading file.", e);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Handle interruption
                throw new RuntimeException("Thread was interrupted.", e);
            }
        }
    }

    public static void writeDocxContent(String content, File file) {
        int retries = 5; // Retry up to 5 times
        boolean success = false;

        // Create or load an existing document
        XWPFDocument doc = new XWPFDocument();
        XWPFParagraph paragraph = doc.createParagraph();
        paragraph.createRun().setText(content);

        while (retries > 0 && !success) {
            try (FileOutputStream fos = new FileOutputStream(file);
                    FileChannel channel = fos.getChannel();
                    FileLock lock = channel.tryLock()) { // Exclusive lock for writing

                if (lock == null) {
                    // If the file is locked, retry after waiting
                    retries--;
                    System.out.println("File is locked. Retrying... (" + retries + " attempts left)");
                    Thread.sleep(1000); // Wait before retrying
                    continue;
                }

                // Write the updated document content to the file
                doc.write(fos);
                success = true; // If successful, exit the loop
            } catch (IOException e) {
                retries--;
                if (retries == 0) {
                    System.err.println("Error writing file after multiple retries: " + e.getMessage());
                    break; // Exit after max retries
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Handle thread interruption
                break;
            }
        }
    }

}
