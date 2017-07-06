package com.mpay24.payment.data;

import java.util.Date;

public class Customer {
	
	public enum Gender {
		Male("M"), Female("F");
		private String shortcut;
		private Gender (String shortcut) {
			this.shortcut = shortcut;
		}
		public String getShort() {
			return shortcut;
		}
		public static Gender getEnum(String gender) {
			if (gender == null) {
				return Female;
			} else if (gender.startsWith("M")) {
				return Male;
			} else {
				return Female;
			}
		}
	}
	private String customerId;
	private String name;
	private Gender gender;
	private Date birthdate;
	private String email;
	private String phoneNumber;
	private String clientIp;
	private Address address;
	private Address shippingAddress;

	
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getClientIp() {
		return clientIp;
	}
	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address billingAddress) {
		this.address = billingAddress;
	}
	public Address getShippingAddress() {
		return shippingAddress;
	}
	public void setShippingAddress(Address shippingAddress) {
		this.shippingAddress = shippingAddress;
	}
	public Gender getGender() {
		return gender;
	}
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	public Date getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

}
