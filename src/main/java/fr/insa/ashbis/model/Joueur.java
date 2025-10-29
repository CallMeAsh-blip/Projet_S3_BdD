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
public class Joueur extends ClasseMiroir {

    private String prenom;
    private String nom;
    private String genre;
    private String dateDeNaissance;
    private int score;
    private int idEquipe;
    private int priority;
    private int idTournoi;

    public Joueur(String prenom, String nom, String genre, String DateDeNaissance, int score, int IdEquipe, int priority, int tournoi) {
        this.prenom = prenom;
        this.nom = nom;
        this.genre = genre;
        this.dateDeNaissance = DateDeNaissance;
        this.score = score;
        this.idEquipe = IdEquipe;
        this.priority = priority;
        this.idTournoi = tournoi;
    }

    

    @Override
    protected Statement saveSansId(Connection con) throws SQLException {
        PreparedStatement pst = con.prepareStatement(
                "insert into joueur(prenom,nom,genre,dateDeNaissance,priority,score,idEquipe,idTournoi) values (?,?,?,?,?,?,?,?)",
                PreparedStatement.RETURN_GENERATED_KEYS
        );
        pst.setString(1, this.getPrenom());
        pst.setString(2, this.getNom());
        pst.setString(3, this.getGenre());
        pst.setString(4, this.getDateDeNaissance());
        pst.setInt(5, this.priority);
        pst.setInt(6, this.getScore());
        pst.setInt(7, this.getIdEquipe());
        pst.setInt(8, this.getIdTournoi());
        pst.executeUpdate();
        return pst;
    }

    public String toString() {
        return ("{joueur " + this.getPrenom() + " " +this.getNom() + ":" + this.getId() + "}");
    }
    
    
    
    public static void testCreer() {
        try {
            Joueur j = new Joueur("test", "test","non binaire","2006-12-08",0,0,1,1);
            System.out.println("joueur :" + j);
            j.saveInDB(ConnectionSimpleSGBD.defaultCon());
            System.out.println("joueur :" + j);
        } catch (SQLException ex) {
            throw new Error(ex);
        }
    }
    public void resetPriority(){
        if(this.priority==1){
            priority=0;
        }
    }
    
    public void setPriority(){
        if(this.priority==0){
            priority=1;
        }
    }
    public static void main(String[] args) {
        testCreer();
    }

    public int getPriority() {
        return priority;
    }

    /**
     * @return the prenom
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * @param prenom the prenom to set
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
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
     * @return the genre
     */
    public String getGenre() {
        return genre;
    }

    /**
     * @param genre the genre to set
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * @return the DateDeNaissance
     */
    public String getDateDeNaissance() {
        return dateDeNaissance;
    }

    /**
     * @param DateDeNaissance the DateDeNaissance to set
     */
    public void setDateDeNaissance(String DateDeNaissance) {
        this.dateDeNaissance = DateDeNaissance;
    }

    /**
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /**
     * @param score the score to set
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * @return the IdEquipe
     */
    public int getIdEquipe() {
        return idEquipe;
    }

    /**
     * @param IdEquipe the IdEquipe to set
     */
    public void setIdEquipe(int IdEquipe) {
        this.idEquipe = IdEquipe;
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