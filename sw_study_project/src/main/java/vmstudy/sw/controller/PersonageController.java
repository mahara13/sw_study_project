package vmstudy.sw.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vmstudy.sw.db.models.Personage;
import vmstudy.sw.db.models.PersonageCall;
import vmstudy.sw.repository.PersonageCallRepository;
import vmstudy.sw.repository.PersonageRepository;
import vmstudy.sw.services.PersonageService;

@RestController
@RequestMapping("/api")
public class PersonageController {

	@Autowired
	private PersonageRepository personageRepository;
	
	@Autowired
	private PersonageCallRepository personageCallRepository;
	
	@Autowired
	private PersonageService personageService;
	
	@GetMapping("/personages")
	public List<Personage> getAllNotes() {
	    return personageRepository.findAll();
	}
	
	@PostMapping("/personages")
	public Personage createNote(@Valid @RequestBody Personage personage) {
	    return personageRepository.save(personage);
	}
	
	@RequestMapping(value="/find/personage", method = RequestMethod.GET)
	public List<String> searchPersonages(@RequestParam("s") String s){
		return personageService.searchByWordStarting(s);
	}
	
	@RequestMapping(value="/personageCallStatistic", method = RequestMethod.GET)
	public List<String> getPersogeCallStatistic(){
		List<PersonageCall> findAll = personageCallRepository.findAll();
		List<String> statistic = findAll.stream().map(pc -> (pc.isFinished() ? "Finished at" : " Processed at ") + pc.getUpdatedAt() +
									", Num of calls performed " + pc.getCalls_count() + 
									", Num of failing calls " + pc.getFailing_calls_count() +
									(pc.isFinished() ? "" : ", Last failled URL " + pc.getUrl()))
						.collect(Collectors.toList());
		return statistic;
	}
}
