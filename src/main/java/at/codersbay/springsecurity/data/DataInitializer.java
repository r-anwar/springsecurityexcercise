package at.codersbay.springsecurity.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;

@Service
public class DataInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @PostConstruct
    public void setup() {

        List<User> users = this.userRepository.findAll();

        if(users.size() > 0) {
            return;
        }

        Role userRole = new Role();
        userRole.setName("USER");

        this.roleRepository.save(userRole);

        Role adminRole = new Role();
        adminRole.setName("ADMIN");

        this.roleRepository.save(adminRole);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        User userMax = new User();
        userMax.setEnabled(true);
        userMax.setUsername("max");
        userMax.setPassword(encoder.encode("yxcvbnm"));
        userMax.setRoles(new HashSet<>());
        userMax.getRoles().add(userRole);

        this.userRepository.save(userMax);

        User adminUser = new User();
        adminUser.setEnabled(true);
        adminUser.setUsername("admin");
        adminUser.setPassword(encoder.encode("yxcvbnm"));
        adminUser.setRoles(new HashSet<>());
        adminUser.getRoles().add(adminRole);

        this.userRepository.save(adminUser);

    }
}
