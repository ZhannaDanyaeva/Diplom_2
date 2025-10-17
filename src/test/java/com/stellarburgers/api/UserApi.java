package com.stellarburgers.api;

import com.stellarburgers.models.User;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserApi {
    private static final String BASE_URL = "https://stellarburgers.education-services.ru/api";

    @Step("Создание пользователя")
    public static Response createUser(User user) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(BASE_URL + "/auth/register");
    }

    @Step("Логин пользователя")
    public static Response login(User user) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(BASE_URL + "/auth/login");
    }

    @Step("Обновление данных пользователя")
    public static Response updateUser(User user, String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .body(user)
                .when()
                .patch(BASE_URL + "/auth/user");
    }

    @Step("Обновление данных пользователя без авторизации")
    public static Response updateUserWithoutAuth(User user) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .patch(BASE_URL + "/auth/user");
    }

    @Step("Удаление пользователя")
    public static Response deleteUser(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .when()
                .delete(BASE_URL + "/auth/user");
    }
}