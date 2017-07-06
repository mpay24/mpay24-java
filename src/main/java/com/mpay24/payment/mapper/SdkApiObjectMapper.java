package com.mpay24.payment.mapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.mpay.soap.client.Address;
import com.mpay.soap.client.AddressMode;
import com.mpay.soap.client.Gender;
import com.mpay.soap.client.Item;
import com.mpay.soap.client.ObjectFactory;
import com.mpay.soap.client.Order;
import com.mpay.soap.client.Payment;
import com.mpay.soap.client.PaymentBILLPAY;
import com.mpay.soap.client.PaymentCC;
import com.mpay.soap.client.PaymentData;
import com.mpay.soap.client.PaymentDataCC;
import com.mpay.soap.client.PaymentDataELV;
import com.mpay.soap.client.PaymentDataTOKEN;
import com.mpay.soap.client.PaymentELV;
import com.mpay.soap.client.PaymentEPS;
import com.mpay.soap.client.PaymentGIROPAY;
import com.mpay.soap.client.PaymentKLARNA;
import com.mpay.soap.client.PaymentMAESTRO;
import com.mpay.soap.client.PaymentPAYPAL;
import com.mpay.soap.client.PaymentPB;
import com.mpay.soap.client.PaymentTOKEN;
import com.mpay.soap.client.PaymentType;
import com.mpay.soap.client.ProfilePaymentCC;
import com.mpay.soap.client.ProfilePaymentELV;
import com.mpay24.payment.data.Customer;
import com.mpay24.payment.data.PaymentRequest;
import com.mpay24.payment.data.ShoppingCart;
import com.mpay24.payment.data.ShoppingCartItem;
import com.mpay24.payment.type.CreditCardPaymentType;
import com.mpay24.payment.type.DebitCardPaymentType;
import com.mpay24.payment.type.DirectDebitPaymentType;
import com.mpay24.payment.type.InvoicePaymentType;
import com.mpay24.payment.type.InvoicePaymentType.Brand;
import com.mpay24.payment.type.OnlineBankingPaymentType;
import com.mpay24.payment.type.PayboxPaymentType;
import com.mpay24.payment.type.PaymentTypeData;
import com.mpay24.payment.type.PaypalPaymentType;
import com.mpay24.payment.type.PaysafecardPaymentType;
import com.mpay24.payment.type.QuickPaymentType;
import com.mpay24.payment.type.RecurringCreditCardPaymentType;
import com.mpay24.payment.type.RecurringDirectDebitPaymentType;
import com.mpay24.payment.type.TokenPaymentType;

public class SdkApiObjectMapper {

	ObjectFactory objectFactory = new ObjectFactory();

	public String getCustomerName(Customer customer) {
		if (customer == null)
			return null;
		return customer.getName();
	}

	public String getCustomerId(Customer customer) {
		if (customer == null)
			return null;
		return customer.getCustomerId();
	}

	public PaymentType mapPaymentTypeData(PaymentTypeData paymentType) {
		if (paymentType instanceof DirectDebitPaymentType) {
			return PaymentType.ELV;
		} else if (paymentType instanceof CreditCardPaymentType) {
			return PaymentType.CC;
		} else if (paymentType instanceof CreditCardPaymentType) {
			return PaymentType.TOKEN;
		} else {
			throw new UnsupportedOperationException("Currently this method only supports the following Payment types: DirectDebit, CreditCard");
		}
		
	}

	public PaymentData mapPaymentData(PaymentTypeData paymentType, String profileId) {
		if (paymentType instanceof DirectDebitPaymentType) {
			return mapPaymentData((DirectDebitPaymentType)paymentType, profileId);
		} else if (paymentType instanceof CreditCardPaymentType) {
			return mapPaymentData((CreditCardPaymentType)paymentType, profileId);
		} else if (paymentType instanceof TokenPaymentType) {
			return mapPaymentData((TokenPaymentType)paymentType, profileId);
		} else {
			throw new UnsupportedOperationException("Currently this method only supports the following Payment types: DirectDebit, CreditCard");
		}
		
	}

	private PaymentData mapPaymentData(DirectDebitPaymentType paymentType, String profileId) {
		PaymentDataELV paymentData = new PaymentDataELV();
		paymentData.setBic(paymentType.getBic());
		paymentData.setBrand(paymentType.getBrand().toString());
		paymentData.setDateOfSignature(paymentType.getDateOfSignature());
		paymentData.setIban(paymentType.getIban());
		paymentData.setMandateID(paymentType.getMandateID());
		paymentData.setProfileID(profileId);
		return paymentData;
	}

	private PaymentData mapPaymentData(CreditCardPaymentType paymentType, String profileId) {
		PaymentDataCC paymentData = new PaymentDataCC();
		paymentData.setBrand(paymentType.getBrand().toString());
		paymentData.setExpiry(getExpiredateAsLong(paymentType.getExpiry()));
		paymentData.setIdentifier(paymentType.getPan());
		paymentData.setProfileID(profileId);
		return paymentData;
	}

	private PaymentData mapPaymentData(TokenPaymentType paymentType, String profileId) {
		PaymentDataTOKEN paymentData = new PaymentDataTOKEN();
		paymentData.setToken(paymentType.getToken());
		paymentData.setProfileID(profileId);
		return paymentData;
	}

	public Payment mapPaymentSystemData(PaymentRequest paymentRequest, PaymentTypeData paymentType) {
		if (paymentType instanceof DirectDebitPaymentType) {
			return mapPaymentSystemData(paymentRequest, (DirectDebitPaymentType) paymentType);
		} else if (paymentType instanceof CreditCardPaymentType) {
			return mapPaymentSystemData(paymentRequest, (CreditCardPaymentType) paymentType);
		} else if (paymentType instanceof DebitCardPaymentType) {
			return mapPaymentSystemData(paymentRequest, (DebitCardPaymentType) paymentType);
		} else if (paymentType instanceof InvoicePaymentType) {
			return mapPaymentSystemData(paymentRequest, (InvoicePaymentType) paymentType);
		} else if (paymentType instanceof OnlineBankingPaymentType) {
			return mapPaymentSystemData(paymentRequest, (OnlineBankingPaymentType) paymentType);
		} else if (paymentType instanceof PaypalPaymentType) {
			return mapPaymentSystemData(paymentRequest, (PaypalPaymentType) paymentType);
		} else if (paymentType instanceof PayboxPaymentType) {
			return mapPaymentSystemData(paymentRequest, (PayboxPaymentType) paymentType);
		} else if (paymentType instanceof RecurringCreditCardPaymentType) {
			return mapPaymentSystemData(paymentRequest, (RecurringCreditCardPaymentType) paymentType);
		} else if (paymentType instanceof RecurringDirectDebitPaymentType) {
			return mapPaymentSystemData(paymentRequest, (RecurringDirectDebitPaymentType) paymentType);
		} else if (paymentType instanceof PaysafecardPaymentType) {
			return mapRedirectPaymentSystemData(paymentRequest);
		} else if (paymentType instanceof QuickPaymentType) {
			return mapRedirectPaymentSystemData(paymentRequest);
		} else if (paymentType instanceof TokenPaymentType) {
			return mapRedirectPaymentSystemData(paymentRequest, (TokenPaymentType) paymentType);
		} else {
			return null;
		}
	}

	public Order mapOrder(PaymentRequest paymentRequest, Customer customer, ShoppingCart shoppingCart) {
		if (customer == null && shoppingCart == null)
			return null;
		Order order = new Order();
		order.setBilling(mapCustomer(customer));
		order.setShoppingCart(mapShoppingCart(shoppingCart));
		return order;
	}

	private com.mpay.soap.client.ShoppingCart mapShoppingCart(ShoppingCart shoppingCart) {
		if (shoppingCart == null)
			return null;
		com.mpay.soap.client.ShoppingCart soapShoppingCart = new com.mpay.soap.client.ShoppingCart();
		soapShoppingCart.setDiscount(convertBigDecimalToInteger(shoppingCart.getDiscount()));
		soapShoppingCart.setShippingCosts(convertBigDecimalToInteger(shoppingCart.getShippingCost()));
		soapShoppingCart.setShippingTax(convertBigDecimalToInteger(shoppingCart.getShippingCostTax()));
		soapShoppingCart.setTax(convertBigDecimalToInteger(shoppingCart.getTax()));
		if (shoppingCart.getItemList() != null) {
			for (ShoppingCartItem shoppingCartItem : shoppingCart.getItemList()) {
				Item item = new Item();
				item.setAmount(convertBigDecimalToInteger(shoppingCartItem.getAmount()));
				item.setDescription(shoppingCartItem.getDescription());
				item.setNumber(shoppingCartItem.getSequenceId());
				item.setProductNr(shoppingCartItem.getProductCode());
				item.setQuantity(shoppingCartItem.getQuantity());
				item.setTax(convertBigDecimalToInteger(shoppingCartItem.getTax()));
				soapShoppingCart.getItem().add(item);
			}

		}
		return soapShoppingCart;
	}

	private Integer convertBigDecimalToInteger(BigDecimal amount) {
		if (amount == null)
			return null;
		return convertToCentAmount(amount).intValue();
	}

	private Double convertToCentAmount(BigDecimal amount) {
		BigDecimal convertedAmount = new BigDecimal(amount.multiply(new BigDecimal(100)).doubleValue());
		convertedAmount.setScale(2, RoundingMode.HALF_UP);
		return convertedAmount.doubleValue();
	}

	private Payment mapPaymentSystemData(PaymentRequest paymentRequest, CreditCardPaymentType paymentTypeData) {
		PaymentCC payment = new PaymentCC();
		payment.setAmount(convertBigDecimalToInteger(paymentRequest.getAmount()));
		payment.setCurrency(paymentRequest.getCurrency());
		payment.setAuth3DS(paymentTypeData.getAuth3DS());
		if (paymentTypeData.getBrand() != null) {
			payment.setBrand(paymentTypeData.getBrand().toString());
		}
		payment.setCvc(paymentTypeData.getCvc());
		payment.setExpiry(getExpiredateAsLong(paymentTypeData.getExpiry()));
		payment.setIdentifier(paymentTypeData.getPan());
		payment.setUseProfile(paymentRequest.isSavePaymentData());
		payment.setProfileID(paymentRequest.getStoredPaymentDataId());
		return payment;
	}

	private Payment mapPaymentSystemData(PaymentRequest paymentRequest, DebitCardPaymentType paymentTypeData) {
		PaymentMAESTRO payment = new PaymentMAESTRO();
		payment.setAmount(convertBigDecimalToInteger(paymentRequest.getAmount()));
		payment.setCurrency(paymentRequest.getCurrency());
		payment.setExpiry(getExpiredateAsLong(paymentTypeData.getExpiry()));
		payment.setIdentifier(paymentTypeData.getPan());
		payment.setUseProfile(paymentRequest.isSavePaymentData());
		payment.setProfileID(paymentRequest.getStoredPaymentDataId());
		return payment;
	}

	private Payment mapPaymentSystemData(PaymentRequest paymentRequest, DirectDebitPaymentType paymentTypeData) {
		PaymentELV payment = new PaymentELV();
		payment.setAmount(convertBigDecimalToInteger(paymentRequest.getAmount()));
		payment.setCurrency(paymentRequest.getCurrency());
		payment.setIban(paymentTypeData.getIban());
		payment.setBic(paymentTypeData.getBic());
		payment.setMandateID(paymentTypeData.getMandateID());
		payment.setDateOfSignature(paymentTypeData.getDateOfSignature());
		payment.setBrand(paymentTypeData.getBrand().toString());
		payment.setUseProfile(paymentRequest.isSavePaymentData());
		payment.setProfileID(paymentRequest.getStoredPaymentDataId());
		return payment;
	}

	private Payment mapPaymentSystemData(PaymentRequest paymentRequest, InvoicePaymentType paymentTypeData) {
		if (paymentTypeData.getBrand() == Brand.BILLPAY) {
			PaymentBILLPAY payment = new PaymentBILLPAY();
			payment.setAmount(convertBigDecimalToInteger(paymentRequest.getAmount()));
			payment.setBrand(paymentTypeData.getPaymentType().toString());
			payment.setCurrency(paymentRequest.getCurrency());
			paymentTypeData.setPaymentType(PaymentType.BILLPAY);
			payment.setUseProfile(paymentRequest.isSavePaymentData());
			payment.setProfileID(paymentRequest.getStoredPaymentDataId());
			return payment;
		} else {
			PaymentKLARNA payment = new PaymentKLARNA();
			payment.setAmount(convertBigDecimalToInteger(paymentRequest.getAmount()));
			payment.setBrand(paymentTypeData.getPaymentType().toString());
			payment.setCurrency(paymentRequest.getCurrency());
			payment.setPClass(paymentTypeData.getKlarnaPclass());
			payment.setPersonalNumber(paymentTypeData.getKlarnaPersonalNumber());
			paymentTypeData.setPaymentType(PaymentType.KLARNA);
			payment.setUseProfile(paymentRequest.isSavePaymentData());
			payment.setProfileID(paymentRequest.getStoredPaymentDataId());
			return payment;
		}
	}

	private Payment mapPaymentSystemData(PaymentRequest paymentRequest, OnlineBankingPaymentType paymentTypeData) {
		if (paymentTypeData.getBrand() == com.mpay24.payment.type.OnlineBankingPaymentType.Brand.EPS) {
			PaymentEPS payment = new PaymentEPS();
			payment.setAmount(convertBigDecimalToInteger(paymentRequest.getAmount()));
			payment.setCurrency(paymentRequest.getCurrency());
			payment.setBankID(paymentTypeData.getStuzzaBankId());
			payment.setBic(paymentTypeData.getBic());
			payment.setBrand(paymentTypeData.getBrand().toString());
			payment.setUseProfile(paymentRequest.isSavePaymentData());
			payment.setProfileID(paymentRequest.getStoredPaymentDataId());
			return payment;
		} else if (paymentTypeData.getBrand() == com.mpay24.payment.type.OnlineBankingPaymentType.Brand.SOFORT) {
			Payment payment = new Payment();
			payment.setAmount(convertBigDecimalToInteger(paymentRequest.getAmount()));
			payment.setCurrency(paymentRequest.getCurrency());
			payment.setUseProfile(paymentRequest.isSavePaymentData());
			payment.setProfileID(paymentRequest.getStoredPaymentDataId());
			return payment;
		} else if (paymentTypeData.getBrand() == com.mpay24.payment.type.OnlineBankingPaymentType.Brand.GIROPAY) {
			PaymentGIROPAY payment = new PaymentGIROPAY();
			payment.setAmount(convertBigDecimalToInteger(paymentRequest.getAmount()));
			payment.setCurrency(paymentRequest.getCurrency());
			payment.setBic(paymentTypeData.getBic());
			payment.setIban(paymentTypeData.getIban());
			payment.setUseProfile(paymentRequest.isSavePaymentData());
			payment.setProfileID(paymentRequest.getStoredPaymentDataId());
			return payment;
		}
		return null;
	}

	private Payment mapPaymentSystemData(PaymentRequest paymentRequest, PaypalPaymentType paymentTypeData) {
		PaymentPAYPAL payment = new PaymentPAYPAL();
		payment.setAmount(convertBigDecimalToInteger(paymentRequest.getAmount()));
		payment.setCurrency(paymentRequest.getCurrency());
		payment.setCustom(paymentTypeData.getCustom());
		payment.setUseProfile(paymentRequest.isSavePaymentData());
		payment.setProfileID(paymentRequest.getStoredPaymentDataId());
		if (paymentTypeData.isExpressCheckout()) {
			payment.setCommit(false);
		}
		return payment;
	}

	private Payment mapPaymentSystemData(PaymentRequest paymentRequest, PayboxPaymentType paymentTypeData) {
		PaymentPB payment = new PaymentPB();
		payment.setAmount(convertBigDecimalToInteger(paymentRequest.getAmount()));
		payment.setCurrency(paymentRequest.getCurrency());
		payment.setIdentifier(paymentTypeData.getMobilePhoneNumber());
		payment.setPayDays(paymentTypeData.getPayDays());
		payment.setReserveDays(paymentTypeData.getReserveDays());
		payment.setUseProfile(paymentRequest.isSavePaymentData());
		payment.setProfileID(paymentRequest.getStoredPaymentDataId());
		return payment;
	}

	private Payment mapPaymentSystemData(PaymentRequest paymentRequest, RecurringDirectDebitPaymentType paymentTypeData) {
		ProfilePaymentELV payment = new ProfilePaymentELV();
		payment.setAmount(convertBigDecimalToInteger(paymentRequest.getAmount()));
		payment.setCurrency(paymentRequest.getCurrency());
		payment.setDateOfSignature(paymentTypeData.getDateOfSignature());
		payment.setMandateID(paymentTypeData.getMandateID());
		payment.setUseProfile(true);
		return payment;
	}

	private Payment mapPaymentSystemData(PaymentRequest paymentRequest, RecurringCreditCardPaymentType paymentTypeData) {
		ProfilePaymentCC payment = new ProfilePaymentCC();
		payment.setAmount(convertBigDecimalToInteger(paymentRequest.getAmount()));
		payment.setCurrency(paymentRequest.getCurrency());
		payment.setCvc(paymentTypeData.getCvc());
		payment.setAuth3DS(paymentTypeData.isAuth3DS());
		return payment;
	}

	private Payment mapRedirectPaymentSystemData(PaymentRequest paymentRequest) {
		Payment payment = new Payment();
		payment.setAmount(convertBigDecimalToInteger(paymentRequest.getAmount()));
		payment.setCurrency(paymentRequest.getCurrency());
		payment.setUseProfile(paymentRequest.isSavePaymentData());
		payment.setProfileID(paymentRequest.getStoredPaymentDataId());
		return payment;
	}

	private Payment mapRedirectPaymentSystemData(PaymentRequest paymentRequest, TokenPaymentType paymentType) {
		PaymentTOKEN payment = new PaymentTOKEN();
		payment.setAmount(convertBigDecimalToInteger(paymentRequest.getAmount()));
		payment.setCurrency(paymentRequest.getCurrency());
		payment.setUseProfile(paymentRequest.isSavePaymentData());
		payment.setProfileID(paymentRequest.getStoredPaymentDataId());
		payment.setToken(paymentType.getToken());
		return payment;
	}


	protected Long getExpiredateAsLong(Date expiryDate) {
		String expiryDateAsString = new SimpleDateFormat("yyMM").format(expiryDate);
		return Long.valueOf(expiryDateAsString);
	}

	public Address mapCustomer(Customer customer) {
		if (customer == null)
			return null;
		if (customer.getAddress() == null)
			return null;
		Address soapAddress = new Address();
		soapAddress.setMode(AddressMode.READONLY);
		soapAddress.setName(customer.getName());
		soapAddress.setBirthday(customer.getBirthdate());
		soapAddress.setEmail(customer.getEmail());
		if (customer.getGender() != null) {
			soapAddress.setGender(Gender.fromValue(customer.getGender().toString().toUpperCase()));
		}
		soapAddress.setPhone(customer.getPhoneNumber());
		mapAddress(soapAddress, customer.getAddress());
		return soapAddress;
	}

	private void mapAddress(Address soapAddress, com.mpay24.payment.data.Address address) {
		if (address == null)
			return;
		if (address.isEditable()) {
			soapAddress.setMode(AddressMode.READWRITE);
		}
		soapAddress.setCity(address.getCity());
		soapAddress.setCountryCode(address.getCountryIso2());
		soapAddress.setState(address.getState());
		soapAddress.setStreet(address.getStreet());
		soapAddress.setStreet2(address.getStreet2());
		soapAddress.setZip(address.getZip());
	}

}
