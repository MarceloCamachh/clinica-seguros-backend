package com.marek_kawalski.clinic_system.email.services;

import com.marek_kawalski.clinic_system.email.model.EmailDTO;

import jakarta.mail.MessagingException;

public interface EmailServices {
    public void sendMail(EmailDTO email)throws MessagingException;
}
