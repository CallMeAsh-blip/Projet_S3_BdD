/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.ashbis.webui.session;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.ashbis.model.Admin;
import fr.insa.ashbis.webui.MainLayout;
import fr.insa.beuvron.utils.database.ConnectionPool;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author ashln
 */

    
@Route(value = "CreationCompte",layout = MainLayout.class)
@PageTitle("Création d'un compte")
public class SignIn extends FormLayout {

    private TextField username;
    private PasswordField password;
    private Button save;
    
    public SignIn() {
        this.username = new TextField("username");
        this.password = new PasswordField("password");
        this.save = new Button("save");
        this.save.addClickListener((t) -> {
            this.doSave();
        });

        this.setAutoResponsive(true);
        this.addFormRow(this.username,this.password);
        this.addFormRow(this.save);
    }
    
    public void doSave() {
        String username = this.username.getValue();
        String pass = this.password.getValue();

        if (username == null || username.isBlank() ||
            pass == null || pass.isBlank()) {
            Notification.show("Tous les champs sont obligatoires");
            return;
        }

        try (Connection con = ConnectionPool.getConnection()) {

            if (Admin.usernameExiste(con, username)) {
                Notification.show("Ce nom d'utilisateur existe déjà");
                return;
            }

            Admin u = new Admin(pass, username);
            u.saveInDB(con);
            Notification.show("Utilisateur " + username + " créé");

            this.username.clear();
            this.password.clear();
        
        } catch (SQLException ex) {
         Notification.show("Erreur BD : " + ex.getMessage());
        }
    }


}

