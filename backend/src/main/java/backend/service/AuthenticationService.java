package backend.service;

import backend.model.dto.AuthenticationRequest;
import backend.model.dto.AuthenticationResponse;

public interface AuthenticationService {

    AuthenticationResponse authenticate (AuthenticationRequest authenticationRequest);
}
