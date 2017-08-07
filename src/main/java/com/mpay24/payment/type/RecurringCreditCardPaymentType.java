package com.mpay24.payment.type;

public class RecurringCreditCardPaymentType extends PaymentTypeData {
    private String cvc;
    private boolean auth3DS = false;

	public RecurringCreditCardPaymentType() {
		super(com.mpay.soap.client.PaymentType.PROFILE);
	}

	public String getCvc() {
		return cvc;
	}

	public void setCvc(String cvc) {
		this.cvc = cvc;
	}

	public boolean isAuth3DS() {
		return auth3DS;
	}

	public void setAuth3DS(boolean auth3ds) {
		auth3DS = auth3ds;
	}

}
