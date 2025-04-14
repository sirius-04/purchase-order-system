package services;

import models.users.User;
import repository.UserRepository;

/**
 *
 * @author Chan Yong Liang
 */
public class AuthenticationManager {
    
    static UserRepository userRepo = new UserRepository();
    static String[] userList = userRepo.getUserRows();

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
            String fetchedUsername = row[2];
            String fetchedPassword = row[3];

            if (fetchedUsername.equals(username)) {
                usernameExists = true;

                if (!fetchedPassword.equals(password)) {
                    throw new IncorrectPasswordException("Invalid password for user: " + username);
                }
                
                User user = userRepo.find(fetchedUserId);

                return user;
            }
        }

        throw new UserNotFoundException("Username not found: " + username);
    }
}
