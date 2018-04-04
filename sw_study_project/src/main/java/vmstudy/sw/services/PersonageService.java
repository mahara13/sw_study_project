package vmstudy.sw.services;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vmstudy.sw.db.models.Personage;
import vmstudy.sw.db.models.PersonageCall;
import vmstudy.sw.repository.PersonageCallRepository;
import vmstudy.sw.repository.PersonageRepository;

@Service
public class PersonageService {

	@Autowired
	PersonageRepository personageRepository;

	@Autowired
	PersonageCallRepository personageCallRepository;

	public void savePersonages(List<Personage> personages) {
		personageRepository.save(personages);
	}

	public void cleanOldPersonages(PersonageCall lastCall) {
		if (lastCall.isFinished()) {
			personageRepository.deleteByCreatedAtBefore(lastCall.getCreatedAt());
		}
	}

	public List<String> searchByWordStarting(String searchWord) {
		PersonageCall lastFinishedCall = personageCallRepository.findTopByStatusOrderByIdDesc(PersonageCall.FINISHED_STATUS);
		List<Personage> personages = personageRepository.findAllByCreatedAtBetweenAndNameContains(
															lastFinishedCall.getCreatedAt(), 
															lastFinishedCall.getUpdatedAt(), 
															searchWord);
		String inFirstWordRegex = "^\\w*" + searchWord + "\\w*";

		Pattern stringSearchPattern = Pattern.compile(searchWord, Pattern.CASE_INSENSITIVE);
		Pattern inFirstWordRegexPattern = Pattern.compile(inFirstWordRegex, Pattern.CASE_INSENSITIVE);

		List<String> inFirstWordList = personages.stream().map(Personage::getName)
				.filter(name -> inFirstWordRegexPattern.matcher(name).find()).sorted((s1, s2) -> {
					Matcher matcher1 = stringSearchPattern.matcher(s1);
					matcher1.find();
					Matcher matcher2 = stringSearchPattern.matcher(s2);
					matcher2.find();
					return Integer.compare(matcher1.start(), matcher2.start());
				}).collect(Collectors.toList());

		List<String> notFirstWordList = personages.stream().map(Personage::getName)
				.filter(name -> inFirstWordList.contains(name) == false).sorted((s1, s2) -> {
					Matcher matcher1 = stringSearchPattern.matcher(s1);
					matcher1.find();
					Matcher matcher2 = stringSearchPattern.matcher(s2);
					matcher2.find();
					return Integer.compare(matcher1.start(), matcher2.start());
				}).collect(Collectors.toList());

		inFirstWordList.addAll(notFirstWordList);

		return inFirstWordList;
	}
}
