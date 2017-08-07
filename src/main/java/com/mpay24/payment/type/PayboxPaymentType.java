package com.mpay24.payment.type;

import com.mpay.soap.client.PaymentType;

public class PayboxPaymentType extends PaymentTypeData {
	private String mobilePhoneNumber;
	private Long payDays;
	private Long reserveDays;

	public PayboxPaymentType() {
		super(PaymentType.PB);
	}

	public String getMobilePhoneNumber() {
		return mobilePhoneNumber;
	}

	public void setMobilePhoneNumber(String mobilePhoneNumber) {
		this.mobilePhoneNumber = mobilePhoneNumber;
	}

	public Long getPayDays() {
		return payDays;
	}

	public void setPayDays(Long payDays) {
		this.payDays = payDays;
	}

	public Long getReserveDays() {
		return reserveDays;
	}

	public void setReserveDays(Long reserveDays) {
		this.reserveDays = reserveDays;
	}

	
}
