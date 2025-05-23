/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import controllers.factory.ControllerFactory;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import models.users.User;
import services.AuthenticationManager;
import views.Main;

/**
 *
 * @author Chan Yong Liang
 */
public class MainController extends BaseController {

    private Main view;
    private JTextField usernameTextField;
    private JTextField passwordTextField;
    private JButton submitButton;

    public MainController(User user) {
        super(user);
    }

    public MainController() {
        this(null);
    }

    @Override
    protected JFrame createView() {
        this.view = new Main();
        return view;
    }

    @Override
    protected void loadInitialData() {
        usernameTextField = view.getUsernameTextField();
        passwordTextField = view.getPasswordTextField();
        submitButton = view.getSubmitButton();
    }

    @Override
    protected void setupCustomListeners() {
        login();
    }

    private void login() {
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AuthenticationManager auth = new AuthenticationManager();
                String enteredUsername = usernameTextField.getText();
                String enteredPassword = passwordTextField.getText();

                try {
                    User user = auth.authorize(enteredUsername, enteredPassword);
                    BaseController controller = ControllerFactory.getControllerFor(user);

                    view.dispose();
                    controller.start();

                } catch (AuthenticationManager.UserNotFoundException ex) {
                    JOptionPane.showMessageDialog(null, "Error: Username not found!", "Login Failed", JOptionPane.ERROR_MESSAGE);
                } catch (AuthenticationManager.IncorrectPasswordException ex) {
                    JOptionPane.showMessageDialog(null, "Error: Incorrect password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
                } catch (AuthenticationManager.InvalidRoleException ex) {
                    JOptionPane.showMessageDialog(null, "Error: Invalid user role!", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }

                // empty the text field after submit
                usernameTextField.setText("");
                passwordTextField.setText("");
            }
        });
    }
}
