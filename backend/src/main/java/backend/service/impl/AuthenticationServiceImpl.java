package backend.service.impl;


import backend.model.dto.AuthenticationRequest;
import backend.model.dto.AuthenticationResponse;
import backend.service.AuthenticationService;
import backend.service.TokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final TokenService tokenService;

    public AuthenticationServiceImpl(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.tokenService = tokenService;
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        final AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
            authenticationResponse.setToken(tokenService.generateToken(userDetailsService.loadUserByUsername(authenticationRequest.getUsername())));
        } catch (AuthenticationException e) {
            //TODO: When we learn logging and exception handling
        }
        return authenticationResponse;
    }

}
