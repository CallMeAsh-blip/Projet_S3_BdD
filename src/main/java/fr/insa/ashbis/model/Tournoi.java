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
public class Tournoi extends ClasseMiroir{
    private String nom;
    private int nbrTerrain;
    private int maxJoueurEquipe;
    private int maxEquipeTerrain;
    private int nbrRonde;

    public Tournoi(String nom, int nbrTerrain, int maxJoueurEquipe, int maxEquipeTerrain, int nbrRonde) {
        this.nom = nom;
        this.nbrTerrain = nbrTerrain;
        this.maxJoueurEquipe = maxJoueurEquipe;
        this.maxEquipeTerrain = maxEquipeTerrain;
        this.nbrRonde = nbrRonde;
    }
    
    @Override
    protected Statement saveSansId(Connection con) throws SQLException {
        PreparedStatement pst = con.prepareStatement(
                "insert into joueur(nom,nbrTerrain,maxJoueurEquipe,maxEquipeTerrain,nbrEonde) values (?,?,?,?,?)",
                PreparedStatement.RETURN_GENERATED_KEYS
        );
        pst.setString(1, this.nom);
        pst.setInt(2, this.nbrTerrain);
        pst.setInt(3, this.maxJoueurEquipe);
        pst.setInt(4, this.maxEquipeTerrain);
        pst.setInt(5, this.nbrRonde);

        pst.executeUpdate();
        return pst;
    }
    
    public String toString(){
        return("Tournoi :"+this.nom+" "+getId());
    }
    /**
     * @return the nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * @param nom the nom to set
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * @return the nbrTerrain
     */
    public int getNbrTerrain() {
        return nbrTerrain;
    }

    /**
     * @param nbrTerrain the nbrTerrain to set
     */
    public void setNbrTerrain(int nbrTerrain) {
        this.nbrTerrain = nbrTerrain;
    }

    /**
     * @return the maxJoueurEquipe
     */
    public int getMaxJoueurEquipe() {
        return maxJoueurEquipe;
    }

    /**
     * @param maxJoueurEquipe the maxJoueurEquipe to set
     */
    public void setMaxJoueurEquipe(int maxJoueurEquipe) {
        this.maxJoueurEquipe = maxJoueurEquipe;
    }

    /**
     * @return the maxEquipeTerrain
     */
    public int getMaxEquipeTerrain() {
        return maxEquipeTerrain;
    }

    /**
     * @param maxEquipeTerrain the maxEquipeTerrain to set
     */
    public void setMaxEquipeTerrain(int maxEquipeTerrain) {
        this.maxEquipeTerrain = maxEquipeTerrain;
    }

    /**
     * @return the nbrRonde
     */
    public int getNbrRonde() {
        return nbrRonde;
    }

    /**
     * @param nbrRonde the nbrRonde to set
     */
    public void setNbrRonde(int nbrRonde) {
        this.nbrRonde = nbrRonde;
    }
    
    
    
}
