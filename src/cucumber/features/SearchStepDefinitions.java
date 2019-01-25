package cucumber.features;

import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class SearchStepDefinitions {
	
	//Notes to the Reviewer
	//Since it's a test project, some of the functionalities have been left behind like narrowing the search, checking the categories on zero results, same search results with capital and lower keys and other page elements on the search page. I just added a few major ones for you to get the idea and to assess my technical skills.
	//Some elements have been used multiple times which resulted a code repetation. On a business level automation script, page object should be used.
	
	public String sonyHomepage = "https://www.sony.com";	
    private static WebDriver driver = null;
    private static WebDriverWait wait = null;    
    private static List<WebElement> searchResults = null;
    
    //Getting ready for the test run
    @Before
    public void testSetup() {
    	System.setProperty("webdriver.chrome.driver", "chromedriver/chromedriver.exe");		
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		System.out.println("Test is running...");
    }
    
    //Closing the browser
    @After
    public void testClosure() {
    	System.out.println("Test run completed");
    	driver.close();
    }
	
	@Given("I am on the Sony homepage")
	public void navigateToSonyHomepage() {
		driver.get(sonyHomepage);
		System.out.println("User is on the Sony homepage");
	}
	
	//Starting the search via Sony.com directly rather than visiting the search page with a keyword or empty string. Common usage would be a redirection via Sony homepage.
	@Given("I have navigated to Sony search page via ([^\"]*) on a query string")
	public void navigateToSonySearchPageViaDirectLink(String value) {
		driver.get(sonyHomepage + "/search?query=" + value);
		System.out.println("User is on the Sony search page for the (" + value + ") keyword");
	}
	
	
	@When("I click on the Search Sony button") //To open the search pane
	public void clickOnSearchButton() {
		driver.findElement(By.id("searchBoxLabel")).click();
		System.out.println("User clicked on the Search Sony button");
	}

	@And("I type the ([^\\\"]*) on the search field")
	public void typeTheKeyword(String value) {
		driver.findElement(By.id("nav-search-input")).sendKeys(value);
		System.out.println("User typed the (" + value + ") keyword on the search field");
	}
	
	//A valid search that has a relevant result should list at least one item in the results list
	@Then("I should see at least one result for the ([^\"]*) on the homepage search pane")
	public void checkResultCountOnHomepage(String value) {
		wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("search-result"))); //Waiting for a search result to appear on the list to continue to test
		searchResults = driver.findElements(By.className("search-result")); //Adding the displayed results to our web element list
		Integer displayedResultCount = searchResults.size(); //Getting size so that we can check if we have at least 1 result for that keyword
		Assert.assertNotEquals("Result is bigger than zero", 0, displayedResultCount, 0);		
		System.out.println("Keyword (" + value + ") has " + displayedResultCount + " results");
		System.out.println("TEST OK");
	    
	}
	
	@Then("I should see at least one result for the ([^\"]*) on the search page")
	public void checkResultCountOnSearchPage(String value) {
		wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("results-list"))); //Waiting for results list to appear to continue to test
		
		searchResults = driver.findElements(By.className("results-p")); //This class name is used by each result item
		Integer displayedResultCount = searchResults.size(); //Getting size of displayed results. It contains only current page, so in an extensive scenario, we'd need to check for "Load More" function or pagination		
		Integer totalResultCount = Integer.parseInt(driver.findElement(By.cssSelector("#page-main-content > section.search-results > div.container.grid > div > div.span8.m-span6 > div.results-summary > div > p > span.bold-dark.p3-bold")).getText()); //Getting the results number message so that we can report the total number of results. Casting it to int is not necessary though		
		
		Assert.assertNotEquals("Result is bigger than zero", 0, displayedResultCount, 0);		
		
		System.out.println("Keyword (" + value + ") has " + displayedResultCount + " results in the current page and the total number of results are " + totalResultCount);
		System.out.println("TEST OK");
	    
	}
	
	//Result relevancy is a tricky business. While it can check for the keyword, it may not be useful when you use more than one keywords since the result that Sony displayes is not like "contain" based
	@And("Results should be relevant with the ([^\"]*)")
	public void checkResultRelevancy(String value) {
		Boolean keywordRelevant = false;
		for (WebElement we : searchResults) {
		    String eText = we.getText().toLowerCase();
		    if (eText.contains(value.toLowerCase())) {
		    	keywordRelevant = true;
		    }
		    //System.out.println(eText);
		}
		//When we find our keyword in the list of the results even once, I pass the test. It can be improved.
		Assert.assertTrue("Keyword (" + value + ") is relevant with the result", keywordRelevant);
		System.out.println("At least one presence of the (" + value + ") keyword has been found in the results");
		System.out.println("TEST OK");
	}
	
	@And("I press enter to start the search")
	public void pressEnter() {
		driver.findElement(By.id("nav-search-input")).sendKeys(Keys.ENTER);
		System.out.println("User pressed the Enter button to start the search");
	}
	
	@Then("I should be redirected to the search page")
	public void verifySonySearchPage() {
		Assert.assertEquals("User is redirected to Sony Search page", "Search Results | Sony US", driver.getTitle()); //Just checking the page title
		System.out.println("User is redirected to the Sony Search page");
		System.out.println("TEST OK");
	}
	
	@And("I should see a zero result message")
	public void checkZeroResultMessage() {
		Assert.assertTrue("Zero result message displayed", driver.findElement(By.className("zero-results")).getText().contains("returned 0 results")); //Checking that if zero results message is displayed
		System.out.println("Zero result message has been displayed on the page");
		System.out.println("TEST OK");
	}
	
	//Just an extention for zero result page test
	@And("I should see a suggested categories section")
	public void checkSuggestedCategoriesSection() {
		Assert.assertTrue("Suggested categories section displayed", driver.findElement(By.cssSelector("#page-main-content > section.search-results > div.zero-results-container.container.level-1 > div > p.t6.light")).getText().contains("What are you looking for?"));
		System.out.println("Suggested Categories section has been displayed on the page");
		System.out.println("TEST OK");
	}
	
	//Just an extention for zero result page test
	@And("I should see a search tips section")
	public void checkSearchTipsSection() {
		Assert.assertTrue("Search tips section displayed", driver.findElement(By.cssSelector("#page-main-content > section.search-results > div.zero-results-container.container.level-1 > div > p.t6.search-tips-title")).getText().contains("Search Tips"));
		System.out.println("Search Tips section has been displayed on the page");
		System.out.println("TEST OK");
	}
	
	@When("I click on the clear search button") //To remove the text of latest search. It's better to use this button rather than clearing text manually so that we also check the functionality of clearing button
	public void clickOnClearSearchButton() {
		driver.findElement(By.className("clear-search")).click();
		System.out.println("User clicked on the clear search button/icon");
	}
	
	@When("I click on the search field of the Sony search page") //Otherwise, the search field is hidden. There can be another more optimal way, it can be investigated.
	public void clickOnSearchField() {
		driver.findElement(By.cssSelector("#page-main-content > section.search-results > div.bar-tone > div > div > form > div.span10.m-span6 > div.search-left > input")).click();
		System.out.println("User clicked on the search field");
	}
	
	@And("I type the ([^\\\"]*) on the search field of the Sony search page")
	public void typeTheKeywordForSearchPage(String value) {
		driver.findElement(By.cssSelector("#page-main-content > section.search-results > div.bar-tone > div > div > form > div.span10.m-span6 > div.search-left > input")).sendKeys(value);
		System.out.println("User typed the (" + value + ") keyword on the search field");
	}
	
	@And("I click on the Search button of the Sony search page")
	public void clickTheSearchButtonOfSearchPage() {
		driver.findElement(By.cssSelector("#page-main-content > section.search-results > div.bar-tone > div > div > form > div.span2.hidden-phone > button")).click();
		System.out.println("User clicked on the Search button");
	}
}