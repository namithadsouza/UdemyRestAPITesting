@MockAPI
Feature: Mocking API Response

#  1. Print No of courses returned by API
#  2.Print Purchase Amount
#  3. Print Title of the first course
#  4. Print All course titles and their respective Prices
#  5. Print no of copies sold by RPA Course
#  6. Verify if Sum of all Course prices matches with Purchase Amount

  Scenario: Mocking API Response
    Given complex json data is available
    Then print the number of courses returned by API
    And print purchase amount
    And print title of first course
    And print all course titles and their respective prices
    And print number of copies sold by "RPA" course
    And verify if sum of all Course prices matches with Purchase Amount
