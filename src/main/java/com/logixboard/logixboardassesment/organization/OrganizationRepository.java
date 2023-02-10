package com.logixboard.logixboardassesment.organization;

import com.logixboard.logixboardassesment.data.organization.Organization;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface OrganizationRepository extends MongoRepository<Organization, String> {
    Optional<Organization> findOrganizationByCode(final String code);
    Boolean existsByCode(final String code);
}
