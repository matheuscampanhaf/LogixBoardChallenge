package com.logixboard.logixboardassesment.organization;

import com.logixboard.logixboardassesment.data.organization.Organization;
import com.logixboard.logixboardassesment.exceptions.ResourceNotFound;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    public void addOrUpdate(final Organization organization) {
        organizationRepository.save(organization);
    }

    public Organization get(final String id) {
        final Optional<Organization> organizationOptional = organizationRepository.findById(id);

        if (!organizationOptional.isPresent()) {
            throw new ResourceNotFound("Organization id not found: " + id);
        }

        return organizationOptional.get();
    }

    public Optional<Organization> getOrganizationByCode(final String organizationCode) {
        return organizationRepository.findOrganizationByCode(organizationCode);
    }

    public boolean existsOrganizationCode(final String organizationCode) {
        return organizationRepository.existsByCode(organizationCode);
    }
}
