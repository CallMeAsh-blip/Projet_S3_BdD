package fr.insa.ashbis.webui.utilisateurs;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.ashbis.model.Admin;
import fr.insa.ashbis.model.Tournoi;
import fr.insa.beuvron.utils.database.ConnectionPool;
import fr.insa.ashbis.webui.layout.MainLayout;
import fr.insa.ashbis.webui.session.SessionInfo;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

@Route(value = "creationTournoi", layout = MainLayout.class)
@PageTitle("Création Tournoi")
public class CreerTournoi extends FormLayout {

    private final TextField nom;
    private final IntegerField nbrTerrain;
    private final IntegerField maxJoueurEquipe;
    private final IntegerField minJoueurEquipe;
    private final IntegerField maxEquipeTerrain;
    private final IntegerField nbrRonde;

    private final IntegerField temps;
    private final Checkbox inscriptionLibre;

    private final Button creer = new Button("Créer le tournoi");
    private Optional<Admin> optAdmin;

    public CreerTournoi() {

        this.nom = new TextField("Nom du tournoi");
        this.nbrTerrain = new IntegerField("Nombre de terrains");
        this.maxJoueurEquipe = new IntegerField("Joueurs max par équipe");
        this.minJoueurEquipe = new IntegerField("Joueurs min par équipe");
        this.maxEquipeTerrain = new IntegerField("Équipes par terrain");
        this.nbrRonde = new IntegerField("Nombre de rondes");

        this.temps = new IntegerField("temps des rondes (en sec)");
        this.inscriptionLibre = new Checkbox("Inscription libre");

        this.optAdmin = SessionInfo.curUser();
        if (optAdmin.isEmpty()) {
            Notification.show("Connexion requise");
            UI.getCurrent().navigate("");
            return;
        }

        nom.setRequired(true);
        nbrTerrain.setMin(1);
        minJoueurEquipe.setMin(1);
        maxJoueurEquipe.setMin(1);
        maxEquipeTerrain.setMin(1);
        nbrRonde.setMin(1);
        temps.setMin(1);
        inscriptionLibre.setValue(false);

        creer.addClickListener(e -> doCreate(optAdmin.get()));

        setResponsiveSteps(
            new ResponsiveStep("0", 1),
            new ResponsiveStep("600px", 2)
        );

        add(
            nom,
            nbrTerrain,
            minJoueurEquipe,
            maxJoueurEquipe,
            maxEquipeTerrain,
            nbrRonde,
            temps,
            inscriptionLibre,
            creer
        );
    }

    private void doCreate(Admin admin) {
        try {
            if (nom.getValue() == null || nom.getValue().isBlank()) {
                Notification.show("Nom obligatoire");
                return;
            }

            Tournoi t = new Tournoi(
                nom.getValue(),
                admin.getId(),
                nbrTerrain.getValue(),
                maxJoueurEquipe.getValue(),
                maxEquipeTerrain.getValue(),
                nbrRonde.getValue(),
                minJoueurEquipe.getValue(),
                temps.getValue(),
                inscriptionLibre.getValue() ? 1 : 0,
                0,
                0
            );

            try (Connection con = ConnectionPool.getConnection()) {
                t.saveInDB(con);
            }

            Notification.show("Tournoi créé : " + nom.getValue());
            UI.getCurrent().navigate("mon-compte");

        } catch (IllegalArgumentException ex) {
            Notification.show(ex.getMessage());
        } catch (SQLException ex) {
            Notification.show("Problème : " + ex.getLocalizedMessage());
        }
    }
}
