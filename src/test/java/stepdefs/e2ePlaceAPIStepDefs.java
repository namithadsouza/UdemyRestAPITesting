package stepdefs;

import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.example.data.PlaceAPI;
import org.example.enums.GoogleAPIResources;

import java.io.IOException;

import static org.example.util.JsonUtil.getJsonPathValue;
import static org.example.util.PlaceAPIUtil.requestSpecification;
import static org.junit.Assert.assertEquals;
import static io.restassured.RestAssured.*;

public class e2ePlaceAPIStepDefs {
    RequestSpecification res;
    ResponseSpecification resspec;
    Response response;
    PlaceAPI data = new PlaceAPI();
    static String place_id;

    @ParameterType("AddPlaceAPI|getPlaceAPI|deletePlaceAPI")
    public static GoogleAPIResources GoogleAPIResources(String endpoint) {
        return GoogleAPIResources.valueOf(endpoint);
    }

    @Given("Add Place Payload with {string}  {string} {string}")
    public void add_Place_Payload_with(String name, String language, String address) throws IOException {
        // Write code here that turns the phrase above into concrete actions
        res = given().spec((requestSpecification()))
                .body(data.addPlacePayLoad(name, language, address));
    }

    @When("user calls {GoogleAPIResources} with {string} http request")
    public void user_calls_with_http_request(GoogleAPIResources googleAPIResources, String method) {
        /*
        User can pass value as String and then convert it to enum as well.
        GoogleAPIResources resourceAPI = GoogleAPIResources.valueOf(resource);
        System.out.println(resourceAPI.getResource());
        */

        resspec = new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();
        if (method.equalsIgnoreCase("POST"))
            response = res.when().post(googleAPIResources.getResource());
        else if (method.equalsIgnoreCase("GET"))
            response = res.when().get(googleAPIResources.getResource());
    }

    @Then("the API call got success with status code {int}")
    public void the_API_call_got_success_with_status_code(Integer int1) {
        // Write code here that turns the phrase above into concrete actions
        assertEquals(200, response.getStatusCode());
    }

    @Then("{string} in response body is {string}")
    public void in_response_body_is(String keyValue, String Expectedvalue) {
        assertEquals(getJsonPathValue(response, keyValue), Expectedvalue);
    }

    @Then("verify place_Id created maps to {string} using {string}")
    public void verify_place_Id_created_maps_to_using(String expectedName, String resource) throws IOException {

        place_id = getJsonPathValue(response, "place_id");
        res = given().spec(requestSpecification()).queryParam("place_id", place_id);
        user_calls_with_http_request(GoogleAPIResources.valueOf(resource), "GET");
        String actualName = getJsonPathValue(response, "name");
        assertEquals(actualName, expectedName);
    }


    @Given("DeletePlace Payload")
    public void deleteplace_Payload() throws IOException {
        res = given().spec(requestSpecification()).body(data.deletePlacePayload(place_id));
    }


}