package DTO;

public class UserTracker {
    String username;
    int CurentTime;

    public UserTracker(String username){
        this.username = username;
        CurentTime = 0;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getCurentTime() {
        return CurentTime;
    }

    public void setCurentTime(int curentTime) {
        CurentTime = curentTime;
    }
}
