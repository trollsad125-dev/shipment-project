package com.application.shipment;

import org.springframework.boot.SpringApplication;

public class TestShipmentApplication {

	public static void main(String[] args) {
		SpringApplication.from(ShipmentApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
