package com.appost.Controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.ParseException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Map;
import java.util.UUID;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.appost.Services.EventManager;
import com.appost.Services.PartecipantManager;
import com.appost.Services.UserManager;
import com.appost.model.Event;
import com.appost.model.Partecipant;
import com.appost.model.Roles;
import com.appost.model.User;

@RestController
public class EventController {

    @Autowired
    private EventManager eventManager;

    @Autowired
    private UserManager userManager;

    @Autowired
    private PartecipantManager partecipantManager;

    @PostMapping("/addEvent")
    public void addNewUser(@RequestBody Map<String, String> event) {
        Event newEvent = new Event();
        DateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd");

        newEvent.setAddress(event.get("address"));
        newEvent.setEventName(event.get("eventName"));
        newEvent.setOrganizer(UUID.fromString(event.get("idOrganizer")));
        try {
            newEvent.setDate((Date) formatter.parse(event.get("date")));
        } catch (ParseException e) {
            System.err.println("Date format error. Send date in the format DD-MM-YYYY");
            System.err.println(e.getMessage());
        }
        User organizer = userManager.searchUserByID(newEvent.getOrganizer()).get();
        if (organizer != null) {
            newEvent.setId(UUID.randomUUID());
            newEvent.setNameOrganizer(organizer.getUsername());
            eventManager.addNewEvent(newEvent);
        }
    }

    @PostMapping("/deleteEvent")
    public void deleteEvent(@RequestBody Map<String, String> toDelete) {
        UUID idEvent = UUID.fromString(toDelete.get("idEvent"));
        UUID idOrganizer = UUID.fromString(toDelete.get("idOrganizer"));

        Event eventToDelete = eventManager.searchEventByID(idEvent).get();
        if (eventToDelete != null) {
            if (idOrganizer.equals(eventToDelete.getOrganizer())) {
                eventManager.deleteEvent(idEvent, idOrganizer);
                partecipantManager.eventDeleted(idEvent);
            }
        }
    }

    @PostMapping("/addFriendToEvent")
    public void addFriendToEvent(@RequestBody Map<String, String> friendToAddEvent) {

        JSONArray friendsToAdd = new JSONArray(friendToAddEvent.get("friends"));

        for (int i = 0; i < friendsToAdd.length(); i++) {
            UUID idPartecipant = UUID.fromString(friendToAddEvent.get("idPartecipant"));
            UUID idFriend = UUID.fromString((String) friendsToAdd.get(i));
            UUID idEvent = UUID.fromString(friendToAddEvent.get("idEvent"));

            partecipantManager.addFriendToEvent(idPartecipant, idEvent, idFriend);
        }
    }

    @PostMapping("/addPartecipant")
    public void addPartecipant(@RequestBody Map<String, String> userPartecipant) {
        UUID idEvent = null;
        UUID idPartecipant = null;
        if(userPartecipant.containsKey("idEvent") && userPartecipant.get("idEvent") != null){
            idEvent = UUID.fromString(userPartecipant.get("idEvent"));
        }
        if(userPartecipant.containsKey("idPartecipant") && userPartecipant.get("idPartecipant") != null){
            idPartecipant = UUID.fromString(userPartecipant.get("idPartecipant"));
        }
        if(idEvent != null && idPartecipant != null){
            if (userManager.searchUserByID(idPartecipant) != null) {
                partecipantManager.addPartecipantToEvent(idPartecipant, idEvent);
            } else {
            //TO DO
            /* inserire risposta da ritornare. informare l'utente che non risulta iscritto e che c'è un errore
             * nel messaggio
             */
            }
        }
        else{
            // TO DO 
            /* inserire risposta da ritornare. informare l'utente che c'è un errore
             * nel messaggio
             */
        }
    }

    @PostMapping("/deletePartecipant")
    public void deletePartecipant(@RequestBody Map<String, String> map)
    {
        if(map.containsKey("idPartecipant") && map.get("idPartecipant") != null){
            if(map.containsKey("idEvent") && map.get("idEvent") != null){
                UUID idPartecipant = UUID.fromString(map.get("idPartecipant"));
                UUID idEvent = UUID.fromString(map.get("idEvent"));

                partecipantManager.removePartecipant(idPartecipant, idEvent);
            }
        }
        else{
            //TO DO informare l'utente dell'errore
        }
    }
    @PostMapping("/availableEvents") 
    public List<Event> availableEvents(@RequestBody Map<String, String> map)
    {
        if(map.containsKey("idUser")){
            UUID idUser = UUID.fromString(map.get("idUser"));
            if(!userManager.newIDUserAvailable(idUser)){
                return eventManager.getAvailableEvents();
            }
            else 
                return null;
        }
        else
            return null;
    }

    @PostMapping("/getMyEvents")
    public List<Event> getMyEvents(@RequestBody Map<String, String> map){
        
        if(map.containsKey("idPartner") && map.get("idPartner") != null ){
            
            UUID idUser = UUID.fromString(map.get("idPartner"));
            User userPartner = userManager.searchUserByID(idUser).get();
            List<Event> myEvents = null;

            if(userPartner != null && userPartner.getRole() == Roles.PARTNER)
            {
                myEvents = eventManager.getMyEvents(idUser.toString());
            }
            return myEvents;
        }
        else{
            //TO DO ritornare informazioni necessarie
            return null;
        }
    }

    @PostMapping("/partecipantsEvent")
    public List<User> partecipantsEvent(@RequestBody Map<String, String> map){

        if(map.containsKey("idEvent") && map.get("idEvent") != null)
        {
            if(map.containsKey("idOrganizer") && map.get("idOrganizer") != null)
            {
                Event event = eventManager.searchEventByID(UUID.fromString(map.get("idEvent"))).get();
                if(event != null && event.getIdOrganizer().equals(UUID.fromString(map.get("idOrganizer"))))
                {
                    List<Partecipant> partecipants = partecipantManager.getAllPartecipantToEvent(event.getId());

                    if(partecipants != null)
                    {
                        List<User> partecipantsUsers = new ArrayList<User>();
                        for(Partecipant p : partecipants)
                        {
                            User user = userManager.searchUserByID(p.getIdPartecipant()).get();

                            if(user != null)
                            {
                                partecipantsUsers.add(user);
                            }
                        }
                        return partecipantsUsers;
                    }
                    else{
                        //TO DO informare l'utente che non ci sono iscritti all'evento
                        return null;
                    }
                }
                else{
                    //TO DO informare l'utente della ricevuta richiesta errata
                    return null;
                }
            }
            else{
                //TO DO informare l'utente della ricevuta richiesta errata
                return null;
            }
        }
        else{
            //TO DO informare l'utente della ricevuta richiesta errata
            return null;
        }

    }

    @PostMapping("/deleteOldEvent")
    public void deleteOldEvent(@RequestBody Map<String, String> map){

        if(map.containsKey("idUser") && map.get("idUser") != null)
        {
            User admin = userManager.searchUserByID(UUID.fromString(map.get("idUser"))).get();
            if(admin != null && admin.getRole() == Roles.ADMIN)
            {
                eventManager.deletePastEvent();
            }
            else{
                //TO DO informare l'utente della ricevuta richiesta errata
            }
        }
        else{
            //TO DO informare l'utente della ricevuta richiesta errata
        }

    }

    @PostMapping("/getEventInfo")
    public Event getEventInfo(@RequestBody Map<String, String> map){

        if(map.containsKey("idEvent") && map.get("idEvent") != null)
        {
            Event event = eventManager.searchEventByID(UUID.fromString(map.get("idEvent"))).get();

            if(event != null)
            {
                return event;
            }
            else{
                //TO DO informare l'utente della ricevuta richiesta errata
                return null;
            }
        }
        else{
            //TO DO informare l'utente della ricevuta richiesta errata
            return null;
        }

    }

}
