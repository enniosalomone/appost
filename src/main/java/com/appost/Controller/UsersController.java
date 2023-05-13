package com.appost.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.appost.Support.RandomString;
import com.appost.Services.EmailService;
import com.appost.Services.EventManager;
import com.appost.Services.PartecipantManager;
import com.appost.Services.UserManager;
import com.appost.model.DiscToApply;
import com.appost.model.Event;
import com.appost.model.Friends;
import com.appost.model.Partecipant;
import com.appost.model.Roles;
import com.appost.model.User;

import org.json.JSONArray;
import org.json.JSONObject;

@RestController
public class UsersController {

    @Autowired
    private UserManager userManager;

    @Autowired
    private PartecipantManager partecipantManager;

    @Autowired
    private EventManager eventManager;

    @Autowired 
    private EmailService emailService;

    @PostMapping("/addUser")
    public String addNewUser(@RequestBody User user) {
        User userAdmin = userManager.searchUserByID(UUID.fromString(user.getId().toString())).get();
        if(userAdmin.getRole() == Roles.ADMIN || user.getRole() == Roles.NORMAL){
        
            User newUser = user;
            user.setResetPasswordRequest(false);
        
            if (newUser.getRole().equals(Roles.NORMAL)) {
                newUser.setPercentageDisc(0);
            }
            UUID newID = UUID.randomUUID();
            while (!userManager.newIDUserAvailable(newID)) {
                newID = UUID.randomUUID();
            }
            newUser.setId(newID);
            userManager.addNewUser(newUser);
            return "Utente " + user.getUsername() + " aggiunto correttamente";
        }
        else{
            return "Non l'autoizzazione per poter aggiungere un nuovo utente con il ruolo assegnatogli.";
        }
    }

    @PostMapping("/increasePercDisc")
    public String increasePercDisc(@RequestBody Map<String, String> map) {

        if (map.containsKey("partecipants") && map.get("partecipants") != null && map.containsKey("idEvent")
                && map.get("idEvent") != null) {
                    Event event = eventManager.searchEventByID(UUID.fromString(map.get("idEvent"))).get();
                    if(UUID.fromString(map.get("idOrganizer")) == event.getIdOrganizer()){
            JSONArray partecipantsToAddDisc = new JSONArray(map.get("partecipants"));
            List<Partecipant> partecipantsToEvent = partecipantManager
                    .getAllPartecipantToEvent(UUID.fromString(map.get("idEvent")));

            if (partecipantsToEvent != null && !partecipantsToAddDisc.toString().equals("") && !partecipantsToAddDisc.isEmpty()) {
                    for (int i = 0; i < partecipantsToAddDisc.length(); i++) {
                        String tempString = (String) partecipantsToAddDisc.get(i);
                        JSONObject jobj = new JSONObject(tempString);

                        if (!userManager.newIDUserAvailable(UUID.fromString(jobj.getString("id")))) {
                            int disc = eventManager.calculatePercDisc(
                                    UUID.fromString(jobj.getString("id")),
                                    UUID.fromString(map.get("idEvent")));

                            User user = userManager.searchUserByID(UUID.fromString(jobj.getString("id")))
                                    .get();
                            int oldPercDisc = user.getPercentageDisc();
                            user.setPercentageDisc(oldPercDisc + disc);

                            userManager.updateUser(user);
                        }
                    }
                    return "Sconti abilitati correttamente";
                              
            }
            else{
            return "Selezionare utenti a cui abilitare lo sconto";
            }

        }else{
            //TO DO informare l'utente dell'errore
            return null;
        }
        }
        else{
            //TO DO informare l'utente dell'errore
            return null;
        }
    }

    @PostMapping("/decreasePercDisc")
    public void decreasePercDisc(@RequestBody Map<String, String> map) {
        UUID idDiscToApply = UUID.fromString(map.get("idDiscToApply"));
        User user = userManager.searchUserByID(UUID.fromString(map.get("idUser"))).get();
        DiscToApply discToApply = userManager.getDiscToApply(idDiscToApply);
        if (user == null)
            user = userManager.searchUserByUsername(map.get("username"));

        if (user != null && idDiscToApply != null) {
            // int percentDiscToSubtract =
            // Integer.parseInt(map.get("percentDiscToSubtract"));
            int percentDiscToSubtract = discToApply.getPercentToDisc();

            int oldPercDisc = user.getPercentageDisc();
            user.setPercentageDisc(oldPercDisc - percentDiscToSubtract);

            if (user.getPercentageDisc() < 0)
                user.setPercentageDisc(0);

            userManager.updateUser(user);
            userManager.deleteDiscToApply(discToApply);
        }

    }

    @PostMapping("/addNewFriend")
    public String addNewFriend(@RequestBody Map<String, String> map) {
        String usernameFriend1 = map.get("usernameFriend1");
        String usernameFriend2 = map.get("usernameFriend2");

        if(userManager.addNewUserFriendship(usernameFriend1, usernameFriend2))
        {
            return "Amico aggiunto correttamente";
        }
        else
        {
            return "L'utente che si desidra aggiungere non è presente nel sistema. " +
            "Assicurati che il nome inserito sia corretto o che l'amico che hai intenzione di aggiungere" +
            "Sia iscritto all'app";
        }
    }

    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> map) {
        if (map.containsKey("username")) {
            User user = userManager.searchUserByUsername(map.get("username"));
            if (user != null) {
                if (map.containsKey("password")) {
                    if (!map.get("password").equals(user.getPassword())) {
                        user = null;
                    }
                } else {
                    return "Errore nel sistema. Riprovare più tardi. Se l'errore persiste reinstallare l'App o "+
                    "segnalare l'accaduto al centro assistenda" +
                    "all'indirizzo appost@x";
                }
            }
            if (user == null){
                return "Username o password errate";
            }

            return new JSONObject(user).toString();
        } else {
            //TO DO inserire la mail di supporto dell'app
            return "Errore nel sistema. Riprovare più tardi. Se l'errore persiste reinstallare l'App o "+
            "segnalare l'accaduto al centro assistenda" +
            "all'indirizzo appost@x";
        }
    }

    @PostMapping(value = "/showSignedUpEvent")
    public List<Event> showSignedUpEvent(@RequestBody Map<String, String> map) {
        if (map.containsKey("idUser") && map.get("idUser") != null) {
            UUID idUser = UUID.fromString(map.get("idUser"));
            List<Partecipant> signedUpEvents = partecipantManager.getSignedUpEventForSpecificUser(idUser);

            if (signedUpEvents != null && signedUpEvents.size() > 0) {
                List<Event> events = new ArrayList<Event>();
                for (Partecipant p : signedUpEvents) {
                    if (eventManager.existingEvent(p.getIdEvent())) {
                        events.add(eventManager.getEvent(p.getIdEvent()));
                    } else {
                        partecipantManager.eventDeleted(p.getIdEvent());
                    }
                }
                return events;
            } else {
                return null;
            }
        } else {
            // TO DO rispondere qualcosa che segnali l'errore
            return null;
        }
    }

    @PostMapping(value = "/addDiscToApply")
    public void addDiscToApply(@RequestBody Map<String, String> map) {

        User user = userManager.searchUserByUsername(map.get("username"));
        User businessUser = userManager.searchUserByUsername(map.get("businessUsername"));
        if (user != null) {
            int percentDiscToSubtract = Integer.parseInt(map.get("percentDiscToSubtract"));

            if (percentDiscToSubtract > 5) {
                percentDiscToSubtract = 5;
            }
            if ((user.getPercentageDisc() - percentDiscToSubtract) < 0) {
                percentDiscToSubtract = user.getPercentageDisc();
            }

            userManager.addDiscToApply(user.getUsername(), percentDiscToSubtract, businessUser.getUsername());
        }
    }

    @PostMapping(value = "/getDiscToApply")
    public List<DiscToApply> getDiscToApply(@RequestBody Map<String, String> map) {
        if (map.containsKey("idUser") && map.get("idUser") != null) {
            User user = userManager.searchUserByID(UUID.fromString(map.get("idUser"))).get();
            if (user != null) {
                List<DiscToApply> response = userManager.getDiscToApplyList(user.getUsername());
                if(response.size() < 1)
                {
                    return null;
                }
                else
                    return response;
            } else {
                // TO DO informare l'utente dell'errore
                return null;
            }
        } else {
            // TO DO informare l'utente dell'errore
            return null;
        }
    }

    @PostMapping(value = "/updateProfile")
    public void updateProfile(@RequestBody Map<String, String> map) {
        if (map.containsKey("idUser") && map.get("idUser") != null) {
            User user = userManager.searchUserByID(UUID.fromString(map.get("idUser"))).get();

            if (user != null) {
                if (map.containsKey("name") && map.get("name") != null) {
                    user.setName(map.get("name"));
                }
                if (map.containsKey("surname") && map.get("surname") != null) {
                    user.setSurname(map.get("surname"));
                }
                if (map.containsKey("email") && map.get("email") != null) {
                    user.setEmail(map.get("email"));
                }
                if (map.containsKey("newPassword") && map.get("newPassword") != null) {
                    if (map.containsKey("oldPassword") && map.get("oldPassword") != null) {
                        if (map.get("oldPassword").equals(user.getPassword())) {
                            user.setPassword(map.get("newPassword"));
                        }
                    }
                }
                userManager.updateUser(user);
            }
        }
    }

    @PostMapping(value = "/getBusinessList")
    public List<User> getBusinessList(@RequestBody Map<String, String> map) {
        if (map.containsKey("idUser") && map.get("idUser") != null
                && !userManager.newIDUserAvailable(UUID.fromString(map.get("idUser")))) {
            List<User> list = userManager.getBusinessList();
            //TO DO togliere le password a tutti gli user ritornati dalla lista
            return list;
        } else {
            // TO DO informare l'utente dell'errore
            return null;
        }
    }

    @PostMapping(value = "/getUserInfo")
    public User getUserInfo(@RequestBody Map<String, String> map) {
        if (map.containsKey("idUser") && map.get("idUser") != null
                && !userManager.newIDUserAvailable(UUID.fromString(map.get("idUser")))) {
            User user = userManager.searchUserByID(UUID.fromString(map.get("idUser"))).get();
            user.setPassword("");
            return user;
        } else {
            // TO DO informare l'utente dell'errore
            return null;
        }
    }

    @PostMapping(value = "/getAllUsers")
    public List<User> getAllUsers(@RequestBody Map<String, String> map) {

        if (map.containsKey("idUser") && map.get("idUser") != null) {
            User admin = userManager.searchUserByID(UUID.fromString(map.get("idUser"))).get();
            if (admin != null && admin.getRole() == Roles.ADMIN) {
                return userManager.gettAllUsers();
            } else {
                return null;
            }
        } else {
            // TO DO informare l'utente dell'errore
            return null;
        }
    }

    @PostMapping(value = "/deleteUsers")
    public String deleteUsers(@RequestBody Map<String, String> map) {
        if (map.containsKey("idUser") && map.get("idUser") != null) {
            User admin = userManager.searchUserByID(UUID.fromString(map.get("idUser"))).get();
            if (admin != null && admin.getRole() == Roles.ADMIN && map.containsKey("idUsers") && map.get("idUsers") != null) {
                JSONArray usersToDelete = new JSONArray(map.get("idUsers"));

                if (!usersToDelete.toString().equals("") && !usersToDelete.isEmpty()) {
                    for (int i = 0; i < usersToDelete.length(); i++) {
                        String tempString = (String) usersToDelete.get(i);
                        if (!userManager.newIDUserAvailable(UUID.fromString(tempString))) {
                            User temUser = userManager.searchUserByID(UUID.fromString(tempString)).get();
                            userManager.deleteUser(temUser);
                        }
                    }
                    return "Utenti selezionato eliminati correttamente";
                }else{
                    return "Nessun utente selezionato";
                }
            }else{
                return "Non hai i permessi necessari per effettuare questa operazione. Se ritieni ci sia un errore contatta gli amministratori";
            }
        }else{
            return "Server error. Rieffettua il login o riprova più tardi";
        }
    }

    @PostMapping(value = "/getFriendsList")
    public List<User> getFriendsList(@RequestBody Map<String, String> map) {
        List<User> friends = new ArrayList<>();
        if (map.containsKey("idUser") && map.get("idUser") != null && !userManager.newIDUserAvailable(UUID.fromString(map.get("idUser")))) {
            
            User user = userManager.searchUserByID(UUID.fromString(map.get("idUser"))).get();
            List<Friends> friendsList =  userManager.getFriendsList(map.get("idUser"));
            for(Friends f : friendsList)
            {
                if(f.getUsernameFriend1().equals(user.getUsername())){
                    friends.add(userManager.searchUserByUsername(f.getUsernameFriend2()));
                }
                else{
                    friends.add(userManager.searchUserByUsername(f.getUsernameFriend1()));
                }
            }
            return friends;
        } else {
            // TO DO informare l'utente dell'errore
            return null;
        }
    }
    
    @PostMapping(value = "/linkResetPassword")
    public String linkResetPassword(@RequestBody Map<String, String> map) {
        if (map.containsKey("emailUser") && map.get("emailUser") != null ) {
            User user = userManager.searchUserByEmail(map.get("emailUser"));
            if(user != null)
            {
                user.setResetPasswordRequest(true);
                //TO DO apparere per la fase non di test
                String body = "Ciao " + user.getUsername() + " Per resettare la password del tuo profilo fai click sul link qui sotto: \n\n" +
                "http://localhost:8080/resetPassword?username=" + user.getUsername();
                emailService.sendSimpleMail(map.get("emailUser"), body, "Reset password Appost");
                return "Riceverai una mail all'indirizzo " + map.get("emailUser") + " per il reset della password del tuo account";
            }
            else{
                return "Non esistono utenti collegati alla mail " + map.get("emailUser");
            }
        }
        else{
            return "Errore del sistema. Riprovare più tardi o contattare gli amministratori";
        }
    }

    @GetMapping(value = "/resetPassword")
    public void resetPassword(@RequestParam String username) {
        if (username != null ) {
            User user = userManager.searchUserByUsername(username);
            if(user != null && user.isResetPasswordRequest())
            {
                String newPassword = RandomString.getAlphaNumericString(8);
                user.setPassword(newPassword);
                user.setResetPasswordRequest(false);
                userManager.updateUser(user);

                //TO DO apparere per la fase non di test
                String body = "Ciao " + user.getUsername() + " La tua password è stata resettata con successo. Accedi all'app con le tua nuove credenziali: \n\n" +
                "Username: " + username + "\n\n" +
                "Password: " + newPassword + "\n\n" + "Suggeriamo di modificare la password con una di tuo gradimento e che sia differente dalla precedente.";
                emailService.sendSimpleMail(user.getEmail(), body, "Appost invio nuove credenziali d'accesso");
            }
            else{
                //TO DO insformare l'utente della mail errata
            }
        }
        else{
            //TO DO insformare l'utente della mail errata
        }
    }

    @PostMapping(value = "/helloworld")
    public String helloWorld() {
        return "Hello World app Appost modificato una seconda volta";
    }

}
