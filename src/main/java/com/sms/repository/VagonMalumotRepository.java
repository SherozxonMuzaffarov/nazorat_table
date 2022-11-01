package com.sms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sms.model.VagonMalumot;
import org.springframework.data.repository.query.Param;


public interface VagonMalumotRepository  extends JpaRepository<VagonMalumot,Long>{

	Optional<VagonMalumot> findByNomer(Integer keyword);

//    @Query("SELECT u FROM VagonMalumot u WHERE u.saqlanganVaqti LIKE %:saqlanganVaqti% and u.depoNomi LIKE %:depoNomi%")
//    List<VagonMalumot> filterByDateAndDepoNomi(@Param("saqlanganVaqti") String saqlanganVaqti, @Param("depoNomi") String depoNomi);
//
//	@Query("SELECT u FROM VagonMalumot u WHERE u.depoNomi = ?1")
//	List<VagonMalumot> findAllByDepoNomi(String depoNomi);
//
//	@Query("SELECT u FROM VagonMalumot u WHERE u.saqlanganVaqti LIKE %:saqlanganVaqti%")
//	List<VagonMalumot> findAllByDate(@Param("saqlanganVaqti")  String saqlanganVaqti);

	@Query("SELECT u FROM VagonMalumot u WHERE u.saqlanganVaqti LIKE %:saqlanganVaqt%")
	List<VagonMalumot> filterByDate(String saqlanganVaqt);

	@Query("SELECT u FROM VagonMalumot u WHERE u.country LIKE %:country%")
	List<VagonMalumot> filterByCountry(String country);

	@Query("SELECT u FROM VagonMalumot u WHERE u.depoNomi LIKE %:depoNomi%")
	List<VagonMalumot> filterByDepoNomi(String depoNomi);

	@Query("SELECT u FROM VagonMalumot u WHERE u.country LIKE %:country% and u.saqlanganVaqti LIKE %:saqlanganVaqt%")
	List<VagonMalumot> filterByCountryAndDate(String country, String saqlanganVaqt);

	@Query("SELECT u FROM VagonMalumot u WHERE u.depoNomi LIKE %:depoNomi% and u.country LIKE %:country%")
	List<VagonMalumot> filterByDepoNomiAndCountry(String depoNomi, String country);

	@Query("SELECT u FROM VagonMalumot u WHERE u.depoNomi LIKE %:depoNomi% and u.saqlanganVaqti LIKE %:saqlanganVaqt%")
	List<VagonMalumot> filterByDepoNomiAndDate(String depoNomi, String saqlanganVaqt);

	@Query("SELECT u FROM VagonMalumot u WHERE u.depoNomi LIKE %:depoNomi% and u.country LIKE %:country% and u.saqlanganVaqti LIKE %:saqlanganVaqt%")
	List<VagonMalumot> filterByDepoNomiCountryAndDate(String depoNomi, String country, String saqlanganVaqt);

}
