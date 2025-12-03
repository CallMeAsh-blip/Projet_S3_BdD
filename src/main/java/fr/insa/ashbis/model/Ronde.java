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
import java.time.LocalDateTime;

/**
 *
 * @author ashln
 */
public class Ronde extends ClasseMiroir{
    private java.sql.Timestamp timestampDebut;
    private java.sql.Timestamp timestampFin;
    private int statut;
    private int idTournoi;

    public Ronde( int statut, int idTournoi) {
//        java.sql.Timestamp dateTimeDebut, java.sql.Timestamp dateTimeFin,
        this.timestampDebut = null;
//        ResultSet rs;
//        java.sql.Timestamp test;
//        test.toLocalDateTime().to
//        java.sql.Date sqd = rs.getDate(1);
//        LocalDateTime ldd = sqd.toLocalDate();
        this.timestampFin = null;
        this.statut = statut;
        this.idTournoi = idTournoi;
    }
    
    @Override
    protected Statement saveSansId(Connection con) throws SQLException {
        PreparedStatement pst = con.prepareStatement(
                "insert into ronde(timestampDebut,statut,idTournoi,timestampFin) values (?,?,?,?)",
                PreparedStatement.RETURN_GENERATED_KEYS
        );
        if (this.timestampDebut == null) {
            pst.setNull(1, java.sql.Types.TIMESTAMP);
        } else {
            pst.setTimestamp(1, this.timestampDebut);
        }
        pst.setInt(2, this.statut);
        pst.setInt(3, this.idTournoi);
        if (this.timestampFin == null) {
            pst.setNull(1, java.sql.Types.TIMESTAMP);
        } else {
            pst.setTimestamp(1, this.timestampFin);
        }
        
        pst.executeUpdate();
        return pst;
    }

    /**
     * @return the dateTimeDebut
     */
    public java.sql.Timestamp getTimestampDebut() {
        return timestampDebut;
    }

    /**
     * @param dateTimeDebut the dateTimeDebut to set
     */
    public void setTimestampDebut(java.sql.Timestamp timestampDebut) {
        this.timestampDebut = timestampDebut;
    }

    /**
     * @return the dateTimeFin
     */
    public java.sql.Timestamp getTimestampFin() {
        return timestampFin;
    }

    /**
     * @param dateTimeFin the dateTimeFin to set
     */
    public void setTimestampFin(java.sql.Timestamp timestampFin) {
        this.timestampFin = timestampFin;
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