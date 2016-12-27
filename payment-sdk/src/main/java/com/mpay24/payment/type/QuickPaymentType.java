package com.mpay24.payment.type;

import com.mpay.soap.client.PaymentType;

public class QuickPaymentType extends PaymentTypeData {

	public QuickPaymentType() {
		super(PaymentType.QUICK);
	}

}
