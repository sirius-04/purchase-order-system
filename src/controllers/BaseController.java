/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import javax.swing.JFrame;
import models.users.User;
import views.Main;

/**
 *
 * @author Chan Yong Liang
 */
public abstract class BaseController {
    protected User currentUser;
    protected JFrame view;
    
    public BaseController(User user) {
        this.currentUser = user;
    }
    
    protected abstract JFrame createView();

    public void start() {
        this.view = createView();
        
        loadInitialData();
        setupListeners();
        
        view.setVisible(true);
    }
    
    protected abstract void loadInitialData();

    protected void logout() {
        view.dispose();
        
        new Main().setVisible(true);
    }

    protected final void setupListeners() {
        setupSharedListeners();
        setupCustomListeners();  
    }
    
    protected void setupSharedListeners() {
        // shared listeners
    }
    
    protected abstract void setupCustomListeners();
}
