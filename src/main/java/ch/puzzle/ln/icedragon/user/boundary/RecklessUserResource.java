package ch.puzzle.ln.icedragon.user.boundary;


import ch.puzzle.ln.icedragon.user.entity.RecklessLogin;
import ch.puzzle.ln.icedragon.user.entity.RecklessPublicKey;
import ch.puzzle.ln.security.jwt.JWTFilter;
import ch.puzzle.ln.security.jwt.TokenProvider;
import ch.puzzle.ln.web.rest.UserJWTController;
import ch.puzzle.ln.web.rest.UserJWTController.JWTToken;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/recklessuser")
public class RecklessUserResource {

    private final RecklessUserService recklessUserService;
    private final TokenProvider tokenProvider;

    public RecklessUserResource(RecklessUserService recklessUserService, TokenProvider tokenProvider) {
        this.recklessUserService = recklessUserService;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping(path = "/challenge")
    public String getChallenge(@RequestBody RecklessPublicKey publicKey) {
        return recklessUserService.generateChallenge(publicKey);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<JWTToken> login(@RequestBody RecklessLogin login) {
        Authentication authentication = authenticate(login);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication, true);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
    }

    private Authentication authenticate(RecklessLogin login) {
        recklessUserService.verifyChallenge(login);
        return new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of(new SimpleGrantedAuthority("RECKLESS"));
            }

            @Override
            public Object getCredentials() {
                return "youre-dumb-but-im-dummy-youre-dumb-but-im-dummy-recklessssss";
            }

            @Override
            public Object getDetails() {
                return "Reckless user";
            }

            @Override
            public Object getPrincipal() {
                return login.getNodePublicKey();
            }

            @Override
            public boolean isAuthenticated() {
                return true;
            }

            @Override
            public void setAuthenticated(boolean b) throws IllegalArgumentException {

            }

            @Override
            public String getName() {
                return "reckless";
            }
        };
    }
}
