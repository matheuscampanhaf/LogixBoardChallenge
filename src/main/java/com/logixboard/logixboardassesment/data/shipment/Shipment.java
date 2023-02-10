package com.logixboard.logixboardassesment.data.shipment;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Unwrapped;

import java.util.ArrayList;
import java.util.Optional;

@Data
@Document
@Builder
public final class Shipment {

    @Id
    private String referenceId;
    private ArrayList<String> organizations;
    private String estimatedTimeArrival;
    private TransportPacks transportPacks;
}
