package in.nineteen96.jwtsecurity.services;

import in.nineteen96.jwtsecurity.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class UserService {

    private List<User> users = new ArrayList<>();

    public UserService() {
        log.info("Constructor Initializing");
        log.info("Creating Users");

        users.add(new User(UUID.randomUUID().toString(), "Darron Moraes", "darron@example.com"));
        users.add(new User(UUID.randomUUID().toString(), "Elesban Moraes", "eleban@example.com"));
        users.add(new User(UUID.randomUUID().toString(), "Allister Lopes", "allister@example.com"));
        users.add(new User(UUID.randomUUID().toString(), "Gavin Gomes", "gavin@example.com"));
        users.add(new User(UUID.randomUUID().toString(), "Pearl Rodrigues", "pearl@example.com"));
    }

    public List<User> getUsers() {
        log.info("Fetching users");
        return users;
    }
}
