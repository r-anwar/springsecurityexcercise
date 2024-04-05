package at.codersbay.springsecurity.api;

import at.codersbay.springsecurity.data.User;
import at.codersbay.springsecurity.security.AuthRequestBody;
import at.codersbay.springsecurity.security.AuthResponseBody;
import at.codersbay.springsecurity.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private JwtService jwtService;


    @GetMapping("/hello")
    public String hello() {
        return "Hello!";
    }

    @GetMapping("/user/hello")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public String userHello() {
        return "Hello, Admin!";
    }


    @GetMapping("/admin/hello")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String adminHello() {
        return "Hello, Admin!";
    }

    @PostMapping("/authenticate")
    public AuthResponseBody authenticateAndGetToken(
            @RequestBody AuthRequestBody authRequest) {

        System.out.println(authRequest);
        System.out.println(authRequest.getUsername());
        System.out.println(authRequest.getPassword());

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequest.getUsername(), authRequest.getPassword()));

        if (authentication.isAuthenticated()) {
            String token = jwtService.generateToken(authRequest.getUsername(), authentication.getAuthorities());
            Object principal = authentication.getPrincipal();
            System.out.println("WE ARE INSIDE THE FUNCTION");
            AuthResponseBody authResponse = new AuthResponseBody();
            authResponse.setToken(token);
            if (principal instanceof User) {
                System.out.println("INSIDE USER");
                UserDetails userDetails = (UserDetails) principal;
                authResponse.setUserDetails(userDetails);
                System.out.println("DIDNT GO INSIDE ANY OF THEM");
                return authResponse;
            }
        }

        throw new UsernameNotFoundException("Invalid user request!");
    }
}
