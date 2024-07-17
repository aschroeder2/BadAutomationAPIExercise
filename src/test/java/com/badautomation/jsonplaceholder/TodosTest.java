package com.badautomation.jsonplaceholder;

import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class TodosTest {

    private Integer id;
    private String value;

    @Test
    public void getAllTodos() {
        Random random = new Random();
        int randomIndex = random.nextInt(10);

        Response response = given().log().all().when().get("https://jsonplaceholder.typicode.com/todos").then().statusCode(HttpStatus.SC_OK).log().all().extract().response();

        List<Object> responseBody = response.jsonPath().getList("");
        Assert.assertEquals(responseBody.size(), 200);

        id = response.path("[" + randomIndex + "].id");
        value = response.path("[" + randomIndex + "].title");
    }

    @Test(dependsOnMethods = "getAllTodos")
    public void GetTodoById() {
        Response response = given().log().all().when().get("https://jsonplaceholder.typicode.com/todos/" + id).then().statusCode(HttpStatus.SC_OK).log().all().extract().response();

        Assert.assertEquals(response.path("title"), value);
        Assert.assertEquals(response.path("completed"), "True");
    }

    @Test(dependsOnMethods = "getAllTodos")
    public void getbyidsuccessful() {
        Response response = given().log().all().when().get("https://jsonplaceholder.typicode.com/todos/" + id).then().log().all().extract().response();

        Assert.assertEquals(response.statusCode(), 200);
    }

    @Test(dependsOnMethods = "getAllTodos")
    public void update() {
        String updateTodoBody = "{\"userId\": 3,\"id\": " + id + ",\"title\": \"Automate tests for JSONPlaceholder\",\"completed\": false}";

        given().log().all().when().body(updateTodoBody).put("https://jsonplaceholder.typicode.com/todos/" + id).then().log().all().extract().response();
    }

    @Test
    public void NEW_TODO() throws JSONException {
        JSONObject body =  new JSONObject().put("userId", 2).put("title", "Automate tests for JSONPlaceholder").put("completed", false);
        Response response = given().log().all().when().body(body.toString()).post("https://jsonplaceholder.typicode.com/todos").then().statusCode(201).log().all().extract().response();
        Assert.assertNotNull(response.path("id"));
    }
}
