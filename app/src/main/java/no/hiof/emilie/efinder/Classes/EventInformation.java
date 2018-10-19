package no.hiof.emilie.efinder.Classes;

import android.media.Image;

public class EventInformation {
    public String eventUID, eventTitle, eventAdress, eventDescription, eventDateTime;

    public int eventPrice, eventAttendants;
    public Image eventImage;

    public EventInformation() {
    }

    public EventInformation(String eventUID, String eventTitle, String eventDateTime, int eventPrice, int eventAttendants, String eventAdress, String eventDescription) {
        this.eventUID = eventUID;
        this.eventTitle = eventTitle;
        this.eventDateTime = eventDateTime;
        this.eventPrice = eventPrice;
        this.eventAttendants = eventAttendants;
        this.eventAdress = eventAdress;
        this.eventDescription = eventDescription;
    }

    public EventInformation(String eventUID, String eventTitle, String eventDateTime, int eventPrice, int eventAttendants, Image eventImage, String eventAdress, String eventDescription) {
        this.eventUID = eventUID;
        this.eventTitle = eventTitle;
        this.eventDateTime = eventDateTime;
        this.eventPrice = eventPrice;
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

    public String getEventAdress() {
        return eventAdress;
    }

    public void setEventAdress(String eventAdress) {
        this.eventAdress = eventAdress;
    }

    public String getEventDateTime() {
        return eventDateTime;
    }

    public void setEventDateTime(String eventDateTime) {
        this.eventDateTime = eventDateTime;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventDate() {
        return eventDateTime;
    }

    public void setEventDate(String eventDate) {
        this.eventDateTime = eventDate;
    }

    public int getEventPrice() {
        return eventPrice;
    }

    public void setEventPrice(int eventPrice) {
        this.eventPrice = eventPrice;
    }

    public int getEventAttendants() {
        return eventAttendants;
    }

    public void setEventAttendants(int eventAttendants) {
        this.eventAttendants = eventAttendants;
    }

    public Image getEventImage() {
        return eventImage;
    }

    public void setEventImage(Image eventImage) {
        this.eventImage = eventImage;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }
}
