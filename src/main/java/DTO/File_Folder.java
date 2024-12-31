package DTO;

// import java.time.LocalDateTime;
// import java.time.format.DateTimeFormatter;

// Định nghĩa lớp File_Folder
public class File_Folder {
    private String Name;
    private String LastTimeWrite;

    // Constructor
    public File_Folder(String name, String LastTW) {
        this.Name = name;
        this.LastTimeWrite = LastTW;
    }

    // Getter và Setter
    public String getName() {
        return this.Name;
    }

    public void setName(String newName) {
        this.Name = newName;
    }

    public String getLastTimeWrite() {
        return this.LastTimeWrite;
    }

    public void setLastTimeWrite(String newLWT) {
        this.LastTimeWrite = newLWT;
    }
}