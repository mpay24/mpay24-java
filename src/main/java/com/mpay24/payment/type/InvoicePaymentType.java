package com.mpay24.payment.type;

import com.mpay.soap.client.PaymentType;

public class InvoicePaymentType extends PaymentTypeData {
	private Brand brand;
	private Long klarnaPclass;
	private String klarnaPersonalNumber;

	public enum Brand {
		BILLPAY,KLARNA
	}

	public InvoicePaymentType() {
		this(PaymentType.INVOICE);
	}
	protected InvoicePaymentType(PaymentType paymentType) {
		super(paymentType);
	}

	
	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public Long getKlarnaPclass() {
		return klarnaPclass;
	}

	public void setKlarnaPclass(Long klarnaPclass) {
		this.klarnaPclass = klarnaPclass;
	}

	public String getKlarnaPersonalNumber() {
		return klarnaPersonalNumber;
	}

	public void setKlarnaPersonalNumber(String klarnaPersonalNumber) {
		this.klarnaPersonalNumber = klarnaPersonalNumber;
	}

	
}
