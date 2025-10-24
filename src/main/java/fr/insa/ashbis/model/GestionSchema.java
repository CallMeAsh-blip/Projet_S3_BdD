/*
Copyright 2000- Francois de Bertrand de Beuvron

This file is ecole of CoursBeuvron.

CoursBeuvron is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

CoursBeuvron is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with CoursBeuvron.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.insa.ashbis.model;

import fr.insa.beuvron.utils.database.ConnectionSimpleSGBD;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author francois
 */
public class GestionSchema {

    /**
     *
     * @param con
     * @throws SQLException
     */
    public static void creeSchema(Connection con)
            throws SQLException {
        try {
            con.setAutoCommit(false);
            try (Statement st = con.createStatement()) {
                // creation des tables
                st.executeUpdate("create table joueur ( "
                        + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "id") + ","
                        + " prenom varchar(64) not null,"
                        + " nom varchar(64) not null,"
                        + " genre varchar(32),"
                        + " DateDeNaissance DATE not null,"
                        + " priority integer,"
                        + " score integer,"
                        + " IdEquipe integer"
                        + ")"
                );
                st.executeUpdate("create table equipe ( "
                        + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "id") + ","
                        + " nom varchar(64) not null,"
                        + " terrain integer"
                        + ")"
                );
                st.executeUpdate("create table matchs ( "
                        + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "id") + ","
                        + " idTerrain integer not null,"
                        + " idRonde integer"
                        + ")"
                );
                st.executeUpdate("create table tournoi ( "
                        + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "id") + ","
                        + " nom varchar(64) not null,"
                        + " NbrTerrain integer not null,"
                        + " MaxJoueurEquipe integer not null,"
                        + " MaxEquipeTerrain integer not null,"
                        + " NbrRonde integer not null"        
                        + ")"
                );
                
                st.executeUpdate("create table ronde ( "
                        + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "id") + ","
                        + " timestampDebut TIMESTAMP,"
                        + " statut integer not null,"
                        + " timestampFin TIMESTAMP"       
                        + ")"
                );
                
                con.commit();
                
            }
        } catch (SQLException ex) {
            con.rollback();
            throw ex;
        } finally {
            con.setAutoCommit(true);
        }
    }
    

    /**
     *
     * @param con
     * @throws SQLException
     */
    public static void deleteSchema(Connection con) throws SQLException {
        try (Statement st = con.createStatement()) {
            
            try {
                st.executeUpdate("drop table joueur");
                st.executeUpdate("drop table equipe");
                st.executeUpdate("drop table tournoi");
                st.executeUpdate("drop table ronde");
                st.executeUpdate("drop table matchs");
            } catch (SQLException ex) {
            }
        }
    }

    /**
     *
     * @param con
     * @throws SQLException
     */
    public static void razBdd(Connection con) throws SQLException {
        deleteSchema(con);
        creeSchema(con);
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        try (Connection con = ConnectionSimpleSGBD.defaultCon()) {
            razBdd(con);
            System.out.println("OK tout");
        } catch (SQLException ex) {
            throw new Error(ex);
        }
    }

}
