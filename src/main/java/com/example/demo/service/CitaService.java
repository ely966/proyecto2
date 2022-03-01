package com.example.demo.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.example.demo.model.Cita;
import com.example.demo.model.CreadencialesCitaConId;
import com.example.demo.model.CredencialesCita;
import com.example.demo.model.Mascota;
import com.example.demo.model.User;
import com.example.demo.repository.CitasRepository;

@Primary
@Service("CitaService")
public class CitaService {
	@Autowired CitasRepository citaRepo;
	 @Autowired private MascotaService mascotaServi;
	
	 /**
	  * Añade una nueva cita
	  * @param cita
	  * @param usuario,cita
	  * @return cita nueva
	  */
	public Cita addCita(CredencialesCita cita,User usuario, Long pet) {
		Cita nuevaCita = new Cita();
		nuevaCita.setCliente(usuario);
		nuevaCita.setFecha(cita.getFecha());
		nuevaCita.setHora(cita.getFecha());
		nuevaCita.setPet(mascotaServi.encontrarId(pet));
		//nuevaCita.setIdpet(pet);
		nuevaCita.setMotivo(cita.getMotivo());
		citaRepo.save(nuevaCita);
		return nuevaCita;
	}
	
	/**
	 * Encontrar una cita por su id
	 * @param id
	 * @return cita encontrada
	 */
	public Cita findCita(Long id){
		Cita cita = new Cita();
		List<Cita> citas=citaRepo.findAll();
		for(int i =0; i < citaRepo.count(); i=i+1) {
			if (citas.get(i).getId().equals(id)) {
				cita= citas.get(i);
			}
		}
		return null;
	}
	
	/**
	 * Mostrar la lista de cita d eun cliente
	 * @param cliente
	 * @return lista de cita de un cliente
	 */
	public List<Cita> mostrarCitasUser(User cliente){
		return cliente.getCitas();
		
	}
	/**
	 * Eliminar una cita
	 * @param usuario
	 * @param cita
	 * @return 
	 */
	public Cita delete(User usuario,Cita cita) {
		/**Eliminamos su union con la mascota**/
		cita.setPet(null);
		//**Eliminamos su union con cliente**/
		cita.setCliente(null);
		//**Guardamos la cita sin uniones**/
		citaRepo.save(cita);
		citaRepo.delete(cita);/**Eliminamos la cita**/
		return cita;
	}
	
	/**
	 * Borrar las citas de una mascota
	 * @param id de pet
	 */
	
	public void deleteByPet(Long id) {
		List<Cita>citas=citaRepo.findAll();
		for (int i=0; citaRepo.count() > i;i=i+1) {
			if(citas.get(i).getPet().getId() == id) {
				citas.get(i).setPet(null);
				citas.get(i).setCliente(null);
				citaRepo.save(citas.get(i));
				citaRepo.deleteById(citas.get(i).getId());
			}
		}
	}
	
	public Cita editarCita(CreadencialesCitaConId cita,User usuario, Long id) {
		Cita citaEditada = new Cita();
		citaEditada.setId(cita.getId());
		citaEditada.setCliente(usuario);
		citaEditada.setFecha(cita.getFecha());
		citaEditada.setHora(cita.getFecha());
		
		Mascota mascota= mascotaServi.encontrarId(cita.getPetid());
		citaEditada.setPet(mascotaServi.encontrarId(cita.getPetid()));
		citaEditada.setMotivo(cita.getMotivo());
		citaRepo.save(citaEditada);
		return citaEditada;
	}
	
	
}
