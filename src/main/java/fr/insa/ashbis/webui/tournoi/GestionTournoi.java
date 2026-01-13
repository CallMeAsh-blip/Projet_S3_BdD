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
import fr.insa.ashbis.model.Joueur_Equipe;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

    try (Connection con = ConnectionPool.getConnection()) {

        Tournoi t = Tournoi.findTournoiById(con, idTournoi);
        int nbRondesExistantes = Ronde.countRondesByTournoi(con, idTournoi);
        int nbRondesMax = t.getNbrRonde();

        if (nbRondesExistantes >= nbRondesMax && Ronde.statutLastRonde(con, idTournoi)==1) {

            H2 fin = new H2("Le tournoi est terminé");
            fin.getStyle()
               .set("color", "black")
               .set("font-weight", "bold");

            add(fin);
            return; 
        }

        int rondesRestantes = nbRondesMax - nbRondesExistantes;

        add(new H2("Panneau de gestion du tournoi"));

        add(new com.vaadin.flow.component.html.Paragraph(
            "Il reste " + rondesRestantes + " ronde(s) à jouer"
        ));

        Button lancer = new Button("Lancer la ronde");

        Button fermerRondeBtn = new Button("ronde en cours");
        fermerRondeBtn.setEnabled(false);
        add(fermerRondeBtn);
        initialiserBoutonFermeture(fermerRondeBtn);

        lancer.addClickListener(e -> {
            try (Connection c = ConnectionPool.getConnection()) {

                int nbRondes = Ronde.countRondesByTournoi(c, idTournoi);

                if (nbRondes >= nbRondesMax) {
                    Notification.show(
                        "Impossible de créer une nouvelle ronde : nombre maximal atteint"
                    );
                    initView(); // rafraîchit la vue
                    return;
                }

                Ronde derniereRonde = Ronde.findDerniereRondeByTournoi(c, idTournoi);
                if (derniereRonde == null || derniereRonde.getStatut() == 1) {
                    lancerRonde();
                    initView(); // refresh après création
                } else {
                    Notification.show(
                        "Impossible de créer une nouvelle ronde : la ronde précédente n'est pas terminée"
                    );
                }

            } catch (SQLException ex) {
                Notification.show("Erreur : " + ex.getMessage());
            }
        });

        Button retour = new Button("Retour", e -> UI.getCurrent().navigate("tournoi"));
        add(lancer, retour);

        // Bouton fermer tournoi
        if (t.getStatut() == 0) {
            Button changerEtat = new Button("Fermer le tournoi");
            changerEtat.addClickListener(e -> changerEtatTournoi());
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
            UI.getCurrent().refreshCurrentRoute(true);

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
        int numeroRonde= Ronde.countRondesByTournoi(con, idTournoi);
        int maxJouerEquipe = tournoi.getMaxJoueurEquipe();
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
                        "R" + numeroRonde + "_T" + terrain + "_Eq" + e,
                        terrain,
                        idRonde,
                        idTournoi
                );
                equipe.saveInDB(con);

                for (int jo=0; jo < minJoueurEquipe; jo++) {
                    Joueur joueur = ordre.get(index++);
                    joueur.setIdEquipe(equipe.getId());
                    joueur.resetPriority();             
                    joueur.updateEquipe(con);
                    joueur.updatePriority(con);
                    Joueur_Equipe je = new Joueur_Equipe(
                        joueur.getId(),
                        equipe.getId(),
                        idRonde,
                        0 // score initial
                    );
                    
                    je.saveInDB(con);
                }
//                }while (jo < maxJouerEquipe && index < ordre.size()) {
//                    Joueur joueur = ordre.get(index);
//                    joueur.setIdEquipe(equipe.getId());
//                    joueur.resetPriority();
//                    joueur.updateEquipe(con);
//                    joueur.updatePriority(con);
//
//                    Joueur_Equipe je = new Joueur_Equipe(joueur.getId(), equipe.getId(), idRonde, 0);
//                    je.saveInDB(con);
//
//                    jo++;
//                    index++;
//                }

            }
            Matchs m = new Matchs(terrain,idRonde,idTournoi);
            m.saveInDB(con);
        }

        // Joueurs non utilisés deviennent prioritaires
        for (int i = index; i < ordre.size(); i++) {
            Joueur j = ordre.get(i);
            j.setPriority();
            
            
            j.updatePriority(con);
        }

        Notification.show(
                "Ronde lancée : " + nbTerrainsUtilisables + " terrains utilisés"
        );

    } catch (SQLException ex) {
        Notification.show("Erreur : " + ex.getMessage());
    }
}

    private void fermerRonde(int idRonde) {
    try (Connection con = ConnectionPool.getConnection()) {

        Ronde r = Ronde.findById(con, idRonde);
        if (r == null || r.getStatut() == 1) {
            Notification.show("Ronde déjà fermée");
            return;
        }

        // Fermer la ronde
        r.setStatut(1);
        r.setTimestampFin(new Timestamp(System.currentTimeMillis()));
        r.updateTimestampFinAndStatut(con);

        Notification.show("Ronde fermée");

        // Récupérer tous les matchs de la ronde
        List<Matchs> matchs = Matchs.allMatchsByRonde(con, idRonde);

        for (Matchs m : matchs) {
            // Récupérer toutes les équipes de ce match
            List<Equipe> equipes = Equipe.equipesByMatchs(con, m.getId(),idRonde);
            if (!equipes.isEmpty()) {
                demanderScoresPourMatch(equipes, idRonde);
            }
        }

    } catch (SQLException ex) {
        Notification.show("Erreur fermeture ronde : " + ex.getMessage());
    }
}



private void demanderScoresPourMatch(List<Equipe> equipes, int idRonde) {
    if (equipes.isEmpty()) return;

    Dialog dialog = new Dialog();
    dialog.setWidth("400px");

    VerticalLayout layout = new VerticalLayout();
    layout.setSpacing(true);

    Map<Equipe, TextField> scoreFields = new HashMap<>();

    for (Equipe e : equipes) {
        TextField tf = new TextField("Score pour " + e.getNom());
        tf.setPlaceholder("Entrez le score");
        layout.add(tf);
        scoreFields.put(e, tf);
    }

    Button ok = new Button("Valider", ev -> {
        try (Connection con = ConnectionPool.getConnection()) {

            for (Equipe e : equipes) {
                TextField tf = scoreFields.get(e);
                int scoreEquipe = Integer.parseInt(tf.getValue());

                Joueur_Equipe.updateScoreRonde(con, e.getId(), idRonde, scoreEquipe);

                List<Joueur> joueurs = Joueur_Equipe.joueursByEquipe(con, e.getId());
                for (Joueur j : joueurs) {
                    j.setScore(j.getScore() + scoreEquipe);
                    j.updateScore(con);
                }
            }

            Notification.show("Scores enregistrés pour toutes les équipes");
            dialog.close();

        } catch (NumberFormatException ex) {
            Notification.show("Score invalide, merci d'entrer un nombre");
        } catch (SQLException ex) {
            Notification.show("Erreur DB : " + ex.getMessage());
        }
    });

    layout.add(ok);
    dialog.add(layout);
    dialog.open();
}



private void initialiserBoutonFermeture(Button fermerRondeBtn) {

    try (Connection con = ConnectionPool.getConnection()) {

        Ronde ronde = Ronde.findDerniereRondeByTournoi(con, idTournoi);
        if (ronde == null || ronde.getStatut() == 1) {
            fermerRondeBtn.setText("impossible de fermer la ronde");
            fermerRondeBtn.setEnabled(false);
            return;
        }

        Tournoi t = Tournoi.findTournoiById(con, idTournoi);

        long dureeMs = t.temps() * 1000L;
        long debutMs = ronde.getTimestampDebut().getTime();
        long restantMs = dureeMs - (System.currentTimeMillis() - debutMs);

        if (restantMs <= 0) {
            activerBoutonFermeture(fermerRondeBtn, ronde.getId());
            return;
        }

        fermerRondeBtn.setEnabled(false);

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(() -> {
            long reste = dureeMs - (System.currentTimeMillis() - debutMs);

            UI.getCurrent().access(() -> {
                if (reste <= 0) {
                    fermerRondeBtn.setText("Fermer la ronde");
                    fermerRondeBtn.setEnabled(true);
                    scheduler.shutdown();
                } else {
                    fermerRondeBtn.setText(
                        "Fermer la ronde (" + (reste / 1000) + " s)"
                    );
                }
            });

        }, 0, 1, TimeUnit.SECONDS);

    } catch (SQLException ex) {
        Notification.show("Erreur timer : " + ex.getMessage());
    }
}

private void activerBoutonFermeture(Button btn, int idRonde) {
    btn.setText("Fermer la ronde");
    btn.setEnabled(true);

    btn.addClickListener(e -> fermerRonde(idRonde));
}


}