package stepdefs;

import static io.restassured.RestAssured.*;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;

public class GoogleOAuthStepdefs {
    String code = null;
    String accessToken = null;

    @Given("authorization code is generated")
    public void authorizationCodeIsGenerated() {
        /*
        This step will fail once the code expires.
        So correct way is to implement selenium code to get authorization code each and every time dynamically
         */
        String url = "https://rahulshettyacademy.com/getCourse.php?iss=https%3A%2F%2Faccounts.google.com&code=4%2F0AeoWuM8H0nlqdyPVzda1yyh4NnT7UDnqnf9GkJ8u7W2tndKzJypFCFQ3AvY0DQfKYNBoLw&scope=email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid&authuser=0&prompt=none";
        String partialcode = url.split("code=")[1];
        code = partialcode.split("&scope")[0];
        System.out.println(code);
    }

    @And("access token is generated")
    public void accessTokenIsGenerated() {
        String response = given().urlEncodingEnabled(false).queryParams("code", code).queryParams("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com").queryParams("client_secret", "erZOWM9g3UtwNRj340YYaK_W").queryParams("grant_type", "authorization_code").queryParams("state", "verifyfjdss").queryParams("session_state", "ff4a89d1f7011eb34eef8cf02ce4353316d9744b..7eb8")
                // .queryParam("scope", "email+openid+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email")
                .queryParams("redirect_uri", "https://rahulshettyacademy.com/getCourse.php").when().log().all().post("https://www.googleapis.com/oauth2/v4/token").asString();
        JsonPath jsonPath = new JsonPath(response);
        accessToken = jsonPath.getString("access_token");
        System.out.println(accessToken);
    }

    @When("api call is made to get the course")
    public void apiCallIsMadeToGetTheCourse() {
        String r2 = given().contentType("application/json").queryParams("access_token", accessToken)
                .expect().defaultParser(Parser.JSON)
                .when().get("https://rahulshettyacademy.com/getCourse.php").asString();
        System.out.println(r2);
    }


}
