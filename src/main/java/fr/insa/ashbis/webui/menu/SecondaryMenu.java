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
package fr.insa.ashbis.webui.menu;

import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import fr.insa.ashbis.model.Tournoi;
import fr.insa.ashbis.webui.session.SessionInfo;
import fr.insa.ashbis.webui.tournoi.GestionTournoi;
import fr.insa.ashbis.webui.tournoi.PlanTerrains;
import fr.insa.ashbis.webui.tournoi.VueEquipes;
import fr.insa.ashbis.webui.tournoi.VueRonde;
import fr.insa.ashbis.webui.tournoi.VueTournoi;
import fr.insa.beuvron.utils.database.ConnectionPool;
import java.sql.Connection;
import java.sql.SQLException;
import static org.apache.coyote.http11.Constants.a;



/**
 *
 * @author ashln
 */
public class SecondaryMenu extends SideNav{
    private Tournoi t;
    public SecondaryMenu(){
        SideNavItem accueil = new SideNavItem("Joueurs",VueTournoi.class);
        SideNavItem vueRonde = new SideNavItem("Ronde",VueRonde.class);
        SideNavItem controlTournoi = new SideNavItem("Gestion Tournoi",GestionTournoi.class);
        SideNavItem VueEquipe = new SideNavItem("liste des Equipes",VueEquipes.class);
        SideNavItem PlanTournoi = new SideNavItem("Plan des Terrains",PlanTerrains.class);
        this.addItem(accueil,vueRonde,PlanTournoi,VueEquipe);
        if (SessionInfo.isAdminTournoi()) {
            this.addItem(controlTournoi);
        }
        
    }
}
