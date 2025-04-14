package models.users;

/**
 *
 * @author Chan Yong Liang
 */
public class InventoryManager extends User {
    public InventoryManager(String userId, UserRole userRole, String username, String password) {
        super(userId, userRole.InventoryManager, username, password);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
