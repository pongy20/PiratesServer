package de.coerdevelopment.pirates.api;

public class Account {

    public int accountID;
    public String mail;
    public String password;

    public Account(int accountID, String mail, String password) {
        this.accountID = accountID;
        this.mail = mail;
        this.password = password;
    }

}
