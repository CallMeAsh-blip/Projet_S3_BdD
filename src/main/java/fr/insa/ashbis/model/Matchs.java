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
public class Matchs extends ClasseMiroir {
    
    private int idTerrain;
    private int idRonde;
    private int idTournoi;

    public Matchs(int idTerrain, int idRonde,int tournoi) {
        this.idTerrain = idTerrain;
        this.idRonde = idRonde;
        this.idTournoi = tournoi;
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
    
}
