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

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.beuvron.utils.database.ConnectionPool;
import fr.insa.beuvron.vaadin.utils.dataGrid.ResultSetGrid;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author francois
 */
@Route(value = "")
@PageTitle("test")
public class VuePrincipale extends VerticalLayout {

    public VuePrincipale() {
        this.add(new H2("Bienvenue dans Likes"));
        this.add(new Paragraph("Une superbe application"));

        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(
                    "SELECT * FROM joueur"
            );

            // Affiche automatiquement les colonnes de la table joueur
            this.add(new ResultSetGrid(pst));

        } catch (SQLException ex) {
            Notification.show("Problème : " + ex.getLocalizedMessage());
        }
    }

}

