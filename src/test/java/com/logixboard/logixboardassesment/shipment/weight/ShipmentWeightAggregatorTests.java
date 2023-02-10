package com.logixboard.logixboardassesment.shipment.weight;

import com.logixboard.logixboardassesment.data.shipment.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ShipmentWeightAggregatorTests {

    private WeightUnitConverter converter;
    private ShipmentWeightAggregator aggregator;

    @BeforeEach
    void setUp() {
        converter = mock(WeightUnitConverter.class);
        aggregator = new ShipmentWeightAggregator(converter);
    }

    @Test
    void apply_returnsAggregatedWeight() {
        when(converter.convert(2.0, WeightUnit.KILOGRAMS, WeightUnit.KILOGRAMS)).thenReturn(2.0);
        when(converter.convert(200.0, WeightUnit.OUNCES, WeightUnit.KILOGRAMS)).thenReturn(4.0);

        assertThat(aggregator.apply(buildShipment(), WeightUnit.KILOGRAMS)).isEqualTo(6.0);
    }

    @Test
    void apply_returnZeroWhenNoPacks() {
        assertThat(aggregator.apply(buildEmptyShipment(), WeightUnit.KILOGRAMS));
    }

    private Shipment buildShipment() {
        return Shipment
                .builder()
                .referenceId("referenceId")
                .organizations(new ArrayList<>(Arrays.asList("MAT", "LOG")))
                .estimatedTimeArrival("2020-11-20T00:00:00")
                .transportPacks(
                        TransportPacks
                                .builder()
                                .nodes(
                                        new ArrayList<>(Arrays.asList(
                                                Pack.builder()
                                                        .totalWeight(TotalWeight.builder().unit(WeightUnit.KILOGRAMS).weight(2.0).build())
                                                        .build(),
                                                Pack.builder()
                                                        .totalWeight(TotalWeight.builder().unit(WeightUnit.OUNCES).weight(200.0).build())
                                                        .build()
                                        )
                                        )
                                )
                                .build()
                )
                .build();
    }

    private Shipment buildEmptyShipment() {
        return Shipment
                .builder()
                .referenceId("referenceId")
                .organizations(new ArrayList<>(Arrays.asList("MAT", "LOG")))
                .estimatedTimeArrival("2020-11-20T00:00:00")
                .transportPacks(
                        TransportPacks
                                .builder()
                                .nodes(new ArrayList<>())
                                .build()
                )
                .build();
    }

}
