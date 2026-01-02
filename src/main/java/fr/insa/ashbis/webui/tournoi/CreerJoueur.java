package fr.insa.ashbis.webui.tournoi;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.ashbis.model.Admin;
import fr.insa.ashbis.model.Joueur;
import fr.insa.beuvron.utils.database.ConnectionPool;
import fr.insa.ashbis.webui.layout.SecondaryLayout;
import fr.insa.ashbis.webui.session.SessionInfo;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import org.checkerframework.checker.units.qual.t;

@Route(value = "creationJoueur", layout = SecondaryLayout.class)
@PageTitle("Création Joueur")
public class CreerJoueur extends FormLayout {
    
    private final TextField nom;
    private final TextField prenom;
    private final ComboBox<String> genre;
    private final DatePicker dateNaissance;
    
    private final Button creer = new Button("Créer joueur");
    private Optional<Admin> optAdmin;

    public CreerJoueur() {

        this.prenom = new TextField("prenom");
        this.nom = new TextField("nom");
        this.genre = new ComboBox("genre");
        this.dateNaissance = new DatePicker("date de naissance");


        prenom.setRequired(true);
        nom.setRequired(true);
        genre.setRequired(true);
        dateNaissance.setRequired(true);
        
        genre.setItems("Homme", "Femme", "Autre");
        genre.setPlaceholder("Sélectionner");


        creer.addClickListener(e -> doCreate());

        setResponsiveSteps(
            new FormLayout.ResponsiveStep("0", 1),
            new FormLayout.ResponsiveStep("600px", 2)
        );

        add(
            nom,
            prenom,
            genre,
            dateNaissance,
            creer
        );
    }

    private void doCreate() {
        try {
            if (nom.isEmpty() || prenom.isEmpty()
                    || genre.isEmpty() || dateNaissance.isEmpty()) {
                Notification.show("Tous les champs sont obligatoires");
                return;
            }

            Integer idTournoi = SessionInfo.getSelectedTournoiId();
            if (idTournoi == null) {
                Notification.show("Aucun tournoi sélectionné");
                return;
            }

            Joueur j = new Joueur(
                prenom.getValue(),
                nom.getValue(), 
                genre.getValue(),
                dateNaissance.getValue().toString(),
                0,
                0,
                0,
                SessionInfo.getSelectedTournoiId()
            );

            try (Connection con = ConnectionPool.getConnection()) {
                j.saveInDB(con);
            }

            Notification.show("Joueur créé : " + prenom.getValue());
            UI.getCurrent().navigate("tournoi");

        } catch (IllegalArgumentException ex) {
            Notification.show(ex.getMessage());
        } catch (SQLException ex) {
            Notification.show("Problème : " + ex.getLocalizedMessage());
        }
    }
}
