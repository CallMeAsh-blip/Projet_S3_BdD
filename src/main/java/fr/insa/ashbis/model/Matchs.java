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
public class Matchs extends ClasseMiroir {
    
    private int idTerrain;
    private int idRonde;
    private int idTournoi;

    public Matchs(int idTerrain, int idRonde,int tournoi) {
        this.idTerrain = idTerrain;
        this.idRonde = idRonde;
        this.idTournoi = tournoi;
    }

    public Matchs(int idTerrain, int idRonde, int idTournoi, int id) {
        super(id);
        this.idTerrain = idTerrain;
        this.idRonde = idRonde;
        this.idTournoi = idTournoi;
    }
    
    
    
    @Override
    protected Statement saveSansId(Connection con) throws SQLException {
        PreparedStatement pst = con.prepareStatement(
                "insert into matchs(idTerrain,idTournoi,idRonde) values (?,?,?)",
                PreparedStatement.RETURN_GENERATED_KEYS
        );
        pst.setInt(1, this.getIdTerrain());
        pst.setInt(2, this.getIdTournoi());
        pst.setInt(3, this.getIdRonde());
        pst.executeUpdate();
        return pst;
    }
    
    public String toString() {
        return ("{Match " + this.getId() + " " + ":" + this.getId() + " " + this.getIdTerrain() + " " + this.getIdRonde() + "}");
    }
    /**
     * @return the idTerrain
     */
    public int getIdTerrain() {
        return idTerrain;
    }

    /**
     * @param idTerrain the idTerrain to set
     */
    public void setIdTerrain(int idTerrain) {
        this.idTerrain = idTerrain;
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
    
    public static void main(String[] args) {
        testCreer();
    }
    
    public static void testCreer() {
        try {
            Matchs m = new Matchs(1, 1,1);
            System.out.println("joueur :" + m);
            m.saveInDB(ConnectionSimpleSGBD.defaultCon());
            System.out.println("joueur :" + m);
        } catch (SQLException ex) {
            throw new Error(ex);
        }
    }
    
    public static List<Matchs> allMatchsByRonde(Connection con, int idRonde)
            throws SQLException {

        List<Matchs> matchs = new ArrayList<>();

        String sql = """
            SELECT id, idTerrain, idRonde, idTournoi
            FROM matchs
            WHERE idRonde = ?
            ORDER BY id
        """;

        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, idRonde);
            
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Matchs m = new Matchs(
                        rs.getInt("idTerrain"),
                        rs.getInt("idRonde"),
                        rs.getInt("idTournoi"),
                        rs.getInt("id")
                    );

                    matchs.add(m);
                }
            }
        }
        return matchs;
    }

}
