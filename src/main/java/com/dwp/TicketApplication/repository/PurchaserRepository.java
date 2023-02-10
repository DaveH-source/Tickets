package com.dwp.TicketApplication.repository;

import com.dwp.TicketApplication.entity.Purchaser;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

  public interface PurchaserRepository extends JpaRepository<Purchaser,Integer> {

    List<Purchaser> findByUsername(String username);
  }

