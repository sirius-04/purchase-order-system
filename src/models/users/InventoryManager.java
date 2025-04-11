package models.users;

/**
 *
 * @author Chan Yong Liang
 */
public class InventoryManager extends User {
    public InventoryManager(String userId, String username, String password) {
        super(userId, username, password);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
