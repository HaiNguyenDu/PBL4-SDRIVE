package DTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class File_Folder {
    String name;
    LocalDateTime lastWriteTime;

    public File_Folder(String name, LocalDateTime lastWriteTime) {
        this.name = name;
        this.lastWriteTime = lastWriteTime;
    }
    public File_Folder(String name, String lastWriteTime) {
        this.name = name;
        this.lastWriteTime = LocalDateTime.parse(lastWriteTime, DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getLastWriteTime() {
        return lastWriteTime;
    }
    
    @Override
    public String toString() {
        // Format the lastWriteTime to the desired pattern
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");
        return name + "|" + lastWriteTime.format(formatter);
    }
}
