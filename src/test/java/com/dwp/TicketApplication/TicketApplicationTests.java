package com.dwp.TicketApplication;



import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.shadow.com.univocity.parsers.common.ArgumentUtils.EMPTY_STRING_ARRAY;
import static org.mockito.Mockito.mockStatic;

import com.dwp.TicketApplication.controller.PurchaserController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class TicketApplicationTests {

	@Test
	void contextLoads() {}

	}
