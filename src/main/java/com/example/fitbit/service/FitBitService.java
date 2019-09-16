package com.example.fitbit.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.fitbit.model.UserData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FitBitService {

  @Value("${datastore.path}")
  private String dataStorePath;

  @Value("${rewards.min.file.count}")
  private Integer minFileCount;

  @Value("${rewards.min.step.count}")
  private Integer minStepCount;

  public String post(UserData userData) {
    ObjectMapper mapper = new ObjectMapper();
    String jsonString = "";
    try {
      jsonString = mapper.writeValueAsString(userData);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    try {
      FileWriter file =
          new FileWriter(dataStorePath + String.valueOf(System.currentTimeMillis()) + ".json");
      file.write(jsonString);
      file.flush();
      file.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("Event created.");
    return "Activity sync complete";
  }

  public String get() {
    File datastore = new File(dataStorePath);
    int fileCount = datastore.list().length;
    System.out.println("Number of data files: " + fileCount);
    int stepCount = 0;
    if (fileCount >= minFileCount) {
      for (final File dataFile : datastore.listFiles()) {
        File file = dataFile.getAbsoluteFile();
        UserData userData = new UserData();
        try {
          userData = new ObjectMapper().readValue(file, UserData.class);
        } catch (IOException e) {
          e.printStackTrace();
        }
        System.out.println("userData >>  " + userData.toString());
        stepCount += userData.getStepsCount();
      }
    }
    if (stepCount > minStepCount)
      return "Congratulations on completing your health goal. You have a reward.";
    else return "Health goal is pending completion.";
  }
}
