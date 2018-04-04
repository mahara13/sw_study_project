package vmstudy.sw.scheduledtasks;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import vmstudy.sw.db.models.Personage;
import vmstudy.sw.db.models.PersonageCall;
import vmstudy.sw.personageshipping.PersonageShipping;
import vmstudy.sw.services.PersonageCallService;
import vmstudy.sw.services.PersonageService;
import vmstudy.sw.utils.DateUtils;

@Component
public class PersonagePump {
	
	private static String START_API_URL;

	@Autowired
	private PersonageShipping personageGet;
	
	@Autowired
	private PersonageService personageService;
	
	@Autowired
	private PersonageCallService personageCallService;
	
	/**
	 * Every 5 minutes we will check if we need update db
	 * If in current was performed successfully data get, call to server will be omitted
	 * In another case we will try to get updated data
	 */
	@Scheduled(fixedRate = 300000) 
    public void reportCurrentTime() {
		PersonageCall processingLastCall = prepareProcessingLastCall(personageCallService.getLastCall());
		if (processingLastCall != null) {			
			List<Personage> personages = personageGet.getPersonages(processingLastCall);
			
			personageCallService.savePersonageCall(processingLastCall);
			sleep(1000);//To make interval at least 2 seconds
			personageService.savePersonages(personages);	
			sleep(1000);//To make interval at least 2 seconds
			personageService.cleanOldPersonages(processingLastCall);
			personageCallService.savePersonageCall(processingLastCall);//To make interval in which personages are valid
		}
    }

	private void sleep(long i) {
		try {
			Thread.sleep(i);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private PersonageCall prepareProcessingLastCall(PersonageCall lastCall) {
		if (lastCall == null) {
			//DB is empty, it's first call
			return new PersonageCall(START_API_URL);
		} 
		
		if (lastCall.isFinished()) {
			if (isTheSameAsCurrentDay(lastCall.getUpdatedAt()) == true) {
				//Do nothing today we had updated data
				return null;
			} else {
				//New day, so potentially server data could be change, so make update
				return new PersonageCall(START_API_URL);
			}
		}
		
		// It means we had interrupted getting process, so pass that last call data to continue
		return lastCall;
	}

	private boolean isTheSameAsCurrentDay(Date updatedAt) {
		return false;
		//return DateUtils.isToday(updatedAt);
	}
	
	@Value("${sw.start_api_url}")
    public void setSvnUrl(String start_api_url) {
		START_API_URL = start_api_url;
    }
}
