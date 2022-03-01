package com.example.demo.error;

public class CitaExistedException extends RuntimeException {
	public CitaExistedException(Long id) {
		super("No existe esa cita");
	}
}
