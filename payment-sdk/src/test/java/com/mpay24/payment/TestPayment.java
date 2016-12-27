package com.mpay24.payment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.ParseException;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

import com.mpay24.payment.data.Payment;
import com.mpay24.payment.type.DirectDebitPaymentType.Brand;

public class TestPayment extends AbstractTestCase {
	public final static Logger log = Logger.getLogger(TestPayment.class);

	@Test
	public void testDirectDebitB4PPayment() throws PaymentException {
		Payment response = mpay24.payment(getTestPaymentRequest(), getDirectDebitTestData(Brand.B4P, "DE09100100101234567893", "PBNKDEFFXXX"), getCustomer());

		assertEquals("OK", response.getReturnCode());
		assertNotNull(response.getmPayTid());
	}

	@Test
	public void testDirectDebitHobexAtPayment() throws PaymentException {
		Payment response = mpay24.payment(getTestPaymentRequest(), getDirectDebitTestData(Brand.HOBEX_AT), getCustomer());

		assertEquals("OK", response.getReturnCode());
		assertNotNull(response.getmPayTid());
	}

	@Test
	public void testDirectDebitHobexDePayment() throws PaymentException {
		Payment response = mpay24.payment(getTestPaymentRequest(), getDirectDebitTestData(Brand.HOBEX_DE), getCustomer());

		assertEquals("OK", response.getReturnCode());
		assertNotNull(response.getmPayTid());
	}

	@Test
	public void testDirectDebitHobexNlPayment() throws PaymentException {
		Payment response = mpay24.payment(getTestPaymentRequest(), getDirectDebitTestData(Brand.HOBEX_NL), getCustomer());

		assertEquals("OK", response.getReturnCode());
		assertNotNull(response.getmPayTid());
	}

	@Test
	public void testDirectDebitBillpayPayment() throws PaymentException {
		Payment response = mpay24.payment(getTestPaymentRequest(), getDirectDebitTestData(Brand.BILLPAY), getCustomer());

		assertEquals("REDIRECT", response.getReturnCode());
		assertNotNull(response.getmPayTid());
	}

	@Test
	public void testDirectDebitAtosPayment() throws PaymentException {
		Payment response = mpay24.payment(getTestPaymentRequest("987654321", 1l), getDirectDebitTestData(Brand.ATOS));

		assertEquals("OK", response.getReturnCode());
		assertNotNull(response.getmPayTid());
	}

	@Test
	public void testVisaPayment() throws ParseException, PaymentException {
		Payment response = mpay24.payment(getTestPaymentRequest(), getVisaTestData());

		assertEquals("OK", response.getReturnCode());
		assertNotNull(response.getmPayTid());
	}

	@Test
	public void testMastercardPayment() throws ParseException, PaymentException {
		Payment response = mpay24.payment(getTestPaymentRequest(), getMastercardTestData());

		assertEquals("OK", response.getReturnCode());
		assertNotNull(response.getmPayTid());
	}

	@Test
	@Ignore
	public void testAmexPayment() throws ParseException, PaymentException {
		Payment response = mpay24.payment(getTestPaymentRequest("987654321", 1l), getAmexTestData());

		assertEquals("OK", response.getReturnCode());
		assertNotNull(response.getmPayTid());
	}

	@Test
	public void testDinersPayment() throws ParseException, PaymentException {
		Payment response = mpay24.payment(getTestPaymentRequest(), getDinersTestData());

		assertEquals("OK", response.getReturnCode());
		assertNotNull(response.getmPayTid());
	}

	@Test
	public void testJcbPayment() throws ParseException, PaymentException {
		Payment response = mpay24.payment(getTestPaymentRequest(), getJcbTestData());

		assertEquals("OK", response.getReturnCode());
		assertNotNull(response.getmPayTid());
	}

	@Test
	public void testEpsPayment() throws ParseException, PaymentException {
		Payment response = mpay24.payment(getTestPaymentRequest("555", 1l), getEpsData());

		assertEquals("REDIRECT", response.getReturnCode());
		assertNotNull(response.getmPayTid());
	}

	@Test
	public void testGiropayPayment() throws ParseException, PaymentException {
		Payment response = mpay24.payment(getTestPaymentRequest("555", 1l), getGiropayData());

		assertEquals("REDIRECT", response.getReturnCode());
		assertNotNull(response.getmPayTid());
	}

	@Test
	public void testSofortPayment() throws ParseException, PaymentException {
		Payment response = mpay24.payment(getTestPaymentRequest("555", 1l), getSofortData());

		assertEquals("REDIRECT", response.getReturnCode());
		assertNotNull(response.getmPayTid());
	}

	@Test
	public void testPaypalPayment() throws ParseException, PaymentException {
		Payment response = mpay24.payment(getTestPaymentRequest("555", 1l), getPaypalData());

		assertEquals("REDIRECT", response.getReturnCode());
		assertNotNull(response.getmPayTid());
	}

	@Test
	public void testPayboxPayment() throws ParseException, PaymentException {
		Payment response = mpay24.payment(getTestPaymentRequest("555", 1l), getPayboxData());

		assertEquals("OK", response.getReturnCode());
		assertNotNull(response.getmPayTid());
	}

	@Test
	public void testPaysafecardPayment() throws ParseException, PaymentException {
		Payment response = mpay24.payment(getTestPaymentRequest("555", 1l), getPaysafecardData());

		assertEquals("REDIRECT", response.getReturnCode());
		assertNotNull(response.getmPayTid());
	}

	@Test
	public void testInvoiceBillpayPayment() throws ParseException, PaymentException {
		Payment response = mpay24.payment(getTestPaymentRequest("666", 100l), getInvoiceBillpayTestData(), getCustomer());

		assertEquals("REDIRECT", response.getReturnCode());
		assertNotNull(response.getmPayTid());
	}

	@Test
	public void testInvoiceKlarnaPayment() throws ParseException, PaymentException {
		Payment response = mpay24.payment(getTestPaymentRequest("555", 290l), getInvoiceKlarnaTestData(), getCustomerWithAddress(null), getShoppingCart());

		assertEquals("OK", response.getReturnCode());
		assertNotNull(response.getmPayTid());
	}

	@Test
	public void testInstallmentBillpayPayment() throws ParseException, PaymentException {
		Payment response = mpay24.payment(getTestPaymentRequest("666", 101l),
						getInstallmentBillpayTestData(), getCustomerWithAddress(null));

		assertEquals("REDIRECT", response.getReturnCode());
		assertNotNull(response.getmPayTid());
	}

	@Test
	public void testInstallmentKlarnaPayment() throws ParseException, PaymentException {
		Payment response = mpay24.payment(getTestPaymentRequest("555", 29400l), getInstallmentKlarnaTestData(), getCustomerWithAddress(null), getShoppingCart());

		assertEquals("OK", response.getReturnCode());
		assertNotNull(response.getmPayTid());
	}


}
