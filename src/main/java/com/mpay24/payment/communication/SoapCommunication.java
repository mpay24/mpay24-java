package com.mpay24.payment.communication;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Holder;

import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.log4j.Logger;

import com.mpay.soap.client.ClearingDetails;
import com.mpay.soap.client.ETP;
import com.mpay.soap.client.ETP_Service;
import com.mpay.soap.client.HistoryEntry;
import com.mpay.soap.client.Order;
import com.mpay.soap.client.Parameter;
import com.mpay.soap.client.PaymentProfile;
import com.mpay.soap.client.PaymentType;
import com.mpay.soap.client.Profile;
import com.mpay.soap.client.SortField;
import com.mpay.soap.client.SortType;
import com.mpay.soap.client.Status;
import com.mpay.soap.client.Transaction;
import com.mpay.soap.client.TransactionDetails;
import com.mpay24.payment.Mpay24.Environment;
import com.mpay24.payment.PaymentException;
import com.mpay24.payment.data.Address;
import com.mpay24.payment.data.Customer;
import com.mpay24.payment.data.Payment;
import com.mpay24.payment.data.PaymentData;
import com.mpay24.payment.data.Refund;
import com.mpay24.payment.data.State;
import com.mpay24.payment.data.Token;
import com.mpay24.payment.data.TokenRequest;

public class SoapCommunication {
	public final static Logger logger = Logger.getLogger(SoapCommunication.class);

	private static final String USERNAME_PREFIX = "u";
	private String merchantId;
	private String password;
	private Environment mode;

	public SoapCommunication(String merchantId, String password, Environment mode) {
		super();
		this.merchantId = merchantId;
		this.password = password;
		this.mode = mode;
	}

	public Payment selectPayment(String mdxi) throws PaymentException {
		Holder<Status> statusHolder = new Holder<Status>();
		Holder<String> returnCodeHolder = new Holder<String>();
		Holder<Integer> errorNumberHolder = new Holder<Integer>();
		Holder<String> errorTextHolder = new Holder<String>();
		Holder<String> locationHolder = new Holder<String>();

		getSoapClientProxy().selectPayment(getMerchantIdAsLong(), mdxi, statusHolder, returnCodeHolder, errorNumberHolder, errorTextHolder, locationHolder);

		return getPaymentResponse(statusHolder, returnCodeHolder, errorNumberHolder, errorTextHolder, locationHolder, null, null, null);
	}

	public Payment acceptPayment(String transactionId, PaymentType paymenttype, com.mpay.soap.client.Payment payment, String customerId, String customerName,
					Order order, String successUrl, String errorUrl, String confirmationUrl) throws PaymentException {
		Holder<Status> statusHolder = new Holder<Status>();
		Holder<String> returnCodeHolder = new Holder<String>();
		Holder<Integer> errorNumberHolder = new Holder<Integer>();
		Holder<String> errorTextHolder = new Holder<String>();
		Holder<String> locationHolder = new Holder<String>();
		Holder<BigInteger> mPayTidHolder = new Holder<BigInteger>();

		getSoapClientProxy().acceptPayment(getMerchantIdAsLong(), transactionId, paymenttype, payment, customerId, customerName, order, successUrl, errorUrl,
						confirmationUrl, (String) null, statusHolder, returnCodeHolder, mPayTidHolder, errorNumberHolder, errorTextHolder, locationHolder);

		return getPaymentResponse(statusHolder, returnCodeHolder, errorNumberHolder, errorTextHolder, locationHolder, mPayTidHolder, payment, order);
	}

	public List<Payment> transactionHistory(Payment payment) throws PaymentException {
		return transactionHistory(payment.getmPayTid());
	}

	public List<Payment> transactionHistory(BigInteger mPayTid) throws PaymentException {
		Holder<Status> statusHolder = new Holder<Status>();
		Holder<String> returnCodeHolder = new Holder<String>();
		Holder<List<HistoryEntry>> historyEntryHolder = new Holder<List<HistoryEntry>>();
		getSoapClientProxy().transactionHistory(getMerchantIdAsLong(), mPayTid, statusHolder, returnCodeHolder, historyEntryHolder);

		checkForError(statusHolder, returnCodeHolder);
		return getPaymentHistory(statusHolder, returnCodeHolder, historyEntryHolder, mPayTid);
	}

	private List<Payment> getPaymentHistory(Holder<Status> statusHolder, Holder<String> returnCodeHolder, Holder<List<HistoryEntry>> historyEntryHolder,
					BigInteger mPayTid) {
		List<Payment> paymentList = new ArrayList<Payment>();
		if (historyEntryHolder == null) {
			return paymentList;
		}
		for (HistoryEntry historyEntry : historyEntryHolder.value) {
			Payment payment = new Payment();
			payment.setAmount(getConvertedAmount(historyEntry.getAmount()));
			payment.setApprovalCode(historyEntry.getApprovalCode());
			payment.setErrorNumber(historyEntry.getErrNo());
			payment.setErrorText(historyEntry.getErrText());
			payment.setmPayTid(mPayTid);
			payment.setParentStateID(historyEntry.getParentStateID());
			payment.setProfileStatus(historyEntry.getProfileStatus());
			payment.setStateID(historyEntry.getStateID());
			payment.setTimeStamp(historyEntry.getTimeStamp().toGregorianCalendar().getTime());
			payment.setState(State.valueOf(historyEntry.getTxState().toString()));
			paymentList.add(payment);
		}
		return paymentList;
	}

	public Payment transactionStatus(Payment payment) throws PaymentException {
		return transactionStatus(payment.getmPayTid(), payment.getTransactionId());
	}

	public Payment transactionStatus(BigInteger mPayTid, String transactionId) throws PaymentException {
		Holder<Status> statusHolder = new Holder<Status>();
		Holder<String> returnCodeHolder = new Holder<String>();
		Holder<List<Parameter>> parameterEntryHolder = new Holder<List<Parameter>>();
		getSoapClientProxy().transactionStatus(getMerchantIdAsLong(), mPayTid, transactionId, statusHolder, returnCodeHolder, parameterEntryHolder);
		checkForError(statusHolder, returnCodeHolder);
		return getPaymentStatus(parameterEntryHolder);
	}

	public Refund manualCredit(Payment payment) throws PaymentException {
		return manualCredit(payment.getmPayTid(), payment.getStateID(), payment.getAmount());
	}

	public Refund manualCredit(BigInteger mPayTid, BigInteger stateId, BigDecimal amount) throws PaymentException {
		Holder<Status> statusHolder = new Holder<Status>();
		Holder<String> returnCodeHolder = new Holder<String>();
		Holder<Transaction> transactionEntryHolder = new Holder<Transaction>();
		getSoapClientProxy().manualCredit(getMerchantIdAsLong(), mPayTid, stateId, getConvertedAmount(amount), statusHolder, returnCodeHolder,
						transactionEntryHolder);
		checkForError(statusHolder, returnCodeHolder);
		return getRefund(returnCodeHolder, transactionEntryHolder);
	}

	public void manualReverse(Payment payment) throws PaymentException {
		manualReverse(payment.getmPayTid());
	}

	public void manualReverse(BigInteger mPayTid) throws PaymentException {
		Holder<Status> statusHolder = new Holder<Status>();
		Holder<String> returnCodeHolder = new Holder<String>();
		Holder<Transaction> transactionEntryHolder = new Holder<Transaction>();
		getSoapClientProxy().manualReverse(getMerchantIdAsLong(), mPayTid, statusHolder, returnCodeHolder, transactionEntryHolder);
		checkForError(statusHolder, returnCodeHolder);
	}

	public Payment manualClear(Payment payment) throws PaymentException {
		Holder<Status> statusHolder = new Holder<Status>();
		Holder<String> returnCodeHolder = new Holder<String>();
		Holder<List<Transaction>> transactionEntryHolder = new Holder<List<Transaction>>();
		List<ClearingDetails> clearingDetailList = new ArrayList<ClearingDetails>();
		ClearingDetails clearingDetail = new ClearingDetails();
		clearingDetail.setMpayTID(payment.getmPayTid());
		clearingDetail.setAmount(getConvertedAmount(payment.getAmount()));
		clearingDetailList.add(clearingDetail);
		getSoapClientProxy().manualClear(getMerchantIdAsLong(), clearingDetailList, statusHolder, returnCodeHolder, transactionEntryHolder);
		checkForError(statusHolder, returnCodeHolder);
		return getPayment(payment, returnCodeHolder, transactionEntryHolder);
	}

	public Payment manualClear(BigInteger mPayTid, BigDecimal amount) throws PaymentException {
		Payment payment = new Payment();
		payment.setmPayTid(mPayTid);
		payment.setAmount(amount);
		return manualClear(payment);
	}

	public Token token(TokenRequest tokenRequest) throws PaymentException {
		return token(tokenRequest.getPaymentType(), tokenRequest.getTemplateSet(), tokenRequest.getStyle(), tokenRequest.getCustomerId(),
						tokenRequest.getProfileId(), tokenRequest.getDomain(), tokenRequest.getLanguage());
	}

	public Token token(PaymentType paymentType, String templateSet, String style, String customerId, String profileId, String domain, String language)
					throws PaymentException {
		Holder<Status> statusHolder = new Holder<Status>();
		Holder<String> returnCodeHolder = new Holder<String>();
		Holder<String> tokenHolder = new Holder<String>();
		Holder<String> apiKeyHolder = new Holder<String>();
		Holder<Integer> errorNumberHolder = new Holder<Integer>();
		Holder<String> errorTextHolder = new Holder<String>();
		Holder<String> locationHolder = new Holder<String>();
		getSoapClientProxy().createPaymentToken(getMerchantIdAsLong(), paymentType, templateSet, style, customerId, profileId, domain, language, statusHolder,
						returnCodeHolder, tokenHolder, apiKeyHolder, errorNumberHolder, errorTextHolder, locationHolder);
		checkForError(statusHolder, errorNumberHolder, errorTextHolder, returnCodeHolder);
		return getToken(returnCodeHolder, tokenHolder, apiKeyHolder, locationHolder);
	}

	public List<Payment> listNotCleared(Long begin, Long size, SortField sortField, SortType sortType, Boolean listInProgress) throws PaymentException {
		Holder<Status> statusHolder = new Holder<Status>();
		Holder<String> returnCodeHolder = new Holder<String>();
		Holder<List<TransactionDetails>> transactionDetailsHolder = new Holder<List<TransactionDetails>>();
		Holder<Long> allHolder = new Holder<Long>();
		getSoapClientProxy().listNotCleared(getMerchantIdAsLong(), begin, size, sortField, sortType, listInProgress, statusHolder, returnCodeHolder,
						transactionDetailsHolder, allHolder);
		checkForError(statusHolder, returnCodeHolder);
		return getPaymentList(returnCodeHolder, transactionDetailsHolder);
	}

	public void createCustomer(String customerId, String customerName, com.mpay.soap.client.Address address, PaymentType pType,
					com.mpay.soap.client.PaymentData paymentData) throws PaymentException {
		Holder<Status> statusHolder = new Holder<Status>();
		Holder<String> returnCodeHolder = new Holder<String>();
		Holder<Integer> errorNumberHolder = new Holder<Integer>();
		Holder<String> errorTextHolder = new Holder<String>();

		getSoapClientProxy().createCustomer(getMerchantIdAsLong(), pType, paymentData, customerId, customerName, address, (String) null, statusHolder,
						returnCodeHolder, errorNumberHolder, errorTextHolder);
		checkForError(statusHolder, returnCodeHolder);
	}

	public List<PaymentData> listProfiles(String customerId, Date expiredBy, Long begin, Long size) throws PaymentException {
		Holder<Status> statusHolder = new Holder<Status>();
		Holder<String> returnCodeHolder = new Holder<String>();
		Holder<List<Profile>> profileListHolder = new Holder<List<Profile>>();
		Holder<Long> allHolder = new Holder<Long>();
		getSoapClientProxy().listProfiles(getMerchantIdAsLong(), customerId, expiredBy, begin, size, statusHolder, returnCodeHolder, profileListHolder,
						allHolder);
		checkForError(statusHolder, returnCodeHolder);
		return getProfileList(returnCodeHolder, profileListHolder);
	}

	public void deleteProfile(String customerId, String profileId) throws PaymentException {
		Holder<Status> statusHolder = new Holder<Status>();
		Holder<String> returnCodeHolder = new Holder<String>();
		getSoapClientProxy().deleteProfile(getMerchantIdAsLong(), customerId, profileId, statusHolder, returnCodeHolder);
		checkForError(statusHolder, returnCodeHolder);
	}

	private List<PaymentData> getProfileList(Holder<String> returnCodeHolder, Holder<List<Profile>> profileListHolder) {
		List<PaymentData> paymentDataList = new ArrayList<PaymentData>();
		for (Profile profile : profileListHolder.value) {
			if (profile.getPayment() == null || profile.getPayment().size() == 0 ) {
				paymentDataList.add(getProfile(profile));
			} else {
				for (PaymentProfile paymentProfile: profile.getPayment()) {
					PaymentData paymentData = getProfile(profile);
					addPaymentProfileData(paymentProfile, paymentData);
					paymentDataList.add(paymentData);
				}
			}
		}
		
		return paymentDataList;
	}

	private PaymentData getProfile(Profile profile) {
		PaymentData storedPaymentData = new PaymentData();
		storedPaymentData.setLastUpdated(profile.getUpdated().toGregorianCalendar().getTime());
		storedPaymentData.setCustomer(getCustomer(profile.getCustomerID()));
		return storedPaymentData;
	}

	private void addPaymentProfileData(PaymentProfile pProfile, PaymentData storedPaymentData) {
		storedPaymentData.setCustomer(getCustomerDetails(storedPaymentData.getCustomer(), pProfile.getAddress()));
		storedPaymentData.getCustomer().setAddress(getAddress(pProfile.getAddress()));
		storedPaymentData.setExpires(pProfile.getExpires());
		storedPaymentData.setIdentifier(pProfile.getIdentifier());
		storedPaymentData.setLastUpdated(pProfile.getUpdated().toGregorianCalendar().getTime());
		storedPaymentData.setPaymentType(com.mpay24.payment.data.PaymentType.fromId(pProfile.getPMethodID().intValue()));
		storedPaymentData.setProfileId(pProfile.getProfileID());
	}

	private Customer getCustomer(String customerId) {
		Customer customer = new Customer();
		customer.setCustomerId(customerId);
		return customer;
	}

	private Customer getCustomerDetails(Customer customer, com.mpay.soap.client.Address address) {
		if (address == null)
			return customer;

		customer.setBirthdate(address.getBirthday());
		customer.setEmail(address.getEmail());
		if (address.getGender() != null) {
			customer.setGender(com.mpay24.payment.data.Customer.Gender.getEnum(address.getGender().toString()));
		}
		customer.setName(address.getName());
		customer.setPhoneNumber(address.getPhone());
		return customer;
	}

	private Address getAddress(com.mpay.soap.client.Address soapAddress) {
		if (soapAddress == null)
			return null;
		Address address = new Address();
		address.setCity(soapAddress.getCity());
		address.setCountryIso2(soapAddress.getCountryCode());
		address.setState(soapAddress.getState());
		address.setStreet(soapAddress.getStreet());
		address.setStreet2(soapAddress.getStreet2());
		address.setZip(soapAddress.getZip());
		return address;
	}

	private List<Payment> getPaymentList(Holder<String> returnCodeHolder, Holder<List<TransactionDetails>> transactionDetailsHolder) {
		List<Payment> paymentList = new ArrayList<Payment>();
		for (TransactionDetails txDetails : transactionDetailsHolder.value) {
			Payment payment = new Payment();
			payment.setAmount(getConvertedAmount(txDetails.getAmount()));
			payment.setCurrency(txDetails.getCurrency());
			payment.setmPayTid(txDetails.getMpayTID());
			payment.setDescription(txDetails.getOrderDescription());
			payment.setPaymentType(txDetails.getPType());
			payment.setStateID(txDetails.getStateID());
			payment.setTransactionId(txDetails.getTid());
			payment.setState(State.valueOf(txDetails.getTStatus().toString()));
			paymentList.add(payment);
		}
		return paymentList;
	}

	private Token getToken(Holder<String> returnCodeHolder, Holder<String> tokenHolder, Holder<String> apiKeyHolder, Holder<String> locationHolder) {
		Token token = new Token();
		token.setToken(tokenHolder.value);
		token.setApiKey(apiKeyHolder.value);
		token.setRedirectLocation(locationHolder.value);
		token.setReturnCode(returnCodeHolder.value);
		return token;
	}

	private Payment getPayment(Payment payment, Holder<String> returnCodeHolder, Holder<List<Transaction>> transactionEntryHolder) {
		if (transactionEntryHolder != null && transactionEntryHolder.value.size() > 0) {
			Transaction tx = transactionEntryHolder.value.get(0);
			payment.setmPayTid(tx.getMpayTID());
			payment.setStateID(tx.getStateID());
			payment.setState(State.valueOf(tx.getTStatus().toString()));
			payment.setTransactionId(tx.getTid());
			payment.setReturnCode(returnCodeHolder.value);
			return payment;
		} else {
			return null;
		}
	}

	private Refund getRefund(Holder<String> returnCodeHolder, Holder<Transaction> transactionEntryHolder) {
		Refund refund = new Refund();
		Transaction tx = transactionEntryHolder.value;
		refund.setmPayTid(tx.getMpayTID());
		refund.setStateID(tx.getStateID());
		refund.setState(tx.getTStatus());
		refund.setTransactionId(tx.getTid());
		refund.setReturnCode(returnCodeHolder.value);
		return refund;
	}

	private Payment getPaymentStatus(Holder<List<Parameter>> parameterEntryHolder) {
		Payment payment = new Payment();
		for (Parameter param : parameterEntryHolder.value) {
			if ("OPERATION".equalsIgnoreCase(param.getName())) {

			} else if ("TID".equalsIgnoreCase(param.getName())) {
				payment.setTransactionId(param.getValue());
			} else if ("STATUS".equalsIgnoreCase(param.getName())) {
				payment.setState(State.valueOf(param.getValue()));
			} else if ("PRICE".equalsIgnoreCase(param.getName())) {
				payment.setAmount(new BigDecimal(param.getValue()));
			} else if ("CURRENCY".equalsIgnoreCase(param.getName())) {
				payment.setCurrency(param.getValue());
			} else if ("P_TYPE".equalsIgnoreCase(param.getName())) {
				payment.setPaymentType(PaymentType.fromValue(param.getValue()));
			} else if ("MPAYTID".equalsIgnoreCase(param.getName())) {
				payment.setmPayTid(convertStringToBigInteger(param.getValue()));
			} else if ("ORDERDESC".equalsIgnoreCase(param.getName())) {
				payment.setDescription(param.getValue());
			} else if ("CUSTOMER".equalsIgnoreCase(param.getName())) {
				payment.getCustomer().setName(param.getValue());
			} else if ("CUSTOMER_EMAIL".equalsIgnoreCase(param.getName())) {
				payment.getCustomer().setEmail(param.getValue());
			} else if ("CUSTOMER_ID".equalsIgnoreCase(param.getName())) {
				payment.getCustomer().setCustomerId(param.getValue());
			}
			logger.debug("name: '" + param.getName() + "', value: '" + param.getValue() + "'");
		}
		return payment;
	}

	private BigInteger convertStringToBigInteger(String value) {
		try {
			return new BigInteger(value);
		} catch (Exception e) {
			return null;
		}
	}

	private BigDecimal getConvertedAmount(long amount) {
		return new BigDecimal(amount / 100);
	}

	private Long getConvertedAmount(BigDecimal amount) {
		if (amount == null)
			return null;
		return amount.multiply(new BigDecimal(100)).longValue();
	}

	private Long getMerchantIdAsLong() {
		return Long.valueOf(this.merchantId);
	}

	private ETP getSoapClientProxy() {
		QName qname = new QName("https://www.mpay24.com/soap/etp/1.5/ETP.wsdl", "ETP");
		ETP_Service etpService = getEtpService(qname);
		ETP etp = etpService.getETP();
		setBindingParameter(etp);
		Client cxfClient = getSoapClient(etp);
		return etp;
	}

	private ETP_Service getEtpService(QName qname) {
		try {
			return new ETP_Service(new URL(mode.getWsdlUrl()), qname);
		} catch (MalformedURLException e) {
			logger.error("Error connecting to Soap Endpoint: " + e.getMessage(), e);
			throw new RuntimeException("Error connecting to Soap Endpoint: " + e.getMessage(), e);
		}
	}

	private void setBindingParameter(ETP etp) {
		((BindingProvider) etp).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, getUsername());
		((BindingProvider) etp).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, password);
		((BindingProvider) etp).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, mode.getEndpoint());
	}

	private Client getSoapClient(ETP etp) {
		Client cxfClient = ClientProxy.getClient(etp);
		LoggingOutInterceptor loggingOutInterceptor = new LoggingOutInterceptor();
		loggingOutInterceptor.setPrettyLogging(true);
//      Uncomment to log the SOAP message (including the basic authentication headers) - not for production!
//		cxfClient.getOutInterceptors().add(loggingOutInterceptor);
		return cxfClient;
	}

	private void disableCertificateChecks(Client cxfClient) {
		HTTPConduit httpConduit = (HTTPConduit) cxfClient.getConduit();
		TLSClientParameters tlsCP = new TLSClientParameters();
		tlsCP.setTrustManagers(getNoCertificationCheckTrustManager());
		tlsCP.setDisableCNCheck(true);
		httpConduit.setTlsClientParameters(tlsCP);
	}

	private TrustManager[] getNoCertificationCheckTrustManager() {
		TrustManager[] simpleTrustManager = new TrustManager[] { new X509TrustManager() {
			public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			}

			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		} };
		return simpleTrustManager;
	}

	private String getUsername() {
		return USERNAME_PREFIX + merchantId;
	}

	private Payment getPaymentResponse(Holder<Status> statusHolder, Holder<String> returnCodeHolder, Holder<Integer> errorNumberHolder,
					Holder<String> errorTextHolder, Holder<String> locationHolder, Holder<BigInteger> mPayTidHolder, com.mpay.soap.client.Payment payment,
					Order order) throws PaymentException {
		checkForError(statusHolder, errorNumberHolder, errorTextHolder, returnCodeHolder);
		Payment paymentResponse = new Payment();
		if (returnCodeHolder != null)
			paymentResponse.setReturnCode(returnCodeHolder.value);
		if (locationHolder != null)
			paymentResponse.setRedirectLocation(locationHolder.value);
		if (mPayTidHolder != null)
			paymentResponse.setmPayTid(mPayTidHolder.value);
		if (payment != null) {
			paymentResponse.setAmount(getConvertedAmount(payment.getAmount()));
			paymentResponse.setTimeStamp(new Date());
		}
		return paymentResponse;
	}

	private void checkForError(Holder<Status> statusHolder, Holder<Integer> errorNumberHolder, Holder<String> errorTextHolder, Holder<String> returnCodeHolder)
					throws PaymentException {
		if (statusHolder != null && statusHolder.value != null && Status.ERROR == statusHolder.value) {
			PaymentException exception = new PaymentException();
			exception.setStatus(statusHolder.value);
			if (errorNumberHolder != null) {
				exception.setErrorCode("" + errorNumberHolder.value);
			}
			if (errorTextHolder != null) {
				exception.setErrorText(errorTextHolder.value);
			}
			if (returnCodeHolder != null) {
				exception.setReturnCode(returnCodeHolder.value);
			}
			throw exception;
		}
	}

	private void checkForError(Holder<Status> statusHolder, Holder<String> returnCodeHolder) throws PaymentException {
		if (statusHolder != null && statusHolder.value != null && Status.ERROR == statusHolder.value) {
			PaymentException exception = new PaymentException();
			exception.setStatus(statusHolder.value);
			if (returnCodeHolder != null) {
				exception.setErrorCode(returnCodeHolder.value);
			}
			throw exception;
		}
	}

}
