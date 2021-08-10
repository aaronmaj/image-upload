package co.leapa.project.repository;

import co.leapa.project.model.Contact;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends CrudRepository<Contact, Integer> {

    public List<Contact> getContacts();
    public Optional<Contact> findContactByName(String name);
}
