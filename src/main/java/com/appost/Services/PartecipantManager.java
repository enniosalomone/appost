package com.appost.Services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.appost.model.FriendsForEvent;
import com.appost.model.Partecipant;
import com.appost.repository.FriendForEventsRepository;
import com.appost.repository.PartecipantRepository;

@Service
public class PartecipantManager {

    @Autowired
    PartecipantRepository partecipantRepository;

    @Autowired
    FriendForEventsRepository friendForEventsRepository;

    /*
     * Questo metodo aggiunge un partecipante ad un evento. viene utilizato se un
     * utente vuole partecipare
     * ad un evento a cui non è iscritto
     */
    public void addPartecipantToEvent(UUID idPartecipant, UUID idEvent) {
        Partecipant partecipant = partecipantRepository.searchPartecipantToEvent(idPartecipant.toString(),
                idEvent.toString());

        if (partecipant == null) {
            partecipant = new Partecipant(idPartecipant, idEvent);
            partecipant.setId(UUID.randomUUID());
            partecipantRepository.save(partecipant);
        } else {
            // TO DO add message to return
        }
    }

    /*
     * Questo metodo rimuove un partecipante da un evento. viene utilizato se un
     * utente non vuole più partecipare
     * ad un evento a cui è iscritto
     */
    public void removePartecipant(UUID idPartecipant, UUID idEvent) {
        Partecipant partecipantToDelete = partecipantRepository.searchPartecipantToEvent(idPartecipant.toString(),
                idEvent.toString());

        if (partecipantToDelete != null) {
            partecipantRepository.delete(partecipantToDelete);
        } else {
            // TO DO add message to return
        }
    }

    /*
     * Questo metodo viene utilizzato per aggiungere un amico ad un evento e
     * collegare il proprio ID
     * a quello dell'amico. L'amico deve essere già iscritto all'evento e la
     * relazione non deve esistere.
     * Il numero di amici aggiunti aumenta lo sconto cumulabile fino ad un massimo
     * di 5
     */
    public void addFriendToEvent(UUID idPartecipant, UUID idEvent, UUID idFriend) {
        List<FriendsForEvent> friendsForEventList = friendForEventsRepository
                .searchFrindsToEvent(idPartecipant.toString(), idEvent.toString());
        boolean relationAlreadyExist = false;

        for (FriendsForEvent f : friendsForEventList) {
            if (f.getIdPartecipant1().equals(idPartecipant) && f.getIdPartecipant2().equals(idFriend))
                relationAlreadyExist = true;

            if (f.getIdPartecipant1().equals(idFriend) && f.getIdPartecipant2().equals(idPartecipant))
                relationAlreadyExist = true;
        }
        Partecipant partecipantFriend = partecipantRepository.searchPartecipantToEvent(idFriend.toString(),
                idEvent.toString());
        Partecipant partecipantID = partecipantRepository.searchPartecipantToEvent(idPartecipant.toString(),
                idEvent.toString());

        if (partecipantFriend != null && partecipantID != null) {
            if (!relationAlreadyExist) {
                if (friendsForEventList.size() < 5) {
                    FriendsForEvent friendsForEvent = new FriendsForEvent();
                    friendsForEvent.setId(UUID.randomUUID());
                    friendsForEvent.setIdEvent(idEvent);
                    friendsForEvent.setIdPartecipant1(idPartecipant);
                    friendsForEvent.setIdPartecipant2(idFriend);

                    friendForEventsRepository.save(friendsForEvent);
                } else {
                    // TO DO
                    /*
                     * informare l'utente che ha raggiunto il numero massimo di amici aggiungibili a
                     * questo evento
                     */
                }
            } else {
                // TO DO
                /*
                 * informare l'utente che la relazione esiste già
                 */
            }
        } else {
            // TO DO
            /*
             * inserire risposta da ritornare. Informare l'utente che uno dei due
             * partecipanti
             * non è iscritto all'evento
             */
        }
    }

    @Transactional
    public void eventDeleted(UUID idEvent) {
       partecipantRepository.eventDeleted(idEvent.toString());
       friendForEventsRepository.eventDeleted(idEvent.toString());
    }

    public List<Partecipant> getSignedUpEventForSpecificUser(UUID idUser){
        List<Partecipant> signedUpEvents = partecipantRepository.getSignedUpEventForSpecificUser(idUser.toString());

        return signedUpEvents;
    }

    public List<Partecipant> getAllPartecipantToEvent(UUID idEvent)
    {
        return partecipantRepository.getAllPartecipantToEvent(idEvent.toString());
    }

}