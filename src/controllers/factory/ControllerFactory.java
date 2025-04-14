/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers.factory;

import controllers.BaseController;
import java.lang.reflect.Constructor;
import models.users.User;
import models.users.UserRole;

/**
 *
 * @author Chan Yong Liang
 */
public class ControllerFactory {

    public static BaseController getControllerFor(User user) {
        for (UserRole role : UserRole.values()) {
            if (role.getModelClass().isInstance(user)) {
                try {
                    Constructor<? extends BaseController> constructor = role.getControllerClass().getConstructor(role.getModelClass());
                    return constructor.newInstance(user);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to instantiate controller for user role: " + role, e);
                }
            }
        }
        throw new IllegalArgumentException("Unknown user type: " + user.getClass().getName());
    }
}
