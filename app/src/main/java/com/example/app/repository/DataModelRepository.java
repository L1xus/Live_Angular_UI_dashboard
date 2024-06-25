
package com.example.app.repository;

import java.util.List;

import com.example.app.model.DataModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface DataModelRepository extends MongoRepository<DataModel, String> {
    //Supports native JSON query string
    @Query("{ 'saleTimestamp' : { $gte: ?0 } }")
    List<DataModel> findSalesGreaterThanDate(String date);
}
