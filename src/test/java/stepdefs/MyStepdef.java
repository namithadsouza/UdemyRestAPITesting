package stepdefs;

import io.cucumber.java.PendingException;
import io.cucumber.java.en.Given;

public class MyStepdef {
    @Given("I am an authorized user")
    public void iAmAnAuthorizedUser() {
        // Write code here that turns the phrase above into concrete actions
        System.out.println("hi.....");
       // throw new PendingException();
    }
}
