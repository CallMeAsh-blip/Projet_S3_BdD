package fr.insa.ashbis.webui.tournoi;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.ashbis.model.Ronde;
import fr.insa.ashbis.webui.layout.SecondaryLayout;
import fr.insa.ashbis.webui.session.SessionInfo;
import fr.insa.beuvron.utils.database.ConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Route(value = "rondes", layout = SecondaryLayout.class)
@PageTitle("Rondes du tournoi")
public class VueRonde extends VerticalLayout
        implements BeforeEnterObserver {

    private Integer idTournoi;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {

        idTournoi = SessionInfo.getSelectedTournoiId();

        if (idTournoi == null) {
            Notification.show("Aucun tournoi sélectionné");
            event.forwardTo("");
            return;
        }

        initView();
    }

    private void initView() {

        removeAll();
        add(new H2("Rondes du tournoi"));

        Grid<Ronde> grid = new Grid<>();

        try (Connection con = ConnectionPool.getConnection()) {

            List<Ronde> rondes = Ronde.allRondesByTournoi(con, idTournoi);

            ListDataProvider<Ronde> dataProvider =
                    new ListDataProvider<>(rondes);

            grid.setDataProvider(dataProvider);

            // Numéro de ronde = position dans la liste
            grid.addColumn(new ValueProvider<Ronde, Integer>() {
                int index = 1;

                @Override
                public Integer apply(Ronde ronde) {
                    return index++;
                }
            }).setHeader("Ronde");

            grid.addColumn(ronde -> {
            if (ronde.getStatut() == 0) {
                return "Ouvert";
            } else {
                return "Fermé";
            }
        }).setHeader("Statut");

        grid.addColumn(new ComponentRenderer<>(ronde -> {
            Button voir = new Button("Voir");
            voir.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            voir.addClickListener(e -> {
                SessionInfo.setSelectedRondeId(ronde.getId());
                UI.getCurrent().navigate("matchs");
            });
            return voir;
        }))
        .setHeader("Action")
        .setAutoWidth(true)
        .setTextAlign(ColumnTextAlign.CENTER);

        } catch (SQLException ex) {
            Notification.show("Erreur : " + ex.getMessage());
            return;
        }

        add(grid);

        Button retour = new Button("Retour au joueurs",
                e -> UI.getCurrent().navigate("tournoi"));

        add(retour);
    }
}
