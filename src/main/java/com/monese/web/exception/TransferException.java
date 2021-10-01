package com.monese.web.exception;

public class TransferException extends RuntimeException {

	private static final long serialVersionUID = 442681069804339550L;

	public TransferException(String msg) {
		super(msg);
	}
}