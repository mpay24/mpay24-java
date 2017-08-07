package com.mpay24.payment.data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import com.mpay.soap.client.PaymentType;

public class Payment {

	private String returnCode;
	private String redirectLocation;
	private BigInteger mPayTid;

	private String transactionId;
	private String description;
	private BigDecimal amount;
	private String currency;
	private PaymentType paymentType;
	private State state;
	private Date timeStamp;
    private BigInteger stateID;
    private BigInteger parentStateID;
    private String approvalCode;
    private Integer errorNumber;
    private String errorText;
    private String profileStatus;
    private Customer customer = new Customer();

	
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public String getRedirectLocation() {
		return redirectLocation;
	}
	public void setRedirectLocation(String redirectLocation) {
		this.redirectLocation = redirectLocation;
	}
	
	public BigInteger getmPayTid() {
		return mPayTid;
	}
	public void setmPayTid(BigInteger mPayTid) {
		this.mPayTid = mPayTid;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	public Date getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
	public BigInteger getStateID() {
		return stateID;
	}
	public void setStateID(BigInteger stateID) {
		this.stateID = stateID;
	}
	public BigInteger getParentStateID() {
		return parentStateID;
	}
	public void setParentStateID(BigInteger parentStateID) {
		this.parentStateID = parentStateID;
	}
	public String getApprovalCode() {
		return approvalCode;
	}
	public void setApprovalCode(String approvalCode) {
		this.approvalCode = approvalCode;
	}
	public Integer getErrorNumber() {
		return errorNumber;
	}
	public void setErrorNumber(Integer errorNumber) {
		this.errorNumber = errorNumber;
	}
	public String getErrorText() {
		return errorText;
	}
	public void setErrorText(String errorText) {
		this.errorText = errorText;
	}
	public String getProfileStatus() {
		return profileStatus;
	}
	public void setProfileStatus(String profileStatus) {
		this.profileStatus = profileStatus;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public PaymentType getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
}
