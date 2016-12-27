package com.mpay24.payment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.text.ParseException;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.mpay24.payment.data.Payment;
import com.mpay24.payment.data.Refund;

public class TestRefund extends AbstractTestCase {
	public final static Logger log = Logger.getLogger(TestRefund.class);

	@Test
	public void testRefundPaymentWithPaymentObject() throws ParseException, PaymentException {
		Payment payment = mpay24.payment(getTestPaymentRequest(), getVisaTestData());
		Refund refund = mpay24.refund(payment);
		assertEquals("OK", refund.getReturnCode());
		assertEquals("CREDITED", refund.getState().toString());
		assertNotNull(refund.getmPayTid());
	}

	@Test
	public void testRefundPaymentWithMpayTid() throws ParseException, PaymentException {
		Payment payment = mpay24.payment(getTestPaymentRequest(), getVisaTestData());
		Refund refund = mpay24.refund(payment.getmPayTid());
		assertEquals("OK", refund.getReturnCode());
		assertEquals("CREDITED", refund.getState().toString());
		assertNotNull(refund.getmPayTid());
	}

	@Test
	public void testPartialRefundPaymentWithMpayTid() throws ParseException, PaymentException {
		Payment payment = mpay24.payment(getTestPaymentRequest(), getVisaTestData());
		Refund refund = mpay24.refund(payment.getmPayTid(), new BigDecimal(0.1));
		assertEquals("OK", refund.getReturnCode());
		assertEquals("CREDITED", refund.getState().toString());
		assertNotNull(refund.getmPayTid());
	}

	@Test
	public void testPartialRefundPartialPaymentWithMpayTid() throws ParseException, PaymentException {
		Payment payment = mpay24.payment(getTestPaymentRequest(), getVisaTestData());
		Refund refund = mpay24.refund(payment.getmPayTid(), payment.getStateID(), payment.getAmount());
		assertEquals("OK", refund.getReturnCode());
		assertEquals("CREDITED", refund.getState().toString());
		assertNotNull(refund.getmPayTid());
	}
}
