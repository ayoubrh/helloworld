package org.helloworld.cucumber.utils;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.DriverVersion;
import io.github.bonigarcia.wdm.FirefoxDriverManager;
import io.github.bonigarcia.wdm.InternetExplorerDriverManager;

/**
 * The {@link Utils} class.
 *
 *
 */
public class Utils {



    /**
     * @param browser
     * @param role
     * @return
     */
    public static WebDriver getWebDriver() {
    	
        String browser = System.getProperty("browser", Browser.CHROME.name());
        System.out.println("browser : "+browser);
        Boolean grid = Boolean.valueOf(System.getProperty("grid", "false"));
        System.out.println("grid : "+grid);
        if(grid) {
        	System.out.println("Avec Selenium Grid");
        	return getWebDriverGrid(Browser.valueOf(browser));
        } else {
        	System.out.println("Sans Selenium Grid");
        	return getWebDriver(Browser.valueOf(browser));
        }
    }

    /**
     * @param browser
     * @return
     */
    public static WebDriver getWebDriver(Browser browser) {
        WebDriver driver;
        switch (browser) {
        case CHROME:
            driver = getWebDriverChrome();
            break;
        case FIREFOX:
            driver = getWebDriverFirefox();
            break;
        case INTERNETEXPLORER:
            driver = getWebDriverInternetExplorer();
            break;
        default:
            driver = getWebDriverChrome();
            break;
        }

        return driver;
    }
    
    
    /**
     * @param browser
     * @return
     */
    public static WebDriver getWebDriverGrid(Browser browser) {
    	String gridUrl = System.getProperty("gridUrl", "http://selenium:4444/wd/hub");
    	System.out.println("gridUrl : "+gridUrl);
        WebDriver driver;
        switch (browser) {
        case CHROME:
            driver = getWebDriverChromeGrid(gridUrl);
            break;
        case FIREFOX:
            driver = getWebDriverFirefoxGrid(gridUrl);
            break;
        case INTERNETEXPLORER:
            driver = getWebDriverInternetExplorerGrid(gridUrl);
            break;
        default:
            driver = getWebDriverFirefoxGrid(gridUrl);
            break;
        }

        return driver;
    }



    public static WebDriver getWebDriverChrome() {
    	
        ChromeDriverManager.getInstance().version(DriverVersion.LATEST.name()).setup();
        DesiredCapabilities capability = DesiredCapabilities.chrome();
        capability.setJavascriptEnabled(true);
        ChromeOptions options = new ChromeOptions();
        if (!Boolean.valueOf(System.getProperty("zap.skip", "true"))) {
            options.addArguments("--proxy-server=localhost:8090");
        }

        options.addArguments("--start-maximized");
        options.addArguments("--disable-web-security");
        options.addArguments("–-allow-running-insecure-content");
        options.addArguments("--lang=fr");
        capability.setCapability(ChromeOptions.CAPABILITY, options);

        WebDriver driver = new ChromeDriver(capability);
        return driver;
    }

    /**
     * @param key
     *            the key
     * @param value
     *            the value
     * @throws InterruptedException
     */
    private static void addHeaderChrome(WebDriver driver, String key, String value, int numAdd) {
        driver.findElement(By.xpath("//button[@tooltip='Add New']")).click();

        WebElement actionElem = driver.findElement(By.id("action_" + numAdd));
        Select select = new Select(actionElem);
        select.selectByVisibleText("Add");

        WebElement nameElem = driver.findElement(By.id("cmh_name_" + numAdd).name("name"));
        nameElem.sendKeys(key);

        WebElement valueElem = driver.findElement(By.id("cmh_value_" + numAdd).name("value"));
        valueElem.sendKeys(value);

        driver.findElement(By.id("btn_save_" + numAdd)).click();
    }

    public static WebDriver getWebDriverFirefox() {
    	
    	
    	
        FirefoxDriverManager.getInstance().version(DriverVersion.LATEST.name()).setup();
        DesiredCapabilities capability = DesiredCapabilities.firefox();
        capability.setJavascriptEnabled(true);

        FirefoxOptions options = new FirefoxOptions();
        if (!Boolean.valueOf(System.getProperty("zap.skip", "true"))) {
            // FIXME
            options.addArguments("--proxy-server=localhost:8090");
        }

        FirefoxProfile profile = new FirefoxProfile();

        return new FirefoxDriver(capability);
    }
    
    public static WebDriver getWebDriverInternetExplorer() {
   	
	    
    	InternetExplorerDriverManager.getInstance().version(DriverVersion.LATEST.name()).setup();

    	
    	DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();


		ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
		
		ieCapabilities.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);


		WebDriver driver = new InternetExplorerDriver(ieCapabilities);


    	return driver;
    }
    
    
    
    public static WebDriver getWebDriverInternetExplorerGrid(String gridUrl) {
		
		DesiredCapabilities capability = DesiredCapabilities.internetExplorer();
	       capability.setBrowserName("internetExplorer");
	       capability.setPlatform(Platform.WIN10);
	       capability.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
	       capability.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
//	       capability.setCapability("seleniumProtocol", "WebDriver");
//	       capability.setCapability("maxInstances", "5");
	      // capability.setVersion("6.1");
	       WebDriver driver = null;
		try {
			
			driver = new RemoteWebDriver(
			   new URL(gridUrl), capability);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	return driver;
    }
    
    public static WebDriver getWebDriverFirefoxGrid(String gridUrl) {
		
		DesiredCapabilities capability = DesiredCapabilities.firefox();
	       capability.setBrowserName("firefox");
	       capability.setPlatform(Platform.WIN10);
//	       capability.setCapability("seleniumPrtocol", "WebDriver");
//	       capability.setCapability("maxInstances", "5");
	      // capability.setVersion("6.1");
	       WebDriver driver = null;
		try {
			driver = new RemoteWebDriver(
			   new URL(gridUrl), capability);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	return driver;
    }
    
    
    
    public static WebDriver getWebDriverChromeGrid(String gridUrl) {
		
		DesiredCapabilities capability = DesiredCapabilities.chrome();
	       capability.setBrowserName("chrome");
	       capability.setPlatform(Platform.WIN10);
//	       capability.setCapability("seleniumProtocol", "WebDriver");
//	       capability.setCapability("maxInstances", "5");
	      // capability.setVersion("6.1");
	       WebDriver driver = null;
		try {
			driver = new RemoteWebDriver(
			   new URL(gridUrl), capability);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	return driver;
    }

}
