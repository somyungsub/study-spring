package io.ssosso.models;

public class License {
  private String licenseId;
  private String organizationId;

  public License withId(String licenseId) {
    this.licenseId = licenseId;
    return this;
  }

  public License withOrganizationId(String organizationId) {
    this.organizationId = organizationId;
    return this;
  }

  public String getLicenseId() {
    return licenseId;
  }

  public void setLicenseId(String licenseId) {
    this.licenseId = licenseId;
  }

  public String getOrganizationId() {
    return organizationId;
  }

  public void setOrganizationId(String organizationId) {
    this.organizationId = organizationId;
  }
}
