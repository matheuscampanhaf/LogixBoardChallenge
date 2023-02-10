package com.logixboard.logixboardassesment.shipment.weight;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

/**
 * A better usage for this counter would be to implment an AtomicDouble, but it would require more time.
 * Since it is a long variable, it is missing the decimal weight and can lead to inaccuracy.
 *
 * The concept here is to always have the total weight precomputed and cached.
 * In a distributed system, I would suggest to use a main cache since it guarantees concurrency.
 *
 */
@Service
public class TotalShipmentsWeightInKgCounter {
    private final AtomicLong totalWeightInKg = new AtomicLong(0);

    public long getValue() {
        return totalWeightInKg.get();
    }
    public void add(final Long weightToIncrement) {
        while(true) {
            final long existingValue = getValue();
            final long newValue = existingValue + weightToIncrement;
            if(totalWeightInKg.compareAndSet(existingValue, newValue)) {
                return;
            }
        }
    }

    public void decrement(final Long weightToDecrement) {
        while(true) {
            final long existingValue = getValue();
            final long newValue = existingValue - weightToDecrement;
            if(totalWeightInKg.compareAndSet(existingValue, newValue)) {
                return;
            }
        }
    }
}