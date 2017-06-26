package com.mpay24.payment;

import static org.junit.Assert.assertNotNull;

import java.text.ParseException;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Test;

import com.mpay24.payment.data.PaymentData;

public class TestCreateCustomer extends AbstractTestCase {
	public final static Logger log = Logger.getLogger(TestCreateCustomer.class);

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCreateCreditCardCustomer() throws ParseException, PaymentException {
		String customerId = "max.mustermann@gmail.com";
		deleteProfileForTest(customerId);
		mpay24.createCustomer(getCustomer(), getVisaTestData());
		List<PaymentData> storedPaymentDataList = mpay24.listCustomers(customerId, null, null, null);
		assertNotNull(storedPaymentDataList);
	}

	private void deleteProfileForTest(String customerId) {
		try {
			mpay24.deleteCustomer(customerId, null);
		} catch (PaymentException e) {
			// OK if Profile does not exist
		}
	}

}
