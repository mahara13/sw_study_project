package vmstudy.sw.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vmstudy.sw.db.models.PersonageCall;
import vmstudy.sw.repository.PersonageCallRepository;

@Service
public class PersonageCallService {
	
	@Autowired
	PersonageCallRepository personageCallRepository;
	
	public PersonageCall getLastCall() {
		return personageCallRepository.findTopByOrderByIdDesc();
	}
	
	public void savePersonageCall(PersonageCall personageCall) {
		personageCallRepository.save(personageCall);
	}

}
