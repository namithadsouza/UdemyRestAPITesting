Feature: OAuth API Validation


  Scenario: OAuth API Validation
    Given bearer token is generated
    Then validate the response from getCourse API
    And get the price for api course "Rest Assured Automation using Java"
    And print the title of all courses for web automation

