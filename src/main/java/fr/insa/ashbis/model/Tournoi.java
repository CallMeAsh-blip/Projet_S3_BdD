/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.ashbis.model;

import fr.insa.beuvron.utils.database.ClasseMiroir;
import fr.insa.beuvron.utils.database.ConnectionSimpleSGBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
    private int idAdmin;

    public Tournoi(String nom, int idAdmin, int nbrTerrain, int maxJoueurEquipe, int maxEquipeTerrain, int nbrRonde) {
        this.nom = nom;
        this.nbrTerrain = nbrTerrain;
        this.maxJoueurEquipe = maxJoueurEquipe;
        this.maxEquipeTerrain = maxEquipeTerrain;
        this.nbrRonde = nbrRonde;
        this.idAdmin = idAdmin;
    }

    public Tournoi( int id,String nom, int idAdmin, int nbrTerrain, int maxJoueurEquipe, int maxEquipeTerrain, int nbrRonde) {
        super(id);
        this.nom = nom;
        this.nbrTerrain = nbrTerrain;
        this.maxJoueurEquipe = maxJoueurEquipe;
        this.maxEquipeTerrain = maxEquipeTerrain;
        this.nbrRonde = nbrRonde;
        this.idAdmin = idAdmin;
    }
    
    
    
    @Override
    protected Statement saveSansId(Connection con) throws SQLException {
        PreparedStatement pst = con.prepareStatement(
                "insert into tournoi(nom,idAdmin,nbrTerrain,maxJoueurEquipe,maxEquipeTerrain,nbrRonde) values (?,?,?,?,?,?)",
                PreparedStatement.RETURN_GENERATED_KEYS
        );
        pst.setString(1, this.nom);
        pst.setInt(2, this.idAdmin);       
        pst.setInt(3, this.nbrTerrain);
        pst.setInt(4, this.maxJoueurEquipe);
        pst.setInt(5, this.maxEquipeTerrain);
        pst.setInt(6, this.nbrRonde);


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
    

    
    public static List<Tournoi> AllTournois(Connection con) throws SQLException {
        List<Tournoi> res = new ArrayList<>();
        try (PreparedStatement pst = con.prepareStatement("select id,nom,idAdmin,nbrTerrain,maxJoueurEquipe,maxEquipeTerrain,nbrRonde from tournoi")) {
            try (ResultSet allU = pst.executeQuery()) {
                while (allU.next()) {
                    res.add(new Tournoi(allU.getInt("id"), allU.getString("nom"),allU.getInt("idAdmin"),
                            allU.getInt("nbrTerrain"),allU.getInt("maxJoueurEquipe"),
                            allU.getInt("maxEquipeTerrain"),allU.getInt("nbrRonde")));
                }
            }
        }
        return res;
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
    
    public static void main(String[] args) {
        testCreer();
    }
    
    
    
    public static void testCreer() {
        try {
            Tournoi t = new Tournoi("test",1,1, 1,1,1);
            System.out.println("joueur :" + t);
            t.saveInDB(ConnectionSimpleSGBD.defaultCon());
            System.out.println("joueur :" + t);
        } catch (SQLException ex) {
            throw new Error(ex);
        }
    }

    /**
     * @return the idAdmin
     */
    public int getIdAdmin() {
        return idAdmin;
    }

    
    
}
