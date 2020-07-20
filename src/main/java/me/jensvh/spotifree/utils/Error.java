package me.jensvh.spotifree.utils;

import java.io.PrintStream;

import me.jensvh.spotifree.Main;

public class Error extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final int errorCode;
	private final String cause;

	public Error(int code, Exception ex, String cause) {
		super(ex.getClass().getName(), ex);
		this.errorCode = code;
		this.cause = cause;
	}
	
	@Override
	public int hashCode() {
		return errorCode;
	}
	
	@Override
	public synchronized Throwable fillInStackTrace() {
		return this;
	}

	
	@Override
	public void printStackTrace(PrintStream s) {
		s.println();
		s.println(getMessage() + " with code: " + errorCode);
		s.println("Cause: " + cause);
		if (Main.debugging) {
			getCause().printStackTrace();
		}
	}
	
}
