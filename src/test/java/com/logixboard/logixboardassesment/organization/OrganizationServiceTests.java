package com.logixboard.logixboardassesment.organization;

import com.logixboard.logixboardassesment.data.EventTypes;
import com.logixboard.logixboardassesment.data.organization.Organization;
import com.logixboard.logixboardassesment.exceptions.ResourceNotFound;
import com.logixboard.logixboardassesment.organization.OrganizationRepository;
import com.logixboard.logixboardassesment.organization.OrganizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.util.AssertionErrors.assertTrue;

public class OrganizationServiceTests {

    private OrganizationRepository organizationRepository;
    private Organization organization;
    private OrganizationService organizationService;

    @BeforeEach
    void setUp() {
        organizationRepository = mock(OrganizationRepository.class);
        organizationService = new OrganizationService(organizationRepository);

    }

    @Test
    void addOrUpdate_saveInRepository() {
        final OrganizationRepository organizationRepository = mock(OrganizationRepository.class);
        final Organization organization = mock(Organization.class);

        final OrganizationService organizationService = new OrganizationService(organizationRepository);

        organizationService.addOrUpdate(organization);

        verify(organizationRepository).save(organization);
    }

    @Test
    void get_returnOrganizationWhenItExists() {
        organization = Organization
                .builder()
                .type(EventTypes.ORGANIZATION)
                .id("id")
                .code("MAT")
                .build();
        when(organizationRepository.findById("id")).thenReturn(Optional.of(organization));

        assertThat(organizationService.get("id")).isEqualTo(organization);
    }

    @Test
    void get_throwsWhenOrganizationNotFound() {
        when(organizationRepository.findById("id")).thenThrow(new ResourceNotFound("ORG NOT FOUND"));
        assertThatThrownBy(() -> organizationService.get("id")).isInstanceOf(ResourceNotFound.class);
    }

    @Test
    void getOrganizationByCode_returnsResultFromRepository() {
        organization = mock(Organization.class);
        when(organizationRepository.findOrganizationByCode("MAT")).thenReturn(Optional.of(organization));

        assertThat(organizationService.getOrganizationByCode("MAT")).isEqualTo(Optional.of(organization));
    }

    @Test
    void existsOrganizationCode_returnsBoolean() {
        when(organizationRepository.existsByCode("MAT")).thenReturn(true);
        assertThat(organizationService.existsOrganizationCode("MAT")).isTrue();
    }
}
