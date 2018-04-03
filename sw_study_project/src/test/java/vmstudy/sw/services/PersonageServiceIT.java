package vmstudy.sw.services;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import vmstudy.sw.models.Personage;
import vmstudy.sw.models.PersonageCall;
import vmstudy.sw.repository.PersonageCallRepository;
import vmstudy.sw.repository.PersonageRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Import(PersonageService.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
public class PersonageServiceIT {

	private List<String> list = Stream.of("Luke Skywalker", "Luko Luko", "NewLuke Skywalker", "Bob Luk", "Should not be finded").collect(Collectors.toList());
	private List<String> searchedByLukList = Stream.of("Luke Skywalker", "Luko Luko", "NewLuke Skywalker", "Bob Luk").collect(Collectors.toList());
	
	private PersonageCall lastCall;
	
	@Autowired
    private TestEntityManager entityManager;
	
	@Autowired
	PersonageRepository personageRepository;
	
	@Autowired
	PersonageCallRepository personageCallRepository;
	
	@Autowired
	PersonageService  personageService;
	
	@Before
	public void before() {
		personageRepository.deleteAll();
		personageCallRepository.deleteAll();
		personageRepository.flush();
		personageCallRepository.flush();
		
		lastCall = new PersonageCall(PersonageCall.FINISHED_STATUS, new Date());
		entityManager.persist(lastCall);
		entityManager.flush();
		
		sleep(1000);
		
		for (String name : list) {
			entityManager.persist(new Personage(name));
		}
		entityManager.flush();
		
		sleep(1000);
		
		personageRepository.flush();
		personageCallRepository.flush();
		lastCall.setUpdatedAt(new Date());
		lastCall = entityManager.persist(lastCall);
		personageCallRepository.flush();
		
		entityManager.flush();
	}
	
	@Test
	public void cleanOldPersonages_ifWasCleaned_test() {
		PersonageCall localLastCall = new PersonageCall(PersonageCall.FINISHED_STATUS, new Date());
		entityManager.persist(localLastCall);
		entityManager.flush();
		
		personageService.cleanOldPersonages(localLastCall);
		personageRepository.flush();
		entityManager.flush();
		assertEquals(personageRepository.findAll().size(), 0);
	}

	@Test
	public void cleanOldPersonages_ifWasNotCleaned_test() {
		PersonageCall localLastCall = new PersonageCall(PersonageCall.PROCESSING_STATUS, new Date());
		entityManager.persist(localLastCall);
		entityManager.flush();
		
		personageService.cleanOldPersonages(localLastCall);
		personageRepository.flush();
		entityManager.flush();
		assertEquals(personageRepository.findAll().size(), list.size());
	}
	
	@Test
	public void searchByWordStarting_notFindedWord_test(){
		List<String> searchByWordStarting = personageService.searchByWordStarting("SWHater");
		personageRepository.flush();
		entityManager.flush();
		assertEquals(searchByWordStarting.size(), 0);
	}
	
	@Test
	public void searchByWordStarting_findedWordAndCorrectOrder_Test(){
		List<String> searchByWordStarting = personageService.searchByWordStarting("Luk");
		personageRepository.flush();
		entityManager.flush();
		
		assertEquals(searchByWordStarting, searchedByLukList);
	}
	
	
	private void sleep(long i) {
		try {
			Thread.sleep(i);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
