package stepdefs;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.example.context.ScenarioContext;
import org.example.pojo.GooglePlace;
import org.example.pojo.Location;
import org.example.util.JsonUtil;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class GoogleMapStepdefs {

    @Autowired
    private ScenarioContext scenarioContext;

    RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("https://rahulshettyacademy.com")
            .addQueryParam("key", "qaclick123")
            .setContentType(ContentType.JSON).build();


    ResponseSpecification responseSpec= new ResponseSpecBuilder()
            .expectStatusCode(200).build();

    @Given("validate the Add Place API of google maps")
    public void validateTheAddPlaceAPIOfGoogleMaps() throws IOException {
        GooglePlace googlePlace= new GooglePlace();
        Location location= new Location();
        location.setLat(-35);
        location.setLng(12.1);
        googlePlace.setLocation(location);
        googlePlace.setAccuracy(1);
        googlePlace.setAddress("Mangalore");
        googlePlace.setName("namitha");
        googlePlace.setPhone_number("123456789");
        googlePlace.setWebsite("http://google.com");
        googlePlace.setLanguage("French-IN");
        googlePlace.setTypes(List.of("park", "shoe"));
       //String googlePlace = new String(Files.readAllBytes(Path.of("src/test/resources/inputFiles/GooglePlaceRequestData.json")));
        //given() - input details(query params, headers, body)
        //when() - actual resource and http method
        //then() - assertions
        /*
        baseURI = "https://rahulshettyacademy.com";
        String response = given().log().all().queryParam("key", "qaclick123")
                .header("Content-Type", "application/json")
                .body(googlePlace)
                .when().post("maps/api/place/add/json")
                .then().log().all()
                .assertThat()
                .statusCode(200)
                .body("scope", equalTo("APP"))
                .header("Server", "Apache/2.4.52 (Ubuntu)")
                .extract().response().asString();
        System.out.println(response);
        JsonPath jsonPath = JsonUtil.rawStringToJson(response);
        String placeId = jsonPath.get("place_id");
        Assert.assertNotNull("Place Id value should not be null", placeId);
        System.out.println(placeId);
        scenarioContext.setData("place_id", placeId);
        */
        RequestSpecification request = given().spec(requestSpec).body(googlePlace);
        Response response=request.when().post("maps/api/place/add/json")
                .then()
                .spec(responseSpec).log().all()
                .extract().response();
        JsonPath jsonPath = response.jsonPath();
        String placeId = jsonPath.get("place_id");
        Assert.assertNotNull("Place Id value should not be null", placeId);
        System.out.println(placeId);
        scenarioContext.setData("place_id", placeId);
    }

    @Given("validate the Get Place API of google maps")
    public void validateTheGetPlaceAPIOfGoogleMaps() {
        String place_id = scenarioContext.getData("place_id").toString();
        System.out.println("place id using sc " + place_id);
        // Write code here that turns the phrase above into concrete actions
        baseURI = "https://rahulshettyacademy.com";
        String response = given().log().all().queryParam("key", "qaclick123")
                .queryParam("place_id", place_id)
                .when().get("maps/api/place/get/json")
                .then().log().all()
                .assertThat()
                .statusCode(200)
                .header("Server", "Apache/2.4.52 (Ubuntu)")
                .extract().response().asString();
        System.out.println("response from get call is " + response);
    }

    @Given("validate the Update Place API of google maps")
    public void validateTheUpdatePlaceAPIOfGoogleMaps() {
        String place_id = scenarioContext.getData("place_id").toString();
        scenarioContext.setData("updatedAddress", "70 winter walk, Mangalore");
        baseURI = "https://rahulshettyacademy.com";
        given().log().all()
                .queryParam("key", "qaclick123")
                .queryParam("place_id", place_id)
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "\"place_id\":\"" + place_id + "\",\n" +
                        "\"address\":\"" + scenarioContext.getData("updatedAddress").toString() + "\",\n" +
                        "\"key\":\"qaclick123\"\n" +
                        "}\n")
                .when().put("maps/api/place/update/json")
                .then().log().all()
                .assertThat()
                .statusCode(200)
                .body("msg", equalTo("Address successfully updated"));
    }

    @And("validated that location is updated")
    public void validatedThatLocationIsUpdated() {
        String place_id = scenarioContext.getData("place_id").toString();
        System.out.println("place id using sc " + place_id);
        baseURI = "https://rahulshettyacademy.com";
        String response = given().log().all().queryParam("key", "qaclick123")
                .queryParam("place_id", place_id)
                .when().get("maps/api/place/get/json")
                .then().log().all()
                .assertThat()
                .statusCode(200)
                .extract().response().asString();
        System.out.println("response from get call is " + response);
        JsonPath jsonPath = JsonUtil.rawStringToJson(response);
        String address = jsonPath.get("address");
        Assert.assertEquals("place value comparison", scenarioContext.getData("updatedAddress").toString(), address);
    }
}