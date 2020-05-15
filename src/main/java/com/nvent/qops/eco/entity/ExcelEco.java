package com.nvent.qops.eco.entity;

public class ExcelEco {
	private String itemNumber;
	private String itemDescription;
	private String stockingType;
	private String drawingNumber;
	private String orderNumber;
	private String invFamily;
	private String sopFamily;
	private String serviceType;
	private String rawMaterial;
	
	
	//defined fields out of Excel Date
	private boolean matchEco;
	private boolean finSubmit;
	private boolean seSubmit;
	private boolean deleteEco;
	private String deStockingType;
	private String deDrawingNumber;
	
	private String vendorCode;
	private String unitPrice;
	private String freight;
	private String duty;
	private String currencyCode;
	private String tradeTerms;
	
	

	public boolean isFinSubmit() {
		return finSubmit;
	}

	public void setFinSubmit(boolean finSubmit) {
		this.finSubmit = finSubmit;
	}

	public boolean isSeSubmit() {
		return seSubmit;
	}

	public void setSeSubmit(boolean seSubmit) {
		this.seSubmit = seSubmit;
	}

	public String getVendorCode() {
		return vendorCode;
	}

	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}

	public String getTradeTerms() {
		return tradeTerms;
	}

	public void setTradeTerms(String tradeTerms) {
		this.tradeTerms = tradeTerms;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getDuty() {
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	public String getFreight() {
		return freight;
	}

	public void setFreight(String freight) {
		this.freight = freight;
	}

	public String getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public String getStockingType() {
		return stockingType;
	}

	public void setStockingType(String stockingType) {
		this.stockingType = stockingType;
	}

	public String getDrawingNumber() {
		return drawingNumber;
	}

	public void setDrawingNumber(String drawingNumber) {
		this.drawingNumber = drawingNumber;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getInvFamily() {
		return invFamily;
	}

	public void setInvFamily(String invFamily) {
		this.invFamily = invFamily;
	}

	public boolean isMatchEco() {
		return matchEco;
	}

	public void setMatchEco(boolean matchEco) {
		this.matchEco = matchEco;
	}



	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getSopFamily() {
		return sopFamily;
	}

	public void setSopFamily(String sopFamily) {
		this.sopFamily = sopFamily;
	}

	public String getDeStockingType() {
		return deStockingType;
	}

	public void setDeStockingType(String deStockingType) {
		this.deStockingType = deStockingType;
	}

	public String getDeDrawingNumber() {
		return deDrawingNumber;
	}

	public void setDeDrawingNumber(String deDrawingNumber) {
		this.deDrawingNumber = deDrawingNumber;
	}

	public String getRawMaterial() {
		return rawMaterial;
	}

	public void setRawMaterial(String rawMaterial) {
		this.rawMaterial = rawMaterial;
	}

	public boolean isDeleteEco() {
		return deleteEco;
	}

	public void setDeleteEco(boolean deleteEco) {
		this.deleteEco = deleteEco;
	}


}
