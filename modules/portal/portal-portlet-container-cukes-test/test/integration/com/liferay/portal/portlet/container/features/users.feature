Feature: Add User

  Scenario: Adding a User

    Given I have a user
    When I create the user
    Then The user can be retrieved by service layer
