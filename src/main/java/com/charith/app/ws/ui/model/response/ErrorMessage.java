package com.charith.app.ws.ui.model.response;

import java.util.Date;

public class ErrorMessage {

	private Date date;
	private String message;

	public ErrorMessage(Date date, String message) {
		this.date = date;
		this.message = message;
	}

	public ErrorMessage() {
	}


	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

}
