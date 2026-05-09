package stepdefs;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.example.enums.GoogleAPIResources;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;


public class Hooks {


    @After
    public void cleanUPScenarioContext() {
        System.out.println("This runs after each scenario");
    }


    @Before("@DeletePlace")
    public void beforeScenario() throws IOException {
        //Note: Because of this hook we can run @DeletePlace scenario independent of first scenario
        //Note : Its good practive to make scenario independent of each other to avoid flaky and non parallel testcases
        System.out.println("This runs before @DeletePlace scenario");
        e2ePlaceAPIStepDefs m = new e2ePlaceAPIStepDefs();
        if (e2ePlaceAPIStepDefs.place_id == null) {
            System.out.println("Creating data as first scenario got failed");
            m.add_Place_Payload_with("Shetty", "French", "Asia");
            m.user_calls_with_http_request(GoogleAPIResources.valueOf("AddPlaceAPI"), "POST");
            m.verify_place_Id_created_maps_to_using("Shetty", "getPlaceAPI");
        }
    }
}
