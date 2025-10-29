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
public class Ronde extends ClasseMiroir{
    private int dateTimeDebut;
    private int dateTimeFin;
    private int statut;
    private int idTournoi;

    public Ronde(int dateTimeDebut, int dateTimeFin, int statut, int idTournoi) {
        this.dateTimeDebut = dateTimeDebut;
        this.dateTimeFin = dateTimeFin;
        this.statut = statut;
        this.idTournoi = idTournoi;
    }
    
    @Override
    protected Statement saveSansId(Connection con) throws SQLException {
        PreparedStatement pst = con.prepareStatement(
                "insert into matchs(dateTimeDebut,statut,idTournoi,dateTimeFin) values (?,?,?,?)",
                PreparedStatement.RETURN_GENERATED_KEYS
        );
        pst.setInt(1, this.dateTimeDebut);
        pst.setInt(2, this.statut);
        pst.setInt(3, this.idTournoi);
        pst.setInt(4, this.dateTimeFin);
        
        pst.executeUpdate();
        return pst;
    }

    /**
     * @return the dateTimeDebut
     */
    public int getDateTimeDebut() {
        return dateTimeDebut;
    }

    /**
     * @param dateTimeDebut the dateTimeDebut to set
     */
    public void setDateTimeDebut(int dateTimeDebut) {
        this.dateTimeDebut = dateTimeDebut;
    }

    /**
     * @return the dateTimeFin
     */
    public int getDateTimeFin() {
        return dateTimeFin;
    }

    /**
     * @param dateTimeFin the dateTimeFin to set
     */
    public void setDateTimeFin(int dateTimeFin) {
        this.dateTimeFin = dateTimeFin;
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
    


}