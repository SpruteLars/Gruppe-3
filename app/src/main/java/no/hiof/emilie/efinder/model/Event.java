package no.hiof.emilie.efinder.model;

public class Event {
    private String uid;
    private String name;
    private String description;
    private String date;
    private String clock;
    private String payment;
    private String attendants;
    private String adresse;
    private String posterUrl;

    public Event() { }

    public Event(String uid, String name, String description, String date, String clock, String payment, String attendants, String adresse, String posterUrl) {
        this.uid = uid;
        this.name = name;
        this.description = description;
        this.date = date;
        this.clock = clock;
        this.payment = payment;
        this.attendants = attendants;
        this.adresse = adresse;
        this.posterUrl = posterUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getClock() {
        return clock;
    }

    public void setClock(String clock) {
        this.clock = clock;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getAttendants() {
        return attendants;
    }

    public void setAttendants(String attendants) {
        this.attendants = attendants;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }
}
