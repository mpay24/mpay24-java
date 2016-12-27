package com.mpay24.payment.data;

import java.math.BigInteger;

import com.mpay.soap.client.TStatus;

public class Refund {
	private BigInteger mPayTid;
	private TStatus state;
	private BigInteger stateID;
	private String transactionId;
	private String returnCode;

	public BigInteger getmPayTid() {
		return mPayTid;
	}
	public void setmPayTid(BigInteger mPayTid) {
		this.mPayTid = mPayTid;
	}
	public TStatus getState() {
		return state;
	}
	public void setState(TStatus status) {
		this.state = status;
	}
	public BigInteger getStateID() {
		return stateID;
	}
	public void setStateID(BigInteger stateID) {
		this.stateID = stateID;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

}
