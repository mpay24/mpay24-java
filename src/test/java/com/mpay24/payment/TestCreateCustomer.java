package com.mpay24.payment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.ParseException;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Test;

import com.mpay24.payment.data.PaymentData;
import com.mpay24.payment.type.DirectDebitPaymentType.Brand;

public class TestCreateCustomer extends AbstractTestCase {
	public final static Logger log = Logger.getLogger(TestCreateCustomer.class);

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testCreateCreditCardCustomerWithoutAddress() throws ParseException, PaymentException {
		String customerId = "12345678987622";
		deleteProfileForTest(customerId);
		mpay24.createCustomer(getCustomer(customerId, "Xenia Wiesbauer"), getVisaTestData());
		List<PaymentData> storedPaymentDataList = mpay24.listCustomers(customerId, null, null, null);
		assertNotNull(storedPaymentDataList);
		assertEquals(1, storedPaymentDataList.size());
	}
	
	@Test
	public void testCreateCreditCardCustomerWithAddress() throws ParseException, PaymentException {
		String customerId = "1234567898763";
		deleteProfileForTest(customerId);
		mpay24.createCustomer(getCustomerWithAddress(customerId, "Xenia Wiesbauer", "Grüngasse 16"), getVisaTestData());
		List<PaymentData> storedPaymentDataList = mpay24.listCustomers(customerId, null, null, null);
		assertNotNull(storedPaymentDataList);
		assertEquals(1, storedPaymentDataList.size());
	}

	@Test
	public void testCreateTwoCustomerProfilesWithAddress() throws ParseException, PaymentException {
		String customerId = "max.mustermann@gmail.com";
		deleteProfileForTest(customerId);
		mpay24.createCustomer(getCustomerWithAddress(customerId, "Xenia Wiesbauer", "Grüngasse 16"), "x", getVisaTestData());
		mpay24.createCustomer(getCustomerWithAddress(customerId, "Xenia Wiesbauer", "Grüngasse 16"), "y", getDirectDebitTestData(Brand.HOBEX_AT));
		List<PaymentData> storedPaymentDataList = mpay24.listCustomers(customerId, null, null, null);
		assertNotNull(storedPaymentDataList);
		assertEquals(2, storedPaymentDataList.size());
	}

	private void deleteProfileForTest(String customerId) {
		try {
			mpay24.deleteCustomer(customerId, null);
		} catch (PaymentException e) {
			// OK if Profile does not exist
		}
	}
}
