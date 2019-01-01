package com.testfork.wechat_gate.domain;

public class BankTemplate {
	private String userType;
	private String mobile;
	private String tradeType;
	private String reason;
	private String effectDate;
	private String expiryDate;
	private Double balance;
	public Double getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = Double.valueOf(balance);
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getTradeType() {
		return tradeType;
	}
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getEffectDate() {
		return effectDate;
	}
	public void setEffectDate(String effectDate) {
		this.effectDate = effectDate;
	}
	public String getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
	@Override
	public String toString() {
		return "BankTemplate [userType=" + userType + ", mobile=" + mobile + ", tradeType=" + tradeType + ", reason="
				+ reason + ", effectDate=" + effectDate + ", expiryDate=" + expiryDate + ", balance=" + balance + "]";
	}
	
	
	
}
