package com.logixboard.logixboardassesment.data.shipment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class TotalWeight {
    private double weight;
    private WeightUnit unit;
}
