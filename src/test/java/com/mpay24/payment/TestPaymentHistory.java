package com.mpay24.payment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.ParseException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mpay24.payment.data.Payment;

public class TestPaymentHistory extends AbstractTestCase {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testPaymentHistory() throws PaymentException, ParseException {
		Payment payment = mpay24.payment(getTestPaymentRequest(), getVisaTestData());
		List<Payment> paymentHistory = mpay24.paymentHistory(payment);
		assertNotNull(paymentHistory);
		assertEquals(1, paymentHistory.size());
		assertEquals("BILLED", paymentHistory.get(0).getState().toString());

	}

}
