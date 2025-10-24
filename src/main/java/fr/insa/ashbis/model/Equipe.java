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
public class Equipe extends ClasseMiroir {
    
    private String nom;
    private int terrain;

    public Equipe(String nom, int terrain) {
        this.nom = nom;
        this.terrain = terrain;
    }

    
    
    @Override
    protected Statement saveSansId(Connection con) throws SQLException {
        PreparedStatement pst = con.prepareStatement(
                "insert into joueur(nom,terrain) values (?,?)",
                PreparedStatement.RETURN_GENERATED_KEYS
        );
        pst.setString(1, this.nom);
        pst.setInt(2, this.terrain);
        pst.executeUpdate();
        return pst;
    }

    /**
     * @return the nom
     */
    public String getNom() {
        return nom;
    }
    
    public String toString() {
        return ("{equipe " + this.nom + ":" + this.getId() + "}");
    }
    
    /**
     * @param nom the nom to set
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * @return the terrain
     */
    public int getTerrain() {
        return terrain;
    }

    /**
     * @param terrain the terrain to set
     */
    public void setTerrain(int terrain) {
        this.terrain = terrain;
    }
    
}
