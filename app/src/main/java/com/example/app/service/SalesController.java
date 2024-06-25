
package com.example.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/carsales")
public class SalesController {

    @Autowired
    private DataService service;

    @GetMapping("/last30days")
    public ResponseEntity<String> findLast30DaysSales() {
        try {
            String data = service.getLast30DaysCarSalesData();
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
