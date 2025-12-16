/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.ashbis.model;

import fr.insa.beuvron.utils.database.ClasseMiroir;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

/**
 *
 * @author ashln
 */
public class Admin extends ClasseMiroir {
    private String password;
    private String username;

    public Admin(String password, String username) {
        super();
        this.password = password;
        this.username = username;
    }

    public Admin(String password, String username, int id) {
        super(id);
        this.password = password;
        this.username = username;
    }
    
    
    
    @Override
    protected Statement saveSansId(Connection con) throws SQLException {
        PreparedStatement pst = con.prepareStatement(
                "insert into admin(username,password) values (?,?)",
                PreparedStatement.RETURN_GENERATED_KEYS
        );
        pst.setString(1, this.username);
        pst.setString(2, this.password);
        pst.executeUpdate();
        return pst;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    
    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    public static Optional<Admin> findBySurnomPass(Connection con,String username,String pass) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement(
                "select id from admin where username = ? and password = ?")) {
            pst.setString(1, username);
            pst.setString(2, pass);
            ResultSet res = pst.executeQuery();
            if (res.next()) {
                int id = res.getInt(1);
                return Optional.of(new Admin(pass, username,id));
            } else {
                return Optional.empty();
            }

        }
    }
    
    
}
