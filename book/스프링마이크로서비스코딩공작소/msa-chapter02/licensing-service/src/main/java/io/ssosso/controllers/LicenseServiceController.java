package io.ssosso.controllers;

import io.ssosso.models.License;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "v1/organizations/{organizationId}/licenses")
public class LicenseServiceController {

  @RequestMapping(value = "/{licenseId}", method = RequestMethod.GET)
  public License getLicenses(@PathVariable String organizationId,
                             @PathVariable String licenseId) {
    System.out.println("organizationId = " + organizationId);
    System.out.println("licenseId = " + licenseId);

    return new License()
            .withId(licenseId)
            .withOrganizationId(organizationId);
  }

  @GetMapping(value = "/test/{licenseId}")
  public License test(@PathVariable String organizationId,
                             @PathVariable String licenseId) {
    System.out.println("=======test========");
    System.out.println("organizationId = " + organizationId);
    System.out.println("licenseId = " + licenseId);
    return new License()
            .withId(licenseId)
            .withOrganizationId(organizationId);
  }
}
