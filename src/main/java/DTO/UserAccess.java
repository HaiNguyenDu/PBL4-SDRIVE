package DTO;

public class UserAccess {
    private String username;
    private String access;
    private boolean isI = false;

    public UserAccess(String username, String access) {
        this.username = username;
        this.access = access;
    }

    public UserAccess(String username, String access, boolean isI) {
        this.username = username;
        this.access = access;
        this.isI = isI;
    }

    public String getUsername() {
        return username;
    }

    public boolean isIh() {
        return isI;
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