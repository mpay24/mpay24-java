package com.mpay24.payment.data;

public class StylingOptions {
	public enum Template {
		DEFAULT, MOBILE, MODERN;
	}
	
	private Template template;

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}
}
