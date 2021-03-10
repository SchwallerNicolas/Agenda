package com.example.agenda;

public class Event {
    int idEvent = 0;
    String EventName = null;
    String EventDate = null;
    String EventStart = null;
    String EventEnd = null;
    String idParticipant = null;
    String EventReminder = null;

    public Event(String eventName, String eventDate, String eventStart, String eventEnd, String idParticipant, String eventReminder) {
        super();
        this.EventName = eventName;
        this.EventDate = eventDate;
        this.EventStart = eventStart;
        this.EventEnd = eventEnd;
        this.idParticipant = idParticipant;
        this.EventReminder = eventReminder;
    }

    public int getIdEvent() {
        return idEvent;
    }
    public void setIdEvent(int id) {
        this.idEvent = id;
    }

    public String getEventName() {
        return EventName;
    }
    public void setEventName(String name) {
        this.EventName = name;
    }

    public String getEventDate() {
        return EventDate;
    }
    public void setEventDate(String dateEvent) {
        this.EventDate = dateEvent;
    }

    public String getEventStart() {
        return EventStart;
    }
    public void setEventStart(String startTime) {
        this.EventStart = startTime;
    }

    public String getEventEnd() {
        return EventEnd;
    }
    public void setEventEnd(String endTime) {
        this.EventEnd = endTime;
    }

    public String getIdParticipant() { return idParticipant; }
    public void setIdParticipant(String id) { this.idParticipant = id; }

    public String getEventReminder() {
        return EventReminder;
    }
    public void setEventReminder(String rappel) {
        this.EventReminder = rappel;
    }

}

