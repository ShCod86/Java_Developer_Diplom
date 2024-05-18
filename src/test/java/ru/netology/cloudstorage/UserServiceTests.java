package ru.netology.cloudstorage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ru.netology.cloudstorage.DTO.AuthenticationRequest;
import ru.netology.cloudstorage.DTO.AuthenticationResponse;
import ru.netology.cloudstorage.repositiry.UsersRepository;
import ru.netology.cloudstorage.service.AuthenticationService;

import java.util.Objects;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = CloudStorageApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:app-integrationtest.properties")
public class UserServiceTests {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private AuthenticationService authenticationService;


    @Test
    public void findUserByLoginAndPasswordTest() {
        var user = usersRepository.findUserByLoginAndPassword("Ivan", "QWERTY");
        assertTrue(user.isPresent());
        assertEquals("Ivan", user.get().getLogin());
    }

    @Test
    public void loginTest() {
        AuthenticationResponse response = authenticationService.login(new AuthenticationRequest("Ivan", "QWERTY"));
        assertNotNull(response);
        assertNotNull(Objects.requireNonNull(response).getAuthToken());
        assertNotEquals(0, response.getAuthToken().length());
    }

    @Test
    public void authenticationLoginExceptionTest() {
        AuthenticationResponse actual = authenticationService.login(new AuthenticationRequest("Ivan", "111222"));
        assertNull(actual);
    }


    @Test
    public void logoutTest() {
        AuthenticationResponse response = authenticationService.login(new AuthenticationRequest("Ivan", "QWERTY"));
        String authToken = Objects.requireNonNull(response).getAuthToken();

        boolean actual = authenticationService.logout(authToken);
        boolean expected = true;
        assertEquals(expected, actual);
    }
}
