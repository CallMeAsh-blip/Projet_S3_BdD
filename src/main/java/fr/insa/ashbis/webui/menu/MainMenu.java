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
import fr.insa.ashbis.webui.VuePrincipale;
import fr.insa.ashbis.webui.session.SessionInfo;

import fr.insa.ashbis.webui.utilisateurs.CreerTournoi;
import fr.insa.ashbis.webui.utilisateurs.VueCompteAdmin;


/**
 *
 * @author francois
 */
public class MainMenu extends SideNav{
    
    public MainMenu() {
        SideNavItem accueil = new SideNavItem("accueil",VuePrincipale.class);
        SideNavItem creerTournoi = new SideNavItem("Créer Tournoi",CreerTournoi.class);
        SideNavItem VueCompte = new SideNavItem("Mon compte",VueCompteAdmin.class);
        this.addItem(accueil,creerTournoi);
        if (SessionInfo.userConnected()) {
            this.addItem(VueCompte);
        }
    }
    
}
