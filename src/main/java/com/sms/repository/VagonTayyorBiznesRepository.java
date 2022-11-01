package com.sms.repository;

import java.util.List;
import java.util.Optional;

import com.sms.model.VagonTayyorUty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sms.model.VagonTayyor;
import com.sms.model.VagonTayyor;

@Repository
public interface VagonTayyorBiznesRepository extends JpaRepository<VagonTayyor, Long> {
	
	Optional<VagonTayyor> findByNomer(Integer nomer);
	
	@Query("SELECT u FROM VagonTayyor u WHERE u.nomer = ?1")
	VagonTayyor searchByNomer(Integer nomer,@Param("oy")  String oy);

	@Query("SELECT u FROM VagonTayyor u WHERE u.createdDate LIKE %:oy% ")
	List<VagonTayyor> findAll(@Param("oy") String oy);

//oyliklar uchun 
	@Query(value = "SELECT count(*) FROM vagon_tayyor u WHERE u.depo_nomi = ?1 and u.vagon_turi = ?2 and u.remont_turi = ?3", nativeQuery = true)
	int countByDepoNomiVagonTuriAndTamirTuri(String depoNomi, String vagonTuri, String remontTuri);

//	@Query(value = "SELECT count(*) FROM vagon_tayyor u WHERE u.depo_nomi = ?1 and u.vagon_turi = ?2 and u.remont_turi = ?3 and u.country = ?4", nativeQuery = true)
//	int countByDepoNomiVagonTuriAndTamirTuri(String depoNomi, String vagonTuri, String remontTuri, String country);

	@Query("SELECT count (*) FROM VagonTayyor u WHERE u.depoNomi LIKE %:depoNomi% and u.vagonTuri LIKE %:vagonTuri% and u.remontTuri LIKE %:tamirTuri% and u.createdDate LIKE %:oy%")
	int countAllActiveByDepoNomiVagonTuriAndTamirTuri(@Param("depoNomi") String depoNomi, @Param("vagonTuri")String vagonTuri,@Param("tamirTuri") String tamirTuri, @Param("oy") String oy);

	@Query( "SELECT count(*) FROM VagonTayyor u WHERE u.depoNomi LIKE %:depoNomi% and u.vagonTuri LIKE %:vagonTuri% and u.remontTuri LIKE %:tamirTuri% and u.createdDate LIKE %:oy% and u.country LIKE %:country%")
	int countAllActiveByDepoNomiVagonTuriAndTamirTuri(@Param("depoNomi")String depoNomi,@Param("vagonTuri") String vagonTuri,@Param("tamirTuri") String tamirTuri,@Param("oy") String oy,@Param("country") String country);

	@Query( "SELECT count(*) FROM VagonTayyor u WHERE u.depoNomi LIKE %:depoNomi% and u.vagonTuri LIKE %:vagonTuri% and u.remontTuri LIKE %:tamirTuri% and u.country LIKE %:country%")
	int countByDepoNomiVagonTuriTamirTuriAndCountry(String depoNomi, String vagonTuri, String tamirTuri, String country);

	//filterniki
	@Query("SELECT u FROM VagonTayyor u WHERE u.depoNomi  LIKE %:depoNomi% and u.vagonTuri LIKE %:vagonTuri% and u.country LIKE %:country% and u.createdDate LIKE %:oy% ")
	List<VagonTayyor> findAllByDepoNomiVagonTuriAndCountry(@Param("depoNomi") String depoNomi,@Param("vagonTuri") String vagonTuri,@Param("country") String country,@Param("oy") String oy);

	@Query("SELECT u FROM VagonTayyor u WHERE u.depoNomi  LIKE %:depoNomi% and u.vagonTuri LIKE %:vagonTuri% and u.createdDate LIKE %:oy% ")
	List<VagonTayyor> findAllByDepoNomiAndVagonTuri(@Param("depoNomi") String depoNomi,@Param("vagonTuri") String vagonTuri,@Param("oy") String oy);

	@Query("SELECT u FROM VagonTayyor u WHERE u.depoNomi  LIKE %:depoNomi% and u.country LIKE %:country% and u.createdDate LIKE %:oy% ")
	List<VagonTayyor> findAllByDepoNomiAndCountry(@Param("depoNomi") String depoNomi,@Param("country") String country,@Param("oy") String oy);

	@Query("SELECT u FROM VagonTayyor u WHERE  u.depoNomi  LIKE %:depoNomi% and u.createdDate LIKE %:oy% ")
	List<VagonTayyor> findAllByDepoNomi(@Param("depoNomi") String depoNomi,@Param("oy") String oy);

	@Query("SELECT u FROM VagonTayyor u WHERE u.vagonTuri LIKE %:vagonTuri%  and u.country LIKE %:country% and u.createdDate LIKE %:oy% ")
	List<VagonTayyor> findAllByVagonTuriAndCountry(@Param("vagonTuri") String vagonTuri,@Param("country") String country,@Param("oy") String oy);

	@Query("SELECT u FROM VagonTayyor u WHERE u.country LIKE %:country% and u.createdDate LIKE %:oy%")
	List<VagonTayyor> findAllBycountry(@Param("country") String country,@Param("oy") String oy);

	@Query("SELECT u FROM VagonTayyor u WHERE u.vagonTuri LIKE %:vagonTuri% and u.createdDate LIKE %:oy%")
	List<VagonTayyor> findAllByVagonTuri(@Param("vagonTuri") String vagonTuri, @Param("oy") String oy);

	//filterniki
	@Query("SELECT u FROM VagonTayyor u WHERE u.depoNomi = ?1 and u.vagonTuri = ?2 and u.country = ?3")
	List<VagonTayyor> findByDepoNomiVagonTuriAndCountry(String depoNomi, String vagonTuri, String country);

	@Query("SELECT u FROM VagonTayyor u WHERE u.depoNomi = ?1 and u.vagonTuri = ?2")
	List<VagonTayyor> findByDepoNomiAndVagonTuri(String depoNomi, String vagonTuri);

	@Query("SELECT u FROM VagonTayyor u WHERE u.depoNomi = ?1 and u.country = ?2")
	List<VagonTayyor> findByDepoNomiAndCountry(String depoNomi, String country);

	@Query("SELECT u FROM VagonTayyor u WHERE u.depoNomi = ?1")
	List<VagonTayyor> findByDepoNomi(String depoNomi);

	@Query("SELECT u FROM VagonTayyor u WHERE u.vagonTuri = ?1 and u.country = ?2")
	List<VagonTayyor> findByVagonTuriAndCountry(String vagonTuri, String country);

	@Query("SELECT u FROM VagonTayyor u WHERE u.country = ?1")
	List<VagonTayyor> findBycountry(String country);

	@Query("SELECT u FROM VagonTayyor u WHERE u.vagonTuri = ?1")
	List<VagonTayyor> findByVagonTuri(String vagonTuri);

}
