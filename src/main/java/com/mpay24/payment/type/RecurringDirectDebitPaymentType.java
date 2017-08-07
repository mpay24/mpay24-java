package com.mpay24.payment.type;

import java.util.Date;

public class RecurringDirectDebitPaymentType extends PaymentTypeData {

	private String mandateID;
    private Date dateOfSignature;

	public RecurringDirectDebitPaymentType() {
		super(com.mpay.soap.client.PaymentType.PROFILE);
	}

	public String getMandateID() {
		return mandateID;
	}

	public void setMandateID(String mandateID) {
		this.mandateID = mandateID;
	}

	public Date getDateOfSignature() {
		return dateOfSignature;
	}

	public void setDateOfSignature(Date dateOfSignature) {
		this.dateOfSignature = dateOfSignature;
	}

}
