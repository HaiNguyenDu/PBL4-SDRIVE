package DTO;

public class LoadFolder {
    private String infor = "";
    @SuppressWarnings("unused")
    private String exitstatus = "exit-status: \n";
    @SuppressWarnings("unused")
    private String exception = "";

    public void setInfor(String content) {
        this.infor += content + "\n";
    }
    public String getInfor() {
        return this.infor;
    }

    public void setExitStatus(String content) {
        this.exitstatus += content + "\n\n";
    }

    public void setException(String content) {
        this.exception += content + "\n\n";
    }
}
