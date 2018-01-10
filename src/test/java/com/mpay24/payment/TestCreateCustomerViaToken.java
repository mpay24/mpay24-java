package com.mpay24.payment;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.mpay.soap.client.PaymentType;
import com.mpay24.payment.data.Token;
import com.mpay24.payment.data.TokenRequest;
import com.mpay24.payment.type.PaymentTypeData;
import com.mpay24.payment.type.TokenPaymentType;

public class TestCreateCustomerViaToken extends AbstractSeleniumTestcase {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCreateCustomerViaToken() throws PaymentException {
		String customerId = "12345678987633";
		deleteProfileForTest(customerId);
		Token token = mpay24.token(getTestTokenRequest(customerId));
		assertEquals("REDIRECT", token.getReturnCode());
		assertNotNull(token.getApiKey());
		assertNotNull(token.getRedirectLocation());
		assertNotNull(token.getToken());
		
		RemoteWebDriver driver = openFirefoxAtUrl(token.getRedirectLocation());
		driver.findElement(By.name("cardnumber")).sendKeys("4444333322221111");
		driver.findElement(By.name("cardnumber")).sendKeys(Keys.TAB);
		driver.findElement(By.id("expiry")).sendKeys("1220");
		driver.findElement(By.id("expiry")).sendKeys(Keys.TAB);
		driver.findElement(By.name("cvc")).sendKeys("123");
		driver.findElement(By.name("cvc")).sendKeys(Keys.TAB);

		mpay24.createCustomer(getCustomerWithAddress(customerId, "Xenia Wiesbauer", "Grüngasse 16"), "x", getTokenPaymentType(token.getToken()));

	}
	
	private PaymentTypeData getTokenPaymentType(String token) {
		return new TokenPaymentType(token);
	}

	private TokenRequest getTestTokenRequest(String customerId) {
		TokenRequest tokenRequest = new TokenRequest();
		tokenRequest.setPaymentType(PaymentType.CC);
		tokenRequest.setTemplateSet("DEFAULT");
		tokenRequest.setStyle("DEFAULT");
		return tokenRequest;
	}

	private void deleteProfileForTest(String customerId) {
		try {
			mpay24.deleteCustomer(customerId, null);
		} catch (PaymentException e) {
			// OK if Profile does not exist
		}
	}

}
