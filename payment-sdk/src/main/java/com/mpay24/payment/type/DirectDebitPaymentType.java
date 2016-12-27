package com.mpay24.payment.type;

import java.util.Date;

import com.mpay.soap.client.PaymentType;

public class DirectDebitPaymentType extends PaymentTypeData {
	private Brand brand;
	private String iban;
	private String bic;
	private String mandateID;
	private Date dateOfSignature;

	public enum Brand {
		B4P("B4P"), HOBEX_AT("HOBEX-AT"), HOBEX_DE("HOBEX-DE"), HOBEX_NL("HOBEX-NL"), BILLPAY("BILLPAY"), ATOS("ATOS");
		
		private String value;
		private Brand(String value) {
			this.value = value;
		}
		public String toString() {
			return value;
		}
	}

	public DirectDebitPaymentType() {
		super(PaymentType.ELV);
	}
    
	public Brand getBrand() {
		return brand;
	}
	public void setBrand(Brand brand) {
		this.brand = brand;
	}
	public String getIban() {
		return iban;
	}
	public void setIban(String iban) {
		this.iban = iban;
	}
	public String getBic() {
		return bic;
	}
	public void setBic(String bic) {
		this.bic = bic;
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
