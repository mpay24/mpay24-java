package com.mpay24.payment.type;

import com.mpay.soap.client.PaymentType;

public class InstallmentPaymentType extends InvoicePaymentType {

	public InstallmentPaymentType() {
		super(PaymentType.HP);
	}

}
