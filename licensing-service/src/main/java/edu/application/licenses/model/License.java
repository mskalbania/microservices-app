package edu.application.licenses.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "licenses")
public class License {

    @Id
    @Column(name = "license_id", nullable = false)
    private String id;

    @Column(name = "organization_id", nullable = false)
    private String organizationId;

    @Column(name = "license_type", nullable = false)
    private String licenseType;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "license_max", nullable = false)
    private int licenseMax;

    @Column(name = "license_allocated")
    private int licenseAllocated;

    @Column(name = "comment")
    private String comment;

    @Transient
    private String organizationName = "";

    @Transient
    private String contactName = "";

    @Transient
    private String contactPhone = "";

    @Transient
    private String contactEmail = "";

    public License withOrganizationInfo(Organization organization) {
        this.organizationName = organization.getName();
        this.contactName = organization.getContactName();
        this.contactPhone = organization.getContactPhone();
        this.contactEmail = organization.getContactEmail();
        return this;
    }
}
