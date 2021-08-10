package co.leapa.project.service;

import co.leapa.project.model.Contact;
import co.leapa.project.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;

    public Contact saveContact(Contact contact){
        return  contactRepository.save(contact);
    }
    public List<Contact> getContacts(){
        return contactRepository.getContacts();
    }
    public Optional<Contact> findOne(Integer id) {
        return contactRepository.findById(id);
    }
    public Contact getContactByName(String name){
        return contactRepository.findContactByName(name).isPresent()?contactRepository.findContactByName(name).get():null;
    }
}
