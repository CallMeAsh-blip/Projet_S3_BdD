package fr.insa.ashbis.webui.tournoi;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.ashbis.model.Equipe;
import fr.insa.ashbis.webui.layout.SecondaryLayout;
import fr.insa.ashbis.webui.session.SessionInfo;
import fr.insa.beuvron.utils.database.ConnectionPool;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Route(value = "equipes", layout = SecondaryLayout.class)
@PageTitle("Équipes du tournoi")
public class VueEquipes extends VerticalLayout
                        implements BeforeEnterObserver {

    private Integer idTournoi;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {

        idTournoi = SessionInfo.getSelectedTournoiId();

        if (idTournoi == null) {
            Notification.show("Aucun tournoi sélectionné");
            event.forwardTo("tournoi");
            return;
        }

        initView();
    }

    private void initView() {

        removeAll();
        add(new H2("Liste des équipes"));

        Grid<Equipe> grid = new Grid<>(Equipe.class, false);

        try (Connection con = ConnectionPool.getConnection()) {

            List<Equipe> equipes =
                    Equipe.equipesByTournoi(con, idTournoi);

            if (equipes.isEmpty()) {
                add(new Span("Aucune équipe créée"));
                return;
            }

            grid.setItems(equipes);

            grid.addColumn(Equipe::getNom)
                .setHeader("Nom de l'équipe")
                .setAutoWidth(true);

            grid.addColumn(new ComponentRenderer<>(equipe -> {
                Button voir = new Button("Voir");
                voir.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                voir.addClickListener(e -> {
                    SessionInfo.setIdEquipeSelectionne(equipe.getId());
                    UI.getCurrent().navigate("equipe");
                });
                return voir;
            }))
            .setHeader("Action")
            .setTextAlign(ColumnTextAlign.CENTER)
            .setAutoWidth(true);

            add(grid);

        } catch (SQLException ex) {
            Notification.show("Erreur SQL : " + ex.getMessage());
            return;
        }

        add(new Button("Retour aux joueurs",
                e -> UI.getCurrent().navigate("tournoi")));
    }
}
