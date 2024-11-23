package DTO;

public class UserAccess {
    private String username;
    private String access;

    public UserAccess(String username, String access) {
        this.username = username;
        this.access = access;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    @Override
    public String toString() {
        return username + ": " + access;
    }

    @Override
    public UserAccess clone() {
        return new UserAccess(this.username, this.access);
    }

}