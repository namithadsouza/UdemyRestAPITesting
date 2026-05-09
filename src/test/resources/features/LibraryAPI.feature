@Library
Feature: Library API Testing with Rest Assured

  Scenario Outline: Delete existing books if present for <author>
    Given books are deleted for "<author>"
    Examples:
      | author        |
      | NamithaDsouza |

  Scenario Outline: Library API - Add book <name>
    Given books are added with details "<name>" "<isbn>" "<aisle>" "<author>"
    Then response status should contain a msg "successfully added"
    And response message id should be combination of "<isbn>" and "<aisle>"
    But response should not be 401
    Examples:
      | name          | isbn  | aisle | author        |
      | Core Java     | cjava | 1     | NamithaDsouza |
      | Advanced Java | ajava | 2     | NamithaDsouza |

  Scenario Outline: Library API - Get book by Id <isbn> and <aisle>
    Given validate the books data is accurate by constructing id using "<isbn>" and "<aisle>"
    And validate book name is "<name>" and author is "<author>"
    Examples:
      | name          | isbn  | aisle | author        |
      | Core Java     | cjava | 1     | NamithaDsouza |
      | Advanced Java | ajava | 2     | NamithaDsouza |