package de.coerdevelopment.pirates.api.repository.auth;

import de.coerdevelopment.pirates.api.Account;
import de.coerdevelopment.standalone.repository.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Table syntax:
 *
 * accountId - PK -> auto increment
 * mail - NN UK
 * password NN
 */
public class AccountRepository extends Repository {

    private static AccountRepository instance;

    public static AccountRepository getInstance() {
        if (instance == null) {
            instance = new AccountRepository();
        }
        return instance;
    }

    private AccountRepository() {
        super("acc");
    }

    public void createTable() {
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS " + tableName
                    + " (accountId INTEGER NOT NULL AUTO_INCREMENT," +
                    "mail VARCHAR(100) NOT NULL," +
                    "password VARCHAR(64) NOT NULL," +
                    "CONSTRAINT pk_accountId PRIMARY KEY(accountid)," +
                    "CONSTRAINT uk_mail UNIQUE(mail))");
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean doesMailExists(String mail) {
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("SELECT accountId FROM " + tableName
                    + " WHERE mail = ?");
            ps.setString(1, mail);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void insertAccount(String mail, String password) {
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("INSERT INTO " + tableName
                    + " (mail,password) VALUES (?,?)");
            ps.setString(1, mail);
            ps.setString(2, password);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePassword(String mail, String newPassword) {
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("UPDATE " + tableName
                    + " SET password = ? WHERE mail = ?");
            ps.setString(1, newPassword);
            ps.setString(2, mail);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updatePassword(int accountId, String newPassword) {
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("UPDATE " + tableName
                    + " SET password = ? WHERE accountId = ?");
            ps.setString(1, newPassword);
            ps.setInt(2, accountId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isPasswordCorrect(String mail, String password) {
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("SELECT password FROM " + tableName
                    + " WHERE mail = ?");
            ps.setString(1, mail);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("password").equals(password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public Account getAccountById(int accountId) {
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("SELECT * FROM " + tableName
                    + " WHERE accountId = ?");
            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return getAccountByResultSetEntry(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Account getAccountByMail(String mail) {
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("SELECT * FROM " + tableName
                    + " WHERE mail = ?");
            ps.setString(1, mail);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return getAccountByResultSetEntry(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Account getAccountByResultSetEntry(ResultSet rs) throws SQLException {
        if (rs == null) {
            return null;
        }
        int accountId = rs.getInt("accountId");
        String mail = rs.getString("mail");
        String password = rs.getString("password");

        return new Account(accountId, mail, password);
    }

}
