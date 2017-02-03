package com.mpay24.payment;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.mpay24.payment.data.Payment;
import com.mpay24.payment.data.PaymentData;
import com.mpay24.payment.type.DirectDebitPaymentType.Brand;
import com.mpay24.payment.type.RecurringCreditCardPaymentType;
import com.mpay24.payment.type.RecurringDirectDebitPaymentType;

public class TestRecurringPayments extends AbstractSeleniumTestcase {
	public final static Logger log = Logger.getLogger(TestRecurringPayments.class);

	@After
	public void tearDown() throws Exception {
		closeFirefox();
	}

	@Test
	public void testRecurringCreditCardPayment() throws ParseException, PaymentException {
		String customerId = "max.mustermann@gmail.com";
		deleteProfileForTest(customerId);
		mpay24.payment(getTestRecurringPaymentRequest(), getVisaTestData(), getCustomer(customerId, "Test Stored Payment"));
		mpay24.payment(getTestPaymentRequest(), new RecurringCreditCardPaymentType(), getCustomer(customerId, "Test Stored Payment"));
	}

	@Test
	public void testRecurringDirectDebitPayment() throws ParseException, PaymentException {
		String customerId = "max.mustermann@gmail.com";
		deleteProfileForTest(customerId);
		mpay24.payment(getTestRecurringPaymentRequest(), getDirectDebitTestData(Brand.HOBEX_AT), getCustomer(customerId, "Test Stored Payment"));
		mpay24.payment(getTestPaymentRequest(), new RecurringDirectDebitPaymentType(), getCustomer(customerId, "Test Stored Payment"));
	}

	@Test
	@Ignore
	public void testRecurringPaypalPayment() throws ParseException, PaymentException {
		fail("Paypal implementation unclear");
	}
	
	@Test
	public void testStoredPaymentDataList() throws ParseException, PaymentException {
		String customerId = "max.mustermann@gmail.com";
		deleteProfileForTest(customerId);
		mpay24.payment(getTestRecurringPaymentRequest(), getVisaTestData(), getCustomer(customerId, "Test Stored Payment"));

		List<PaymentData> storedPaymentDataList = mpay24.listCustomers(customerId, null, null, null);
		assertNotNull(storedPaymentDataList);
		assertTrue(storedPaymentDataList.size() > 0);
		
	}

	@Test
	public void testStorePaymentDataViaPaymentPanel() throws ParseException, PaymentException {
		String customerId = "max.mustermann@gmail.com";
		
		deleteProfileForTest(customerId);
		
		Payment response = mpay24.paymentPage(getTestPaymentRequest(1.0, null, null, true, null), getCustomer(customerId, "Max Mustermann"));
		assertSuccessfullResponse(response);
		
		doCreditCardPaymentOnPaymentPanel(response);
		
		List<PaymentData> storedPaymentDataList = mpay24.listCustomers(customerId, null, 0l, 10l);
		assertNotNull(storedPaymentDataList);
		assertTrue(storedPaymentDataList.size() > 0);
		
	}

	private void doCreditCardPaymentOnPaymentPanel(Payment response) {
		RemoteWebDriver driver = openFirefoxAtUrl(response.getRedirectLocation());
		driver.findElement(By.name("selCC|VISA")).click();
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("cardnumber")));
		
		driver.findElement(By.id("cardnumber")).sendKeys("4444333322221111");
		driver.findElement(By.id("cvc")).sendKeys("123");
		(new Select(driver.findElement(By.id("expiry-month")))).selectByValue("05");
		(new Select(driver.findElement(By.id("expiry-year")))).selectByValue(getYear());
		
		driver.findElement(By.id("right")).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("right")));
		driver.findElement(By.id("right")).click();
	}

	private String getYear() {
		return new SimpleDateFormat("yy").format(new Date());
	}

	private void deleteProfileForTest(String customerId) {
		try {
			mpay24.deleteCustomer(customerId, null);
		} catch (PaymentException e) {
			// OK if Profile does not exist
		}
	}

}
