package capstone.entity.model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by Phat Duong, Adam Mandel, Nina Tham, Derek Albano, Ryan Hannett on 2/14/2017.
 */
@Entity
public class AuthenticationCookie {

    /**
     * Unique username of this AuthenticationCookie
     */
    @Id
    private String username;

    /**
     * The encrypted randomly generated token
     */
    private String token;

    AuthenticationCookie(){}

    /**
     * Method to get the username
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Method to set the username of the AuthenticationCookie
     * @param username the username of the AuthenticationCookie
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Method to get the token of the AuthenticationCookie
     * @return the token of the AuthenticationCookie
     */
    public String getToken() {
        return token;
    }

    /**
     * Method to set the token of the AuthenticationCookie
     * @param token the new token of the AuthenticationCookie
     */
    public void setToken(String token) {
        this.token = token;
    }

}
