package com.mpay24.payment;

import java.text.ParseException;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.mpay24.payment.Mpay24.Environment;
import com.mpay24.payment.data.Payment;

public class TestCancel extends AbstractTestCase {
	public final static Logger log = Logger.getLogger(TestCancel.class);
	protected Mpay24 mpay24 = new Mpay24("80005", "*******", Environment.TEST);

	@Test
	public void testCancelPayment() throws ParseException, PaymentException {
		Payment payment = mpay24.payment(getTestPaymentRequest(), getVisaTestData());
		mpay24.cancel(payment);
	}

	@Test
	public void testCancelPaymentWithMpaytid() throws ParseException, PaymentException {
		Payment payment = mpay24.payment(getTestPaymentRequest(), getVisaTestData());
		mpay24.cancel(payment.getmPayTid());
	}



}
