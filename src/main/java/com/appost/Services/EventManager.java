package com.appost.Services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm");

        for(Event e : events)
        {
            try
            {
                LocalDateTime tempLocalDate = LocalDateTime.now().minusDays(7);
                Date tempDate = formatter.parse(tempLocalDate.format(dateTimeFormatter));
                Date eventDate = formatter.parse(e.getDate());

                if(eventDate.before(tempDate))
                {
                    partecipantManager.eventDeleted(e.getId());
                    eventRepository.delete(e);
                }
            }
            catch(Exception exception)
            {
                System.err.println(exception.getMessage());
                eventRepository.delete(e);

            }
        }
    }

    public List<Event> getAvailableEvents()
    {
        List<Event> events = eventRepository.findAll();
        List<Event> returnEvents = new ArrayList<Event>();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm" );
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm");
        for(Iterator<Event> i = events.iterator(); i.hasNext();)
        {
            try
            {
                Event e = i.next();
                Date eventDate =(Date) formatter.parse(e.getDate());
                LocalDateTime tempLocalDate = LocalDateTime.now();
                Date tempDate = formatter.parse(tempLocalDate.format(dateTimeFormatter));
                if(eventDate.before(tempDate))
                {
                    i.remove();
                }
                else{
                    returnEvents.add(e);
                }
            }
            catch(Exception exception)
            {
                System.err.println(exception.getMessage());
            }
        }
        return returnEvents;
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

    public void updateEvent(Event event) {
        eventRepository.save(event);
      }
}