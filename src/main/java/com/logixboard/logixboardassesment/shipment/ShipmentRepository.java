package com.logixboard.logixboardassesment.shipment;

import com.logixboard.logixboardassesment.data.shipment.Shipment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ShipmentRepository extends MongoRepository<Shipment, String> {
}
