package vmstudy.sw.repository;


import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import vmstudy.sw.db.models.Personage;

@Repository
public interface PersonageRepository extends JpaRepository<Personage, Long> {
	@Modifying
    @Transactional
    public void deleteByCreatedAtBefore(Date expiryDate);
	
	@Transactional(readOnly = true)
	public List<Personage> findAllByCreatedAtBetweenAndNameContains(Date startDate, Date endDate, String searchWord);
}
