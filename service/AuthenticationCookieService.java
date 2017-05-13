package capstone.service;

import capstone.entity.dao.AuthenticationCookieDao;
import capstone.entity.model.AuthenticationCookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Adam Mandel, Derek Albano, Nina Tham, Phat Duong, and Ryan Harnett.
 * This class is developed for providing AuthenticationCookies stored in database to be accessed throughout the system.
 */
@Service
public class AuthenticationCookieService {
    /**
     * The instance of the AuthenticationCookie Data Access Object
     */
    private AuthenticationCookieDao authenticationCookieDao;

    /**
     * Constructor to wire the AuthenticationCookieDao
     * @param authenticationCookieDao the authenticationCookieDao object to wire
     */
    @Autowired
    public AuthenticationCookieService(AuthenticationCookieDao authenticationCookieDao){
        this.authenticationCookieDao = authenticationCookieDao;
    }

    /**
     * Method to get the list of all AuthenticationCookie object from the database
     * @return the list of all AuthenticationCookie object from the database
     */
    public List<AuthenticationCookie> getAuthenticationCookiesList(){
        return authenticationCookieDao.getList();
    }

    /**
     * Method to get the AuthenticationCookie that has the provided key
     * @param key the key of the AuthenticationCookie
     * @return the AuthenticationCookie that has the provided key
     */
    public AuthenticationCookie getAuthenticationCookies(String key){
        return authenticationCookieDao.get(key);
    }

    /**
     * Method to add a new AuthenticationCookie object to the database
     * @param cookies the AuthenticationCookie object to be added to the database
     */
    public void addAuthenticationCookies(AuthenticationCookie cookies){
        authenticationCookieDao.updateOrInsert(cookies);
    }

}
