package com.mpay24.payment.data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
	private List<ShoppingCartItem> itemList = new ArrayList<ShoppingCartItem>();
	private String description;
	private BigDecimal tax;
	private BigDecimal taxPercentage;
	private BigDecimal discount;
	private BigDecimal subTotal;
	private BigDecimal shippingCost;
	private BigDecimal shippingCostTax;
	
	
	public List<ShoppingCartItem> getItemList() {
		return itemList;
	}
	public void setItemList(List<ShoppingCartItem> itemList) {
		this.itemList = itemList;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public BigDecimal getTax() {
		return tax;
	}
	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}
	public BigDecimal getDiscount() {
		return discount;
	}
	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}
	public BigDecimal getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}
	public BigDecimal getShippingCost() {
		return shippingCost;
	}
	public void setShippingCost(BigDecimal shippingCost) {
		this.shippingCost = shippingCost;
	}
	public BigDecimal getTaxPercentage() {
		return taxPercentage;
	}
	public void setTaxPercentage(BigDecimal taxPercentage) {
		this.taxPercentage = taxPercentage;
	}
	public void addItem(ShoppingCartItem shoppingCartItem) {
		if (itemList == null) itemList = new ArrayList<ShoppingCartItem>();
		getItemList().add(shoppingCartItem);
	}
	public BigDecimal getShippingCostTax() {
		return shippingCostTax;
	}
	public void setShippingCostTax(BigDecimal shippingCostTax) {
		this.shippingCostTax = shippingCostTax;
	}
}
