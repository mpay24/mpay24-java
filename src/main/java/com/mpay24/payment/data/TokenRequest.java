package com.mpay24.payment.data;

import com.mpay.soap.client.PaymentType;

public class TokenRequest {
	private PaymentType paymentType;
	private String templateSet;
	private String style;
	private String customerId;
	private String profileId;
	private String domain;
	private String language;
	
	public PaymentType getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}
	public String getTemplateSet() {
		return templateSet;
	}
	public void setTemplateSet(String templateSet) {
		this.templateSet = templateSet;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getProfileId() {
		return profileId;
	}
	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
}
