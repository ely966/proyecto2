package com.example.demo.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MascotaExistedException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1875509380030645657L;

	public MascotaExistedException(Long id) {
		super("No existe esa mascota");
	}
}
