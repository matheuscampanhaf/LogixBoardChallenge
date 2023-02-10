package com.logixboard.logixboardassesment.shipment;

import com.logixboard.logixboardassesment.data.EventTypes;
import com.logixboard.logixboardassesment.data.organization.Organization;
import com.logixboard.logixboardassesment.data.shipment.*;
import com.logixboard.logixboardassesment.exceptions.ResourceNotFound;
import com.logixboard.logixboardassesment.organization.OrganizationService;
import com.logixboard.logixboardassesment.shipment.weight.ShipmentsWeightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ShipmentServiceTests {
    private ShipmentRepository shipmentRepository;
    private OrganizationService organizationService;
    private ShipmentsWeightService shipmentsWeightService;
    private ShipmentService shipmentService;

    @BeforeEach
    void setUp() {
        shipmentRepository = mock(ShipmentRepository.class);
        organizationService = mock(OrganizationService.class);
        shipmentsWeightService = mock(ShipmentsWeightService.class);
        shipmentService = new ShipmentService(shipmentRepository, organizationService, shipmentsWeightService);
    }

    @Test
    void addOrUpdate_savesShipmentWithExistingOrganizations() {
        final Shipment shipment = buildShipment();
        when(organizationService.existsOrganizationCode("MAT")).thenReturn(true);
        when(organizationService.existsOrganizationCode("LOG")).thenReturn(true);

        shipmentService.addOrUpdate(shipment);

        verify(shipmentsWeightService).updateShipmentsTotalWeightCounter(shipment);
        verify(shipmentRepository).save(shipment);
    }

    @Test
    void addOrUpdate_throwsWhenOrganizationDoesNotExist() {
        final Shipment shipment = buildShipment();
        when(organizationService.existsOrganizationCode("MAT")).thenReturn(true);
        when(organizationService.existsOrganizationCode("LOG")).thenReturn(false);

        assertThatThrownBy(
                () -> shipmentService.addOrUpdate(shipment))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Could not find organization with code: LOG");
        verifyNoInteractions(shipmentRepository);
    }

    @Test
    void getShipmentResponse_returnsShipmentWithEnrichedOrganizationIfCodeExists() {
        final Shipment shipment = buildShipment();
        when(organizationService.getOrganizationByCode("MAT")).thenReturn(Optional.of(Organization.builder().code("MAT").id("id").type(EventTypes.ORGANIZATION).build()));
        when(organizationService.getOrganizationByCode("LOG")).thenReturn(Optional.empty());
        when(shipmentRepository.findById("referenceId")).thenReturn(Optional.of(shipment));

        final ShipmentResponse result = shipmentService.getShipmentResponse("referenceId");

        final ShipmentResponse expectedResult = ShipmentResponse.builder()
                .referenceId(shipment.getReferenceId())
                .organizations(new ArrayList<>(Arrays.asList(Organization.builder().code("MAT").id("id").type(EventTypes.ORGANIZATION).build())))
                .estimatedTimeArrival(shipment.getEstimatedTimeArrival())
                .transportPacks(shipment.getTransportPacks())
                .build();

        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void getShipmentResponse_throwsWhenShipmentDoesNotExist() {
        final Shipment shipment = buildShipment();
        when(shipmentRepository.findById("referenceId")).thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> shipmentService.getShipmentResponse("referenceId"))
                .isInstanceOf(ResourceNotFound.class)
                .hasMessage("Could not find shipment with referenceId: referenceId");
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
                                                        .build()
                                        )
                                        )
                                )
                                .build()
                )
                .build();
    }

}
