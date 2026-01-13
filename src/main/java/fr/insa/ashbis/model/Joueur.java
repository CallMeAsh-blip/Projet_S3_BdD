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
import java.util.Optional;

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

    public Joueur(String prenom, String nom, String genre, String dateDeNaissance, int score, int idEquipe, int priority, int idTournoi, int id) {
        super(id);
        this.prenom = prenom;
        this.nom = nom;
        this.genre = genre;
        this.dateDeNaissance = dateDeNaissance;
        this.score = score;
        this.idEquipe = idEquipe;
        this.priority = priority;
        this.idTournoi = idTournoi;
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
        pst.setDate(4, java.sql.Date.valueOf(this.dateDeNaissance));
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
            Joueur j = new Joueur("test", "test","non binaire","2006-12-08",0,1,0,1);
            System.out.println("joueur :" + j);
            j.saveInDB(ConnectionSimpleSGBD.defaultCon());
            System.out.println("joueur :" + j);
        } catch (SQLException ex) {
            throw new Error(ex);
        }
    }
    public void resetPriority(){
        this.priority=0;    
    }
    
    public void setPriority(){
        this.priority=1;
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


    public static List<Joueur> allJoueursByTournoi(Connection con, int idTournoi) throws SQLException {
        List<Joueur> joueurs = new ArrayList<>();
        String sql = "SELECT id, prenom, nom, genre, dateDeNaissance, score, idEquipe, priority, idTournoi FROM joueur WHERE idTournoi = ? order by score DESC";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, idTournoi);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    joueurs.add(fromResultSet(rs));
                }
            }
        }
        return joueurs;
    }
    
    public static int countJoueurByTournoi(Connection con, int idTournoi)
        throws SQLException {

        String sql = "SELECT COUNT(*) FROM joueur WHERE idTournoi = ?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, idTournoi);
            try (ResultSet rs = pst.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    
    public String findEquipe(Connection con) throws SQLException {
        if (this.idEquipe ==0) {
            return "Aucune équipe";
        }
        else{
            try (PreparedStatement pst = con.prepareStatement(
                    "SELECT nom FROM equipe WHERE id = ?")) {

                pst.setInt(1, this.idEquipe);
                ResultSet res = pst.executeQuery();

                if (res.next()) {
                    return res.getString("nom");
                } else {
                    return "Aucune équipe";
                }
            }
        }
    }

    public static List<Joueur> allJoueursPrioritairesByTournoi(Connection con, int idTournoi)
        throws SQLException {

        List<Joueur> joueurs = new ArrayList<>();

        String sql = """
            SELECT id, prenom, nom, genre, dateDeNaissance, score,
            idEquipe, priority, idTournoi
            FROM joueur
            WHERE idTournoi = ? AND priority = 1
            """;

        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, idTournoi);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    joueurs.add(fromResultSet(rs));
                }
            }
        }
        return joueurs;
    }
    
    public void updatePriority(Connection con) throws SQLException {

        if (this.getId() == -1) {
            throw new IllegalStateException("Joueur non enregistré");
        }

        try (PreparedStatement pst = con.prepareStatement(
            "UPDATE joueur SET priority = ? WHERE id = ?")) {

            pst.setInt(1, this.priority);
            pst.setInt(2, this.getId());
            pst.executeUpdate();
        }
    }

    public void updateEquipe(Connection con) throws SQLException {

        if (this.getId() <= 0) {
            throw new IllegalStateException("Joueur non enregistré");
        }

        try (PreparedStatement pst = con.prepareStatement(
            "UPDATE joueur SET idEquipe = ? WHERE id = ?")) {

            pst.setInt(1, this.idEquipe);
            pst.setInt(2, this.getId());
            pst.executeUpdate();
        }
    }
    
    public void deleteInDB(Connection con) throws SQLException {
        if (this.getId() <= 0) {
            throw new IllegalStateException("joueur non enregistré");
        }
        
        try (PreparedStatement pst = con.prepareStatement(
                "DELETE FROM tournoi WHERE id = ?")) {
            pst.setInt(1, this.getId());
            pst.executeUpdate();
        }
    }
    
    public static List<Joueur> allJoueursByEquipe(Connection con, int idEquipe) throws SQLException {
    List<Joueur> joueurs = new ArrayList<>();
    String sql = "SELECT * FROM joueur WHERE idEquipe = ?";
    try (PreparedStatement pst = con.prepareStatement(sql)) {
        pst.setInt(1, idEquipe);
        try (ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                joueurs.add(fromResultSet(rs));
            }
        }
    }
    return joueurs;
}
    public void updateScore(Connection con) throws SQLException {
        String sql = "UPDATE joueur SET score = ? WHERE id = ?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, this.score);
            pst.setInt(2, this.getId());
            pst.executeUpdate();
        }
    }
    
    public static Joueur fromResultSet(ResultSet rs) throws SQLException {
        Joueur j = new Joueur(rs.getString("prenom"),
            rs.getString("nom"),
            rs.getString("genre"),
            rs.getDate("dateDeNaissance").toString(),
            rs.getInt("score"),
            rs.getInt("idEquipe"),
            rs.getInt("priority"),
            rs.getInt("idTournoi"),
            rs.getInt("id"));
        return j;
    }
    
    public static List<Joueur> allJoueursNotInTournoi(Connection con, int idTournoi) throws SQLException {
        List<Joueur> joueurs = new ArrayList<>();
        String sql = "SELECT * FROM joueur WHERE idTournoi != ?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, idTournoi);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    joueurs.add(fromResultSet(rs));
                }
            }
        }
        return joueurs;
    }
    
    public static void ImportJoueurInTournois(Connection con, int idTournoi, Joueur j) throws SQLException{
        PreparedStatement pst = con.prepareStatement(
                "insert into joueur(prenom,nom,genre,dateDeNaissance,priority,score,idEquipe,idTournoi) values (?,?,?,?,?,?,?,?)",
                PreparedStatement.RETURN_GENERATED_KEYS
        );
        pst.setString(1, j.getPrenom());
        pst.setString(2, j.getNom());
        pst.setString(3, j.getGenre());
        pst.setDate(4, java.sql.Date.valueOf(j.dateDeNaissance));
        pst.setInt(5, 0);
        pst.setInt(6, 0);
        pst.setInt(7, 0);
        pst.setInt(8, idTournoi);
        pst.executeUpdate();
    }
    
    
    public static Joueur JoueursHighestScore(Connection con, int idTournoi) throws SQLException {
    String sql = "SELECT * FROM joueur WHERE idTournoi = ? order by score DESC limit 1";
    try (PreparedStatement pst = con.prepareStatement(sql)) {
        pst.setInt(1, idTournoi);
        try (ResultSet rs = pst.executeQuery()){ 
            if(rs.next()){
               return fromResultSet(rs); 
            }
            
        }
    }
    return null;
}

}