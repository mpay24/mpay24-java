package com.mpay24.payment.type;

import com.mpay.soap.client.PaymentType;

public class PaypalPaymentType extends PaymentTypeData {
	private boolean expressCheckout = false;
	private String custom;

	public PaypalPaymentType() {
		super(PaymentType.PAYPAL);
	}

	public boolean isExpressCheckout() {
		return expressCheckout;
	}

	public void setExpressCheckout(boolean expressCheckout) {
		this.expressCheckout = expressCheckout;
	}

	public String getCustom() {
		return custom;
	}

	public void setCustom(String custom) {
		this.custom = custom;
	}



}
