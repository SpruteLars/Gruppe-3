package no.hiof.emilie.efinder.Classes;

import android.media.Image;

import java.util.Date;

public class EventHandler {
    public String eventUID, eventTitle, eventAdress, eventDescription;
    public Date eventDateTime;
    public int eventPrice, eventAttendants;
    public Image eventImage;

    public EventHandler(String eventUID, String eventTitle, Date eventDateTime, int eventPrice, int eventAttendants, Image eventImage, String eventAdress, String eventDescription) {
        this.eventUID = eventUID;
        this.eventTitle = eventTitle;
        this.eventDateTime = eventDateTime;
        this.eventPrice = eventPrice;
        this.eventAttendants = eventAttendants;
        this.eventImage = eventImage;
        this.eventAdress = eventAdress;
        this.eventDescription = eventDescription;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public Date getEventDate() {
        return eventDateTime;
    }

    public void setEventDate(Date eventDate) {
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
