/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import java.lang.reflect.Constructor;
import models.users.User;
import models.users.UserRole;
import services.FileManager;

/**
 *
 * @author Chan Yong Liang
 */
public class UserRepository {

    private final String fileName = "users";
    FileManager fm = new FileManager();

    public void save(User user) {
        String row = formatUserToRow(user);
        
        fm.writeFile(fileName, row);
    }
    
    public void update(User updatedUser) {
        //
    }

    public User find(String targetUserId) {
        String[] userRow = getUserRows();

        for (String rowData : userRow) {
            String[] columns = rowData.split(",");

            if (columns.length > 0 && columns[0].trim().equals(targetUserId)) {
                String userId = columns[0].trim();
                String roleName = columns[1].trim();
                String username = columns[2].trim();
                String password = columns[3].trim();

                try {
                    UserRole role = UserRole.valueOf(roleName);

                    Constructor<? extends User> constructor = role.getModelClass().getConstructor(String.class, String.class, String.class);

                    return constructor.newInstance(userId, username, password);

                } catch (Exception e) {
                    throw new RuntimeException("Failed to create user instance for role: " + roleName, e);
                }
            }
        }

        throw new IllegalArgumentException("User not found: " + targetUserId);
    }

    public String[] getUserRows() {
        return fm.readFile(fileName);
    }

    private String formatUserToRow(User user) {
        String rowFormat = "%s,%s,%s,%s";

        String formattedRow = String.format(rowFormat,
                user.getUserId(),
                user.getUserRole(),
                user.getUsername(),
                user.getPassword()
        );

        return formattedRow;
    }
}
