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
public class Equipe extends ClasseMiroir {
    
    private String nom;
    private int terrain;
    private int idTournoi;
    private int idRonde;
    public Equipe(String nom, int terrain, int idRonde, int tournoi) {
        this.nom = nom;
        this.terrain = terrain;
        this.idRonde = idRonde;
        this.idTournoi = tournoi;
    }

    public Equipe(String nom, int terrain, int idRonde, int idTournoi, int id) {
        super(id);
        this.idRonde = idRonde;
        this.nom = nom;
        this.terrain = terrain;
        this.idTournoi = idTournoi;
    }

    
    
    @Override
    protected Statement saveSansId(Connection con) throws SQLException {
        PreparedStatement pst = con.prepareStatement(
                "insert into equipe(nom,idTerrain,idRonde,idTournoi) values (?,?,?,?)",
                PreparedStatement.RETURN_GENERATED_KEYS
        );
        pst.setString(1, this.nom);
        pst.setInt(2, this.terrain);
        pst.setInt(3, this.idRonde);
        pst.setInt(4, this.idTournoi);
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
            Equipe e = new Equipe("test", 1,1,1);
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
    
    public static List<Equipe> equipesByTournoiandRonde(Connection con, int idTournoi,int idRonde) throws SQLException {
        List<Equipe> equipes = new ArrayList<>();
        String sql = "SELECT * FROM equipe WHERE idTournoi = ? and idRonde = ? ORDER BY idTerrain, id";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, idTournoi);
            pst.setInt(2, idRonde);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    equipes.add(fromResultSet(rs));
                }
            }
        }
        return equipes;
    }

    public static Equipe fromResultSet(ResultSet rs) throws SQLException {
        Equipe e = new Equipe(
                    rs.getString("nom"),
                    rs.getInt("idTerrain"),
                    rs.getInt("idRonde"),
                    rs.getInt("idTournoi"),
                    rs.getInt("id")
                );
        return e;
    }

    public static List<Equipe> equipesByTournoi(Connection con, int idTournoi) throws SQLException {
        List<Equipe> equipes = new ArrayList<>();
        String sql = "SELECT * FROM equipe WHERE idTournoi = ?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, idTournoi);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    equipes.add(fromResultSet(rs));
                }
            }
        }
        return equipes;
    }
    
    public static List<Equipe> equipesByMatchs(Connection con, int idMatchs, int idRonde) throws SQLException {
        int idTerrain;
        String sql = "SELECT idTerrain FROM matchs WHERE id = ?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, idMatchs);
            try (ResultSet rs = pst.executeQuery()) {
                rs.next();
                idTerrain = rs.getInt(1);
            }
        }
        List<Equipe> equipes = new ArrayList<>();
        String sql1 = "SELECT * FROM equipe WHERE idTerrain = ? and idRonde = ?";
        try (PreparedStatement pst = con.prepareStatement(sql1)) {
            pst.setInt(1, idTerrain);
            pst.setInt(2, idRonde);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    equipes.add(fromResultSet(rs));
                }
            }
        }
        return equipes;
    }
    
}
