package com.example.demo.error;

public class EmailExistedException extends RuntimeException {
	
	public EmailExistedException(String email) {
		super("El email ya existe");
	}

}
