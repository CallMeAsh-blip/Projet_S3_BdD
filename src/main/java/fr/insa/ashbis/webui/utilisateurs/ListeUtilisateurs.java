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

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.ashbis.model.Tournoi;
import fr.insa.beuvron.utils.database.ConnectionPool;
import fr.insa.ashbis.webui.MainLayout;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author francois
 */
@Route(value = "utilisateurs/liste",layout = MainLayout.class)
@PageTitle("Likes")
public class ListeUtilisateurs extends VerticalLayout {

    public ListeUtilisateurs() {
        this.add(new H2("Liste de tous les utilisateurs"));
        Grid<Tournoi> grid = new Grid<>();
        grid.addColumn(Tournoi::getNom).setHeader("nom du tournoi");
        grid.addColumn(Tournoi::getMaxJoueurEquipe).setHeader("joueur par équipe");
        grid.addColumn(Tournoi::getNbrTerrain).setHeader("terrains");
        try (Connection con = ConnectionPool.getConnection()) {
            grid.setItems(Tournoi.AllTournois(con));
        } catch (SQLException ex) {
            Notification.show("Problème : " + ex.getLocalizedMessage());
        }
        this.add(grid);
    }

}
