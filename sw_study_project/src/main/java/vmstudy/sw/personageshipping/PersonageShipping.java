package vmstudy.sw.personageshipping;

import java.util.List;

import org.springframework.stereotype.Component;

import vmstudy.sw.models.Personage;
import vmstudy.sw.models.PersonageCall;

@Component
public interface PersonageShipping {
	List<Personage> getPersonages(PersonageCall lastCall);
}
