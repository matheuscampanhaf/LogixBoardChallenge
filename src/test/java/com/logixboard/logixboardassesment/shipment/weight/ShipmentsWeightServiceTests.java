package com.logixboard.logixboardassesment.shipment.weight;

import com.logixboard.logixboardassesment.data.shipment.*;
import com.logixboard.logixboardassesment.shipment.ShipmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class ShipmentsWeightServiceTests {
    private ShipmentWeightAggregator shipmentWeightAggregator;
    private TotalShipmentsWeightInKgCounter shipmentsTotalWeightInKgCounter;
    private WeightUnitConverter weightUnitConverter;
    private ShipmentRepository shipmentRepository;
    private ShipmentsWeightService shipmentsWeightService;

    @BeforeEach
    void setUp() {
        shipmentWeightAggregator = mock(ShipmentWeightAggregator.class);
        shipmentsTotalWeightInKgCounter = mock(TotalShipmentsWeightInKgCounter.class);
        weightUnitConverter = mock(WeightUnitConverter.class);
        shipmentRepository = mock(ShipmentRepository.class);
        shipmentsWeightService = new ShipmentsWeightService(shipmentWeightAggregator, shipmentsTotalWeightInKgCounter, weightUnitConverter, shipmentRepository);
    }

    @Test
    void getTotalWeightOfAllShipmentsV1_returnsZeroIfNoShipment() {
        when(shipmentRepository.findAll()).thenReturn(new ArrayList<>());
        assertThat(shipmentsWeightService.getTotalWeightOfAllShipmentsV1(WeightUnit.KILOGRAMS)).isEqualTo(0.0);
    }

    @Test
    void getTotalWeightOfAllShipmentsV1_returnsTotalWeight() {
        final Shipment shipment1 = buildShipment();
        final Shipment shipment2 = buildEmptyShipment();

        when(shipmentRepository.findAll()).thenReturn(new ArrayList<>(Arrays.asList(shipment1, shipment2)));
        when(shipmentWeightAggregator.apply(shipment1, WeightUnit.KILOGRAMS)).thenReturn(6.0);
        when(shipmentWeightAggregator.apply(shipment2, WeightUnit.KILOGRAMS)).thenReturn(0.0);

        assertThat(shipmentsWeightService.getTotalWeightOfAllShipmentsV1(WeightUnit.KILOGRAMS)).isEqualTo(6.0);
    }

    @Test
    void getTotalWeightOfAllShipmentsV2_returnsTotalWeight() {
        when(shipmentsTotalWeightInKgCounter.getValue()).thenReturn(1875L);
        when(weightUnitConverter.convert(1875.0, WeightUnit.KILOGRAMS, WeightUnit.OUNCES)).thenReturn(0.3);

        assertThat(shipmentsWeightService.getTotalWeightOfAllShipmentsV2(WeightUnit.OUNCES)).isEqualTo(0.3);
    }

    @Test
    void updateShipmentsTotalWeightCounter_addsWeight_whenFirstUpdate() {
        final Shipment incomingShipment = buildShipment();
        final Shipment shipmentOnDb = buildEmptyShipment();

        when(shipmentRepository.findById("referenceId")).thenReturn(Optional.empty());
        when(shipmentWeightAggregator.apply(incomingShipment, WeightUnit.KILOGRAMS)).thenReturn(6.0);

        shipmentsWeightService.updateShipmentsTotalWeightCounter(incomingShipment);

        verify(shipmentsTotalWeightInKgCounter).add(6L);
        verifyNoMoreInteractions(shipmentsTotalWeightInKgCounter);
    }

    @Test
    void updateShipmentsTotalWeightCounter_doesNothing_whenNoWeightChange() {
        final Shipment incomingShipment = buildShipment();
        final Shipment shipmentOnDb = buildShipment();

        when(shipmentRepository.findById("referenceId")).thenReturn(Optional.of(shipmentOnDb));
        when(shipmentWeightAggregator.apply(incomingShipment, WeightUnit.KILOGRAMS)).thenReturn(6.0);
        when(shipmentWeightAggregator.apply(shipmentOnDb, WeightUnit.KILOGRAMS)).thenReturn(6.0);

        shipmentsWeightService.updateShipmentsTotalWeightCounter(incomingShipment);

        verifyNoInteractions(shipmentsTotalWeightInKgCounter);
    }

    @Test
    void updateShipmentsTotalWeightCounter_changeCounter_whenWeightChanges() {
        final Shipment incomingShipment = buildShipment();
        final Shipment shipmentOnDb = buildEmptyShipment();

        when(shipmentRepository.findById("referenceId")).thenReturn(Optional.of(shipmentOnDb));
        when(shipmentWeightAggregator.apply(incomingShipment, WeightUnit.KILOGRAMS)).thenReturn(6.0);
        when(shipmentWeightAggregator.apply(shipmentOnDb, WeightUnit.KILOGRAMS)).thenReturn(0.0);

        shipmentsWeightService.updateShipmentsTotalWeightCounter(incomingShipment);

        verify(shipmentsTotalWeightInKgCounter).decrement(0L);
        verify(shipmentsTotalWeightInKgCounter).add(6L);
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
