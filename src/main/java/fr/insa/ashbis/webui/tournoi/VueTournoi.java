
package fr.insa.ashbis.webui.tournoi;

import fr.insa.ashbis.webui.layout.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.router.RouterLink;
import fr.insa.ashbis.model.Joueur;
import fr.insa.ashbis.model.Tournoi;
import fr.insa.beuvron.utils.database.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ashln
 */
@Route(value = "tournoi", layout = MainLayout.class)
@PageTitle("Tournoi")
public class VueTournoi extends VerticalLayout {

    public VueTournoi() {

        // Récupération de l'id du tournoi depuis l'URL
        int idTournoi = 0;
        try {
            idTournoi = Integer.parseInt(UI.getCurrent().getInternals().getActiveViewLocation().getSegments().get(1));
        } catch (Exception e) {
            Notification.show("Tournoi introuvable");
            UI.getCurrent().navigate("");
            return;
        }

        try (Connection con = ConnectionPool.getConnection()) {

            // Récupérer le tournoi
            Tournoi tournoi = null;
            for (Tournoi t : Tournoi.AllTournois(con)) {
                if (t.getId() == idTournoi) {
                    tournoi = t;
                    break;
                }
            }

            if (tournoi == null) {
                Notification.show("Tournoi introuvable");
                UI.getCurrent().navigate("");
                return;
            }

            add(new H2("Tournoi : " + tournoi.getNom()));

            Grid<Joueur> grid = new Grid<>();

            grid.addColumn(Joueur::getPrenom).setHeader("Prénom");
            grid.addColumn(Joueur::getNom).setHeader("Nom");
            grid.addColumn(Joueur::getGenre).setHeader("Genre");
            grid.addColumn(Joueur::getDateDeNaissance).setHeader("Date de naissance");
            grid.addColumn(Joueur::getScore).setHeader("Score");

            grid.addColumn(new ComponentRenderer<>(joueur -> {
                Button btn = new Button("Détails");
                btn.addClickListener(e ->
                        Notification.show("Joueur : " + joueur.getPrenom() + " " + joueur.getNom())
                );
                return btn;
            })).setHeader("Action");

            ListDataProvider<Joueur> dataProvider = new ListDataProvider<>(Joueur.allJoueursByTournoi(con, idTournoi));
            grid.setDataProvider(dataProvider);


            add(grid);

        } catch (SQLException ex) {
            Notification.show("Problème : " + ex.getLocalizedMessage());
        }

    }

    
}
