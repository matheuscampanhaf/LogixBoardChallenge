package com.logixboard.logixboardassesment.shipment.weight;

import com.logixboard.logixboardassesment.data.shipment.Shipment;
import com.logixboard.logixboardassesment.data.shipment.TotalWeight;
import com.logixboard.logixboardassesment.data.shipment.WeightUnit;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.BiFunction;

@Service
@AllArgsConstructor
public class ShipmentWeightAggregator implements BiFunction<Shipment, WeightUnit, Double> {

    private final WeightUnitConverter weightUnitConverter;

    @Override
    public Double apply(final Shipment shipment, final WeightUnit desiredWeightUnit) {
        if (shipment.getTransportPacks().getNodes().isEmpty()) {
            return 0D;
        }

        return shipment
                .getTransportPacks()
                .getNodes()
                .stream()
                .map(pack -> getConvertedWeightOfPack(pack.getTotalWeight(), desiredWeightUnit))
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    private Double getConvertedWeightOfPack(final TotalWeight totalWeight, final WeightUnit desiredWeightUnit) {
        return weightUnitConverter.convert(totalWeight.getWeight(), totalWeight.getUnit(), desiredWeightUnit);
    }
}
