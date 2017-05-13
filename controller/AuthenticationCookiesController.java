package capstone.controller;

import capstone.entity.model.AuthenticationCookie;
import capstone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by FPT on 2/15/2017.
 */
@Controller
public class AuthenticationCookiesController extends AuthenticateController {

    /**
     * The instance of the UserService that contains the necessary methods to access and manipulate
     * Users stored in the backend
     */
    private UserService userService;

    @Autowired
    public AuthenticationCookiesController(UserService userService){
        super(userService);
        this.userService = userService;
    }

    /**
     * Method to add an authenticationCookie to the session to allow
     * the user to login using the authenticationCookie instead of a password
     * @param authenticationCookie the authentication cookie to add
     * @return true if the cookie was added, false otherwise
     */
    @RequestMapping(value = "/api/addCookies",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> addCookies(
            @RequestBody AuthenticationCookie authenticationCookie){
        boolean success = false;
        if(authenticate() != null){
            success = userService.saveCookies(authenticationCookie);
        }
        if(success){
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);
        }
    }
}
