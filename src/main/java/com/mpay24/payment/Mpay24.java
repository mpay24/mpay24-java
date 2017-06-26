package com.mpay24.payment;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.mpay.soap.client.Order;
import com.mpay.soap.client.PaymentType;
import com.mpay.soap.client.SortField;
import com.mpay.soap.client.SortType;
import com.mpay24.payment.communication.SoapCommunication;
import com.mpay24.payment.data.Customer;
import com.mpay24.payment.data.Payment;
import com.mpay24.payment.data.PaymentData;
import com.mpay24.payment.data.PaymentRequest;
import com.mpay24.payment.data.Refund;
import com.mpay24.payment.data.ShoppingCart;
import com.mpay24.payment.data.StylingOptions;
import com.mpay24.payment.data.Token;
import com.mpay24.payment.data.TokenRequest;
import com.mpay24.payment.mapper.SdkApiObjectMapper;
import com.mpay24.payment.mapper.SdkMdxiMapper;
import com.mpay24.payment.type.PaymentTypeData;

public class Mpay24 {

	public final static Logger logger = Logger.getLogger(Mpay24.class);

	private SdkApiObjectMapper mapper = new SdkApiObjectMapper();
	private SdkMdxiMapper mdxiMapper = new SdkMdxiMapper();
	private SoapCommunication soapCommunication;

	public enum Environment {
		INTEGRATION("https://it.mpay24.com/app/bin/etpproxy_v15", "https://it.mpay24.com/soap/etp/1.5/ETP.wsdl"), 
		TEST("https://test.mpay24.com/app/bin/etpproxy_v15", "https://test.mpay24.com/soap/etp/1.5/ETP.wsdl"), 
		PRODUCTION("https://www.mpay24.com/app/bin/etpproxy_v15", "https://www.mpay24.com/soap/etp/1.5/ETP.wsdl");

		private String endpoint;
		private String wsdlUrl;

		Environment(String endpoint, String wsdlUrl) {
			this.endpoint = endpoint;
			this.wsdlUrl = wsdlUrl;
		}

		public String getEndpoint() {
			return this.endpoint;
		}

		public String getWsdlUrl() {
			return wsdlUrl;
		}
	}

	public Mpay24(String merchantId, String password) {
		this(merchantId, password, Environment.PRODUCTION);
	}

	public Mpay24(String merchantId, String password, Environment mode) {
		super();
		soapCommunication = new SoapCommunication(getMerchantIdInCaseOfUserId(merchantId), password, mode);
	}

	public Payment paymentPage(PaymentRequest paymentRequest) throws PaymentException {
		return paymentPage(paymentRequest, null, null, null);
	}
	public Payment paymentPage(PaymentRequest paymentRequest, StylingOptions stylingOptions) throws PaymentException {
		return paymentPage(paymentRequest, null, null, stylingOptions);
	}
	public Payment paymentPage(PaymentRequest paymentRequest, Customer customer, StylingOptions stylingOptions) throws PaymentException {
		return paymentPage(paymentRequest, null, null, stylingOptions);
	}
	public Payment paymentPage(PaymentRequest paymentRequest, Customer customer) throws PaymentException {
		return paymentPage(paymentRequest, customer, null, null);
	}
	public Payment paymentPage(PaymentRequest paymentRequest, ShoppingCart shoppingCart) throws PaymentException {
		return paymentPage(paymentRequest, null, shoppingCart);
	}
	public Payment paymentPage(PaymentRequest paymentRequest, ShoppingCart shoppingCart, StylingOptions stylingOptions) throws PaymentException {
		return paymentPage(paymentRequest, null, shoppingCart, stylingOptions);
	}
	public Payment paymentPage(PaymentRequest paymentRequest, Customer customer, ShoppingCart shoppingCart) throws PaymentException {
		return paymentPage(paymentRequest, customer, shoppingCart, null);
	}
	public Payment paymentPage(PaymentRequest paymentRequest, Customer customer, ShoppingCart shoppingCart, StylingOptions stylingOptions) throws PaymentException {
		return getSoapCommunication().selectPayment(getMdxiOrderAsString(paymentRequest, customer, shoppingCart, stylingOptions));
	}

	private String getMdxiOrderAsString(PaymentRequest paymentRequest, Customer customer, ShoppingCart shoppingCart, StylingOptions stylingOptions) {
		return getMdxiMapper().constructAndMarshalOrder(paymentRequest, customer, shoppingCart, stylingOptions);
	}

	public Payment payment(PaymentRequest paymentRequest, PaymentTypeData paymentTypeData) throws PaymentException {
		return payment(paymentRequest, paymentTypeData, null);
	}
	public Payment payment(PaymentRequest paymentRequest, PaymentTypeData paymentTypeData, Customer customer) throws PaymentException {
		return payment(paymentRequest, paymentTypeData, customer, null);
	}
	public Payment payment(PaymentRequest paymentRequest, PaymentTypeData paymentTypeData, Customer customer, ShoppingCart shoppingCart) throws PaymentException {
		com.mpay.soap.client.Payment payment = mapper.mapPaymentSystemData(paymentRequest, paymentTypeData);
		Order order = mapper.mapOrder(paymentRequest, customer, shoppingCart);

		return soapCommunication.acceptPayment(paymentRequest.getTransactionID(), paymentTypeData.getPaymentType(), payment,
						mapper.getCustomerId(customer), mapper.getCustomerName(customer), order, paymentRequest.getSuccessUrl(), paymentRequest.getErrorUrl(),
						paymentRequest.getConfirmationUrl());
	}
	
	public List<Payment> paymentHistory(BigInteger mPayTid) throws PaymentException {
		return soapCommunication.transactionHistory(mPayTid);
	}
	public List<Payment> paymentHistory(Payment payment) throws PaymentException {
		return soapCommunication.transactionHistory(payment);
	}
	
	public Payment paymentDetails(BigInteger mPayTid) throws PaymentException {
		return paymentDetails(mPayTid, null);
	}
	public Payment paymentDetails(String transactionId) throws PaymentException {
		return paymentDetails(null, transactionId);
	}
	private Payment paymentDetails(BigInteger mPayTid, String transactionId) throws PaymentException {
		return soapCommunication.transactionStatus(mPayTid, transactionId);
	}
	public Payment paymentDetails(Payment payment) throws PaymentException {
		return soapCommunication.transactionStatus(payment);
	}
	
	public Refund refund(BigInteger mPayTid) throws PaymentException {
		return refund(mPayTid, null, null);
	}
	public Refund refund(BigInteger mPayTid, BigDecimal amount) throws PaymentException {
		return refund(mPayTid, null, amount);
	}
	public Refund refund(BigInteger mPayTid, BigInteger stateId, BigDecimal amount) throws PaymentException {
		return soapCommunication.manualCredit(mPayTid, stateId, amount);
	}
	public Refund refund(Payment payment) throws PaymentException {
		return soapCommunication.manualCredit(payment);
	}
	
	public void cancel(BigInteger mPayTid) throws PaymentException {
		soapCommunication.manualReverse(mPayTid);
	}
	public void cancel(Payment payment) throws PaymentException {
		soapCommunication.manualReverse(payment);
	}
	
	public Payment capture(BigInteger mPayTid) throws PaymentException {
		return capture(mPayTid, null);
	}
	public Payment capture(BigInteger mPayTid, BigDecimal amount) throws PaymentException {
		return soapCommunication.manualClear(mPayTid, amount);
	}
	public Payment capture(Payment payment) throws PaymentException {
		return soapCommunication.manualClear(payment);
	}
	
	public Token token(TokenRequest tokenRequest) throws PaymentException {
		return soapCommunication.token(tokenRequest);
	}
	
	public List<Payment> listAuthorizations(Long begin, Long size, SortField sortField, SortType sortType, Boolean listInProgress) throws PaymentException {
		return soapCommunication.listNotCleared(begin, size, sortField, sortType, listInProgress);
	}
	
	public List<PaymentData> listCustomers(String customerId, Date expiredBy, Long begin, Long size) throws PaymentException {
		return soapCommunication.listProfiles(customerId, expiredBy, begin, size);
	}
	
	public void deleteCustomer(String customerId, String profileId) throws PaymentException {
		soapCommunication.deleteProfile(customerId, profileId);
	}
	
	public void createCustomer(Customer customer, PaymentTypeData paymentTypeData) {
		PaymentType paymentType = mapper.mapPaymentTypeData(paymentTypeData);
		com.mpay.soap.client.PaymentData paymentData = mapper.mapPaymentData(paymentTypeData);
		soapCommunication.createCustomer(customer.getCustomerId(), customer.getName(), paymentType, paymentData);
	}

	private SoapCommunication getSoapCommunication() {
		return soapCommunication;
	}

	private SdkMdxiMapper getMdxiMapper() {
		return mdxiMapper;
	}

	private String getMerchantIdInCaseOfUserId(String merchantId) {
		if (merchantId.startsWith("u") || merchantId.startsWith("U")) {
			return merchantId.substring(1);
		} else {
			return merchantId;
		}
	}

}
