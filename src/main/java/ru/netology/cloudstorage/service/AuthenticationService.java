package ru.netology.cloudstorage.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.netology.cloudstorage.DTO.AuthenticationRequest;
import ru.netology.cloudstorage.DTO.AuthenticationResponse;
import ru.netology.cloudstorage.entity.User;
import ru.netology.cloudstorage.model.Session;
import ru.netology.cloudstorage.repositiry.UsersRepository;
import ru.netology.cloudstorage.utility.IdGen;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
@Slf4j
public class AuthenticationService {
    private final ConcurrentMap<String, Session> sessions;
    private final UsersRepository usersRepository;

    public AuthenticationService(UsersRepository usersRepository) {
        this.sessions = new ConcurrentHashMap<>();
        this.usersRepository = usersRepository;
    }

    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {
        AuthenticationResponse authenticationResponse;
        Optional<User> userFromDB = usersRepository.findUserByLoginAndPassword(authenticationRequest.getLogin(), authenticationRequest.getPassword());
        if (userFromDB.isPresent()) {
            Session session = new Session(IdGen.generateId(), userFromDB.get().getId());
            sessions.put(session.getId(), session);
            authenticationResponse = new AuthenticationResponse(session.getId());
            log.info("User ".concat(authenticationRequest.getLogin()).concat(" is authorized"));
        } else {
            log.error("Authorization error");
            authenticationResponse = null;
        }
        return authenticationResponse;
    }

    public boolean logout(String authToken) {
        Session result = sessions.getOrDefault(authToken, null);
        boolean mark;
        if (result != null) {
            sessions.remove(result.getId(), result);
            mark = true;
            log.info("User ".concat(authToken).concat(" is logout."));
        } else {
            log.warn("User not found in session.");
            mark = false;
        }
        return mark;
    }
    public Session getSession(String authToken) {
        return sessions.get(authToken);
    }
}
