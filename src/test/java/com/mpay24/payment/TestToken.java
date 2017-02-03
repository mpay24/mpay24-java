package com.mpay24.payment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.ParseException;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.mpay.soap.client.PaymentType;
import com.mpay24.payment.data.Payment;
import com.mpay24.payment.data.Token;
import com.mpay24.payment.data.TokenRequest;
import com.mpay24.payment.type.PaymentTypeData;
import com.mpay24.payment.type.TokenPaymentType;

public class TestToken extends AbstractSeleniumTestcase {
	public final static Logger log = Logger.getLogger(TestToken.class);

	@After
	public void tearDown() throws Exception {
		closeFirefox();
	}


	@Test
	public void testTokenGerman() throws ParseException, PaymentException {
		Token token = mpay24.token(getTestTokenRequest(null));
		assertEquals("REDIRECT", token.getReturnCode());
		assertNotNull(token.getApiKey());
		assertNotNull(token.getRedirectLocation());
		assertNotNull(token.getToken());
		
		RemoteWebDriver driver = openFirefoxAtUrl(token.getRedirectLocation());
		assertEquals("Kartennummer", driver.findElement(By.className("identifierlabel")).getText());
		assertEquals("Gültig bis", driver.findElement(By.className("expirylabel")).getText());
		assertEquals("Prüfnummer (CVN)", driver.findElement(By.className("cvclabel")).getText());
	}

	@Test
	public void testTokenEnglish() throws ParseException, PaymentException {
		Token token = mpay24.token(getTestTokenRequest("EN"));
		assertEquals("REDIRECT", token.getReturnCode());
		assertNotNull(token.getApiKey());
		assertNotNull(token.getRedirectLocation());
		assertNotNull(token.getToken());
		
		RemoteWebDriver driver = openFirefoxAtUrl(token.getRedirectLocation());
		assertEquals("Card Number", driver.findElement(By.className("identifierlabel")).getText());
		assertEquals("Valid thru", driver.findElement(By.className("expirylabel")).getText());
		assertEquals("CVC / CVN", driver.findElement(By.className("cvclabel")).getText());
	}

	@Test
	public void testTokenPaymentWith3DS() throws ParseException, PaymentException {
		Token token = mpay24.token(getTestTokenRequest(null));
		assertEquals("REDIRECT", token.getReturnCode());
		assertNotNull(token.getApiKey());
		assertNotNull(token.getRedirectLocation());
		assertNotNull(token.getToken());
		
		RemoteWebDriver driver = openFirefoxAtUrl(token.getRedirectLocation());
		driver.findElement(By.name("cardnumber")).sendKeys("4444333322221111");
		driver.findElement(By.name("cardnumber")).sendKeys(Keys.TAB);
		driver.findElement(By.id("cardYear")).sendKeys("1220");
		driver.findElement(By.id("cardYear")).sendKeys(Keys.TAB);
		driver.findElement(By.name("cvc")).sendKeys("123");
		driver.findElement(By.name("cvc")).sendKeys(Keys.TAB);

		Payment response = mpay24.payment(getTestPaymentRequest(), getTokenPaymentType(token.getToken()));

		assertEquals("REDIRECT", response.getReturnCode());
		assertNotNull(response.getmPayTid());
	}
	
	@Test
	public void testTokenPaymentWithout3DS() throws ParseException, PaymentException, InterruptedException {
		Token token = mpay24.token(getTestTokenRequest(null));
		assertEquals("REDIRECT", token.getReturnCode());
		assertNotNull(token.getApiKey());
		assertNotNull(token.getRedirectLocation());
		assertNotNull(token.getToken());
		
		RemoteWebDriver driver = openFirefoxAtUrl(token.getRedirectLocation());
		driver.findElement(By.name("cardnumber")).sendKeys("4444333322221111");
		driver.findElement(By.name("cardnumber")).sendKeys(Keys.TAB);
		driver.findElement(By.id("cardYear")).sendKeys("0520");
		driver.findElement(By.id("cardYear")).sendKeys(Keys.TAB);
		driver.findElement(By.name("cvc")).sendKeys("123");
		driver.findElement(By.name("cvc")).sendKeys(Keys.TAB);

		TimeUnit.SECONDS.sleep(1l);
		
		Payment response = mpay24.payment(getTestPaymentRequest(), getTokenPaymentType(token.getToken()));

		assertEquals("OK", response.getReturnCode());
		assertNotNull(response.getmPayTid());
	}

	private PaymentTypeData getTokenPaymentType(String token) {
		return new TokenPaymentType(token);
	}


	private TokenRequest getTestTokenRequest(String language) {
		TokenRequest tokenRequest = new TokenRequest();
		tokenRequest.setPaymentType(PaymentType.CC);
		tokenRequest.setTemplateSet("DEFAULT");
		tokenRequest.setStyle("DEFAULT");
		tokenRequest.setLanguage(language);
		return tokenRequest;
	}

	

}
