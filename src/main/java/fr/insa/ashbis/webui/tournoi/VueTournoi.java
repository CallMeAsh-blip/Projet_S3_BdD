

package fr.insa.ashbis.webui.tournoi;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.ashbis.model.Joueur;
import fr.insa.ashbis.model.Ronde;
import fr.insa.ashbis.model.Tournoi;
import fr.insa.ashbis.webui.layout.SecondaryLayout;
import fr.insa.ashbis.webui.session.SessionInfo;
import fr.insa.beuvron.utils.database.ConnectionPool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(value = "tournoi", layout = SecondaryLayout.class)
@PageTitle("Joueurs du tournoi")
public class VueTournoi extends VerticalLayout
        implements com.vaadin.flow.router.BeforeEnterObserver {

    private Integer idTournoi;
    HorizontalLayout bouttons = new HorizontalLayout();
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        
        idTournoi = SessionInfo.getSelectedTournoiId();

        if (idTournoi == null) {
            Notification.show("Aucun tournoi sélectionné");
            event.forwardTo("");
            UI.getCurrent().refreshCurrentRoute(true);
            return;
        }

        initView();
    }

    private void initView() {

        add(new H2("Joueurs inscrits au tournoi"));
        try (Connection con = ConnectionPool.getConnection()){
            
            Tournoi t = Tournoi.findTournoiById(con, idTournoi);
        int nbRondesExistantes = Ronde.countRondesByTournoi(con, idTournoi);
        int nbRondesMax = t.getNbrRonde();

        if (nbRondesExistantes >= nbRondesMax && Ronde.statutLastRonde(con, idTournoi)==1) {

            H2 fin = new H2("Le tournoi est terminé");
            fin.getStyle()
               .set("color", "black")
               .set("font-weight", "bold");

            add(fin);
            
            Joueur meilleur;
        try {
            meilleur = Joueur.JoueursHighestScore(con, idTournoi);
        } catch (SQLException ex) {
            Notification.show("Erreur récupération du vainqueur");
            return;
        }

        if (meilleur != null) {
            add(new com.vaadin.flow.component.html.Paragraph(
            "Vainqueur du tournoi :"
            ));

            add(new com.vaadin.flow.component.html.Paragraph(
            meilleur.getPrenom() + " " + meilleur.getNom()
            + " — Score : " + meilleur.getScore()
            ));
        } else {
            add(new com.vaadin.flow.component.html.Paragraph(
            "Aucun joueur trouvé"
            ));
        }
        }
        }catch (SQLException ex) {
            Notification.show("Problème : " + ex.getMessage());
        }
        TextField recherche = new TextField("Rechercher un joueur");
        recherche.setPlaceholder("Prénom ou nom...");
        recherche.setClearButtonVisible(true);

        Grid<Joueur> grid = new Grid<>();

        grid.addColumn(Joueur::getPrenom).setHeader("Prénom").setSortable(true);
        grid.addColumn(Joueur::getNom).setHeader("Nom").setSortable(true);
        grid.addColumn(Joueur::getGenre).setHeader("Genre");
        
        Map<Integer, String> equipeParId = new HashMap<>();

        try (Connection con = ConnectionPool.getConnection()) {

            PreparedStatement pst = con.prepareStatement(
                "SELECT id, nom FROM equipe WHERE idTournoi = ?"
            );
            pst.setInt(1, idTournoi);

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                equipeParId.put(rs.getInt("id"), rs.getString("nom"));
            }
        }catch (SQLException ex) {
            Notification.show("Problème : " + ex.getMessage());
        }


        try (Connection con = ConnectionPool.getConnection()) {

            grid.addColumn(j -> {
                    if(j.getIdEquipe() ==0){
                        return("Aucune Equipe");
                    }
                    return equipeParId.getOrDefault(j.getIdEquipe(), "Aucune Equipe");
            }).setHeader("Équipe");

            grid.addColumn(Joueur::getScore).setHeader("Score");

            List<Joueur> joueurs =
                    Joueur.allJoueursByTournoi(con, idTournoi);

            ListDataProvider<Joueur> dataProvider =
                    new ListDataProvider<>(joueurs);

            grid.setDataProvider(dataProvider);

            recherche.addValueChangeListener(e -> {
                String filtre = e.getValue().toLowerCase();
                dataProvider.setFilter(j ->
                        j.getNom().toLowerCase().contains(filtre)
                     || j.getPrenom().toLowerCase().contains(filtre));
            });

            add(recherche, grid);
            

            Tournoi tournoi = Tournoi.findTournoiById(con, idTournoi);
            
            Button importer = new Button("Importer des joueurs");
            importer.addClickListener(e -> ouvrirImportJoueurs());

            boolean peutAjouter =
                    (tournoi.getInscriptionlibre() == 1
                 || SessionInfo.isAdminTournoi()) && tournoi.getStatut()==0;

            if (peutAjouter) {
                Button creerJoueur = new Button("Créer un joueur");
                creerJoueur.addClickListener(e ->
                        UI.getCurrent().navigate("creationJoueur")
                );
                
                bouttons.add(creerJoueur);
                if(SessionInfo.isAdminTournoi()){
                    bouttons.add(importer);
                }
            }

        } catch (SQLException ex) {
            Notification.show("Problème : " + ex.getMessage());
        }

        Button retour = new Button("Retour aux tournois",
                e -> UI.getCurrent().navigate(""));

        

        
        bouttons.add(retour);
        add(bouttons);
    }
    
    private void ouvrirImportJoueurs() {

    Dialog dialog = new Dialog();
    dialog.setWidth("600px");

    VerticalLayout layout = new VerticalLayout();
    layout.add(new H3("Importer des joueurs"));

    Grid<Joueur> grid = new Grid<>(Joueur.class, false);

    grid.addColumn(Joueur::getPrenom).setHeader("Prénom");
    grid.addColumn(Joueur::getNom).setHeader("Nom");

    // Sélection par checkbox
    grid.setSelectionMode(Grid.SelectionMode.MULTI);

    try (Connection con = ConnectionPool.getConnection()) {
        List<Joueur> joueurs = Joueur.allJoueursNotInTournoi(con, idTournoi);
        grid.setItems(joueurs);
    } catch (SQLException ex) {
        Notification.show("Erreur chargement joueurs");
        return;
    }

    Button importer = new Button("Importer la sélection");
    importer.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    importer.addClickListener(e -> {
        try (Connection con = ConnectionPool.getConnection()) {

            for (Joueur j : grid.getSelectedItems()) {
                Joueur.ImportJoueurInTournois(con, idTournoi, j);
            }

            Notification.show("Joueurs importés");
            dialog.close();
            initView(); // recharge la page

        } catch (SQLException ex) {
            Notification.show("Erreur import : " + ex.getMessage());
        }
    });

    Button annuler = new Button("Annuler", e -> dialog.close());

    layout.add(grid, importer, annuler);
    dialog.add(layout);
    dialog.open();
}

}
