package com.marek_kawalski.clinic_system.email.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.marek_kawalski.clinic_system.email.model.EmailDTO;
import com.marek_kawalski.clinic_system.email.services.EmailServices;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/send-email")
public class EmailController {
   @Autowired
   EmailServices emailServices;    

   @PostMapping("/enviar")
   public ResponseEntity<String> sendEmail(@RequestBody EmailDTO email) throws MessagingException{
      emailServices.sendMail(email);
      return new ResponseEntity<>("correo Enviado exitosamente", HttpStatus.OK);
   }
}
