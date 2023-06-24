package com.appost.Services;
 
public interface EmailService {
    String sendSimpleMail(String receiver, String body, String subject);
    String getSender();
    //String sendMailWithAttachment(EmailDetails details);

}