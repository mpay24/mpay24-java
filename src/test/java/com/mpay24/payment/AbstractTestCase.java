package com.mpay24.payment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.mpay24.payment.Mpay24.Environment;
import com.mpay24.payment.data.Address;
import com.mpay24.payment.data.Customer;
import com.mpay24.payment.data.Customer.Gender;
import com.mpay24.payment.data.Payment;
import com.mpay24.payment.data.PaymentRequest;
import com.mpay24.payment.data.PaymentRequest.Language;
import com.mpay24.payment.data.PaymentType;
import com.mpay24.payment.data.ShoppingCart;
import com.mpay24.payment.data.ShoppingCartItem;
import com.mpay24.payment.data.StylingOptions;
import com.mpay24.payment.data.StylingOptions.Template;
import com.mpay24.payment.type.CreditCardPaymentType;
import com.mpay24.payment.type.DebitCardPaymentType;
import com.mpay24.payment.type.DirectDebitPaymentType;
import com.mpay24.payment.type.DirectDebitPaymentType.Brand;
import com.mpay24.payment.type.InstallmentPaymentType;
import com.mpay24.payment.type.InvoicePaymentType;
import com.mpay24.payment.type.OnlineBankingPaymentType;
import com.mpay24.payment.type.PayboxPaymentType;
import com.mpay24.payment.type.PaymentTypeData;
import com.mpay24.payment.type.PaypalPaymentType;
import com.mpay24.payment.type.PaysafecardPaymentType;
import com.mpay24.payment.type.QuickPaymentType;

public abstract class AbstractTestCase {
	protected Mpay24 mpay24 = new Mpay24("93975", getPassword(), Environment.TEST);

	protected String getPassword() {
		return System.getProperty("mpay24.merchant.password");
	}

	protected Customer getCustomerWithAddress(String customerId, String customerName, String street2) {
		Customer customer = getCustomer(customerId, customerName);
		customer.setAddress(getAddress(street2));
		return customer;
	}
	protected Customer getCustomerWithAddress() {
		return getCustomerWithAddress(null);
	}
	protected Customer getCustomerWithAddress(String street2) {
		Customer customer = getCustomer();
		customer.setAddress(getAddress(street2));
		return customer;
	}

	protected Address getAddress(String street2) {
		Address address = new Address();
		address.setEditable(true);
		address.setCity("Neuss");
		address.setCountryIso2("DE");
		address.setCountryName("Deutschland");
		address.setName("Testperson-de Approved");
		address.setPhoneNumber("0765260000");
		address.setStreet("Hellersbergstraße 14");
		address.setStreet2(street2);
		address.setState("Ankeborg");
		address.setZip("41460");
		return address;
	}

	protected String removeDomain(String value) {
		return value.replaceAll("https.*mpay24.com", "");
	}


	protected StylingOptions getStylingOptions(Template template) {
		StylingOptions styling = new StylingOptions();
		styling.setTemplate(template);
		return styling;
	}

	protected PaymentRequest getTestPaymentRequest(String tid, Long amount) {
		PaymentRequest paymentRequest = new PaymentRequest();
		paymentRequest.setAmount(new BigDecimal(amount));
		paymentRequest.setTransactionID(tid);
		return paymentRequest;
	}

	protected ShoppingCart getShoppingCart() {
		return getTestShoppingCart(getTestShoppingCartItemList());
	}

	protected ShoppingCart getTestShoppingCart() {
		return getTestShoppingCart(null);
	}
	protected ShoppingCart getTestShoppingCart(List<ShoppingCartItem> shoppingCartItemList) {
		ShoppingCart shoppingCart = new ShoppingCart();
		shoppingCart.setItemList(shoppingCartItemList);
		shoppingCart.setDescription("my happy melon");
		shoppingCart.setDiscount(new BigDecimal(-10f));
		shoppingCart.setShippingCost(new BigDecimal(-5f));
		shoppingCart.setSubTotal(new BigDecimal(100f));
		shoppingCart.setTax(new BigDecimal(20f));
		shoppingCart.setTaxPercentage(new BigDecimal(20f));
		return shoppingCart;
	}

	protected List<ShoppingCartItem> getTestShoppingCartItemList() {
		List<ShoppingCartItem> itemList = new ArrayList<ShoppingCartItem>();
		itemList.add(getShoppingCartItem("1", "AA123U234AP", "Apfel", 10l, 0.5f, 5f));
		itemList.add(getShoppingCartItem("5", "AA123U234BI", "Birne", 5l, 1.0f, 5f));
		itemList.add(getShoppingCartItem("2", "AA123U234ZW", "Zwetschke", 20l, 0.8f, 16f));
		itemList.add(getShoppingCartItem("3", "AA123U234BA", "Banane", 1000l, 0.1f, 100f));
		itemList.add(getShoppingCartItem("4", "AA123U234PF", "Pflaume", 1l, 0.55f, 0.55f));
		return itemList;
	}


	protected ShoppingCartItem getShoppingCartItem(String sequenceId, String productCode, String description, long quantity, float itemPrice, float price) {
		ShoppingCartItem item = new ShoppingCartItem();
		item.setSequenceId(sequenceId);
		item.setProductCode(productCode);
		item.setDescription(description);
		item.setQuantity(quantity);
		item.setItemAmount(new BigDecimal(itemPrice));
		item.setAmount(new BigDecimal(price));
		return item;
	}
	
	protected List<PaymentType> getTestPaymentInclusionList(PaymentType... paymentType) {
		List<PaymentType> paymentInclusionList = new ArrayList<PaymentType>();
		paymentInclusionList.addAll(Arrays.asList(paymentType));
		return paymentInclusionList;
	}

	protected PaymentRequest getTestPaymentRequest() {
		return getTestPaymentRequest(1.0, null, null);
	}
	protected PaymentRequest getTestPaymentRequest(List<PaymentType> paymentTypeInclusionList) {
		return getTestPaymentRequest(1.0, paymentTypeInclusionList, null);
	}
	protected PaymentRequest getTestPaymentRequest(Double amount, List<PaymentType> paymentTypeInclusionList) {
		return getTestPaymentRequest(amount, paymentTypeInclusionList, null);
	}
	protected PaymentRequest getTestPaymentRequest(Double amount, List<PaymentType> paymentTypeInclusionList, Language language) {
		return getTestPaymentRequest(amount, paymentTypeInclusionList, language, false, null);
	}
	protected PaymentRequest getTestRecurringPaymentRequest() {
		return getTestPaymentRequest(1.0, null, null, true, null);
	}
	protected PaymentRequest getTestPaymentRequest(Double amount, List<PaymentType> paymentTypeInclusionList, Language language, Boolean savePaymentData, String storedPaymentDataId) {
		PaymentRequest paymentRequest = new PaymentRequest();
		paymentRequest.setAmount(new BigDecimal(amount));
		paymentRequest.setTransactionID("1");
		paymentRequest.setPaymentTypeInclusionList(paymentTypeInclusionList);
		paymentRequest.setLanguage(language);
		paymentRequest.setSavePaymentData(savePaymentData);
		paymentRequest.setStoredPaymentDataId(storedPaymentDataId);

		return paymentRequest;
	}
	
	protected Customer getCustomer() {
		return getCustomer("07071960", "Testperson-de Approved");
	}
	protected Customer getCustomer(String customerId, String customerName) {
		Customer customer = new Customer();
		customer.setCustomerId(customerId);
		customer.setName(customerName);
		customer.setBirthdate(getDate("1960-07-07"));
		customer.setClientIp("127.0.0.1");
		customer.setEmail("youremail@email.com");
		customer.setGender(Gender.Male);
		customer.setPhoneNumber("01522113356");
		return customer;
	}

	protected Date getDate(String string) {
		try {
			return new SimpleDateFormat("yyyy-MM-dd").parse(string);
		} catch (ParseException e) {
			return null;
		}
	}


	protected void assertSuccessfullResponse(Payment response) {
		assertEquals("REDIRECT", response.getReturnCode());
		assertNotNull(response.getRedirectLocation());
	}

	
	protected DirectDebitPaymentType getDirectDebitTestData(Brand brand, String iban, String bic) {
		DirectDebitPaymentType paymentType = new DirectDebitPaymentType();
		paymentType.setIban(iban);
		paymentType.setBic(bic);
		paymentType.setMandateID("ABC123456789");
		paymentType.setDateOfSignature(new Date());
		paymentType.setBrand(brand);
		return paymentType;
	}

	protected DirectDebitPaymentType getDirectDebitTestData(Brand brand) {
		return getDirectDebitTestData(brand, "DE92760700120750007700", "DEUTDEMM760");
	}

	protected PaymentTypeData getVisaTestData() throws ParseException {
		CreditCardPaymentType paymentType = new CreditCardPaymentType();
		paymentType.setPan("4444333322221111");
		paymentType.setCvc("123");
		paymentType.setExpiry(getCreditCardMonthYearDate("12/2016"));
		paymentType.setBrand(com.mpay24.payment.type.CreditCardPaymentType.Brand.VISA);
		return paymentType;
	}

	protected PaymentTypeData getMaestroTestData() throws ParseException {
		DebitCardPaymentType paymentType = new DebitCardPaymentType();
		paymentType.setBrand(com.mpay24.payment.type.DebitCardPaymentType.Brand.MAESTRO);
		paymentType.setExpiry(getCreditCardMonthYearDate("12/2020"));
		paymentType.setPan("6700555544444444");
		return paymentType;
	}
	
	
	protected PaymentTypeData getMastercardTestData() throws ParseException {
		CreditCardPaymentType paymentType = new CreditCardPaymentType();
		paymentType.setPan("5413330089600010");
		paymentType.setCvc("533");
		paymentType.setExpiry(getCreditCardMonthYearDate("12/2025"));
		paymentType.setBrand(com.mpay24.payment.type.CreditCardPaymentType.Brand.MASTERCARD);
		return paymentType;
	}

	protected PaymentTypeData getAmexTestData() throws ParseException {
		CreditCardPaymentType paymentType = new CreditCardPaymentType();
		paymentType.setPan("374245455400001");
		paymentType.setCvc("2234");
		paymentType.setExpiry(getCreditCardMonthYearDate("01/2017"));
		paymentType.setBrand(com.mpay24.payment.type.CreditCardPaymentType.Brand.AMEX);
		return paymentType;
	}

	protected PaymentTypeData getDinersTestData() throws ParseException {
		CreditCardPaymentType paymentType = new CreditCardPaymentType();
		paymentType.setPan("36177614633117");
		paymentType.setCvc("331");
		paymentType.setExpiry(getCreditCardMonthYearDate("03/2020"));
		paymentType.setBrand(com.mpay24.payment.type.CreditCardPaymentType.Brand.DINERS);
		return paymentType;
	}

	protected OnlineBankingPaymentType getEpsData() {
		return getOnlinebankingPayment(com.mpay24.payment.type.OnlineBankingPaymentType.Brand.EPS);
	}

	protected OnlineBankingPaymentType getGiropayData() {
		OnlineBankingPaymentType paymentType = new OnlineBankingPaymentType();
		paymentType.setBrand(com.mpay24.payment.type.OnlineBankingPaymentType.Brand.GIROPAY);
		paymentType.setBic("TESTDETT421");
		return paymentType;
	}
	
	protected OnlineBankingPaymentType getSofortData() {
		return getOnlinebankingPayment(com.mpay24.payment.type.OnlineBankingPaymentType.Brand.SOFORT);
	}
	

	protected PaymentTypeData getPaypalData() {
		return new PaypalPaymentType();
	}

	protected PaymentTypeData getPayboxData() {
		PayboxPaymentType paymentType = new PayboxPaymentType();
		paymentType.setMobilePhoneNumber("+436642222222");
		return paymentType;
	}

	protected PaymentTypeData getPaysafecardData() {
		return new PaysafecardPaymentType();
	}

	protected PaymentTypeData getQuickData() {
		return new QuickPaymentType();
	}

	protected OnlineBankingPaymentType getOnlinebankingPayment(com.mpay24.payment.type.OnlineBankingPaymentType.Brand brand) {
		OnlineBankingPaymentType paymentType = new OnlineBankingPaymentType();
		paymentType.setBrand(brand);
		paymentType.setBic("SPFKAT2BXXX");
		return paymentType;
	}

	protected PaymentTypeData getJcbTestData() throws ParseException {
		CreditCardPaymentType paymentType = new CreditCardPaymentType();
		paymentType.setPan("3562114400015015");
		paymentType.setCvc("620");
		paymentType.setExpiry(getCreditCardMonthYearDate("05/2020"));
		paymentType.setBrand(com.mpay24.payment.type.CreditCardPaymentType.Brand.JCB);
		return paymentType;
	}

	protected PaymentTypeData getInvoiceKlarnaTestData() {
		return getInvoiceTestData(new InvoicePaymentType(), com.mpay24.payment.type.InvoicePaymentType.Brand.KLARNA);
	}

	protected PaymentTypeData getInvoiceBillpayTestData() {
		return getInvoiceTestData(new InvoicePaymentType(), com.mpay24.payment.type.InvoicePaymentType.Brand.BILLPAY);
	}

	protected PaymentTypeData getInstallmentBillpayTestData() {
		return getInvoiceTestData(new InstallmentPaymentType(), com.mpay24.payment.type.InvoicePaymentType.Brand.BILLPAY);
	}

	protected PaymentTypeData getInstallmentKlarnaTestData() {
		return getInvoiceTestData(new InstallmentPaymentType(), com.mpay24.payment.type.InvoicePaymentType.Brand.KLARNA);
	}

	protected PaymentTypeData getInvoiceTestData(InvoicePaymentType paymentType, com.mpay24.payment.type.InvoicePaymentType.Brand brand) {
		paymentType.setBrand(brand);
		paymentType.setKlarnaPersonalNumber("07071960");
		return paymentType;
	}

	protected Date getCreditCardMonthYearDate(String string) throws ParseException {
		return new SimpleDateFormat("MM/yyyy").parse(string);
	}

	
}
