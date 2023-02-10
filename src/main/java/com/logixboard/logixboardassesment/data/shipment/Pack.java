package com.logixboard.logixboardassesment.data.shipment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class Pack {
    private TotalWeight totalWeight;
}
