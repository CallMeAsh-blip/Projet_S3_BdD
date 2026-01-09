/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.ashbis.webui.utilisateurs;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.ashbis.model.Admin;
import fr.insa.ashbis.model.Tournoi;
import fr.insa.ashbis.webui.layout.MainLayout;
import fr.insa.ashbis.webui.session.SessionInfo;
import fr.insa.beuvron.utils.database.ConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

@Route(value = "mon-compte", layout = MainLayout.class)
@PageTitle("Mon compte")
public class VueCompteAdmin extends VerticalLayout {

    public VueCompteAdmin() {

        Optional<Admin> optAdmin = SessionInfo.curUser();


        if (optAdmin.isEmpty()) {
            Notification.show("Veuillez vous connecter");
            UI.getCurrent().navigate("");
            return;
        }

        Admin admin = optAdmin.get();

        add(new H2("Votre compte"));
        add(new Paragraph("Nom d'utilisateur : " + admin.getUsername()));

        Grid<Tournoi> grid = new Grid<>();
        grid.addColumn(Tournoi::getNom).setHeader("Tournoi");
        grid.addColumn(Tournoi::getNbrTerrain).setHeader("Terrains");
        grid.addColumn(Tournoi::getMinJoueurEquipe).setHeader("Joueurs / équipe");
        

        grid.addComponentColumn(t -> {

        Button gerer = new Button("Gérer");
        gerer.addClickListener(e ->{
            SessionInfo.setSelectedTournoiId(t.getId());
            UI.getCurrent().navigate("tournoi");
        });
        
        Button copier = new Button("Copier");
        copier.addClickListener(e ->{
            SessionInfo.setSelectedTournoiId(t.getId());
            UI.getCurrent().navigate("copierTournoi");
        });

        Button supprimer = new Button("Supprimer");
        supprimer.getStyle().set("color", "red");

        supprimer.addClickListener(e -> {
            ConfirmDialog dialog = new ConfirmDialog();
            dialog.setHeader("Confirmation");
            dialog.setText("Supprimer le tournoi \"" + t.getNom() + "\" ?");

            dialog.setCancelable(true);
            dialog.setConfirmText("Supprimer");
            dialog.setConfirmButtonTheme("error primary");

            dialog.addConfirmListener(ev -> {
                try (Connection con = ConnectionPool.getConnection()) {
                    t.deleteInDB(con);
                    grid.setItems(
                        Tournoi.allTournoisByAdmin(con, admin.getId())
                    );
                    Notification.show("Tournoi supprimé");
                } catch (SQLException ex) {
                    Notification.show("Erreur suppression : " + ex.getMessage());
                }
            });

            dialog.open();
        });

        return new HorizontalLayout(gerer, supprimer,copier);

    }).setHeader("Actions");


        try (Connection con = ConnectionPool.getConnection()) {
            grid.setItems(Tournoi.allTournoisByAdmin(con, admin.getId()));
        } catch (SQLException ex) {
            Notification.show("Erreur BD : " + ex.getMessage());
        }

        add(new H2("Mes tournois"));
        add(grid);

        Button logout = new Button("Déconnexion", e -> {
            SessionInfo.logout();
            UI.getCurrent().navigate("");
        });

        add(logout);
    }
}
