package com.mpay24.payment.type;

import java.util.Date;

import com.mpay.soap.client.PaymentType;

public class DebitCardPaymentType extends PaymentTypeData {
	private Brand brand;
	private String pan;
	private Date expiry;

	public enum Brand {
		MAESTRO
	}


	public DebitCardPaymentType() {
		super(PaymentType.MAESTRO);
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

}
