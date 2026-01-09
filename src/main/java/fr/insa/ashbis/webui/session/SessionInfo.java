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
package fr.insa.ashbis.webui.session;

import com.vaadin.flow.server.VaadinSession;
import fr.insa.ashbis.model.Admin;
import fr.insa.ashbis.model.Tournoi;
import java.io.Serializable;
import java.util.Optional;

/**
 *
 * @author francois
 */
public class SessionInfo implements Serializable{
    private static Integer selectedTournoiId = null;
    
    private static final long serialVersionUID = 1L;
    
    private Admin curUser;
    
    private static int lastCreatedRonde=-1;
    
    private static int idAdmin;
    
    private static int idAdminTournoi;
    
    private static int selectedRondeId;
    
    private static int idEquipeSelectionne;
    
    private static Tournoi tournoicopier;
    
    public static SessionInfo getOrCreate() {
        VaadinSession curSession = VaadinSession.getCurrent();
        SessionInfo curInfo = curSession.getAttribute(SessionInfo.class);
        if (curInfo == null) {
            curInfo = new SessionInfo();
            curSession.setAttribute(SessionInfo.class, curInfo);
        }
        return curInfo;
    }
    
    public static void login(Admin u) {
        SessionInfo curInfo = getOrCreate();
        curInfo.curUser = u;
    }
    
    public static void logout() {
        SessionInfo curInfo = getOrCreate();
        curInfo.curUser = null;
    }
    
    public static Optional<Admin> curUser() {
        Admin u = getOrCreate().curUser;
        if (u == null) {
            return Optional.empty();
        } else {
            return Optional.of(u);
        }
    }
    
    public static void setlastCreatedRonde(int id){
        lastCreatedRonde = id;
    }
    
    public static void setIdEquipeSelectionne(int id){
        idEquipeSelectionne = id;
    }
    
    public static int getIdEquipeSelectionne(){
        return idEquipeSelectionne;
    }
    
    public static int getLastCreatedRonde(){
        return lastCreatedRonde;
    }
    
    public static boolean userConnected() {
        return curUser().isPresent();
    }
    

    public static void setSelectedTournoiId(Integer id) {
        selectedTournoiId = id;
    }

    public static Integer getSelectedTournoiId() {
        return selectedTournoiId;
    }
    
    public static void setSelectedRondeId(Integer id) {
        selectedRondeId = id;
    }

    public static Integer getSelectedRondeId() {
        return selectedRondeId;
    }

    /**
     * @return the idAdmin
     */
    public static int getIdAdmin() {
        return idAdmin;
    }

    /**
     * @param idAdmin the idAdmin to set
     */
    public static void setIdAdmin(int id) {
        idAdmin = id;
    }

    /**
     * @return the idAdminTournoi
     */
    public static int getIdAdminTournoi() {
        return idAdminTournoi;
    }

    /**
     * @param idAdminTournoi the idAdminTournoi to set
     */
    public static void setIdAdminTournoi(int id) {
        idAdminTournoi = id;
    }
    
    public static boolean isAdminTournoi(){
        return idAdmin==idAdminTournoi;
    }

    /**
     * @return the tournoicopier
     */
    public static Tournoi getTournoicopier() {
        return tournoicopier;
    }

    /**
     * @param aTournoicopier the tournoicopier to set
     */
    public static void setTournoicopier(Tournoi aTournoicopier) {
        tournoicopier = aTournoicopier;
    }
    
    
    
}
