package runner;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"stepdefs", "config"},
       // tags = "@Regression",
        plugin = {"pretty", "html:target/cucumber-reports/htmlReport.html",
        "json:target/jsonReports/jsonReport.json"}
)
public class TestRunner {
}