package com.logixboard.logixboardassesment.shipment;

import com.logixboard.logixboardassesment.data.shipment.Shipment;
import com.logixboard.logixboardassesment.data.shipment.ShipmentResponse;
import com.logixboard.logixboardassesment.data.shipment.TotalWeight;
import com.logixboard.logixboardassesment.data.shipment.WeightUnit;
import com.logixboard.logixboardassesment.shipment.weight.ShipmentsWeightService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RestController
public final class ShipmentController {

    private final ShipmentService shipmentService;
    private final ShipmentsWeightService shipmentsWeightService;


    @PutMapping("/api/v1/shipment")
    public void addOrUpdateShipment(@RequestBody final Shipment shipment) {
        log.info("Received shipment update: " + shipment);
        shipmentService.addOrUpdate(shipment);
    }

    @GetMapping("/api/v1/shipment/{referenceId}")
    public ShipmentResponse getShipment(@PathVariable("referenceId") final String referenceId) {
        log.info("Received request to get shipment: " + referenceId);
        return shipmentService.getShipmentResponse(referenceId);
    }

    @GetMapping("/api/v1/shipments/weight")
    public TotalWeight getTotalWeightOfAllShipmentsInUnitV1(@RequestParam("weightUnit") final WeightUnit weightUnit) {
        log.info("[V1] Received request to get total weight in unit: " + weightUnit);
        final double totalWeight = shipmentsWeightService.getTotalWeightOfAllShipmentsV1(weightUnit);
        return TotalWeight
                .builder()
                .unit(weightUnit)
                .weight(totalWeight)
                .build();
    }

    @GetMapping("/api/v2/shipments/weight")
    public TotalWeight getTotalWeightOfAllShipmentsInUnitV2(@RequestParam("weightUnit") final WeightUnit weightUnit) {
        log.info("[V2] Received request to get total weight in unit: " + weightUnit);
        return TotalWeight
                .builder()
                .weight(shipmentsWeightService.getTotalWeightOfAllShipmentsV2(weightUnit))
                .unit(weightUnit)
                .build();
    }
}
