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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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

    public Ronde(Timestamp timestampDebut, Timestamp timestampFin, int statut, int idTournoi, int id) {
        super(id);
        this.timestampDebut = timestampDebut;
        this.timestampFin = timestampFin;
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
            pst.setNull(4, java.sql.Types.TIMESTAMP);
        } else {
            pst.setTimestamp(4, this.timestampFin);
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
    
    public static List<Ronde> allRondesByTournoi(Connection con, int idTournoi)
        throws SQLException {

        List<Ronde> rondes = new ArrayList<>();

        String sql = """
            SELECT id, timestampDebut, timestampFin, statut, idTournoi
            FROM ronde
            WHERE idTournoi = ?
            ORDER BY id
        """;

        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, idTournoi);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Ronde r = new Ronde(                           
                            rs.getTimestamp("timestampDebut"),
                            rs.getTimestamp("timestampFin"),
                            rs.getInt("statut"),
                            rs.getInt("idTournoi"),
                            rs.getInt("id")
                    );
                    rondes.add(r);
                }
            }
        }
        return rondes;
    }
    
     public static int statutById(Connection con, int idRonde) throws SQLException {
        String sql = "SELECT statut FROM ronde WHERE id = ?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, idRonde);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("statut"); // retourne le statut
                } else {
                    throw new SQLException("Ronde introuvable pour id = " + idRonde);
                }
            }
        }
    }
     
   

    public void close(Connection con) throws SQLException {
        this.timestampFin = new Timestamp(System.currentTimeMillis());
        this.statut = 1;
        updateTimestampFinAndStatut(con);
    }

    
    public void updateTimestampFinAndStatut(Connection con) throws SQLException {

        if (this.getId() == -1) {
            throw new IllegalStateException(
            "Impossible de mettre à jour la ronde : ronde non sauvegardée"
            );
        }   

        
    
        String sql = """
            UPDATE ronde
            SET timestampFin = ?, statut = ?
            WHERE id = ?
            """;

        try (PreparedStatement pst = con.prepareStatement(sql)) {


            if (this.timestampFin == null) {
                pst.setNull(1, java.sql.Types.TIMESTAMP);
            } else {
                pst.setTimestamp(1, this.timestampFin);
            }


            pst.setInt(2, this.statut);


            pst.setInt(3, this.getId());

            pst.executeUpdate();
        }
    }
    
    public static Ronde findDerniereRondeByTournoi(Connection con, int idTournoi) throws SQLException {
    // retourne la dernière ronde créée pour ce tournoi
    String sql = "SELECT * FROM ronde WHERE idTournoi = ? ORDER BY id DESC LIMIT 1";
    try (PreparedStatement pst = con.prepareStatement(sql)) {
        pst.setInt(1, idTournoi);
        try (ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                return new Ronde(
                    rs.getTimestamp("timestampDebut"),
                    rs.getTimestamp("timestampFin"),
                    rs.getInt("statut"),
                    rs.getInt("idTournoi"),
                    rs.getInt("id")
                );
            } else {
                return null; // aucune ronde créée
            }
        }
    }
}

    public static int countRondesByTournoi(Connection con, int idTournoi) throws SQLException {
    String sql = "SELECT COUNT(*) AS nb FROM ronde WHERE idTournoi = ?";
    try (PreparedStatement pst = con.prepareStatement(sql)) {
        pst.setInt(1, idTournoi);
        try (ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("nb");
            } else {
                return 0;
            }
        }
    }
}

    public static Ronde findById(Connection con, int idRonde) throws SQLException {
    String sql = "SELECT * FROM ronde WHERE id = ?";
    try (PreparedStatement pst = con.prepareStatement(sql)) {
        pst.setInt(1, idRonde);
        try (ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                return new Ronde(
                    rs.getTimestamp("timestampDebut"),
                    rs.getTimestamp("timestampFin"),
                    rs.getInt("statut"),
                    rs.getInt("idTournoi"),
                    rs.getInt("id")
                );
            } else {
                return null;
            }
        }
    }
}
    

}