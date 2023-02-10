package com.logixboard.logixboardassesment.shipment;

import com.logixboard.logixboardassesment.data.organization.Organization;
import com.logixboard.logixboardassesment.data.shipment.Shipment;
import com.logixboard.logixboardassesment.data.shipment.ShipmentResponse;
import com.logixboard.logixboardassesment.exceptions.ResourceNotFound;
import com.logixboard.logixboardassesment.organization.OrganizationService;
import com.logixboard.logixboardassesment.shipment.weight.ShipmentsWeightService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public final class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final OrganizationService organizationService;
    private final ShipmentsWeightService shipmentsWeightService;

    public void addOrUpdate(final Shipment shipment) {
        shipment.getOrganizations().forEach(this::validateOrganizationCodeExists);
        shipmentsWeightService.updateShipmentsTotalWeightCounter(shipment);
        shipmentRepository.save(shipment);
    }

    public ShipmentResponse getShipmentResponse(final String referenceId) {
        final Optional<Shipment> shipmentOptional = shipmentRepository.findById(referenceId);

        if (!shipmentOptional.isPresent()) {
            throw new ResourceNotFound("Could not find shipment with referenceId: " + referenceId);
        }

        return convertToShipmentResponse(shipmentOptional.get());
    }

    private void validateOrganizationCodeExists(final String organizationCode) {
        if(!organizationService.existsOrganizationCode(organizationCode)) {
            throw new IllegalArgumentException("Could not find organization with code: " + organizationCode);
        }
    }

    private ShipmentResponse convertToShipmentResponse(final Shipment shipment) {
        final ArrayList<Organization> enrichedOrganizations =
                shipment.getOrganizations().stream()
                .map(organizationService::getOrganizationByCode)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toCollection(ArrayList::new));

        return ShipmentResponse.builder()
                .referenceId(shipment.getReferenceId())
                .organizations(enrichedOrganizations)
                .estimatedTimeArrival(shipment.getEstimatedTimeArrival())
                .transportPacks(shipment.getTransportPacks())
                .build();
    }
}
