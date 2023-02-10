package com.logixboard.logixboardassesment.organization;

import com.logixboard.logixboardassesment.data.organization.Organization;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/organization")
public final class OrganizationController {
    private final OrganizationService organizationService;

    @PutMapping
    public void addOrUpdateOrganization(@RequestBody final Organization organization) {
        log.info("Received organization update: " + organization);
        organizationService.addOrUpdate(organization);
    }

    @GetMapping("/{id}")
    public Organization getOrganization(@PathVariable("id") final String id) {
        log.info("Received get for organization id: " + id);
        return organizationService.get(id);
    }
}
