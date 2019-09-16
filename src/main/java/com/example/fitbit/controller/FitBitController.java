package com.example.fitbit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.fitbit.model.UserData;
import com.example.fitbit.service.FitBitService;

@RestController
public class FitBitController {

  @Autowired private FitBitService fitbitService;

  @PostMapping(
      path = "/health/customer/metric",
      consumes = "application/json",
      produces = "text/plain")
  public ResponseEntity<String> post(@RequestBody UserData userData) {
    String response = fitbitService.post(userData);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping(path = "/reward/customer/1", produces = "text/plain")
  public ResponseEntity<String> get() {
    String response = fitbitService.get();
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
