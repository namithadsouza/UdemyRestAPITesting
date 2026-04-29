package stepdefs;

import io.cucumber.java.PendingException;
import io.cucumber.java.en.Given;
import io.restassured.path.json.JsonPath;
import org.example.context.ScenarioContext;
import org.example.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.*;

public class LibraryStepdefs {
    
    @Autowired
    private ScenarioContext scenarioContext;
    
    @Given("books are deleted for {string}")
    public void booksAreDeletedForAuthor(String author) {
        baseURI = "https://rahulshettyacademy.com";
        String response = given().log().all()
                .header("Content-Type", "application/json")
                .queryParam("AuthorName", author)
                .when().delete("Library/v1/DeleteBook.php")
                .then().log().all()
                .assertThat()
                .statusCode(200)
                .extract().response().asString();
        
        System.out.println("Delete response: " + response);
        JsonPath jsonPath = JsonUtil.rawStringToJson(response);
        String result = jsonPath.get("msg");
        System.out.println("Delete result: " + result);
        scenarioContext.setData("deleteResponse", response);
    }

    @Given("books are added with details <name> <isbn> <aisle> <author>")
    public void booksAreAddedWithDetailsNameIsbnAisleAuthor() {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }


}
