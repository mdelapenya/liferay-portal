Feature: Upload a file in a site

  Scenario: Upload a file in a site using Document Library remote services

    Given I have a group, a user with permissions in the group, and a file
    When I upload a file with user' credentials
    Then the file is stored in the site
