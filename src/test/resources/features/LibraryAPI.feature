Feature: Library API Testing with Rest Assured

 # @namitha
  Scenario Outline : Prerequisite - Delete existing books if present
    Given books are deleted for <author>
    Examples:
      | author  |
      | Namitha |


  Scenario Outline: Library API - Add book
    Given books are added with details <name> <isbn> <aisle> <author>
    Examples:
      | name          | isbn  | aisle | author  |
      | Core Java     | cjava | 1     | Namitha |
      | Advanced Java | ajava | 2     | Namitha |

  Scenario Outline: Library API - Get book by Id
    Given validate the books data is accurate by constructing id using <isbn> and <aisle>
    And validate book name is <name> and author is <author>
    Examples:
      | name          | isbn  | aisle | author  |
      | Core Java     | cjava | 1     | Namitha |
      | Advanced Java | ajava | 2     | Namitha |

  Scenario Outline: Library API - Get book by Author
    Given get book by <author> and validate the books data is accurate
    Examples:
      | author  |
      | Namitha |

  Scenario Outline: Library API - delete book by Author
    Given isbn <isbn> and aisle <aisle> delete the books by using id value
    Examples:
      | isbn  | aisle |
      | cjava | 1     |
      | ajava | 2     |
