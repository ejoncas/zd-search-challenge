
package com.zendesk.codingchallenge.search.model;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

/**
 * POJO that represents a Ticket
 */
public class Ticket extends BaseEntity<UUID> {

    public enum Type {
        task, question, problem, incident,
    }

    public enum Priority {
        low, normal, high, urgent
    }

    public enum Status {
        closed, hold, open, pending
    }

    private Type type;
    private String subject;
    private String description;
    private Priority priority;
    private Status status;
    @SerializedName("submitter_id")
    private int submitterId;
    @SerializedName("assignee_id")
    private int assigneeId;
    @SerializedName("organization_id")
    private int organizationId;
    @SerializedName("has_incidents")
    private boolean hasIncidents;
    @SerializedName("due_at")
    private String dueAt;
    private String via;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getSubmitterId() {
        return submitterId;
    }

    public void setSubmitterId(int submitterId) {
        this.submitterId = submitterId;
    }

    public int getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(int assigneeId) {
        this.assigneeId = assigneeId;
    }

    public int getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
    }

    public boolean isHasIncidents() {
        return hasIncidents;
    }

    public void setHasIncidents(boolean hasIncidents) {
        this.hasIncidents = hasIncidents;
    }

    public String getDueAt() {
        return dueAt;
    }

    public void setDueAt(String dueAt) {
        this.dueAt = dueAt;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }
}
