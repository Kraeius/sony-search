Feature: Sony Search Function

Scenario Outline: Valid Search on the Sony Homepage with 1 Keyword
Given I am on the Sony homepage
When I click on the Search Sony button
And I type the <Keyword> on the search field
Then I should see at least one result for the <Keyword> on the homepage search pane
And Results should be relevant with the <Keyword>

Examples:
|Keyword|
|playstation|
|computer|
|television|

Scenario Outline: Valid Search on the Sony Homepage with 2 Keywords
Given I am on the Sony homepage
When I click on the Search Sony button
And I type the <Keyword> on the search field
Then I should see at least one result for the <Keyword> on the homepage search pane

Examples:
|Keyword|
|playstation gamepad|
|laptop battery|
|sony headphone|

Scenario Outline: Valid Search on Homepage with a Product ID
Given I am on the Sony homepage
When I click on the Search Sony button
And I type the <ProductID> on the search field
Then I should see at least one result for the <ProductID> on the homepage search pane

Examples:
|ProductID|
|DVP-SR510H|

Scenario: Empty Search on the Sony Homepage
Given I am on the Sony homepage
When I click on the Search Sony button
And I press enter to start the search
Then I should be redirected to the search page
And I should see a zero result message
And I should see a suggested categories section
And I should see a search tips section

Scenario Outline: Direct Valid Search via a Query String
Given I have navigated to Sony search page via <Keyword> on a query string
Then I should see at least one result for the <Keyword> on the search page
And Results should be relevant with the <Keyword>

Examples:
|Keyword|
|aibo|
|smartwatch|
|Xperia|

Scenario Outline: Conducting a New Valid Search on the Sony Search Page
Given I have navigated to Sony search page via <Keyword> on a query string
When I click on the clear search button
And I click on the search field of the Sony search page
And I type the <New Keyword> on the search field of the Sony search page
And I click on the Search button of the Sony search page
Then I should see at least one result for the <New Keyword> on the search page
And Results should be relevant with the <New Keyword>

Examples:
|Keyword|New Keyword|
|aibo|playstation|