/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.ashbis.webui.tournoi;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.ashbis.model.Admin;
import fr.insa.ashbis.model.Equipe;
import fr.insa.ashbis.model.Joueur;
import fr.insa.ashbis.model.Matchs;
import fr.insa.ashbis.model.Ronde;
import fr.insa.ashbis.model.Tournoi;
import fr.insa.ashbis.webui.layout.SecondaryLayout;
import fr.insa.ashbis.webui.session.SessionInfo;
import fr.insa.beuvron.utils.database.ConnectionPool;
import com.vaadin.flow.component.dialog.Dialog;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author ashln
 */


@Route(value = "GestionTournoi", layout = SecondaryLayout.class)
@PageTitle("Gestion du Tournoi")
public class GestionTournoi extends VerticalLayout
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
        
        Optional<Admin> optAdmin = SessionInfo.curUser();


        if (optAdmin.isEmpty() ) {
            Notification.show("Veuillez vous connecter");
            UI.getCurrent().navigate("");
            return;

            
        }

        initView();
    }

    private void initView() {

    removeAll();
    add(new H2("Lancer une nouvelle ronde"));
    
    Button lancer = new Button("Lancer la ronde");
lancer.addClickListener(e -> {
    try (Connection con = ConnectionPool.getConnection()) {

        fermerDerniereRondeSiTempsDepasse();


        int nbRondesExistantes = Ronde.countRondesByTournoi(con, idTournoi);
        Tournoi t = Tournoi.findTournoiById(con, idTournoi);

        if (nbRondesExistantes >= t.getNbrRonde()) {
            Notification.show("Impossible de créer une nouvelle ronde : le nombre maximum de rondes (" + t.getNbrRonde() + ") est atteint.");
            return;
        }

        Ronde derniereRonde = Ronde.findDerniereRondeByTournoi(con, idTournoi);
        if (derniereRonde == null || derniereRonde.getStatut() == 1) {
            lancerRonde();
        } else {
            Notification.show("Impossible de créer une nouvelle ronde : la ronde précédente n'est pas terminée !");
        }

    } catch (SQLException ex) {
        Notification.show("Erreur : " + ex.getMessage());
    }
});


    Button retour = new Button("Retour", e -> UI.getCurrent().navigate("tournoi"));
    add(lancer, retour);

    Button changerEtat = new Button("Fermer le tournoi");
    changerEtat.addClickListener(e -> changerEtatTournoi());

    // Ajouter le bouton "Fermer le tournoi" seulement si le tournoi est ouvert
    try (Connection con = ConnectionPool.getConnection()) {
        Tournoi t = Tournoi.findTournoiById(con, idTournoi);
        if (t.getStatut() == 0) {
            add(changerEtat);
        }
    } catch (SQLException ex) {
        Notification.show("Erreur : " + ex.getMessage());
    }
}


    private void lancerRonde() {

        try (Connection con = ConnectionPool.getConnection()) {
            
            Tournoi tournoi = Tournoi.findTournoiById(con, idTournoi);
                

            if (tournoi == null) {
                Notification.show("Tournoi introuvable");
                return;
            }

            if (tournoi.getStatut() == 0) {
                Notification.show("Le tournoi n'est pas fermé il est impossible de lancer une ronde");
                return;
            }else{

                Ronde r = new Ronde(
                    0, // statut : ouvert
                    idTournoi
                );
                r.setTimestampDebut(new Timestamp(System.currentTimeMillis()));

                r.saveInDB(con);
                
                SessionInfo.setlastCreatedRonde(r.getId());


                Notification.show("Ronde lancée avec succès");
            
                lancerMatchs();
            }
        } catch (SQLException ex) {
            Notification.show("Erreur : " + ex.getMessage());
        }
    }
    
    private void changerEtatTournoi() {

        try (Connection con = ConnectionPool.getConnection()) {

        Tournoi tournoi = Tournoi.findTournoiById(con, idTournoi);
                

        if (tournoi == null) {
            Notification.show("Tournoi introuvable");
            return;
        }

        if (tournoi.getStatut() == 1) {
            Notification.show("Le tournoi est déjà fermé");
            return;
        }

        int nbJoueurs = Joueur.countJoueurByTournoi(con, idTournoi);

            int minJoueurs = tournoi.getMaxEquipeTerrain()*tournoi.getMinJoueurEquipe();
                
            if (nbJoueurs < minJoueurs) {
                Notification.show(
                        "Impossible de fermer le tournoi : "
                      + nbJoueurs + " joueurs inscrits, minimum requis = "
                      + minJoueurs
                );
                return;
            }

            tournoi.setStatut(1);
            tournoi.updateStatut(con);

            Notification.show("Tournoi fermé avec succès");

        } catch (SQLException ex) {
            Notification.show("Erreur : " + ex.getMessage());
        }
    }
    
    private void lancerMatchs() {

    try (Connection con = ConnectionPool.getConnection()) {

        int idRonde = SessionInfo.getLastCreatedRonde();

        Tournoi tournoi = Tournoi.findTournoiById(con, idTournoi);

        if (tournoi == null) {
            Notification.show("Tournoi introuvable");
            return;
        }

        if (tournoi.getStatut() == 0) {
            Notification.show("Le tournoi n'est pas fermé");
            return;
        }

        List<Joueur> joueurs = Joueur.allJoueursByTournoi(con, idTournoi);

        int minJoueurEquipe = tournoi.getMinJoueurEquipe();
        int maxEquipeTerrain = tournoi.getMaxEquipeTerrain();
        int joueursParTerrain = minJoueurEquipe * maxEquipeTerrain;

        int nbTerrainsUtilisables = Math.min(
                joueurs.size() / joueursParTerrain,
                tournoi.getNbrTerrain()
        );

        if (nbTerrainsUtilisables == 0) {
            Notification.show("Pas assez de joueurs pour lancer une ronde");
            return;
        }

        List<Joueur> prioritaires = new ArrayList<>();
        List<Joueur> nonPrioritaires = new ArrayList<>();

        for (Joueur j : joueurs) {
            if (j.getPriority() == 1) {
                prioritaires.add(j);
            } else {
                nonPrioritaires.add(j);
            }
        }

        java.util.Collections.shuffle(prioritaires);
        java.util.Collections.shuffle(nonPrioritaires);

        List<Joueur> ordre = new ArrayList<>();
        ordre.addAll(prioritaires);
        ordre.addAll(nonPrioritaires);

        int index = 0;

        for (int terrain = 1; terrain <= nbTerrainsUtilisables; terrain++) {

            for (int e = 1; e <= maxEquipeTerrain; e++) {

                Equipe equipe = new Equipe(
                        "R" + idRonde + "_T" + terrain + "_E" + e,
                        terrain,
                        idRonde,
                        idTournoi
                );
                equipe.saveInDB(con);

                for (int j = 0; j < minJoueurEquipe; j++) {
                    Joueur joueur = ordre.get(index++);
                    joueur.setIdEquipe(equipe.getId());
                    joueur.setPriority(); // a joué cette ronde                   
                    joueur.updateEquipe(con);
                    joueur.updatePriority(con);

                }
            }
            Matchs m = new Matchs(terrain,idRonde,idTournoi);
            m.saveInDB(con);
        }

        // Joueurs non utilisés deviennent prioritaires
        for (int i = index; i < ordre.size(); i++) {
            Joueur j = ordre.get(i);
            j.resetPriority();
            j.updatePriority(con);
        }

        Notification.show(
                "Ronde lancée : " + nbTerrainsUtilisables + " terrains utilisés"
        );

    } catch (SQLException ex) {
        Notification.show("Erreur : " + ex.getMessage());
    }
}

    private void fermerDerniereRondeSiTempsDepasse() {
    try (Connection con = ConnectionPool.getConnection()) {
        Ronde derniereRonde = Ronde.findDerniereRondeByTournoi(con, idTournoi);
        if (derniereRonde != null && derniereRonde.getStatut() == 0) {
            long delai = Tournoi.findTournoiById(con, idTournoi).temps() * 1000L;
            long tempsEcoule = System.currentTimeMillis() - derniereRonde.getTimestampDebut().getTime();

            if (tempsEcoule >= delai) {
                derniereRonde.setStatut(1);
                derniereRonde.updateTimestampFinAndStatut(con);
                Notification.show("La dernière ronde a été automatiquement fermée.");

                List<Equipe> equipes = Equipe.equipesByTournoiandRonde(con, idTournoi, derniereRonde.getId());
                demanderScoresPourEquipes(equipes); // pas de con ici !
            }
        }
    } catch (SQLException ex) {
        Notification.show("Erreur lors de la fermeture automatique : " + ex.getMessage());
    }
}

private void demanderScoresPourEquipes(List<Equipe> equipes) {
    if (equipes.isEmpty()) return;

    Equipe e = equipes.get(0);
    TextField scoreField = new TextField("Score pour " + e.getNom());
    scoreField.setPlaceholder("Entrez le score");

    Dialog dialog = new Dialog();
    Button ok = new Button("OK", ev -> {
        try (Connection con = ConnectionPool.getConnection()) { // nouvelle connection par équipe
            int scoreEquipe = Integer.parseInt(scoreField.getValue());
            List<Joueur> joueurs = Joueur.allJoueursByEquipe(con, e.getId());
            for (Joueur j : joueurs) {
                j.setScore(scoreEquipe + j.getScore());
                j.updateScore(con);
            }
            Notification.show("Scores mis à jour pour " + e.getNom());
        } catch (SQLException ex) {
            Notification.show("Erreur DB : " + ex.getMessage());
        } catch (NumberFormatException ex) {
            Notification.show("Score invalide");
        }

        dialog.close();
        demanderScoresPourEquipes(new ArrayList<>(equipes.subList(1, equipes.size()))); // nouvelle liste pour récursion sûre
    });

    dialog.add(scoreField, ok);
    dialog.open();
}

}