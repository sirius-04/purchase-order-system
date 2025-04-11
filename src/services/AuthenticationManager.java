package services;

import models.users.InventoryManager;
import models.users.User;
import models.users.Admin;
import models.users.FinanceManager;
import models.users.PurchaseManager;
import models.users.SalesManager;

/**
 *
 * @author Chan Yong Liang
 */
public class AuthenticationManager {

    static FileManager fm = new FileManager();
    static String[] userList = fm.readFile("users");

    // custom exceptions for different login errors
    public static class UserNotFoundException extends Exception {
        public UserNotFoundException(String message) {
            super(message);
        }
    }

    public static class IncorrectPasswordException extends Exception {
        public IncorrectPasswordException(String message) {
            super(message);
        }
    }

    public static class InvalidRoleException extends Exception {
        public InvalidRoleException(String message) {
            super(message);
        }
    }

    public static User login(String username, String password) throws UserNotFoundException, IncorrectPasswordException, InvalidRoleException {
        boolean usernameExists = false;

        for (String eachUser : userList) {
            String[] row = eachUser.split(",");

            // skip invalid rows
            if (row.length < 4) {
                continue;
            }

            String fetchedUserId = row[0];
            String fetchedRole = row[1];
            String fetchedUsername = row[2];
            String fetchedPassword = row[3];

            if (fetchedUsername.equals(username)) {
                usernameExists = true;

                if (!fetchedPassword.equals(password)) {
                    throw new IncorrectPasswordException("Invalid password for user: " + username);
                }

                return switch (fetchedRole) {
                    case "SalesManager" -> new SalesManager(fetchedUserId, fetchedUsername, fetchedPassword);
                    case "PurchaseManager" -> new PurchaseManager(fetchedUserId, fetchedUsername, fetchedPassword);
                    case "FinanceManager" -> new FinanceManager(fetchedUserId, fetchedUsername, fetchedPassword);
                    case "InventoryManager" -> new InventoryManager(fetchedUserId, fetchedUsername, fetchedPassword);
                    case "Admin" -> new Admin(fetchedUserId, fetchedUsername, fetchedPassword);
                    default -> throw new InvalidRoleException("Invalid role detected for user: " + username);
                };
            }
        }

        throw new UserNotFoundException("Username not found: " + username);
    }
}
