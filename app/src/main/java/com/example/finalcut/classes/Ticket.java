package com.example.finalcut.classes;

public class Ticket {
    private String eventID;
    private String ticketNumber;
    private String serviceName;
    private String state;
    private boolean completed;
    private String opinion;
    private int rating;
    private boolean servOver;
    private boolean waitOVer;
    private String description;
    private String queueID;
    private String parentID;
    private long proposalTime;
    private String maxServime;
    private String window;


    public Ticket(String eventID, String ticketNumber, String serviceName, String state, String servOver, String waitOver) {
        this.eventID = eventID;
        this.ticketNumber = ticketNumber;
        this.serviceName = serviceName;
        this.state= state;
        this.servOver = Boolean.parseBoolean(servOver);
        this.waitOVer = Boolean.parseBoolean(waitOver);
    }


    public Ticket(String serviceString, String queueID, String parentID,String maxServime) {
        this.serviceName = serviceString;
        this.queueID = queueID;
        this.parentID = parentID;
        this.maxServime = maxServime;
    }


    public Ticket(String eventID, String ticketNumber, String serviceName, long proposalTime) {
        this.eventID = eventID;
        this.ticketNumber = ticketNumber;
        this.serviceName = serviceName;
        this.proposalTime = proposalTime;
    }

    public Ticket(String ticketNumber, String window) {
        this.ticketNumber = ticketNumber;
        this.window = window;
    }

    public boolean isCompleted() {
        return completed;
    }

    public String getMaxServime() {
        return maxServime;
    }

    public void setMaxServime(String maxServime) {
        this.maxServime = maxServime;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public long getProposalTime() {
        return proposalTime;
    }

    public void setProposalTime(long proposalTime) {
        this.proposalTime = proposalTime;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEventID() {
        return eventID;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getState() {
        return state;
    }
    public boolean isServOver() {
        return this.servOver;
    }

    public boolean isWaitOVer() {
        return this.waitOVer;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public void setRating(String rating) {
        this.rating = Integer.parseInt(rating);
    }

    public String getOpinion() {
        return opinion;
    }

    public int getRating() {
        return rating;
    }

    public String getQueueID() {
        return queueID;
    }

    public String getParentID() {
        return parentID;
    }

    public String getWindow() {
        return window;
    }

    public void setWindow(String window) {
        this.window = window;
    }
}
