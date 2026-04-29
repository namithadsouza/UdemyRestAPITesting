package stepdefs;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import io.cucumber.java.PendingException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.example.context.ScenarioContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class JiraStepdefs {
    public static final String authValue = "Basic bmFtaXRoYWRzb3V6YTk5QGdtYWlsLmNvbTpBVEFUVDN4RmZHRjBsemx2LWhQenlXVFNCSWNybW1QOGs4VUtOTERhaUpiTGVpd1dMRUJWd0Y0UDQ3QUh0UU1PZXNfMDZ6LUg1c1F2ZWRzQUluNVZSeWVPRGVHWG03akM1WTBfX2tJOWN4XzJwTl9KZHVqUWJ3TXBpdGRvYXIyOHFFbWhOeXJSMnQ2MU11U3JqdlFpdmlNWHliWGVmNjB6VTl3T1RROF9pbnBSREJybjV4N1RGcXM9NUUzRUZGNUE=";

    @Autowired
    ScenarioContext scenarioContext;

    @Given("issue is created")
    public void issueIsCreated() throws IOException {
        String inputPayload = new String(Files.readAllBytes(Path.of("src/test/resources/inputFiles/JiraIssueRequestPayload.json")));
        RestAssured.baseURI = "https://namithadsouza.atlassian.net/";
        String response = given().header("Content-Type", "application/json")
                .header("Authorization", authValue)
                .body(inputPayload)
                .when().post("rest/api/3/issue")
                .then().log().all()
                .assertThat()
                .statusCode(201)
                .extract().response().asString();
        JsonPath jsonPath = new JsonPath(response);
        String jiraIssueId = jsonPath.get("id");
        scenarioContext.setData("jiraIssueId", jiraIssueId);
         /*
    {
    "id": "10043",
    "key": "SCRUM-7",
    "self": "https://namithadsouza.atlassian.net/rest/api/3/issue/10043"
    }
    */
    }

    @Then("add attachment")
    public void addAttachment() {
        RestAssured.baseURI = "https://namithadsouza.atlassian.net/";
        String response = given().header("Content-Type", "multipart/form-data")
                .header("Authorization", authValue)
                .header("X-Atlassian-Token", "no-check")
                .pathParams("key", scenarioContext.getData("jiraIssueId"))
                .multiPart("file", new File("src/test/resources/inputFiles/jiraCat.jpg"))
                .when().post("rest/api/3/issue/{key}/attachments")
                .then().log().all()
                .assertThat()
                .statusCode(200)
                .extract().response().asString();
    }

}
