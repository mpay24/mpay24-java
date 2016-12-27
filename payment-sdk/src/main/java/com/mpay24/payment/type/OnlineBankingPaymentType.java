package com.mpay24.payment.type;

import com.mpay.soap.client.PaymentType;

public class OnlineBankingPaymentType extends PaymentTypeData {
	private Brand brand;
	private String bic;
	private Long stuzzaBankId;
	private String iban;

	public enum Brand {
		EPS, SOFORT, GIROPAY
	}
	
	public OnlineBankingPaymentType() {
		super(null);
	}
	
	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		setPaymentType(PaymentType.fromValue(brand.toString()));
		this.brand = brand;
	}

	public String getBic() {
		return bic;
	}

	public void setBic(String bic) {
		this.bic = bic;
	}

	public Long getStuzzaBankId() {
		return stuzzaBankId;
	}

	public void setStuzzaBankId(Long stuzzaBankId) {
		this.stuzzaBankId = stuzzaBankId;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}
	

}
