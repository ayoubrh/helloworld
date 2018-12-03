package org.hellowworld.cucumber.steps;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.hellowworld.cucumber.utils.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class HelloWorld {
	public WebDriver driver;
	private LocalDateTime dateCreation;
	
	@Before()
    public void setUp(){
    	driver = Utils.getWebDriver();
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
    }
	
	@Given("^Ouverture du formulaire \"([^\"]*)\"$")
    public void ouverture_formulaire(String url) throws Throwable {

        driver.navigate().to(url);
    }
	
	@When("^Renseigner le champ avec la valeur (.+)$")
    public void Saisie_message(String msg) throws Throwable {
		
		driver.findElement(By.id("forhelloworldmessage")).sendKeys(msg);
    }
	
	@When("^Click sur le bouton envoyer$")
    public void click() throws Throwable {
		driver.findElement(By.cssSelector("button.btn.btn-default")).click();
		this.dateCreation = LocalDateTime.now();
		System.out.println("dateCreation : "+dateCreation);
    }
    

    @Then("^verification du message : (.+)$")
    public void messageShouldBeDisplayed(String msg) throws Throwable {
    	
    	List<WebElement> rows = driver.findElements(By.cssSelector("tbody > tr"));
    	
    	System.out.println("ROWS : "+rows.size());
    	String msgR = rows.get(rows.size()-1).findElement(By.cssSelector("td:nth-of-type(1)")).getText();
    	System.out.println("msgR : "+msgR);
    	String dateR = rows.get(rows.size()-1).findElement(By.cssSelector("td:nth-of-type(2)")).getAttribute("id");
    	System.out.println("dateR : "+dateR);
    	//DateTimeFormatter df = DateTimeFormatter.ofPattern("MMM dd, yyyy, HH:mm:ss a");

//    	Instant instant = Instant.parse(dateR);
//
//        System.out.println(instant);

        //get date time only
        LocalDateTime result = LocalDateTime.parse(dateR);

        System.out.println(result);
        
    	if(!msgR.equals(msg)) {
        	System.out.println("Le message "+msg+" n'existe pas");
        	fail("Le message "+msg+" n'existe pas dans la liste");
        } else {
        	if(!result.equals(this.dateCreation)) {
        		System.out.println("La date de création n'est pas correcte ");
            	fail("La date de création n'est pas correcte");
        	}
        }
    }
	
	
	@After()
    public void tearDown(Scenario scenario){
        driver.quit();
    }

}
