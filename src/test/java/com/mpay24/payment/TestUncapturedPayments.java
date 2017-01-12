package com.mpay24.payment;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.mpay24.payment.data.Payment;

public class TestUncapturedPayments extends AbstractTestCase {
	public final static Logger log = Logger.getLogger(TestUncapturedPayments.class);

	@Test
	public void testVisaPayment() throws ParseException, PaymentException {
		List<Payment> paymentList = mpay24.listAuthorizations(0l, 100l, null, null, false);
		assertNotNull(paymentList);
		assertTrue(paymentList.size() > 0);
	}



}
