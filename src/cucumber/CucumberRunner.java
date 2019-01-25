package cucumber;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
		features = "src/cucumber/features/",
		glue = "SearchStepDefinitions",
		monochrome = true, 
		plugin = {"pretty", "html:target/cucumber-reports", "json:target/cucumber-reports/Cucumber.json", "junit:target/cucumber-reports/Cucumber.xml"}
		)

public class CucumberRunner {

}
