package com.mpay24.payment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

public class AbstractSeleniumTestcase extends AbstractTestCase {

	FirefoxDriver driver;
//	ChromeDriver chromeDriver;

	protected RemoteWebDriver openFirefoxAtUrl(String url) {
		String geckoDriver = getGeckoDriver();
		if (geckoDriver != null)
			System.setProperty("webdriver.gecko.driver", geckoDriver);
		driver = new FirefoxDriver();
		driver.get(url);
		return driver;

//		driver = new ChromeDriver();
//		driver.get(url);
//		return driver;
	}
	
	// Define your desired webdriver  Run -> Run Configurations... -> (JUnit/mpay24-java) -> Environment
	protected String getGeckoDriver() {
		return System.getenv("GECKO_DRIVER");
	}

	protected void closeFirefox() {
		if (driver != null) driver.quit();
	}

	protected void assertUIElement(String url, String id, String value) {
		RemoteWebDriver driver = openFirefoxAtUrl(url);
		assertEquals(value, driver.findElementById(id).getText());
	}

	protected void assertNotExistent(RemoteWebDriver driver, By by) {
		try {
			driver.findElement(by);
			fail("Element with name '" + by + "' found");
		} catch (NoSuchElementException e) {
			; // That is what we expect
		}
	}


}
