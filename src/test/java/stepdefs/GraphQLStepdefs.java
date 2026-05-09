package stepdefs;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.context.ScenarioContext;
import org.example.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;

import static org.example.util.JsonUtil.getJsonPathValue;

import static io.restassured.RestAssured.*;


public class GraphQLStepdefs {

    @Autowired
    ScenarioContext scenarioContext;

    @Given("Character Id {int} retrieve character details")
    public void characterIdRetrieveCharacterDetails(int characterId) {
        Response response = given().log().all()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"query\": \"query($characterId: Int!){\\n  character(characterId:$characterId){\\n    name\\n    location{\\n      id\\n      name\\n    }\\n  }\\n}\",\n" +
                        "    \"variables\": {\n" +
                        "        \"characterId\": " + characterId + "\n" +
                        "    }\n" +
                        "}")
                .when().post("https://rahulshettyacademy.com/gq/graphql")
                .then().extract().response();
        System.out.println(response.getBody().asString());
        int locationId = Integer.parseInt(JsonUtil.getJsonPathValue(response, "data.character.location.id"));

        scenarioContext.setData("locationId", locationId);
    }

    @And("retrieve location details")
    public void retrieveLocationDetails() {
        int locationId = (int) scenarioContext.getData("locationId");
        String body = "{\n" +
                "    \"query\": \"query($locationId: Int!){\\n  location(locationId:$locationId){\\n    name\\n    id\\n    type\\n  }\\n}\",\n" +
                "    \"variables\": {\n" +
                "        \"locationId\":" + locationId + "\n" +
                "    }\n" +
                "}";
        Response response = given()
                .header("Content-Type", "application/json")
                .body(body)
                .when().post("https://rahulshettyacademy.com/gq/graphql")
                .then().extract().response();
        System.out.println(response.getBody().asString());

    }
}
