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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ashln
 */
public class Joueur_Equipe extends ClasseMiroir {
    private int idJoueur;
    private int idEquipe;
    private int idRonde;
    private int score;

    public Joueur_Equipe(int idJoueur, int idEquipe, int idRonde,int score, int id) {
        super(id);
        this.idJoueur = idJoueur;
        this.idEquipe = idEquipe;
        this.idRonde = idRonde;
        this.score = score;
    }

    public Joueur_Equipe(int idJoueur, int idEquipe, int idRonde,int score) {
        this.idJoueur = idJoueur;
        this.idEquipe = idEquipe;
        this.idRonde = idRonde;
        this.score = score;
    }
    
    
    
    @Override
    protected Statement saveSansId(Connection con) throws SQLException {
        PreparedStatement pst = con.prepareStatement(
                "insert into joueur_equipe(idJoueur, idEquipe, idRonde, score) values (?,?,?,?)",
                PreparedStatement.RETURN_GENERATED_KEYS
        );
        pst.setInt(1, this.getIdJoueur());
        pst.setInt(2, this.getIdEquipe());
        pst.setInt(3, this.getIdRonde());
        pst.setInt(4, this.getScore());
        pst.executeUpdate();
        return pst;
    }

    /**
     * @return the idJoueur
     */
    public int getIdJoueur() {
        return idJoueur;
    }

    /**
     * @param idJoueur the idJoueur to set
     */
    public void setIdJoueur(int idJoueur) {
        this.idJoueur = idJoueur;
    }

    /**
     * @return the idEquipe
     */
    public int getIdEquipe() {
        return idEquipe;
    }

    /**
     * @param idEquipe the idEquipe to set
     */
    public void setIdEquipe(int idEquipe) {
        this.idEquipe = idEquipe;
    }

    /**
     * @return the idRonde
     */
    public int getIdRonde() {
        return idRonde;
    }

    /**
     * @param idRonde the idRonde to set
     */
    public void setIdRonde(int idRonde) {
        this.idRonde = idRonde;
    }
    
    
    public static List<Joueur> joueursByEquipe(Connection con, int idEquipe) throws SQLException {

        List<Joueur> res = new ArrayList<>();

        try (PreparedStatement pst = con.prepareStatement(
                "SELECT j.* "
              + "FROM joueur j "
              + "JOIN joueur_equipe je ON j.id = je.idJoueur "
              + "WHERE je.idEquipe = ?"
        )) {
            pst.setInt(1, idEquipe);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    res.add(Joueur.fromResultSet(rs));
                }
            }
        }
        return res;
    }
    
    public static List<Equipe> equipesByJoueurAndRonde(Connection con, int idJoueur, int idRonde) throws SQLException {

        List<Equipe> res = new ArrayList<>();

        try (PreparedStatement pst = con.prepareStatement(
                "SELECT e.* "
              + "FROM equipe e "
              + "JOIN joueur_equipe je ON e.id = je.idEquipe "
              + "WHERE je.idJoueur = ? AND je.idRonde = ?"
        )) {
            pst.setInt(1, idJoueur);
            pst.setInt(2, idRonde);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    res.add(Equipe.fromResultSet(rs));
                }
            }
        }
        return res;
    }
    
    public static void deleteByRonde(Connection con, int idRonde)
            throws SQLException {

        try (PreparedStatement pst = con.prepareStatement(
                "DELETE FROM joueur_equipe WHERE idRonde = ?"
        )) {
            pst.setInt(1, idRonde);
            pst.executeUpdate();
        }
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
    
    public static void updateScoreRonde(Connection con, int idEquipe, int idRonde, int score) throws SQLException {
        

    String sql = """
        UPDATE joueur_equipe
        SET score = ?
        WHERE idEquipe = ? AND idRonde = ?
    """;

    try (PreparedStatement pst = con.prepareStatement(sql)) {
        pst.setInt(1, score);
        pst.setInt(2, idEquipe);
        pst.setInt(3, idRonde);
        pst.executeUpdate();

    }
}
    public static Map<Integer, Integer> scoresParRonde(
        Connection con, int idRonde
) throws SQLException {

    Map<Integer, Integer> res = new HashMap<>();

    String sql = """
        SELECT idEquipe, score
        FROM joueur_equipe
        WHERE idRonde = ?
    """;

    try (PreparedStatement pst = con.prepareStatement(sql)) {
        pst.setInt(1, idRonde);

        try (ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                res.put(
                    rs.getInt("idEquipe"),
                    rs.getInt("score")
                );
            }
        }
    }
    return res;
}



}
