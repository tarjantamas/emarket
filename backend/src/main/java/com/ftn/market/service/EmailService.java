package com.ftn.market.service;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

  private final JavaMailSender javaMailSender;

  @Async
  public void sendMail(final String recipient, final String subject, final String text) {
    try {
      final MimeMessage message = javaMailSender.createMimeMessage();
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
      message.setSubject(subject);
      message.setText(text, "utf-8", "html");

      javaMailSender.send(message);
      log.info("Successfully sent email to '{}'", recipient);
    } catch (final Exception e) {
      log.error("Exception while sending mail to '{}'", recipient, e);
    }
  }
}