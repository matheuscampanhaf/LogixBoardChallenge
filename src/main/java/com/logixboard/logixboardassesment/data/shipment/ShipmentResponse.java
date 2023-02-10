package com.logixboard.logixboardassesment.data.shipment;

import com.logixboard.logixboardassesment.data.organization.Organization;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Data
@Builder
public final class ShipmentResponse {
    private String referenceId;
    private ArrayList<Organization> organizations;
    private String estimatedTimeArrival;
    private TransportPacks transportPacks;
}
