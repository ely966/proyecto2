package com.example.demo.error;

public class MascotaExistedException extends RuntimeException {
	public MascotaExistedException(Long id) {
		super("No existe esa mascota");
	}
}
