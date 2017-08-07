package com.mpay24.payment.data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PaymentRequest {

	public enum Language {
		EN,
	    DE,
	    BG,
	    FR,
	    HU,
	    NL,
	    ES,
	    IT,
	    CS,
	    HR,
	    SK,
	    SL,
	    SR,
	    RO,
	    RU,
	    PL,
	    PT,
	    TR,
	    ZH,
	    JA,
	    DA,
	    FI,
	    SV,
	    NO,
	    UK,
	    EL;
	}
	
	private static final String DEFAULT_CURRENCY_EUR = "EUR";
	private String transactionID;
	private BigDecimal amount;
	private String currency;
	private String userField;
	private String description;
	private String successUrl;
	private String errorUrl;
	private String confirmationUrl;
	private String token;
	private Language language;
	private boolean savePaymentData = false;
	private String storedPaymentDataId;

	private List<PaymentType> paymentTypeInclusionList = new ArrayList<PaymentType>();

	public String getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getSuccessUrl() {
		return successUrl;
	}

	public void setSuccessUrl(String successUrl) {
		this.successUrl = successUrl;
	}

	public String getErrorUrl() {
		return errorUrl;
	}

	public void setErrorUrl(String errorUrl) {
		this.errorUrl = errorUrl;
	}

	public String getConfirmationUrl() {
		return confirmationUrl;
	}

	public void setConfirmationUrl(String confirmationUrl) {
		this.confirmationUrl = confirmationUrl;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}


	public String getCurrency() {
		if (currency == null) {
			return DEFAULT_CURRENCY_EUR;
		} else {
			return currency;
		}
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getUserField() {
		return userField;
	}

	public void setUserField(String userField) {
		this.userField = userField;
	}

	public List<PaymentType> getPaymentTypeInclusionList() {
		return paymentTypeInclusionList;
	}

	public void setPaymentTypeInclusionList(List<PaymentType> paymentTypeInclusionList) {
		this.paymentTypeInclusionList = paymentTypeInclusionList;
	}
	public void addPaymentTypeInclusion(PaymentType paymentType) {
		if (this.paymentTypeInclusionList == null) paymentTypeInclusionList = new ArrayList<PaymentType>();
		getPaymentTypeInclusionList().add(paymentType);
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public String getStoredPaymentDataId() {
		return storedPaymentDataId;
	}

	public void setStoredPaymentDataId(String storedPaymentDataId) {
		this.storedPaymentDataId = storedPaymentDataId;
	}

	public boolean isSavePaymentData() {
		return savePaymentData;
	}

	public void setSavePaymentData(boolean savePaymentData) {
		this.savePaymentData = savePaymentData;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
