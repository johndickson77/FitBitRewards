package com.example.fitbit.service;

import java.io.File;
import java.io.FileWriter;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.fitbit.model.UserData;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FitBitService {

	@Value("${customerdetails.file.path}")
	private String customerDetailsFilePath;

	@Value("${customerdetails.file.name}")
	private String customerDetailsFileName;

	@Value("${rewards.min.file.count}")
	private Integer rewardsMinFileCount;

	@Value("${rewards.min.step.count}")
	private Integer rewardsMinStepCount;

	private static String CUSTOMER_ACTIVITY_ADDED = "Customer Activity added successfully.";
	private static String CUSTOMER_ACTIVITY_ADDED_WITH_REWARD = "Customer Activity added successfully.And the customer is "
			+ "eligible for reward as his/her step count meets the rewards count.";

	public String savecustomerDetails(UserData userData) {

		try {
			ObjectMapper mapper = new ObjectMapper();
			String jsonString = mapper.writeValueAsString(userData);

			FileWriter file = new FileWriter(customerDetailsFilePath + customerDetailsFileName + "-"
					+ DateTime.now().toString("yyyyMMddHHmmss") + ".json");
			file.write(jsonString);
			file.flush();
			file.close();

			int fileCount = new File(customerDetailsFilePath).list().length;
			System.out.println("Number of files present : " + fileCount);

			if (fileCount >= rewardsMinFileCount) {
				final File folder = new File(customerDetailsFilePath);
				int stepCount = listFilesForFolder(folder);
				if (stepCount >= rewardsMinStepCount) {
					return CUSTOMER_ACTIVITY_ADDED_WITH_REWARD;
				}
			}
		} catch (Exception e) {
			System.out.println("Exception occured (savecustomerDetails):  " + e.getMessage());
		}
		return CUSTOMER_ACTIVITY_ADDED;
	}

	public int listFilesForFolder(final File folder) {
		Integer stepCount = 0;
		try {
			for (final File fileEntry : folder.listFiles()) {
				if (fileEntry.isDirectory()) {
					listFilesForFolder(fileEntry);
				} else {
					ObjectMapper mapper = new ObjectMapper();
					File file = new File(customerDetailsFilePath + fileEntry.getName());
					UserData userData = mapper.readValue(file, UserData.class);
					System.out.println("userData >>  " + userData.toString());
					stepCount += userData.getStepsCount();
				}
			}
			System.out.println("Total Step Count " + stepCount);
		} catch (Exception e) {
			System.out.println("Exception occured (listFilesForFolder) :" + e.getMessage());
		}
		return stepCount;
	}
}
