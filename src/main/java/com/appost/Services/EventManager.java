package com.appost.Services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appost.model.Event;

import com.appost.repository.EventRepository;
import com.appost.repository.FriendForEventsRepository;



@Service
public class EventManager {
    
    @Autowired
    EventRepository eventRepository;

    @Autowired
    FriendForEventsRepository friendForEventsRepository;

    @Autowired
    private PartecipantManager partecipantManager;

    public Optional<Event> searchEventByID(UUID idEvent)
    {
        return eventRepository.findById(idEvent);
    }
    public void addNewEvent(Event newEvent)
    {
        if(!existingEvent(newEvent))
        {
            eventRepository.save(newEvent);
        }
    }

    public Event getEvent(UUID idEvent)
    {
        return eventRepository.findById(idEvent).get();
    }
    
    public boolean existingEvent(Event newEvent)
    {
        List<Event> eventsOrganizer = (List<Event>) eventRepository.searchEventsByOrganizer(newEvent.getOrganizer().toString());
       
        boolean eventExist = false;

        for (Event event : eventsOrganizer)
        {
            if(newEvent.getAddress().equalsIgnoreCase(event.getAddress()) && 
            newEvent.getEventName().equalsIgnoreCase(event.getEventName()) &&
            newEvent.getDate().compareTo(event.getDate())==0)
            {
                eventExist = true;
            }
        }
        return eventExist;
    }

    public boolean existingEvent(UUID idEvent)
    {
        Event e = eventRepository.findById(idEvent).get();

        if(e != null)
            return true;
        else
            return false;
    }
    
    public void deleteEvent(UUID idEvent, UUID idOrgnizer)
    {
        Event event = eventRepository.findById(idEvent).get();
        if(event.getOrganizer().equals(idOrgnizer))
            eventRepository.delete(event);
    }

    public void deletePastEvent()
    {
        List<Event> events = eventRepository.findAll();

        DateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd");

        for(Event e : events)
        {
            try
            {
                LocalDate tempLocalDate = LocalDate.now().minusDays(7);
                Date tempDate = (Date) formatter.parse(tempLocalDate.toString());

                if(e.getDate().before(tempDate))
                {
                    partecipantManager.eventDeleted(e.getId());
                    eventRepository.delete(e);
                }
            }
            catch(Exception exception)
            {
                System.err.println(exception.getMessage());
            }
        }
    }

    public List<Event> getAvailableEvents()
    {
        List<Event> events = eventRepository.findAll();

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        for(Event e : events)
        {
            try
            {
                LocalDate tempLocalDate = LocalDate.now();
                Date tempDate = (Date) formatter.parse(tempLocalDate.toString());
                if(e.getDate().before(tempDate))
                {
                    events.remove(e);
                }
            }
            catch(Exception exception)
            {
                System.err.println(exception.getMessage());
            }
        }
        return events;
    }

    public List<Event> getMyEvents(String idPartner)
    {
        List<Event> myEvents = eventRepository.searchEventsByOrganizer(idPartner);

        return myEvents;
    }

    public int calculatePercDisc(UUID idPartecipant, UUID idEvent)
    {
        int disc = friendForEventsRepository.calculatePercDisc(idPartecipant.toString(), idEvent.toString());

        if(disc > 4)
            disc = 5;
        if(disc <= 0)
            disc = 1;

        return disc;

    }
}