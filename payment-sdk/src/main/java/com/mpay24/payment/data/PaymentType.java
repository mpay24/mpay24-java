package com.mpay24.payment.data;

public enum PaymentType {
	CreditCard("CC", null),

	CreditCard_Amex("CC", "AMEX", 1),

	CreditCard_Diners("CC", "DINERS", 2),

	CreditCard_Jcb("CC", "JCB", 3),

	CreditCard_Mastercard("CC", "MASTERCARD", 4),

	CreditCard_Visa("CC", "VISA", 5),

	CreditCard_Airplus("CC", "AIRPLUS", 6),

	CardBleu("CB", 9),

	Maestro("MAESTRO", 10),

	EPS("EPS", 20),

	PayBox("PB", 43),

	Giropay("GIROPAY", 50),

	DirectDebit("ELV", null),

	DirectDebit_Atos("ELV", "ATOS", 60),

	DirectDebit_Hobex_AT("ELV", "HOBEX-AT", 61),

	DirectDebit_Hobex_DE("ELV", "HOBEX-DE", 62),

	DirectDebit_Hobex_NL("ELV", "HOBEX-NL", 63),

	DirectDebit_Billpay("ELV", "BILLPAY", 65),

	DirectDebit_B4P("ELV", "B4P", 66),

	Paypal("PAYPAL", 70), Paysafecard("PSC", 80),

	Invoice("INVOICE", 110),

	Invoice_Billpay("BILLPAY", "INVOICE", 111),

	Invoice_Klarna("KLARNA", "INVOICE", 112),

	Installment_Billpay("BILLPAY", "HP", 121),

	Installment_Klarna("KLARNA", "HP", 122),

	Sofort("SOFORT", 140), MasterPass("MASTERPASS", 150);

	private String paymentType;
	private String brand;
	private Integer id;

	private PaymentType(String paymentType, Integer id) {
		this.paymentType = paymentType;
		this.id = id;
	}

	private PaymentType(String paymentType, String brand, Integer id) {
		this.paymentType = paymentType;
		this.brand = brand;
		this.id = id;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public String getBrand() {
		return brand;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public static com.mpay24.payment.data.PaymentType fromId(Integer paymentMethodId) {
		for (PaymentType paymentType : PaymentType.values()) {
			if (paymentType.id == paymentMethodId) {
				return paymentType;
			}
		}
		throw new IllegalArgumentException("The Payment Method Id " + paymentMethodId + " does not exist!");

	}

}
