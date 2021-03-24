package com.charith.app.ws.ui.model.response;

public enum ErrorMessages {

	MISSING_REQUIRED_FIELD("Missing required field. Please check documentation for required fields."),
	USER_NOT_FOUND("Requested user not found in the database. Please check the user ID.");

	private String errorMessage;
	
	private ErrorMessages(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	
}
