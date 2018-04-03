package vmstudy.sw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vmstudy.sw.models.PersonageCall;

@Repository
public interface PersonageCallRepository extends JpaRepository<PersonageCall, Long>{
	PersonageCall findTopByOrderByIdDesc();
	PersonageCall findTopByStatusOrderByIdDesc(String status);
}

