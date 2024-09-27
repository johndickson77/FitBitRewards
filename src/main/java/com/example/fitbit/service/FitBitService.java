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

  private static final String NEW_LINE = System.getProperty("line.separator");
  private static final String ACTIVITY_PLACE_HOLDER = "ACTIVITY_PLACE_HOLDER";
  private static final String DAY_PLACE_HOLDER = "DAY_PLACE_HOLDER";
  private static final String STEPS_PLACE_HOLDER = "STEPS_PLACE_HOLDER";
  private static final String ACTIVITY_TEMPLATE =
      "<tr><th>Day " + DAY_PLACE_HOLDER + "</th><th>" + STEPS_PLACE_HOLDER + " steps</th></tr>";
  private static final String REWARDS_PLACE_HOLDER = "REWARDS_PLACE_HOLDER";

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
    StringBuilder activityBuilder = new StringBuilder();
    if (fileCount >= minFileCount) {
      int i = 1;
      for (final File dataFile : datastore.listFiles()) {
        File file = dataFile.getAbsoluteFile();
        UserData userData = new UserData();
        try {
          userData = new ObjectMapper().readValue(file, UserData.class);
        } catch (IOException e) {
          e.printStackTrace();
        }
        append(
            activityBuilder,
            new String(ACTIVITY_TEMPLATE)
                .replace(DAY_PLACE_HOLDER, String.valueOf(i))
                .replace(STEPS_PLACE_HOLDER, userData.getStepsCount().toString()));
        stepCount += userData.getStepsCount();
        i++;
      }
    }
    if (stepCount > minStepCount)
      return createDashboardTemplate()
          .replace(ACTIVITY_PLACE_HOLDER, activityBuilder.toString())
          .replace(
              REWARDS_PLACE_HOLDER,
              "<p><br>Congratulations, you have earned a reward.</br><br>Please <a href=\"https://images.unsplash.com/photo-1496637721836-f46d116e6d34?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=500&q=60\">click here</a> to redeem.</br></p>");
    else
      return createDashboardTemplate()
          .replace(ACTIVITY_PLACE_HOLDER, activityBuilder.toString())
          .replace(
              REWARDS_PLACE_HOLDER,
              "<p>Health goal is pending. Please complete to earn a reward.</p>");
  }

  private String createDashboardTemplate() {
    StringBuilder builder = new StringBuilder();
    append(builder, "<!DOCTYPE html>");
    append(builder, "<html>");
    append(builder, "<head>");
    append(builder, "<title>Activity Dashboard</title>");
    append(builder, "<style>");
    append(builder, "body {");
    append(builder, "background-color: black;");
    append(builder, "text-align: center;");
    append(builder, "color: white;");
    append(builder, "font-family: Arial, Helvetica, sans-serif;");
    append(builder, "}");
    append(builder, "table, th, td {");
    append(builder, "border: 1px solid white;");
    append(builder, "}");
    append(builder, "</style>");
    append(builder, "</head>");
    append(builder, "<body>");
    append(builder, "<h1>Dashboard</h1>");
    append(builder, "<h2>Activity</h2>");
    append(builder, "<table align=\"center\">");
    append(builder, ACTIVITY_PLACE_HOLDER);
    append(builder, "</table>");
    builder.append("<h2>Rewards</h2>");
    append(builder, REWARDS_PLACE_HOLDER);
    append(builder, "</body>");
    append(builder, "</html>");
    return builder.toString();
  }

  private void append(StringBuilder stringBuilder, String text) {
    stringBuilder.append(text);
    stringBuilder.append(NEW_LINE);
  }
}
