package stepdefs;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.path.json.JsonPath;
import org.example.context.ScenarioContext;
import org.example.util.JsonUtil;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MockCouseStepDefs {

    @Autowired
    ScenarioContext scenarioContext;

    @Given("complex json data is available")
    public void complexJsonDataIsAvailable() throws IOException {
        String inputPayload = new String(Files.readAllBytes(Path.of("src/test/resources/inputFiles/CoursesMockData.json")));
        scenarioContext.setData("mockCoursesData",inputPayload);
    }

    @Then("print the number of courses returned by API")
    public void printTheNumberOfCoursesReturnedByAPI() {
        String mockCoursesData = scenarioContext.getData("mockCoursesData").toString();
        JsonPath courseListJson = JsonUtil.rawStringToJson(mockCoursesData);
        System.out.println("Number of courses " + courseListJson.getInt("courses.size()"));
    }

    @And("print purchase amount")
    public void printPurchaseAmount() {
        String mockCoursesData = scenarioContext.getData("mockCoursesData").toString();
        JsonPath courseListJson = JsonUtil.rawStringToJson(mockCoursesData);
        System.out.println("Purchase amount is " + courseListJson.get("dashboard.purchaseAmount"));
    }

    @And("print title of first course")
    public void printTitleOfFirstCourse() {
        String mockCoursesData = scenarioContext.getData("mockCoursesData").toString();
        JsonPath courseListJson = JsonUtil.rawStringToJson(mockCoursesData);
        System.out.println(" Title of first course is " + courseListJson.get("courses[0].title"));
    }

    @And("print all course titles and their respective prices")
    public void printAllCourseTitlesAndTheirRespectivePrices() {
        String mockCoursesData = scenarioContext.getData("mockCoursesData").toString();
        JsonPath courseListJson = JsonUtil.rawStringToJson(mockCoursesData);
        int courseListSize = courseListJson.getInt("courses.size()");
        for (int i = 0; i < courseListSize; i++) {
            System.out.println("index " + i + " course name " + courseListJson.get("courses[" + i + "].title") + " course price " + courseListJson.get("courses[" + i + "].price"));
        }
    }

    @And("print number of copies sold by {string} course")
    public void printNumberOfCopiesSoldByCourse(String courseName) {
        String mockCoursesData = scenarioContext.getData("mockCoursesData").toString();
        JsonPath courseListJson = JsonUtil.rawStringToJson(mockCoursesData);
        int courseListSize = courseListJson.getInt("courses.size()");
        for (int i = 0; i < courseListSize; i++) {
            if (courseName.equals((courseListJson.get("courses[" + i + "].title")))) {
                System.out.println("numbber of copies sold for " + courseName + " is " + courseListJson.get("courses[" + i + "].copies"));
            }
        }
    }

    @And("verify if sum of all Course prices matches with Purchase Amount")
    public void verifyIfSumOfAllCoursePricesMatchesWithPurchaseAmount() {

        String mockCoursesData = scenarioContext.getData("mockCoursesData").toString();
        JsonPath courseListJson = JsonUtil.rawStringToJson(mockCoursesData);
        int courseListSize = courseListJson.getInt("courses.size()");
        int sum = 0;
        for (int i = 0; i < courseListSize; i++) {
            int pricePerBookType = courseListJson.getInt("courses[" + i + "].copies") * courseListJson.getInt("courses[" + i + "].price");
            sum += pricePerBookType;
        }
        System.out.println("total sum calculated is " + sum);
        Assert.assertEquals("Sum should match ", courseListJson.getInt("dashboard.purchaseAmount"), sum);
    }

}
