package co.leapa.project.controller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import co.leapa.project.model.Contact;
import co.leapa.project.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/v1/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @GetMapping
    public ResponseEntity<List<Contact>> getContacts() {
        return ResponseEntity.ok(contactService.getContacts());
    }

    @GetMapping(value = "/{name}")
    public ResponseEntity<Contact> getContactByName(@PathVariable("name") String name) {
        return ResponseEntity.ok(contactService.getContactByName(name));

    }

    @PostMapping
    public ResponseEntity<Contact> saveContact(@RequestBody Contact contact) {
        return ResponseEntity.ok(contactService.saveContact(contact));
    }

    @PutMapping(value = "/{id}")
    ResponseEntity<Integer> uploadImage(@PathVariable("id") Integer id, @RequestParam MultipartFile multipartImage) throws Exception {
        Contact contact = contactService.findOne(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        contact.setImageName(multipartImage.getName());
        contact.setPhoto(multipartImage.getBytes());

        return ResponseEntity.ok(contactService.saveContact(contact).getId());

    }

    @GetMapping(value = "/{id}/image", produces = MediaType.IMAGE_JPEG_VALUE)
    Resource downloadImage(@PathVariable Integer id) {
        byte[] image = contactService.findOne(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
                .getPhoto();

        return new ByteArrayResource(image);
    }
}
