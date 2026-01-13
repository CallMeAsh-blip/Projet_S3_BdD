/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.ashbis.webui.session;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.ashbis.model.Admin;
import fr.insa.ashbis.webui.layout.MainLayout;
import fr.insa.beuvron.utils.database.ConnectionPool;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

/**
 *
 * @author ashln
 */

@Route(value = "Connection",layout = MainLayout.class)
@PageTitle("Connection")
public class Login extends FormLayout{
    
    public TextField surnom;
    public PasswordField pass;
    public Button login;

    public Login() {
        this.surnom = new TextField("username : ");
        this.pass = new PasswordField("password : ");
        this.login = new Button("login");
        this.login.addClickListener((t) -> {
            this.doLogin();
        });
        this.add(this.surnom, this.pass, this.login);
    }

    public void doLogin() {
        String surnom = this.surnom.getValue();
        String pass = this.pass.getValue();
        try (Connection con = ConnectionPool.getConnection()) {
            Optional<Admin> trouve = Admin.findBySurnomPass(con, surnom, pass);
            if (trouve.isEmpty()) {
                Notification.show("username ou password incorrect");
            } else {
                SessionInfo.login(trouve.get());
                SessionInfo.setIdAdmin(trouve.get().getId());
                UI.getCurrent().refreshCurrentRoute(true);
                UI.getCurrent().navigate("");
            }
        } catch (SQLException ex) {
            Notification.show("Problème "+ex.getLocalizedMessage());
        }
    }
    
}
