package co.leapa.project.controller;

import co.leapa.project.model.Contact;
import co.leapa.project.service.ContactService;
import co.leapa.project.utils.PDFGenerator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/contacts")
public class ContactController {
    private static Logger logger = LoggerFactory.getLogger(ContactController.class);
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

    @PostMapping(value = "/image")
    ResponseEntity<Contact> saveContactWithImage(@RequestPart("name") String name,@RequestPart("phoneNumber") String phoneNumber,@RequestPart("image") MultipartFile multipartImage)  {

        logger.debug("Receiving Multipart object request...");
        Contact contact = new Contact();
        try {
            contact.setName(name);
            contact.setPhone(phoneNumber);
            contact.setImageName(multipartImage.getOriginalFilename());
            contact.setPhoto(multipartImage.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }


        return ResponseEntity.ok(contactService.saveContact(contact));

    }

    @PutMapping(value = "/{id}")
    ResponseEntity<Contact> uploadImage(@PathVariable("id") Integer id, @RequestPart("image") MultipartFile multipartImage) throws Exception {
        logger.debug("Receiving Multipart request...");
        Contact contact = contactService.findOne(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        contact.setImageName(multipartImage.getOriginalFilename());
        contact.setPhoto(multipartImage.getBytes());

        return ResponseEntity.ok(contactService.saveContact(contact));

    }

    @GetMapping(value = "/{id}/image", produces = MediaType.IMAGE_JPEG_VALUE)
    Resource downloadImage(@PathVariable Integer id) {
        byte[] image = contactService.findOne(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
                .getPhoto();

        return new ByteArrayResource(image);
    }

    @GetMapping(value = "/{id}/image", produces = MediaType.APPLICATION_PDF_VALUE)
    ResponseEntity<InputStreamResource> exportToPdf(@PathVariable("id") Integer id) throws IOException {
        Contact contact = contactService.findOne(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.CONTENT_TYPE,"application/pdf");
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + contact.getImageName() + ".pdf");

        byte[] imageBytes =  contact.getPhoto();

        ByteArrayInputStream bis = PDFGenerator.generatePDF(imageBytes);

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));

    }



}
