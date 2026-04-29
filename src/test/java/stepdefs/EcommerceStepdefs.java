package stepdefs;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.example.context.ScenarioContext;
import org.example.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.ArrayList;

import static io.restassured.RestAssured.*;

@Slf4j
public class EcommerceStepdefs {

    @Autowired
    ScenarioContext scenarioContext;

    @Given("user is logged in")
    public void userIsLoggedIn() {
        RequestSpecBuilder baseUrl = new RequestSpecBuilder()
                .setBaseUri("https://rahulshettyacademy.com/");
        ECommerceLogin eCommerceLogin=new ECommerceLogin();
        eCommerceLogin.setUserEmail("namithadsouza99@gmail.com");
        eCommerceLogin.setUserPassword("Namitha@98");
        RequestSpecification loginSpec = baseUrl.setContentType(ContentType.JSON).build();
        RequestSpecification loginSpecUpdated = given().spec(loginSpec).body(eCommerceLogin);
        LoginResponse loginResponse = loginSpecUpdated.when()
                .post("api/ecom/auth/login")
                .then().extract().response().as(LoginResponse.class);
        scenarioContext.setData("token", loginResponse.getToken());
        scenarioContext.setData("userId", loginResponse.getUserId());
    }

    @And("adds the product")
    public void addsTheProduct() {
        RequestSpecBuilder baseUrl = new RequestSpecBuilder()
                .setBaseUri("https://rahulshettyacademy.com/");
        RequestSpecification spec = baseUrl.addHeader("authorization", scenarioContext.getData("token").toString())
                .setContentType(ContentType.MULTIPART)
                .addFormParam("productName", "namitha")
                .addFormParam("productAddedBy", scenarioContext.getData("userId").toString())
                .addFormParam("productCategory", "fashion")
                .addFormParam("productSubCategory", "skirt")
                .addFormParam("productPrice", "100")
                .addFormParam("productDescription", "Dior Skirt")
                .addFormParam("productFor", "Women")
                .addMultiPart("productImage", new File("src/test/resources/inputFiles/jiraCat.jpg"))
                .build();
        ProductResponse response = given().spec(spec)
                .when().post("/api/ecom/product/add-product")
                .then().extract().as(ProductResponse.class);
        scenarioContext.setData("productId", response.getProductId());
    }

    @When("makes the purchase the product")
    public void makesThePurchaseTheProduct() {
        RequestSpecBuilder baseUrl = new RequestSpecBuilder()
                .setBaseUri("https://rahulshettyacademy.com/");
        Order order = new Order();
        order.setProductOrderedId(scenarioContext.getData("productId").toString());
        order.setCountry("India");
        OrderRquest orderRquest = new OrderRquest();
        ArrayList<Order> orders = new ArrayList<>();
        orders.add(order);
        orderRquest.setOrders(orders);
        RequestSpecification spec = baseUrl.addHeader("authorization", scenarioContext.getData("token").toString())
                .setContentType(ContentType.JSON)
                .setBody(orderRquest)
                .build();
        OrderResponse response = given().spec(spec)
                .when()
                .post("api/ecom/order/create-order")
                .then()
                .extract().as(OrderResponse.class);
        scenarioContext.setData("orderId", response.getOrders().getFirst());
    }

    @Then("view the purchase in order history")
    public void viewThePurchaseInOrderHistory() {
        RequestSpecBuilder baseUrl = new RequestSpecBuilder()
                .setBaseUri("https://rahulshettyacademy.com/");
        RequestSpecification spec = baseUrl.addHeader("authorization", scenarioContext.getData("token").toString())
                .build();
        Response response = given().spec(spec).queryParam("id", scenarioContext.getData("orderId"))
                .when().get()
                .then().statusCode(200).extract().response();
    }

    @And("delete the order history")
    public void deleteTheOrderHistory() {
        RequestSpecBuilder baseUrl = new RequestSpecBuilder()
                .setBaseUri("https://rahulshettyacademy.com/");
        RequestSpecification spec = baseUrl.addHeader("authorization", scenarioContext.getData("token").toString())
                .build();
        Response response = given().spec(spec)
                .when().delete("api/ecom/order/delete-order/" + scenarioContext.getData("orderId"))
                .then().statusCode(200).extract().response();
    }

    @And("delete the product")
    public void deleteTheProduct() {
        RequestSpecBuilder baseUrl = new RequestSpecBuilder()
                .setBaseUri("https://rahulshettyacademy.com/");
        RequestSpecification spec = baseUrl.addHeader("authorization", scenarioContext.getData("token").toString())
                .build();
        Response response = given().spec(spec)
                .when().delete("api/ecom/product/delete-product/" + scenarioContext.getData("productId"))
                .then().statusCode(200).extract().response();
    }
}
