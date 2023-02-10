package com.logixboard.logixboardassesment.shipment.weight;

import com.logixboard.logixboardassesment.data.shipment.WeightUnit;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class WeightUnitConverter {

    /**
     * As improvement, it should be passed as dependency injection.
     */
    private final Map<WeightUnit, Map<WeightUnit, Double>> WEIGHT_CONVERTION_MAP =
            Map.of(
                    WeightUnit.KILOGRAMS, Map.<WeightUnit, Double>of(
                            WeightUnit.KILOGRAMS, 1D,
                            WeightUnit.OUNCES, 35.2,
                            WeightUnit.POUNDS, 2.2
                    ),
                    WeightUnit.OUNCES, Map.<WeightUnit, Double>of(
                            WeightUnit.KILOGRAMS, 0.02,
                            WeightUnit.OUNCES, 1D,
                            WeightUnit.POUNDS, 0.06
                    ),
                    WeightUnit.POUNDS, Map.<WeightUnit, Double>of(
                            WeightUnit.KILOGRAMS, 0.45,
                            WeightUnit.OUNCES, 16D,
                            WeightUnit.POUNDS, 1D
                    )
            );

    public Double convert(final Double weight, final WeightUnit fromUnit, final WeightUnit toUnit) {
        return weight * WEIGHT_CONVERTION_MAP.get(fromUnit).get(toUnit);
    }
}
