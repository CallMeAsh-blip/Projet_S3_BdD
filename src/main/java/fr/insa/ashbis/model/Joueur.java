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

    private String surnom;
    private String categorie;
    private int taillecm;
    private int priority;

    public Joueur(String surnom, String categorie, int taillecm) {
        this.surnom = surnom;
        this.categorie = categorie;
        this.taillecm = taillecm;
        this.priority=0;
    }

    @Override
    protected Statement saveSansId(Connection con) throws SQLException {
        PreparedStatement pst = con.prepareStatement(
                "insert into joueur(surnom,priority,categorie,taillecm) values (?,?,?,?)",
                PreparedStatement.RETURN_GENERATED_KEYS
        );
        pst.setString(1, this.surnom);
        pst.setInt(2, this.priority);
        pst.setString(3, this.categorie);
        pst.setInt(4, this.taillecm);
        pst.executeUpdate();
        return pst;
    }

    public String toString() {
        return ("{joueur" + this.surnom + ":" + this.getId() + "}");
    }

    public static void testCreer() {
        try {
            Joueur j = new Joueur("test", "j", 183);
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

    /**
     * @return the surnom
     */
    public String getSurnom() {
        return surnom;
    }

    /**
     * @param surnom the surnom to set
     */
    public void setSurnom(String surnom) {
        this.surnom = surnom;
    }

    /**
     * @return the categorie
     */
    public String getCategorie() {
        return categorie;
    }

    /**
     * @param categorie the categorie to set
     */
    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    /**
     * @return the taillecm
     */
    public int getTaillecm() {
        return taillecm;
    }

    /**
     * @param taillecm the taillecm to set
     */
    public void setTaillecm(int taillecm) {
        this.taillecm = taillecm;
    }


}
