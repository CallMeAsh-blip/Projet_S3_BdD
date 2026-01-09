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
    private int minJoueurEquipe;
    private int maxEquipeTerrain;
    private int nbrRonde;
    private int idAdmin;
    private int temps;
    private int inscriptionlibre;
    private int numRonde;
    private int statut;
    
    public Tournoi(String nom, int idAdmin, int nbrTerrain, int maxJoueurEquipe, int maxEquipeTerrain, int nbrRonde, int minJoueurEquipe, int randomizationTeam, int inscriptionlibre, int numRonde, int statut) {
        this.nom = nom;
        this.nbrTerrain = nbrTerrain;
        this.maxJoueurEquipe = maxJoueurEquipe;
        this.maxEquipeTerrain = maxEquipeTerrain;
        this.nbrRonde = nbrRonde;
        this.idAdmin = idAdmin;
        this.minJoueurEquipe=minJoueurEquipe;
        this.temps = randomizationTeam;
        this.inscriptionlibre = inscriptionlibre;
        this.numRonde = numRonde;
        this.statut = statut;
    }

    public Tournoi( int id,String nom, int idAdmin, int nbrTerrain, int maxJoueurEquipe, int maxEquipeTerrain, int nbrRonde, int minJoueurEquipe, int randomizationTeam, int inscriptionlibre, int numRonde, int statut) {
        super(id);
        this.nom = nom;
        this.nbrTerrain = nbrTerrain;
        this.maxJoueurEquipe = maxJoueurEquipe;
        this.maxEquipeTerrain = maxEquipeTerrain;
        this.nbrRonde = nbrRonde;
        this.idAdmin = idAdmin;
        this.minJoueurEquipe = minJoueurEquipe;
        this.temps = randomizationTeam;
        this.inscriptionlibre = inscriptionlibre;
        this.numRonde = numRonde;
        this.statut = statut;
    }
    
    
    
    
    @Override
    protected Statement saveSansId(Connection con) throws SQLException {
        
        if (minJoueurEquipe > maxJoueurEquipe) {
            throw new IllegalArgumentException(
            "Le nombre minimum de joueurs ne peut pas dépasser le maximum"
             );
        }

        PreparedStatement pst = con.prepareStatement(
                "insert into tournoi(nom,idAdmin,nbrTerrain,maxJoueurEquipe,maxEquipeTerrain,nbrRonde,minJoueurEquipe,temps,inscriptionlibre,numRonde,statut) values (?,?,?,?,?,?,?,?,?,?,?)",
                PreparedStatement.RETURN_GENERATED_KEYS
        );
        pst.setString(1, this.nom);
        pst.setInt(2, this.idAdmin);       
        pst.setInt(3, this.nbrTerrain);
        pst.setInt(4, this.maxJoueurEquipe);
        pst.setInt(5, this.maxEquipeTerrain);
        pst.setInt(6, this.nbrRonde);
        pst.setInt(7, this.getMinJoueurEquipe());
        pst.setInt(8, this.temps());
        pst.setInt(9, this.getInscriptionlibre());
        pst.setInt(10, this.getNumRonde());
        pst.setInt(11, this.getStatut());
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
        try (PreparedStatement pst = con.prepareStatement("select id,nom,idAdmin,nbrTerrain,maxJoueurEquipe,maxEquipeTerrain,nbrRonde,minJoueurEquipe,temps,inscriptionlibre,numRonde,statut from tournoi")) {
            try (ResultSet allU = pst.executeQuery()) {
                while (allU.next()) {
                    res.add(new Tournoi(allU.getInt("id"), allU.getString("nom"),allU.getInt("idAdmin"),
                            allU.getInt("nbrTerrain"),allU.getInt("maxJoueurEquipe"),
                            allU.getInt("maxEquipeTerrain"),allU.getInt("nbrRonde"),allU.getInt("minJoueurEquipe"),
                            allU.getInt("temps"),allU.getInt("inscriptionlibre"),
                            allU.getInt("numRonde"),allU.getInt("statut")));
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
    
//    public static void main(String[] args) {
//        testCreer();
//    }
    
    
    
//    public static void testCreer() {
//        try {
//            Tournoi t = new Tournoi("test",1,1, 1,1,1,1,1,0);
//            System.out.println("joueur :" + t);
//            t.saveInDB(ConnectionSimpleSGBD.defaultCon());
//            System.out.println("joueur :" + t);
//        } catch (SQLException ex) {
//            throw new Error(ex);
//        }
//    }

    /**
     * @return the idAdmin
     */
    public int getIdAdmin() {
        return idAdmin;
    }
    
    public static List<Tournoi> allTournoisByAdmin(Connection con, int idAdmin) throws SQLException {
        List<Tournoi> res = new ArrayList<>();

        try (PreparedStatement pst = con.prepareStatement(
                "SELECT id, nom, idAdmin, nbrTerrain, maxJoueurEquipe, maxEquipeTerrain, nbrRonde, minJoueurEquipe,temps,inscriptionlibre,numRonde,statut " +
                "FROM tournoi WHERE idAdmin = ?")) {
            pst.setInt(1, idAdmin);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    res.add(new Tournoi(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getInt("idAdmin"),
                        rs.getInt("nbrTerrain"),
                        rs.getInt("maxJoueurEquipe"),
                        rs.getInt("maxEquipeTerrain"),
                        rs.getInt("nbrRonde"),
                        rs.getInt("minJoueurEquipe"),
                        rs.getInt("temps"),
                        rs.getInt("inscriptionlibre"),
                        rs.getInt("numRonde"),
                        rs.getInt("statut")
                    ));
                }
            }
        }
        return res;
    }

    /**
     * @return the minJoueurEquipe
     */
    public int getMinJoueurEquipe() {
        return minJoueurEquipe;
    }

    /**
     * @param minJoueurEquipe the minJoueurEquipe to set
     */
    public void setMinJoueurEquipe(int minJoueurEquipe) {
        this.minJoueurEquipe = minJoueurEquipe;
    }

    /**
     * @return the randomizationTeam
     */
    public int temps() {
        return getTemps();
    }

    /**
     * @param randomizationTeam the randomizationTeam to set
     */
    public void temps(int randomizationTeam) {
        this.setTemps(randomizationTeam);
    }

    /**
     * @return the inscriptionlibre
     */
    public int getInscriptionlibre() {
        return inscriptionlibre;
    }

    /**
     * @param inscriptionlibre the inscriptionlibre to set
     */
    public void setInscriptionlibre(int inscriptionlibre) {
        this.inscriptionlibre = inscriptionlibre;
    }


    public void deleteInDB(Connection con) throws SQLException {
        if (this.getId() <= 0) {
            throw new IllegalStateException("Tournoi non enregistré");
        }
        
        try (PreparedStatement pst = con.prepareStatement(
                "DELETE FROM tournoi WHERE id = ?")) {
            pst.setInt(1, this.getId());
            pst.executeUpdate();
        }
    }
    
    public static Tournoi findTournoiById(Connection con, int id) throws SQLException {

        try (PreparedStatement pst = con.prepareStatement(
                "SELECT id, nom, idAdmin, nbrTerrain, maxJoueurEquipe, maxEquipeTerrain, nbrRonde, minJoueurEquipe,temps,inscriptionlibre,numRonde,statut " +
                "FROM tournoi WHERE id = ?")) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Tournoi t = new Tournoi(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getInt("idAdmin"),
                        rs.getInt("nbrTerrain"),
                        rs.getInt("maxJoueurEquipe"),
                        rs.getInt("maxEquipeTerrain"),
                        rs.getInt("nbrRonde"),
                        rs.getInt("minJoueurEquipe"),
                        rs.getInt("temps"),
                        rs.getInt("inscriptionlibre"),
                        rs.getInt("numRonde"),
                        rs.getInt("statut")
                    );
                return t;
                }else{
                    return null;
                }
            }
        }
    }

    /**
     * @return the numRonde
     */
    public int getNumRonde() {
        return numRonde;
    }

    /**
     * @param numRonde the numRonde to set
     */
    public void setNumRonde(int numRonde) {
        this.numRonde = numRonde;
    }

    /**
     * @return the statut
     */
    public int getStatut() {
        return statut;
    }

    /**
     * @param statut the statut to set
     */
    public void setStatut(int statut) {
        this.statut = statut;
    }

    public void updateStatut(Connection con) throws SQLException {

        if (this.getId() <= 0) {
            throw new IllegalStateException("Tournoi non enregistré");
        }

        try (PreparedStatement pst = con.prepareStatement(
            "UPDATE tournoi SET statut = ? WHERE id = ?")) {
            pst.setInt(1, this.statut);
            pst.setInt(2, this.getId());
            pst.executeUpdate();
        }
}

    /**
     * @return the temps
     */
    public int getTemps() {
        return temps;
    }

    /**
     * @param temps the temps to set
     */
    public void setTemps(int temps) {
        this.temps = temps;
    }

    
}
