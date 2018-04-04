package vmstudy.sw.personageshipping;

import java.util.List;

import org.springframework.stereotype.Component;

import vmstudy.sw.db.models.Personage;
import vmstudy.sw.db.models.PersonageCall;

public interface PersonageShipping {
	List<Personage> getPersonages(PersonageCall lastCall);
}
