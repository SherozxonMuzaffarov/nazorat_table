package com.sms.repository;

import com.sms.model.MetrologiyaModel;
import com.sms.model.MetrologiyaSkladModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MetrologiyaSkladRepository extends JpaRepository<MetrologiyaSkladModel, Integer> {

    @Query("SELECT u FROM MetrologiyaSkladModel u WHERE u.depoNomi = ?1")
    List<MetrologiyaSkladModel> findAllByDepoNomi(String depoNomi);
}
