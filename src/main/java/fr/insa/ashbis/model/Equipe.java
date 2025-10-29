/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.ashbis.model;

import fr.insa.beuvron.utils.database.ClasseMiroir;
import fr.insa.beuvron.utils.database.ConnectionSimpleSGBD;
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
    private int idTournoi;
    public Equipe(String nom, int terrain, int tournoi) {
        this.nom = nom;
        this.terrain = terrain;
        this.idTournoi = tournoi;
    }

    
    
    @Override
    protected Statement saveSansId(Connection con) throws SQLException {
        PreparedStatement pst = con.prepareStatement(
                "insert into equipe(nom,idTerrain,idTournoi) values (?,?,?)",
                PreparedStatement.RETURN_GENERATED_KEYS
        );
        pst.setString(1, this.nom);
        pst.setInt(2, this.terrain);
        pst.setInt(3, this.idTournoi);
        pst.executeUpdate();
        return pst;
    }

    /**
     * @return the nom
     */
    public String getNom() {
        return nom;
    }
    
    public static void main(String[] args) {
        testCreerE();
    }
    
    public static void testCreerE() {
        try {
            Equipe e = new Equipe("test", 1,1);
            System.out.println("joueur :" + e);
            e.saveInDB(ConnectionSimpleSGBD.defaultCon());
            System.out.println("joueur :" + e);
        } catch (SQLException ex) {
            throw new Error(ex);
        }
    }
    
    public String toString() {
        return ("{equipe " + this.nom + ":" + this.getId() + " " + " tournoi : "+ this.getIdTournoi()+"}");
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

    /**
     * @return the tournoi
     */
    public int getIdTournoi() {
        return idTournoi;
    }

    /**
     * @param tournoi the tournoi to set
     */
    public void setIdTournoi(int tournoi) {
        this.idTournoi = tournoi;
    }
    
}
