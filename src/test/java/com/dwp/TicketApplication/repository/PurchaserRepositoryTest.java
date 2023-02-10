package com.dwp.TicketApplication.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.dwp.TicketApplication.entity.Purchaser;
import java.util.Date;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class PurchaserRepositoryTest {

  @Autowired
  PurchaserRepository repository;

  @Test
  void testCreateReadAndDelete() {

    Purchaser purchaser = new Purchaser(1, "Ethan", 1, 1, 1, 2, 30, new Date());
    repository.save(purchaser);
    Iterable<Purchaser> purchasers = repository.findAll();
    Assertions.assertThat(purchasers).extracting(Purchaser::getUsername).containsOnly("Ethan");

    repository.deleteAll();
    Assertions.assertThat(repository.findAll()).isEmpty();
  }

  @Test
  void testFindByUsername() {

    Purchaser purchaser = new Purchaser(1, "Robert", 2, 0, 0, 2, 40, new Date());
    repository.save(purchaser);
    List<Purchaser> purchasers = repository.findByUsername("Robert");
    Assertions.assertThat(purchasers).extracting(Purchaser::getUsername).containsOnly("Robert");

  }
}