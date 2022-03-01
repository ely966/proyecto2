package com.example.demo.model;


import java.sql.Time;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor


public class CredencialesCita {
	private Date fecha;
	private Long petid;
	private String motivo;
	
	public CredencialesCita() {
		super();
	}
	
	
}
