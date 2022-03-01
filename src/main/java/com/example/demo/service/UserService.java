package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepo;

@Primary
@Service("UserService")
public class UserService {
	
	@Autowired
	private UserRepo userRepo;
	
    @Autowired private PasswordEncoder passwordEncoder;

	public List<User> findAll(){
		return userRepo.findAll();
	}
	
	public User findById (Long id) {
		return userRepo.getById(id);
	}
	
	public User add (User user) {
		return userRepo.save(user);
	}

	//public User edit (User user, Long id) {
		//if(userRepo.existsById(id)) {
		//	user.setId(id);
		//	return userRepo.save(user);
		//}else {
		//	return null;
		//}
	//}
	
	public User delete (Long id) {
		if(userRepo.existsById(id)) {
			
			User user = userRepo.findById(id).get();
			user.setMascotas(null);
			user.setCitas(null);
			userRepo.save(user);
			userRepo.deleteById(id);
			return user;
		}else {
			return null;
		}
	}
	
	
	public Boolean findByEmail(List<User> users, String emailNuevoCorreo){
		Boolean encontrado =false;
		int i=0;
		while ( i<users.size() && !encontrado) {
			if(users.get(i).getEmail().equals(emailNuevoCorreo)) {
				//Si el correo es encontrado. No puede repetirse el correo
				encontrado=true;
				return true;
				
			}
			else {
				i=i+1;
			}
			}
		return false;
	}
	
	public void init () {
		User userEjemplo= new User("lucinda","lucinda@gmail.com","a");
		String encodedPass = passwordEncoder.encode(userEjemplo.getPassword());
		userEjemplo.setPassword(encodedPass);
	    userRepo.save(userEjemplo);
	    
		User userEjwmplo2= new User("Pedro","pedro@gmail.com","a");
		String encodedPass2 = passwordEncoder.encode(userEjemplo.getPassword());
		userEjemplo.setPassword(encodedPass2);
	    userRepo.save(userEjwmplo2);
	}
}
