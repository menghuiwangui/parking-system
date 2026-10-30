package com.example.campus_parking_system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.campus_parking_system.mapper")
public class CampusParkingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(CampusParkingSystemApplication.class, args);
	}

}
