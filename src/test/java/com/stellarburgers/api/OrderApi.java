package com.stellarburgers.api;

import com.stellarburgers.models.Order;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderApi {
    private static final String BASE_URL = "https://stellarburgers.education-services.ru/api";

    @Step("Создание заказа")
    public static Response createOrder(Order order, String accessToken) {
        if (accessToken != null) {
            return given()
                    .header("Content-type", "application/json")
                    .header("Authorization", accessToken)
                    .body(order)
                    .when()
                    .post(BASE_URL + "/orders");
        } else {
            return given()
                    .header("Content-type", "application/json")
                    .body(order)
                    .when()
                    .post(BASE_URL + "/orders");
        }
    }

    @Step("Получение заказов пользователя")
    public static Response getUserOrders(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .when()
                .get(BASE_URL + "/orders");
    }

    @Step("Получение заказов пользователя без авторизации")
    public static Response getUserOrdersWithoutAuth() {
        return given()
                .when()
                .get(BASE_URL + "/orders");
    }

    @Step("Получение списка ингредиентов")
    public static Response getIngredients() {
        return given()
                .when()
                .get(BASE_URL + "/ingredients");
    }
}