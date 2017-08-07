package com.mpay24.payment;

import com.mpay.soap.client.Status;

public class PaymentException extends Exception {
	private static final long serialVersionUID = -8356132170233002179L;

	private Status status;
	private String errorCode;
	private String errorText;
	private String returnCode;

	public PaymentException() {
		super();
	}
	public PaymentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	public PaymentException(String message, Throwable cause) {
		super(message, cause);
	}
	public PaymentException(String message) {
		super(message);
	}
	public PaymentException(Throwable cause) {
		super(cause);
	}

	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorText() {
		return errorText;
	}
	public void setErrorText(String errorText) {
		this.errorText = errorText;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	@Override
	public String toString() {
		return "PaymentException [status=" + status + ", errorCode=" + errorCode + ", errorText=" + errorText + ", returnCode=" + returnCode + "]";
	}

}
