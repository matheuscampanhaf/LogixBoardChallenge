package com.logixboard.logixboardassesment.shipment.weight;

import com.logixboard.logixboardassesment.data.shipment.Shipment;
import com.logixboard.logixboardassesment.data.shipment.WeightUnit;
import com.logixboard.logixboardassesment.shipment.ShipmentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ShipmentsWeightService {

    private final ShipmentWeightAggregator shipmentWeightAggregator;
    private final TotalShipmentsWeightInKgCounter shipmentsTotalWeightInKgCounter;
    private final WeightUnitConverter weightUnitConverter;
    private final ShipmentRepository shipmentRepository;

    public double getTotalWeightOfAllShipmentsV1(final WeightUnit desiredWeightUnit) {
        final List<Shipment> allShipments = shipmentRepository.findAll();

        if (allShipments.isEmpty()) {
            return 0;
        }

        return allShipments.stream()
                .map(shipment -> shipmentWeightAggregator.apply(shipment, desiredWeightUnit))
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    public double getTotalWeightOfAllShipmentsV2(final WeightUnit desiredWeightUnit) {
        return weightUnitConverter.convert((double) shipmentsTotalWeightInKgCounter.getValue(), WeightUnit.KILOGRAMS, desiredWeightUnit);
    }

    public void updateShipmentsTotalWeightCounter(final Shipment shipment) {
        final Optional<Shipment> shipmentOnDb = shipmentRepository.findById(shipment.getReferenceId());

        if (shipmentOnDb.isEmpty()) {
            shipmentsTotalWeightInKgCounter.add(shipmentWeightAggregator.apply(shipment, WeightUnit.KILOGRAMS).longValue());
            return;
        }

        final Double shippingOnDbWeightInKg = shipmentWeightAggregator.apply(shipmentOnDb.get(), WeightUnit.KILOGRAMS);
        final Double incomingShippingWeightInKg = shipmentWeightAggregator.apply(shipment, WeightUnit.KILOGRAMS);
        if (!Objects.equals(shippingOnDbWeightInKg, incomingShippingWeightInKg)) {
            shipmentsTotalWeightInKgCounter.decrement(shippingOnDbWeightInKg.longValue());
            shipmentsTotalWeightInKgCounter.add(incomingShippingWeightInKg.longValue());
        }
    }
}
