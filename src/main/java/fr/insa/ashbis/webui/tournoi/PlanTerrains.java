/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


package fr.insa.ashbis.webui.tournoi;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.ashbis.model.Tournoi;
import fr.insa.ashbis.webui.layout.SecondaryLayout;
import fr.insa.ashbis.webui.session.SessionInfo;
import fr.insa.beuvron.utils.database.ConnectionPool;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author ashln
 */

@Route(value = "plan-terrains", layout = SecondaryLayout.class)
@PageTitle("plan des terrains")
public class PlanTerrains extends FlexLayout {

    private static final int LARGEUR_TERRAIN = 140;
    private static final int HAUTEUR_TERRAIN = 90;

    public PlanTerrains() {
        try (Connection con = ConnectionPool.getConnection()) {
            Tournoi t = Tournoi.findTournoiById(con, SessionInfo.getSelectedTournoiId());
            int nbTerrains= t.getNbrTerrain();
            setFlexWrap(FlexWrap.WRAP);
            setJustifyContentMode(JustifyContentMode.CENTER);
            getStyle().set("gap", "20px");
            setWidthFull();

            for (int i = 1; i <= nbTerrains; i++) {
                add(creerTerrain(i));
            }
        }catch (SQLException ex) {
            Notification.show("Erreur : " + ex.getMessage());
            return;
        }
        
    }

    private Component creerTerrain(int numero) {

        Div terrain = new Div();
        terrain.setText(String.valueOf(numero));

        terrain.getStyle()
                .set("width", LARGEUR_TERRAIN + "px")
                .set("height", HAUTEUR_TERRAIN + "px")
                .set("border", "3px solid #2563eb")
                .set("border-radius", "12px")
                .set("display", "flex")
                .set("align-items", "center")
                .set("justify-content", "center")
                .set("font-size", "28px")
                .set("font-weight", "bold")
                .set("cursor", "pointer")
                .set("background-color", "#eff6ff")
                .set("transition", "0.2s");

        // 🖱️ interaction au clic
        terrain.addClickListener(e ->
                Notification.show("Terrain " + numero)
        );

        // ✨ effet hover
        terrain.getElement().addEventListener("mouseenter", e ->
                terrain.getStyle().set("background-color", "#dbeafe")
        );
        terrain.getElement().addEventListener("mouseleave", e ->
                terrain.getStyle().set("background-color", "#eff6ff")
        );

        return terrain;
    }
}
