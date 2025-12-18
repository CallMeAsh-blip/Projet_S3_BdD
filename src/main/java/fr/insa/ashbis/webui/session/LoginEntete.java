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

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import fr.insa.ashbis.model.Admin;
import fr.insa.beuvron.utils.database.ConnectionPool;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

/**
 *
 * @author francois
 */
public class LoginEntete extends HorizontalLayout {

    public Button login;
    public Button signin;
    
    public LoginEntete() {
        this.login = new Button("Se connecter");
        this.login.addClickListener((t) -> {
            UI.getCurrent().navigate("Connection");
        });
        this.signin = new Button("Créer un compte");
        this.signin.addClickListener((t) -> {
            UI.getCurrent().navigate("CreationCompte");
        });
        this.add(this.signin, this.login);
    }

}
