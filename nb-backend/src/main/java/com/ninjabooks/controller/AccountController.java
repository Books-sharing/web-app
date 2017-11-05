package com.ninjabooks.controller;

import com.ninjabooks.domain.User;
import com.ninjabooks.error.exception.user.UserAlreadyExistException;
import com.ninjabooks.json.message.MessageResponse;
import com.ninjabooks.json.user.UserRequest;
import com.ninjabooks.json.user.UserResponse;
import com.ninjabooks.security.user.SpringSecurityUser;
import com.ninjabooks.security.utils.TokenUtils;
import com.ninjabooks.service.rest.account.AccountService;
import com.ninjabooks.util.SecurityHeaderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@RestController
public class AccountController
{
    private final AccountService accountService;
    private final TokenUtils tokenUtils;
    private final UserDetailsService userDetailsService;
    private final SecurityHeaderUtils securityHeaderUtils;

    @Autowired
    public AccountController(AccountService accountService,
                             TokenUtils tokenUtils,
                             UserDetailsService userDetailsService,
                             SecurityHeaderUtils securityHeaderUtils) {
        this.accountService = accountService;
        this.tokenUtils = tokenUtils;
        this.userDetailsService = userDetailsService;
        this.securityHeaderUtils = securityHeaderUtils;
    }

    /**
     * Create new user in system. If any exception not occurs, then return http status
     * 201 (created).
     *
     * @param userRequest - received from request
     * @throws UserAlreadyExistException if user already exist in DB
     *
     * @return message when user successfully created
     */

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public MessageResponse createUser(@RequestBody UserRequest userRequest) throws
        UserAlreadyExistException {
        User userFromRequest = new User(userRequest.getName(), userRequest.getEmail(), userRequest.getPassword());
        accountService.createUser(userFromRequest);

        return new MessageResponse("User was successfully created");
    }

    /**
     * After proper authorization return basic info about user like:
     * - id
     * - name
     * - email
     *
     * @param httpServletRequest - http request which contains authorization header with
     *                           jwt token
     * @return user info as json (content: id, fistname, lastname, email)
     */

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ResponseEntity<UserResponse> getAuthenticatedUser(HttpServletRequest httpServletRequest) throws Exception {
        String header = httpServletRequest.getHeader("Authorization");
        String token = securityHeaderUtils.obtainTokenFromRequest(header);
        String email = tokenUtils.getUsernameFromToken(token);
        SpringSecurityUser user = (SpringSecurityUser) userDetailsService.loadUserByUsername(email);

        return new ResponseEntity<>(new UserResponse(user), HttpStatus.FOUND);
    }
}
