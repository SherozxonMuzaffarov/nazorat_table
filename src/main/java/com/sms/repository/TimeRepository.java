package com.sms.repository;

import com.sms.model.LastActionTimes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeRepository extends JpaRepository<LastActionTimes, Integer> {
}
