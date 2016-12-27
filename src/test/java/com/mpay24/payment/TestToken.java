package com.mpay24.payment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.ParseException;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.mpay.soap.client.PaymentType;
import com.mpay24.payment.data.Token;
import com.mpay24.payment.data.TokenRequest;

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

	private TokenRequest getTestTokenRequest(String language) {
		TokenRequest tokenRequest = new TokenRequest();
		tokenRequest.setPaymentType(PaymentType.CC);
		tokenRequest.setTemplateSet("DEFAULT");
		tokenRequest.setStyle("DEFAULT");
		tokenRequest.setLanguage(language);
		return tokenRequest;
	}



}
