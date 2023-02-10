package com.logixboard.logixboardassesment.shipment.weight;

import com.logixboard.logixboardassesment.data.shipment.WeightUnit;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class WeightUnitConverterTests {

    @Test
    void convert_convertsCorrectlyGivenTheConvertionMap() {
        final WeightUnitConverter weightUnitConverter = new WeightUnitConverter();
        assertThat(weightUnitConverter.convert(2.0, WeightUnit.POUNDS, WeightUnit.OUNCES)).isEqualTo(32.0);
    }
}
