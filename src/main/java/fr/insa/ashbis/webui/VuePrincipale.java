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
package fr.insa.ashbis.webui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.ashbis.model.Tournoi;
import fr.insa.beuvron.utils.database.ConnectionPool;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author francois
 */
@Route(value = "", layout = MainLayout.class)
@PageTitle("Likes")
public class VuePrincipale extends VerticalLayout {

    public VuePrincipale() {
        this.add(new H2("Bienvenue sur votre site de gestion de tournoi"));
        this.add(new Paragraph("une superbe application"));
        Grid<Tournoi> grid = new Grid<>();
        grid.addColumn(Tournoi::getNom).setHeader("nom du tournoi");
        grid.addColumn(Tournoi::getMaxJoueurEquipe).setHeader("joueur par équipe");
        grid.addColumn(Tournoi::getNbrTerrain).setHeader("terrains");
        grid.addColumn(new ComponentRenderer<>(tournoi -> {
            Button btn = new Button("Voir");
            btn.addClickListener(e -> 
                UI.getCurrent().navigate("tournoi/" + tournoi.getId())
            );
            return btn;
        })).setHeader("Action");

        try (Connection con = ConnectionPool.getConnection()) {
            grid.setItems(Tournoi.AllTournois(con));
        } catch (SQLException ex) {
            Notification.show("Problème : " + ex.getLocalizedMessage());
        }
        this.add(grid);
       
    }

}
