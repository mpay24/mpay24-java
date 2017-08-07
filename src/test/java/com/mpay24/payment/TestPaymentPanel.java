package com.mpay24.payment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.mpay24.payment.data.Payment;
import com.mpay24.payment.data.PaymentRequest.Language;
import com.mpay24.payment.data.PaymentType;
import com.mpay24.payment.data.StylingOptions.Template;

public class TestPaymentPanel extends AbstractSeleniumTestcase {
	public final static Logger log = Logger.getLogger(TestPaymentPanel.class);

	@After
	public void tearDown() throws Exception {
		closeFirefox();
	}

	@Test
	public void testMostSimpleRedirectPayment() throws PaymentException {
		Payment response = mpay24.paymentPage(getTestPaymentRequest());
		assertSuccessfullResponse(response);
		RemoteWebDriver driver = openFirefoxAtUrl(response.getRedirectLocation());
		WebElement element = driver.findElementById("CC");
		assertNotNull(element.findElement(By.xpath("//input[@name='selCC|MASTERCARD']")));
		assertNotNull(element.findElement(By.xpath("//input[@name='selCC|VISA']")));
	}

	@Test
	public void testEmptyCustomerPayment() throws PaymentException {
		Payment response = mpay24.paymentPage(getTestPaymentRequest(), getCustomer());
		assertSuccessfullResponse(response);
	}

	@Test
	public void testUseProfilePayment() throws PaymentException {
		Payment response = mpay24.paymentPage(getTestPaymentRequest(1.0, null, null, true, null), getCustomer("max.mustermann@gmail.com", "Max Mustermann"));
		assertSuccessfullResponse(response);
	}

	@Test
	public void testCustomerIdPayment() throws PaymentException {
		Payment response = mpay24.paymentPage(getTestPaymentRequest(), getCustomer("max.mustermann@gmail.com", "Max Mustermann"));
		assertSuccessfullResponse(response);
	}

	@Test
	public void testRestrictPaymenttype() throws PaymentException {
		Payment response = mpay24.paymentPage(getTestPaymentRequest(getTestPaymentInclusionList(PaymentType.CreditCard)));
		assertSuccessfullResponse(response);

		RemoteWebDriver driver = openFirefoxAtUrl(response.getRedirectLocation());
		assertEquals("Kreditkarte", driver.findElementById("ptype_desc").getText());
	}

	@Test
	public void testRestrictPaymenttypeInvoice() throws PaymentException {
		Payment response = mpay24.paymentPage(getTestPaymentRequest(getTestPaymentInclusionList(PaymentType.DirectDebit)));
		assertSuccessfullResponse(response);

		RemoteWebDriver driver = openFirefoxAtUrl(response.getRedirectLocation());
		assertEquals("Lastschrift", driver.findElementById("ptype_desc").getText());
	}

	@Test
	public void testRestrictPaymenttypeAndBrand() throws PaymentException {
		Payment response = mpay24.paymentPage(getTestPaymentRequest(getTestPaymentInclusionList(PaymentType.CreditCard_Mastercard)));
		assertSuccessfullResponse(response);
		assertUIElement(response.getRedirectLocation(), "ptype_desc", "MasterCard");
	}

	@Test
	public void testShoppingCartAmounts() throws PaymentException {
		Payment response = mpay24.paymentPage(getTestPaymentRequest(120.0, null), getTestShoppingCart());
		assertSuccessfullResponse(response);
		RemoteWebDriver driver = openFirefoxAtUrl(response.getRedirectLocation());
		WebElement element = driver.findElementById("cart");
		assertEquals("Rabatt:", element.findElement(By.xpath("tfoot/tr[1]/th")).getText());
		assertEquals("-10,00", element.findElement(By.xpath("tfoot/tr[1]/td")).getText());

		assertEquals("Versandkosten:", element.findElement(By.xpath("tfoot/tr[2]/th")).getText());
		assertEquals("-5,00", element.findElement(By.xpath("tfoot/tr[2]/td")).getText());
		
		assertEquals("Zwischensumme:", element.findElement(By.xpath("tfoot/tr[3]/th")).getText());
		assertEquals("100,00", element.findElement(By.xpath("tfoot/tr[3]/td")).getText());
		
		assertEquals("20.00% USt.:", element.findElement(By.xpath("tfoot/tr[4]/th")).getText());
		assertEquals("20,00", element.findElement(By.xpath("tfoot/tr[4]/td")).getText());
				
		assertEquals("Gesamtpreis:", element.findElement(By.xpath("tfoot/tr[5]/th")).getText());
		assertEquals("EUR 120,00", element.findElement(By.xpath("tfoot/tr[5]/td")).getText());

	}
	
	@Test
	public void testShoppingCartItems() throws PaymentException {
		Payment response = mpay24.paymentPage(getTestPaymentRequest(120.0, null), getTestShoppingCart(getTestShoppingCartItemList()));
		assertSuccessfullResponse(response);
		RemoteWebDriver driver = openFirefoxAtUrl(response.getRedirectLocation());
		
		WebElement element = driver.findElementById("cart");
		assertEquals("Rabatt:", element.findElement(By.xpath("tfoot/tr[1]/th")).getText());
	}
	
	@Test
	public void testLanguageEnglish() throws PaymentException {
		Payment response = mpay24.paymentPage(getTestPaymentRequest(1.0, null, Language.EN), getTestShoppingCart(getTestShoppingCartItemList()));
		assertSuccessfullResponse(response);
		
		RemoteWebDriver driver = openFirefoxAtUrl(response.getRedirectLocation());
		WebElement element = driver.findElementById("cart");
		assertEquals("No.", element.findElement(By.xpath("thead/tr[1]/th[1]")).getText());
		assertEquals("Prod. No.", element.findElement(By.xpath("thead/tr[1]/th[2]")).getText());
		assertEquals("Product Name", element.findElement(By.xpath("thead/tr[1]/th[3]")).getText());
		assertEquals("Amount ordered", element.findElement(By.xpath("thead/tr[1]/th[4]")).getText());
		assertEquals("Price/Item", element.findElement(By.xpath("thead/tr[1]/th[5]")).getText());
		assertEquals("Total", element.findElement(By.xpath("thead/tr[1]/th[6]")).getText());
		assertEquals("Discount:", element.findElement(By.xpath("tfoot/tr[1]/th")).getText());
		assertEquals("Shipping Costs:", element.findElement(By.xpath("tfoot/tr[2]/th")).getText());
		assertEquals("Subtotal:", element.findElement(By.xpath("tfoot/tr[3]/th")).getText());
		assertEquals("Order Total:", element.findElement(By.xpath("tfoot/tr[5]/th")).getText());
	}
	
	@Test
	public void testTemplateDefault() throws PaymentException {
		Payment response = mpay24.paymentPage(getTestPaymentRequest(), getStylingOptions(Template.DEFAULT));
		assertSuccessfullResponse(response);
		
		RemoteWebDriver driver = openFirefoxAtUrl(response.getRedirectLocation());
		assertEquals("/app/checkout/css/DEFAULT_WEB.css", removeDomain(driver.findElement(By.xpath("//head/link[@rel='stylesheet']")).getAttribute("href")));
	}

	@Test
	public void testTemplateMobile() throws PaymentException {
		Payment response = mpay24.paymentPage(getTestPaymentRequest(), getStylingOptions(Template.MOBILE));
		assertSuccessfullResponse(response);
		
		RemoteWebDriver driver = openFirefoxAtUrl(response.getRedirectLocation());
		assertEquals("/app/checkout/css/MOBILE_WEB.css", removeDomain(driver.findElement(By.xpath("//head/link[@rel='stylesheet']")).getAttribute("href")));
	}
	
	@Test
	public void testRemoveDomain() {
		assertEquals("/app/checkout/css/MOBILE_WEB.css", removeDomain("https://test.mpay24.com/app/checkout/css/MOBILE_WEB.css"));
	}
	
	@Test
	public void testShippingAddress() throws PaymentException {
		Payment response = mpay24.paymentPage(getTestPaymentRequest(), getCustomerWithAddress(null));
		assertSuccessfullResponse(response);
		
		RemoteWebDriver driver = openFirefoxAtUrl(response.getRedirectLocation());
		driver.findElement(By.name("selCC|VISA")).click();
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("cardnumber"))); 
		assertEquals("Testperson-de Approved", driver.findElement(By.name("BillingAddr/Name")).getAttribute("value"));
		assertEquals("Hellersbergstraße 14", driver.findElement(By.name("BillingAddr/Street")).getAttribute("value"));
		assertNotExistent(driver, By.name("BillingAddr/Street2"));
		assertEquals("41460", driver.findElement(By.name("BillingAddr/Zip")).getAttribute("value"));
		assertEquals("Neuss", driver.findElement(By.name("BillingAddr/City")).getAttribute("value"));
		assertEquals("Ankeborg", driver.findElement(By.name("BillingAddr/State")).getAttribute("value"));
		assertEquals("Deutschland", driver.findElement(By.name("BillingAddr/Country/@Code")).findElement(By.xpath("option[@selected='']")).getText());
	}

	@Test
	public void testShippingAddressWithStreet2() throws PaymentException {
		Payment response = mpay24.paymentPage(getTestPaymentRequest(), getCustomerWithAddress("Coconut 3"));
		assertSuccessfullResponse(response);
		
		RemoteWebDriver driver = openFirefoxAtUrl(response.getRedirectLocation());
		driver.findElement(By.name("selPAYPAL|PAYPAL")).click();
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.elementToBeClickable(By.name("BillingAddr/Street"))); 
		assertEquals("Testperson-de Approved", driver.findElement(By.name("BillingAddr/Name")).getAttribute("value"));
		assertEquals("Hellersbergstraße 14", driver.findElement(By.name("BillingAddr/Street")).getAttribute("value"));
		assertEquals("Coconut 3", driver.findElement(By.name("BillingAddr/Street2")).getAttribute("value"));
		assertEquals("41460", driver.findElement(By.name("BillingAddr/Zip")).getAttribute("value"));
		assertEquals("Neuss", driver.findElement(By.name("BillingAddr/City")).getAttribute("value"));
		assertEquals("Ankeborg", driver.findElement(By.name("BillingAddr/State")).getAttribute("value"));
		assertEquals("Deutschland", driver.findElement(By.name("BillingAddr/Country/@Code")).findElement(By.xpath("option[@selected='']")).getText());
	}

	@Test
	public void testFullExample() throws PaymentException {
		Payment response = mpay24.paymentPage(getTestPaymentRequest(), getCustomerWithAddress(), getShoppingCart(), getStylingOptions(Template.MOBILE));
		assertSuccessfullResponse(response);
	}

}
