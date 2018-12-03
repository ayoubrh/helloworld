package org.hellowworld.cucumber;

import org.junit.runner.RunWith;
import cucumber.api.junit.Cucumber;
import cucumber.api.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin =  {"pretty", "html:target/results/html","json:target/results/JSON/cucumber.json"},
        features = {"src/test/resources/features"},
        glue = {""}
)
public class AppIT{
}
