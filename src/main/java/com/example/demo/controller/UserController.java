package com.example.demo.controller;


import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.error.CitaExistedException;
import com.example.demo.error.EmailExistedException;
import com.example.demo.error.MascotaExistedException;
import com.example.demo.model.Cita;
import com.example.demo.model.CreadencialesCitaConId;
import com.example.demo.model.CredencialesCita;
import com.example.demo.model.Mascota;
import com.example.demo.model.User;
import com.example.demo.repository.CitasRepository;
import com.example.demo.repository.MascotaRepository;
import com.example.demo.repository.UserRepo;
import com.example.demo.service.CitaService;
import com.example.demo.service.MascotaService;
import com.example.demo.service.UserService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class UserController {

    @Autowired private UserRepo userRepo;
    @Autowired private MascotaRepository mascotaRepo;
    @Autowired private MascotaService mascotaServi;
    @Autowired private CitaService citaServi;
    @Autowired private CitasRepository citaRepo;
    @Autowired private UserService serviUser;
    /**
     * Recoge el token y con este recoge la informacion
     * @return
     */
    @GetMapping("/user")
    public User getUserDetails(){
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepo.findByEmail(email).get();
    }
    
  /**Usuario**/
    
    @DeleteMapping("/cliente")
    public User borrarUsuario(){

        try {
     	String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
         User usuario = userRepo.findByEmail(email).get();
        serviUser.delete(usuario.getId());
        return usuario;
        }catch (AuthenticationException authExc){
            throw new RuntimeException("No tienes permiso o no tienes un usuario con permiso");
        } 
    }
    
 /**Mascota**/  
    /**
     *  Con este metodo añadiremos mascota nueva y gracia asu relacion, se le añadira al cliente
     * @param Trae la informacion de la mascota. Comprueba que el usuario
     * @return l amscota nueva
     */
    
    @PostMapping("/cliente/mascota")
    public Mascota addPet(@RequestBody Mascota mascota){
    
       try {
    	String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User usuario = userRepo.findByEmail(email).get();
        
        	if (usuario !=null) {
        		mascotaServi.addMascota(mascota, usuario); 

        		return mascota;
        	}else {
        		throw new RuntimeException("Incorrecto Crear mascota");
        	}
       }catch (AuthenticationException authExc){
           throw new RuntimeException("Incorrecto Crear mascota");
       }


       // return userRepo.findByEmail(email).get();//.get
    }
    

    /**
     * Con este metodo mostraremos las mascotas del usuario del token
     * Se recoge el email que es unico del token
     * @return
     */
    
    @GetMapping("/cliente/mascota")
    public List<Mascota> mascotasDelUser(){
    	
    	try {
    		String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User usuario = userRepo.findByEmail(email).get();
            if(usuario != null) {
            	 List<Mascota> mascotas =mascotaServi.mostrarMascotadeUser(usuario);
            	 return mascotas;
            }else {
            	 throw new IllegalArgumentException("\"El usuario no existe\"");
            }
    	}catch (AuthenticationException authExc){
            throw new RuntimeException("No tienes permiso");
        }      
 
    }
    
    @GetMapping("/cliente/mascota/{id}")
    public Mascota mascotaporId(@PathVariable Long id){
    	
    	try {
    		String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User usuario = userRepo.findByEmail(email).get();
            if(usuario != null) {
            	 return mascotaRepo.getById(id);
            }else {
            	 throw new IllegalArgumentException("\"El usuario no existe\"");
            }
    	}catch (AuthenticationException authExc){
            throw new RuntimeException("No tienes permiso");
        }      
 
    }
  
    
    /**
     * Borrar una mascota por su id
     * @param id
     * @return
     */
     
    @DeleteMapping("/cliente/mascota/{id}")
    public ResponseEntity<?> deletePets(@PathVariable Long id){
    	String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User usuario = userRepo.findByEmail(email).get();
    	
		if (usuario != null) {//*si el usuario no es null*/
			Boolean pet= mascotaRepo.existsById(id);
			
			if (pet) {//*Si la mascota existe**/
				Mascota MascotaSeleccionada= mascotaRepo.getById(id);
				citaServi.deleteByPet(id);
				mascotaServi.delete(MascotaSeleccionada);
				
				return ResponseEntity.noContent().build();
			}else {
				throw new MascotaExistedException(id);
			}
			
		
		} else {
			
			throw new EmailExistedException(email);
		}
        
    }
    
    
    
    
    /**
     * Editar la mascota. Recge usuario del token
     * @param mascota
     * @return
     */
    
    @PutMapping("/cliente/mascota")
    public Mascota editarMascota(@RequestBody Mascota mascota) {
    	String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User usuario = userRepo.findByEmail(email).get();
    	
		if (usuario == null) {
			throw new EmailExistedException(email);
		} else {
			
			Boolean pet= mascotaRepo.existsById(mascota.getId());
			if (pet) {/**Si existe la cita**/
				
				mascotaServi.editarMascota(mascota, usuario);
				
				return mascota;
			}else {
				throw new MascotaExistedException(mascota.getId());
			}
		
		}
        
    }
    
    
    
    
    
//**Citas**//
    
    /**
     * Con este metodo añadiremos cita nueva
     * @param Trae la informacion de la cita. COmprueba que el usuario y la mascota existe
     * @return
     * dato extra: falta añadir veterinario
     */
    @PostMapping("/cliente/mascota/{id}/cita")
    public Cita addCita(@RequestBody CredencialesCita cita,@PathVariable Long id){
        
        
        try {
        	String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User usuario = userRepo.findByEmail(email).get();
        	if(usuario != null) {
        	
        		Boolean pet= mascotaRepo.existsById(id);
        			if (pet) {//*Si la mascota existe**/
        				Mascota mascota = mascotaRepo.getById(id);
        				Cita nuevaCita=citaServi.addCita(cita, usuario, id);
            	    	//citaRepo.save(nuevaCita);
            	    	List<Cita>citas=citaRepo.findAll();
                		return nuevaCita;
        			}else {
        				throw new MascotaExistedException(cita.getPetid());
        			}
        	   
        	}
        	else {
           	 throw new IllegalArgumentException("\"El usuario no existe\"");
           }
        }catch (AuthenticationException authExc){
            throw new RuntimeException("Incorrecto Crear cita");
        }
    }
    
    /**
     * Mostrar la lista de citas veterinarias que ha solicitado el cliente
     * @return lista de citas veterinarias
     */
    
    @GetMapping("/cliente/cita")
    public List<Cita> citasDelUsuario(){
    	
    	try {
    		String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User usuario = userRepo.findByEmail(email).get();
            if(usuario != null) {
            	return citaServi.mostrarCitasUser(usuario);
            }else {
            	throw new IllegalArgumentException("\"El usuario no existe\"");	
            }
    	}catch (AuthenticationException authExc){
            throw new RuntimeException("No tienes permiso");
        }      
    }
    
    
    /**
     * Borrar cita 
     * @param id
     * @return
     */
    
    @DeleteMapping("/cliente/cita/{id}")
    public ResponseEntity<?> deleteCita(@PathVariable Long id){
    	String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User usuario = userRepo.findByEmail(email).get();
    	
		if (usuario == null) {
			throw new EmailExistedException(email);
		} else {
			
			Boolean cita = citaRepo.existsById(id);
			
			if (cita) {/**Si existe la cita**/
				
				Cita citaSeleccionada= citaRepo.getById(id);/**Recogemos la cita**/
				citaServi.delete(usuario,citaSeleccionada);
				
				return ResponseEntity.noContent().build();
			}else {
				throw new CitaExistedException(id);
			}
		
		}
        
    }
    
    /**
     * Editar una cita
     * @param mascota
     * @return cita editada
     */
    @PutMapping("/cliente/mascota/{id}/cita")
    public Cita editarMascota(@RequestBody CreadencialesCitaConId cita,@PathVariable Long id) {
    	String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User usuario = userRepo.findByEmail(email).get();
    	
		if (usuario == null) {
			throw new EmailExistedException(email);
		} else {
			
			Boolean citaExist = citaRepo.existsById(cita.getId());
			if (citaExist) {/**Si existe la cita**/
				
				return citaServi.editarCita(cita, usuario, id);
				
				
			}else {
				throw new CitaExistedException(cita.getId());
			}
		
		}
        
    }
    
    
    
    
    
   
}