package ru.netology.CourseProject_3_TransferMoney;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CourseProject3TransferMoneyApplicationTests {
    @Autowired
    private TestRestTemplate restTemplate;

    @Container
    private static final GenericContainer<?> myApp = new GenericContainer<>("tcapp:1.0")
            .withExposedPorts(5500);

    @Test
    void contextLoads() {
        ResponseEntity<String> forEntity = restTemplate.getForEntity("http://localhost:" + myApp.getMappedPort(5500)
                + "/transfer", String.class); // проверка, что просто запустился и эндпоинты отвечают?
        ResponseEntity<String> forEntity2 = restTemplate.getForEntity("http://localhost:" + myApp.getMappedPort(5500)
                + "/confirmOperation", String.class);
    }
}