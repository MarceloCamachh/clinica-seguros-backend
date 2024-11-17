package com.marek_kawalski.clinic_system.email.services;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.marek_kawalski.clinic_system.email.model.EmailDTO;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailServices {
   private final JavaMailSender javaMailSender;
   private final TemplateEngine templateEngine;

    public EmailServiceImpl(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }
    @Override
    public void sendMail(EmailDTO email) throws MessagingException{
       try{
       MimeMessage message = javaMailSender.createMimeMessage();
       MimeMessageHelper helper= new MimeMessageHelper(message,true,"UTF-8");
       helper.setTo(email.getDestinatario());
       helper.setSubject(email.getAsunto());
       //helper.setText("otra opcion");
       Context context = new Context();
       context.setVariable("mensaje",email.getMensaje());
       String contentHTML = templateEngine.process("email",context);
       helper.setText(contentHTML,true);
       javaMailSender.send(message);
      }catch(Exception e){
        throw new RuntimeException("Error al enviar el correo "+e.getMessage(),e);
      }
    }
   
}
