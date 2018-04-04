package vmstudy.sw.personageshipping;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import vmstudy.sw.db.models.Personage;
import vmstudy.sw.db.models.PersonageCall;

@Component
@Profile("dev")
public class DevPersonageShipping implements PersonageShipping{

	@Override
	public List<Personage> getPersonages(PersonageCall lastCall) {
		List<Personage> result = new ArrayList<Personage>();
		Random random = new Random();
		if (random.nextInt(1000) > 450) {
			for (int i = 0; i < 5; i++) {
				Personage personage = new Personage();
				if (random.nextInt(1000) > 450) {
					personage.setName("Luke Skywalker" + String.valueOf(random.nextInt(1000)));
				} else {
					personage.setName(String.valueOf(random.nextInt(1000)) + "Luke Skywalker");
				}
				result.add(personage);
			}
			lastCall.incrementCallsCount();
			lastCall.setFinishedStatus();
			lastCall.setUrl(null);
		} else
		{
			lastCall.incrementCallsCount();
			lastCall.incrementFailingCount();
			lastCall.setUrl("dev failing url");
		}
		return result;
	}

}
