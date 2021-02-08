package com.mio.boat;

public class UserBean {
    private String userName;
    private String userState;
    private boolean isSelected;
    private String userAccount;
    private String userPass;
    private String uuid;
    private String token;
    
    public UserBean(){
        
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserState(String userState) {
        this.userState = userState;
    }

    public String getUserState() {
        return userState;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getUserPass() {
        return userPass;
    }
}
