package utils;

import java.util.List;
import models.users.User;
import repository.UserRepository;

/**
 *
 * @author Chan Yong Liang
 */
public class AuthenticationManager {
    
    static UserRepository userRepo = new UserRepository();
    static List<User> userList = userRepo.getAll().stream().filter(user -> user.getStatus() == User.Status.active).toList();

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

    public static User authorize(String username, String password) throws UserNotFoundException, IncorrectPasswordException, InvalidRoleException {
        boolean usernameExists = false;

        for (User eachUser : userList) {
            String fetchedUserId = eachUser.getUserId();
            String fetchedUsername = eachUser.getUsername();
            String fetchedPassword = eachUser.getPassword();

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
