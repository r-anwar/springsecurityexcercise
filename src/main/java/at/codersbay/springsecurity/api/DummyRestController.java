package at.codersbay.springsecurity.api;

import at.codersbay.springsecurity.security.AuthRequestBody;
import at.codersbay.springsecurity.security.AuthResponseBody;
import at.codersbay.springsecurity.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class DummyRestController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtService;


    @GetMapping("/hello")
    public String hello() {
        return "Hello!";
    }

    @GetMapping("/user/hello")
    public String userHello() {
        return "Hello, User!";
    }

    @GetMapping("/admin/hello")
    public String adminHello() {
        return "Hello, Admin!";
    }

    @GetMapping("/moderator/hello")
    public String moderatorHello() {
        return "Hello, Moderator!";
    }

    /**
     *
     * @param authRequest
     * @return
     */
    @PostMapping("/authenticate")
    public AuthResponseBody authenticateAndGetToken(
            @RequestBody AuthRequestBody authRequest) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequest.getUsername(), authRequest.getPassword()));

        if (authentication.isAuthenticated()) {
            String token = jwtService.generateToken(authRequest.getUsername(), authentication.getAuthorities());
            Object principal = authentication.getPrincipal();

            AuthResponseBody authResponse = new AuthResponseBody();
            authResponse.setToken(token);

            UserDetails userDetails = (UserDetails) principal;
            authResponse.setUsername(userDetails.getUsername());
            authResponse.setAuthorities(userDetails.getAuthorities());
            return authResponse;

        }

        throw new UsernameNotFoundException("Invalid user request!");
    }
}
