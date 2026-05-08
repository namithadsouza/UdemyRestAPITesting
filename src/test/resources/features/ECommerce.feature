Feature: E2E Validation of ecommerce website

  @Ecommerce
  Scenario: ecommerce website "https://rahulshettyacademy.com/client/"
    Given user is logged in
    And adds the product
    When makes the purchase the product
    Then view the purchase in order history
    And delete the order history
    And delete the product