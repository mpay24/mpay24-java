package com.mpay24.payment.type;

import java.util.Date;

import com.mpay.soap.client.PaymentType;

public class CreditCardPaymentType extends PaymentTypeData {
	private Brand brand;
	private String pan;
	private Date expiry;
	private String cvc;
	private Boolean auth3DS;

	public enum Brand {
		VISA, MASTERCARD, DINERS, JCB, AMEX
	}


	public CreditCardPaymentType() {
		super(PaymentType.CC);
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public Date getExpiry() {
		return expiry;
	}

	public void setExpiry(Date expiry) {
		this.expiry = expiry;
	}

	public String getCvc() {
		return cvc;
	}

	public void setCvc(String cvc) {
		this.cvc = cvc;
	}

	public Boolean getAuth3DS() {
		return auth3DS;
	}

	public void setAuth3DS(Boolean auth3ds) {
		auth3DS = auth3ds;
	}

}
