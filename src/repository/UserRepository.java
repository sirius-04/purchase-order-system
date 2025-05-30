/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import java.lang.reflect.Constructor;
import models.users.User;
import models.users.UserRole;

/**
 *
 * @author Chan Yong Liang
 */
public class UserRepository extends BaseRepository<User> {

    public UserRepository() {
        super("users", "%s,%s,%s,%s,%s");
    }

    @Override
    protected User parseRow(String[] columns) {
        String userId = columns[0].trim();
        String roleName = columns[1].trim();
        String username = columns[2].trim();
        String password = columns[3].trim();

        UserRole role = UserRole.valueOf(roleName);
        User.Status status = User.Status.valueOf(columns[4].trim());

        try {
            Constructor<? extends User> constructor = role.getModelClass()
                    .getConstructor(String.class, UserRole.class, String.class, String.class, User.Status.class);

            return constructor.newInstance(userId, role, username, password, status);
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate user of role: " + roleName, e);
        }
    }

    @Override
    protected String formatToRow(User user) {
        return String.format(rowFormat,
                user.getUserId(),
                user.getUserRole(),
                user.getUsername(),
                user.getPassword(),
                user.getStatus()
        );
    }
}
