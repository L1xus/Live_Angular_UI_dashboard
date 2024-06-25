
package com.example.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.example.app.model.DataModel;
import com.example.app.repository.DataModelRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CarSalesKafkaListener {

    @Autowired
    private DataModelRepository demoRepository;

    @KafkaListener(topics = "${spring.kafka.topic.demo}", groupId = "demo-group")
    public void readDataStream(@Payload String record) {
        System.out.println("Received record: " + record); // Log the incoming record
        // Parse JSON string to DataModel object and save to MongoDB
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            DataModel dataModel = objectMapper.readValue(record, DataModel.class);
            demoRepository.save(dataModel);
            System.out.println("Saved dataModel: " + dataModel);
        } catch (Exception e) {
            System.err.println("Error processing record: " + record);
            e.printStackTrace();
        }
    }
}
