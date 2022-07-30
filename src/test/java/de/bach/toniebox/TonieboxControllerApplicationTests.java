package de.bach.toniebox;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest
class TonieboxControllerApplicationTests {

	@Autowired
	TonieboxController controller;
	
	@Test
	void contextLoads() {
	}

	@Test
	void testClientErrorWhenAuthorizationFailed() {
		WebTestClient
				.bindToController(controller)
				.build()
				.get()
				.uri("/getCreativeTonie")
				.exchange()
				.expectStatus().is4xxClientError();
	}

}
