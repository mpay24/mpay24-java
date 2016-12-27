package com.mpay24.payment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mpay24.payment.data.Payment;

public class TestPaymentStatus extends AbstractTestCase {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testPaymentStatusUsingPaymentObject() throws PaymentException, ParseException {
		Payment payment = mpay24.payment(getTestPaymentRequest(), getVisaTestData());
		Payment paymentStatus = mpay24.paymentStatus(payment);
		assertNotNull(paymentStatus);
		assertEquals("BILLED", paymentStatus.getState().toString());
		assertEquals("EUR", paymentStatus.getCurrency());
		assertEquals(new BigDecimal(100l), paymentStatus.getAmount());
		assertEquals("CC", paymentStatus.getPaymentType().toString());
		assertNotNull(paymentStatus.getmPayTid());
		assertEquals("1", paymentStatus.getTransactionId());

	}

	@Test
	public void testPaymentStatusUsingMpayTid() throws PaymentException, ParseException {
		Payment payment = mpay24.payment(getTestPaymentRequest(), getVisaTestData());
		Payment paymentStatus = mpay24.paymentStatus(payment.getmPayTid());
		assertNotNull(paymentStatus);
		assertEquals("BILLED", paymentStatus.getState().toString());
		assertEquals("EUR", paymentStatus.getCurrency());
		assertEquals(new BigDecimal(100l), paymentStatus.getAmount());
		assertEquals("CC", paymentStatus.getPaymentType().toString());
		assertNotNull(paymentStatus.getmPayTid());
		assertEquals("1", paymentStatus.getTransactionId());

	}

	@Test
	public void testPaymentStatusUsingTransactionId() throws PaymentException, ParseException {
		String tid = getRandomTransactionId();
		Payment payment = mpay24.payment(getTestPaymentRequest(tid, 1l), getVisaTestData());
		Payment paymentStatus = mpay24.paymentStatus(tid);
		assertNotNull(paymentStatus);
		assertEquals("BILLED", paymentStatus.getState().toString());
		assertEquals("EUR", paymentStatus.getCurrency());
		assertEquals(new BigDecimal(100l), paymentStatus.getAmount());
		assertEquals("CC", paymentStatus.getPaymentType().toString());
		assertNotNull(paymentStatus.getmPayTid());
		assertEquals(tid, paymentStatus.getTransactionId());

	}

	private String getRandomTransactionId() {
		return String.valueOf(new Random().nextInt(10000000));
	}
}
