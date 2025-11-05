/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.ashbis.model;

import fr.insa.beuvron.utils.database.ClasseMiroir;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author ashln
 */
public class Admin extends ClasseMiroir {
    private String password;
    private int idTournoi;
    private String username;
    
    @Override
    protected Statement saveSansId(Connection con) throws SQLException {
        PreparedStatement pst = con.prepareStatement(
                "insert into joueur(idTournoi,username,password) values (?,?,?)",
                PreparedStatement.RETURN_GENERATED_KEYS
        );
        pst.setString(2, this.username);
        pst.setString(3, this.password);

        pst.setInt(1, this.idTournoi);
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
     * @return the idTournoi
     */
    public int getIdTournoi() {
        return idTournoi;
    }

    /**
     * @param idTournoi the idTournoi to set
     */
    public void setIdTournoi(int idTournoi) {
        this.idTournoi = idTournoi;
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
    
    
    
}
