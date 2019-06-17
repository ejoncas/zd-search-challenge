package com.zendesk.codingchallenge.search.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * POJO that represents an Organisation
 */
public class Organisation extends BaseEntity<Integer> {

    @SerializedName("domain_names")
    private List<String> domainNames;
    private String details;
    @SerializedName("shared_tickets")
    private boolean sharedTickets;

    private String name;

    public List<String> getDomainNames() {
        return domainNames;
    }

    public void setDomainNames(List<String> domainNames) {
        this.domainNames = domainNames;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public boolean isSharedTickets() {
        return sharedTickets;
    }

    public void setSharedTickets(boolean sharedTickets) {
        this.sharedTickets = sharedTickets;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
