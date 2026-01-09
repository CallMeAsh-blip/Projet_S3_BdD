package fr.insa.ashbis.webui.tournoi;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.ashbis.model.Joueur;
import fr.insa.ashbis.model.Joueur_Equipe;
import fr.insa.ashbis.webui.layout.SecondaryLayout;
import fr.insa.ashbis.webui.session.SessionInfo;
import fr.insa.beuvron.utils.database.ConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Route(value = "equipe", layout = SecondaryLayout.class)
@PageTitle("Équipe")
public class JoueurEquipe extends VerticalLayout
                          implements BeforeEnterObserver {

    private Integer idEquipe;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {

        idEquipe = SessionInfo.getIdEquipeSelectionne();

        if (idEquipe == null) {
            Notification.show("Aucune équipe sélectionnée");
            event.forwardTo("equipes");
            return;
        }

        initView();
    }

    private void initView() {

        removeAll();

        add(new H2("Membres de l'équipe"));

        Grid<Joueur> grid = new Grid<>(Joueur.class, false);

        try (Connection con = ConnectionPool.getConnection()) {

            List<Joueur> joueurs =
                    Joueur_Equipe.joueursByEquipe(con, idEquipe);

            grid.setDataProvider(new ListDataProvider<>(joueurs));

            grid.addColumn(Joueur::getPrenom)
                .setHeader("Prénom")
                .setSortable(true);

            grid.addColumn(Joueur::getNom)
                .setHeader("Nom")
                .setSortable(true);

            grid.addColumn(Joueur::getGenre)
                .setHeader("Genre");

            grid.addColumn(Joueur::getScore)
                .setHeader("Score");

        } catch (SQLException ex) {
            Notification.show("Erreur SQL : " + ex.getMessage());
            return;
        }

        add(grid);

        add(new Button("Retour aux équipes",
                e -> UI.getCurrent().navigate("equipes")));
    }
}
