/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.ashbis.webui.tournoi;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import fr.insa.ashbis.model.Equipe;
import fr.insa.ashbis.model.Joueur_Equipe;
import fr.insa.ashbis.model.Matchs;
import fr.insa.ashbis.webui.layout.SecondaryLayout;
import fr.insa.ashbis.webui.session.SessionInfo;
import fr.insa.beuvron.utils.database.ConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
/**
 *
 * @author ashln
 */

@Route(value = "matchs", layout = SecondaryLayout.class)
@PageTitle("Matchs de la ronde")
public class VueMatch extends VerticalLayout
        implements BeforeEnterObserver {

    private Integer idRonde;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {

        idRonde = SessionInfo.getSelectedRondeId();

        if (idRonde == null) {
            Notification.show("Aucune ronde sélectionnée");
            event.forwardTo("rondes");
            return;
        }

        initView();
    }

    private void initView() {
        
        removeAll();
        add(new H2("Matchs et équipes participantes"));

        Grid<Matchs> grid = new Grid<>();

        try (Connection con = ConnectionPool.getConnection()) {
            Map<Integer, Integer> scoreParEquipe = Joueur_Equipe.scoresParRonde(con, idRonde);

            List<Equipe> toutesEquipes = Equipe.equipesByTournoiandRonde(con, SessionInfo.getSelectedTournoiId(), idRonde);
            Map<Integer, List<Equipe>> equipesParTerrain = toutesEquipes.stream()
            .collect(Collectors.groupingBy(Equipe::getTerrain));

            List<Matchs> matchs =
                    Matchs.allMatchsByRonde(con, idRonde);
            
            System.out.println(matchs);
            grid.setItems(matchs);


            AtomicInteger compteur = new AtomicInteger(1);

            grid.addColumn(m -> compteur.getAndIncrement())
            .setHeader("Match");


            grid.addColumn(Matchs::getIdTerrain)
                    .setHeader("Terrain");

            grid.addColumn(match -> {
                List<Equipe> equipes = equipesParTerrain.get(match.getIdTerrain());
                if (equipes == null || equipes.isEmpty()) {
                    return "Aucune équipe";
                }
                return equipes.stream()
                .map(Equipe::getNom)
                .collect(Collectors.joining("  vs  "));
            }).setHeader("Équipes");
            
            grid.addColumn(match -> {
    List<Equipe> equipes = equipesParTerrain.get(match.getIdTerrain());
    if (equipes == null || equipes.isEmpty()) {
        return "";
    }

    return equipes.stream()
        .map(e -> {
            Integer score = scoreParEquipe.get(e.getId());
            return e.getNom() + " : " + (score != null ? score : "-");
        })
        .collect(Collectors.joining(" | "));
}).setHeader("Score final");


        } catch (SQLException ex) {
            Notification.show("Erreur : " + ex.getMessage());
            return;
        }

        add(grid);

        add(new Button("Retour aux rondes",
                e -> UI.getCurrent().navigate("rondes")));
    }
}

