package com.example.agenda;

public class Event {
    int idEvent = 0;
    String whichPerson = null;
    String EventName = null;
    String EventDate = null;
    String EventStart = null;
    String EventEnd = null;
    int idParticipant = 0;

    public Event(String user, String eventName, String eventDate, String eventStart, String eventEnd) {
        super();
        this.whichPerson = user;
        this.EventName = eventName;
        this.EventDate = eventDate;
        this.EventStart = eventStart;
        this.EventEnd = eventEnd;
        //this.idParticipant = idParticipant;
    }

    public int getIdEvent() {
        return idEvent;
    }
    public void setIdEvent(int id) {
        this.idEvent = id;
    }

    public String getWhichPerson() {
        return whichPerson;
    }
    public void setWhichPerson(String name) {
        this.EventName = name;
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

    //public int getIdParticipant() { return idParticipant; }
    //public void setIdParticipant(int id) { this.idParticipant = id; }

}
