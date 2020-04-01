package edu.application.licenses.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
}
