package com.stellarburgers.tests;

import com.stellarburgers.api.UserApi;
import com.stellarburgers.models.User;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class AuthTest {
    private User user;
    private String accessToken;

    @Before
    public void setUp() {
        user = new User("test-auth-" + System.currentTimeMillis() + "@yandex.ru",
                "password", "TestUser");

        // Создаем пользователя для тестов
        Response response = UserApi.createUser(user);
        accessToken = response.path("accessToken");
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            UserApi.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Логин под существующим пользователем")
    public void testLoginWithValidCredentials() {
        Response response = UserApi.login(user);

        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(user.getEmail().toLowerCase()))
                .body("user.name", equalTo(user.getName()))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }

    @Test
    @DisplayName("Логин с неверным паролем")
    public void testLoginWithInvalidPassword() {
        User invalidUser = new User(user.getEmail(), "wrongpassword", user.getName());

        Response response = UserApi.login(invalidUser);

        response.then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Изменение данных пользователя с авторизацией")
    public void testUpdateUserWithAuth() {
        User updatedUser = new User("updated-" + user.getEmail(), "newpassword", "UpdatedName");

        Response response = UserApi.updateUser(updatedUser, accessToken);

        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(updatedUser.getEmail().toLowerCase()))
                .body("user.name", equalTo(updatedUser.getName()));
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    public void testUpdateUserWithoutAuth() {
        User updatedUser = new User("updated-" + user.getEmail(), "newpassword", "UpdatedName");

        Response response = UserApi.updateUserWithoutAuth(updatedUser);

        response.then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }
}