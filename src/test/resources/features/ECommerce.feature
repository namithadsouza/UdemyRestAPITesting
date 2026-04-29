Feature: E2E Validation of ecommerce website
  Login API -> Create Product -> Purchase Order on created Product -> Delete order -> Delete Product

  Scenario: ecommerce website "https://rahulshettyacademy.com/client/"
    Given user is logged in
    And adds the product
    When makes the purchase the product
    Then view the purchase in order history
    And delete the order history
    And delete the product