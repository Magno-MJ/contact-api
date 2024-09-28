package com.study.contactapi.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.study.contactapi.domain.user.User;
import com.study.contactapi.dto.CreateContactBodyDTO;
import com.study.contactapi.dto.UpdateContactBodyDTO;
import com.study.contactapi.dto.ContactResponseDTO;
import com.study.contactapi.services.ContactService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/contact")
@Tag(name = "Contact Controller", description = "Controller that manages the user contacts")
public class ContactController {
    @Autowired
    private ContactService contactService;

    @Operation(description = "Create a contact", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ContactResponseDTO.class)
                    )
            ),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping()
    public ResponseEntity<ContactResponseDTO> createContact(@Validated @RequestBody CreateContactBodyDTO createContactBodyDto, @AuthenticationPrincipal User user) {
        ContactResponseDTO createdContact = contactService.createContact(createContactBodyDto, user);

        return new ResponseEntity<>(createdContact, HttpStatus.CREATED);
    }

    @Operation(description = "Find all contacts", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Ok",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ContactResponseDTO.class))
                    )
            ),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping()
    public ResponseEntity<List<ContactResponseDTO>> findAllContacts(@AuthenticationPrincipal User user) {
        List<ContactResponseDTO> contactsFound = contactService.findAllContacts(user.getId());

        return ResponseEntity.ok(contactsFound);
    }

    @Operation(description = "Find contact by id", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Ok",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ContactResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Contact not found", content = @Content(mediaType = "application/json")),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/{contactId}")
    public ResponseEntity<ContactResponseDTO> findContactById(@PathVariable String contactId, @AuthenticationPrincipal User user) {
        ContactResponseDTO contactFound = contactService.findContactById(contactId, user.getId());

        return ResponseEntity.ok(contactFound);
    }

    @Operation(description = "Update contact by id", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Ok",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ContactResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Contact not found", content = @Content(mediaType = "application/json")),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping("/{contactId}")
    public ResponseEntity<ContactResponseDTO> updateContactById(
            @PathVariable String contactId,
            @AuthenticationPrincipal User user,
            @Validated @RequestBody UpdateContactBodyDTO updateContactBodyDto
    ) {
        ContactResponseDTO contactFound = contactService.updateContactById(contactId, user.getId(), updateContactBodyDto);

        return ResponseEntity.ok(contactFound);
    }

    @Operation(description = "Delete contact by id", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Ok",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ContactResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Contact not found", content = @Content(mediaType = "application/json")),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/{contactId}")
    public ResponseEntity<ContactResponseDTO> deleteContactById(
            @PathVariable String contactId,
            @AuthenticationPrincipal User user
    ) {
        ContactResponseDTO contactFound = contactService.deleteContactById(contactId, user.getId());

        return ResponseEntity.ok(contactFound);
    }
}
