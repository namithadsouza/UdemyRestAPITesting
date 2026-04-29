import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.example.context.ScenarioContext;
import org.example.pojo.Course;
import org.example.pojo.UdemyCourseResponse;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class OAuthStepdefs {
    @Autowired
    ScenarioContext scenarioContext;


    @Given("bearer token is generated")
    public void bearerTokenIsGenerated() {
        /*
        The formParam() method does not support sending files. To send files, you should use the multiPart() method.
         */

        RestAssured.baseURI = "https://rahulshettyacademy.com/oauthapi/";
        String response = given()
                .formParam("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
                .formParam("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
                .formParam("grant_type", "client_credentials")
                .formParam("scope", "trust")
                .when()
                .post("oauth2/resourceOwner/token")
                .then()
                .log().all()
                .extract().response().asString();
        JsonPath jsonPath = new JsonPath(response);
        /*
        {
    "access_token": "lGOCv6ftPOkf7X7MSy5n4Q==",
    "token_type": "Bearer",
    "expires_in": 3600,
    "refresh_token": "ZPsMC8QZ/ijFLKkJATdV1Q==",
    "scope": "create"
      }
     */
        scenarioContext.setData("accessToken", jsonPath.get("access_token"));

    }

    @Then("validate the response from getCourse API")
    public void validateTheResponseFromGetCourseAPI() {
        baseURI = "https://rahulshettyacademy.com/oauthapi/";
        UdemyCourseResponse udemyCourseAPIResponse = given()
                .queryParam("access_token", scenarioContext.getData("accessToken").toString())
                .when()
                .get("getCourseDetails").as(UdemyCourseResponse.class);
        Assert.assertEquals("LinkedIn Value is not matching", "https://www.linkedin.com/in/rahul-shetty-trainer/", udemyCourseAPIResponse.getLinkedIn());
        scenarioContext.setData("udemyCourseData", udemyCourseAPIResponse);
    }

    @And("get the price for api course {string}")
    public void getThePriceForApiCourse(String courseTitle) {
        UdemyCourseResponse udemyCourseAPIResponse = (UdemyCourseResponse) scenarioContext.getData("udemyCourseData");
        Course requiredCourse = udemyCourseAPIResponse.getCourses().getApi().stream()
                .filter(course -> course.getCourseTitle().equals(courseTitle)).findFirst().orElseThrow();
        System.out.println("price of " + courseTitle + " = " + requiredCourse.getPrice());
    }

    @And("print the title of all courses for web automation")
    public void printTheTitleOfAllCoursesForWebAutomation() {
        UdemyCourseResponse udemyCourseAPIResponse = (UdemyCourseResponse) scenarioContext.getData("udemyCourseData");
        udemyCourseAPIResponse.getCourses().getWebAutomation().forEach(course -> {
            System.out.println(course.getCourseTitle());
        });
    }
}
