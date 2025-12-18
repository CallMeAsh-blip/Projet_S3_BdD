/*
Copyright 2000- Francois de Bertrand de Beuvron

This file is part of CoursBeuvron.

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
package fr.insa.ashbis.webui.utilisateurs;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.ashbis.model.Admin;
import fr.insa.ashbis.model.Tournoi;
import fr.insa.beuvron.utils.database.ConnectionPool;
import fr.insa.ashbis.webui.MainLayout;
import fr.insa.ashbis.webui.session.SessionInfo;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

/**
 *
 * @author francois
 */
@Route(value = "creationTournoi",layout = MainLayout.class)
@PageTitle("Creéation Tournoi")
public class CreerTournoi extends FormLayout {

    private TextField nom;
    private IntegerField nbrTerrain;
    private IntegerField maxJoueurEquipe;
    private IntegerField minJoueurEquipe;
    private IntegerField maxEquipeTerrain;
    private IntegerField nbrRonde;

    private Button creer = new Button("Créer le tournoi");

    public CreerTournoi() {
        
        this.nom = new TextField("Nom du tournoi");
        this.nbrTerrain = new IntegerField("Nombre de terrains");
        this.maxJoueurEquipe = new IntegerField("Joueurs max par équipe");
        this.minJoueurEquipe = new IntegerField("Joueurs min par équipe");
        this.maxEquipeTerrain = new IntegerField("Équipes par terrain");
        this.nbrRonde = new IntegerField("Nombre de rondes");
        

        Optional<Admin> optAdmin = SessionInfo.curUser();

        if (optAdmin.isEmpty()) {
            Notification.show("Connexion requise");
            UI.getCurrent().navigate("");
            return;
        }

        nom.setRequired(true);
        nbrTerrain.setMin(1);
        maxJoueurEquipe.setMin(1);
        maxEquipeTerrain.setMin(1);
        nbrRonde.setMin(1);

        creer.addClickListener(e -> doCreate(optAdmin.get()));

        setResponsiveSteps(
            new ResponsiveStep("0", 1),
            new ResponsiveStep("600px", 2)
        );

        add(nom, nbrTerrain, maxJoueurEquipe, maxEquipeTerrain, nbrRonde, creer);
    }

    private void doCreate(Admin admin) {
        try {
            String n = nom.getValue();

            if (n == null || n.isBlank()) {
                Notification.show("Nom obligatoire");
                return;
            }

            Tournoi t = new Tournoi(
                n,
                admin.getId(),
                nbrTerrain.getValue(),
                maxJoueurEquipe.getValue(),
                maxEquipeTerrain.getValue(),
                nbrRonde.getValue(),
                minJoueurEquipe.getValue()
            );

            try (Connection con = ConnectionPool.getConnection()) {
                t.saveInDB(con);
            }

            Notification.show("Tournoi créé : " + n);
            UI.getCurrent().navigate("mon-compte");

        } catch (SQLException | NullPointerException ex) {
            Notification.show("Erreur : valeurs invalides");
        }
    }
}
