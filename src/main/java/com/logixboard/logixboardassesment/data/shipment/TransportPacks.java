package com.logixboard.logixboardassesment.data.shipment;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Data
@Builder
public final class TransportPacks {
    private ArrayList<Pack> nodes;
}
