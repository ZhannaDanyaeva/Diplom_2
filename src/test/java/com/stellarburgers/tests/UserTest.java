package com.stellarburgers.tests;

import com.stellarburgers.api.UserApi;
import com.stellarburgers.models.User;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class UserTest {
    private User user;
    private String accessToken;

    @Before
    public void setUp() {
        user = new User("test-user-" + System.currentTimeMillis() + "@yandex.ru",
                "password", "TestUser");
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            UserApi.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    public void testCreateUniqueUser() {
        Response response = UserApi.createUser(user);

        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(user.getEmail().toLowerCase()))
                .body("user.name", equalTo(user.getName()))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());

        accessToken = response.path("accessToken");
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    public void testCreateExistingUser() {
        // Сначала создаем пользователя
        UserApi.createUser(user);
        accessToken = UserApi.createUser(user).path("accessToken");

        // Пытаемся создать такого же пользователя снова
        Response response = UserApi.createUser(user);

        response.then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без заполнения обязательного поля")
    public void testCreateUserWithoutRequiredField() {
        User invalidUser = new User("", "password", "TestUser");

        Response response = UserApi.createUser(invalidUser);

        response.then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
}