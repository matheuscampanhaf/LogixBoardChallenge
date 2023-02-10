# Logixboard Backend Engineering Take-Home Challenge

This code implements the challenge using java + spring and mongoDB.

### PUT /api/v1/organization endpoint
Adds or update organization. You can't add a organization code that already
exists to a different organization id.

### GET /api/v1/organization/{id} endpoint
Retrieves organization if it exists

### PUT /api/v1/shipment endpoint
Add or update shipment. If the shipment contains an organization code that does not
exist, the operation will fail.

### GET /api/v1/shipment/{referenceId} endpoint
Retrieves shipment with enriched organization information. If the organization code has changed
during the shipment time, it will return the new id for that organization code or nothing if there is
no organization with that code.

### GET /api/v1/totalWeight?weightUnit={weightUnit}
Retrieves total weight of shipments in the desired unit.

### GET /api/v2/totalWeight?weightUnit={weightUnit}
Retrieves total weight of shipments in the desired unit.
The difference between v2 and v1 is that v1 does a findAll() on DB, which can
cause problems in scale (if we have 1M shipments it will break the request). Given that,
v2 uses a "cache" that has the precomputed total weight, hence request is faster and more performatic.

## Tests
Writing tests to all the classes takes too long, so I added to the main ones.
To run tests:
```agsl
./gradlew test
```

## Improvements to be done
- Validate required parameters in request. If something is missing return bad request.
- Improve TotalShipmentsWeightInKgCounter to allow decimal values. Toda it is using AtomicLong to avoid concurrency, we could implement an AtomicDouble.
- Add coverage check for tests.
- Add spring integration tests.


