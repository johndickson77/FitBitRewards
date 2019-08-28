package com.example.fitbit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.fitbit.model.UserData;
import com.example.fitbit.service.FitBitService;

@RestController
public class FitBitController {

	@Autowired
	FitBitService fitbitService;

	@PostMapping(path = "/savecustomerdetails", consumes = "application/json", produces = "application/json")
	public ResponseEntity<String> customerDetails(@RequestBody UserData userData) {
		String response = fitbitService.savecustomerDetails(userData);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
