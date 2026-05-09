@OAuth2
  Feature: Open Authorization 2.0 validation

    Scenario: Get courseList
      Given authorization code is generated
      And access token is generated
      Then api call is made to get the course
