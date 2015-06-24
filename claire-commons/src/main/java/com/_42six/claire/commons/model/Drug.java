package com._42six.claire.commons.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * POJO for drug details
 */
@XmlRootElement
@SuppressWarnings("unused")
public class Drug {
    private String brandName;
    private String genericName;
    private String description;
    private String pharmacodynamics;

    public String getBrandName() {
        return brandName;
    }

    public Drug setBrandName(String brandName) {
        this.brandName = brandName;
        return this;
    }

    public String getGenericName() {
        return genericName;
    }

    public Drug setGenericName(String genericName) {
        this.genericName = genericName;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Drug setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getPharmacodynamics() {
        return pharmacodynamics;
    }

    public Drug setPharmacodynamics(String pharmacodynamics) {
        this.pharmacodynamics = pharmacodynamics;
        return this;
    }
}
