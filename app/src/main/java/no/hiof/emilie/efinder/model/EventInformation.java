package no.hiof.emilie.efinder.model;

import android.media.Image;

public class EventInformation {
    public String
            eventUID,
            eventTitle,
            eventAdress,
            eventDescription,
            eventDate,
            eventTime,
            eventImage;
    public int
            eventPayment,
            eventAttendants;


    public EventInformation() {
    }

    public EventInformation(String eventUID, String eventTitle, String eventDateTime, int eventPayment, int eventAttendants, String eventAdress, String eventDescription) {
        this.eventUID = eventUID;
        this.eventTitle = eventTitle;
        this.eventDate = eventDateTime;
        this.eventPayment = eventPayment;
        this.eventAttendants = eventAttendants;
        this.eventAdress = eventAdress;
        this.eventDescription = eventDescription;
    }

    public EventInformation(String eventUID, String eventTitle, String eventDateTime, int eventPayment, int eventAttendants, String eventImage, String eventAdress, String eventDescription) {
        this.eventUID = eventUID;
        this.eventTitle = eventTitle;
        this.eventDate = eventDateTime;
        this.eventPayment = eventPayment;
        this.eventAttendants = eventAttendants;
        this.eventImage = eventImage;
        this.eventAdress = eventAdress;
        this.eventDescription = eventDescription;
    }

    public String getEventUID() {
        return eventUID;
    }

    public void setEventUID(String eventUID) {
        this.eventUID = eventUID;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventAdress() {
        return eventAdress;
    }

    public void setEventAdress(String eventAdress) {
        this.eventAdress = eventAdress;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventImage() {
        return eventImage;
    }

    public void setEventImage(String eventImage) {
        this.eventImage = eventImage;
    }

    public int getEventPayment() {
        return eventPayment;
    }

    public void setEventPayment(int eventPayment) {
        this.eventPayment = eventPayment;
    }

    public int getEventAttendants() {
        return eventAttendants;
    }

    public void setEventAttendants(int eventAttendants) {
        this.eventAttendants = eventAttendants;
    }
}