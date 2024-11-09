package com.example.demo.service;

import com.example.demo.entity.Appointment;
import com.example.demo.entity.AppointmentDetail;
import com.example.demo.model.EmailDetail;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {

    @Autowired
    TemplateEngine templateEngine;

    @Autowired
    JavaMailSender javaMailSender;

    public void sendWelcomeEmail(EmailDetail emailDetail) {
        try {
            Context context = new Context();
            context.setVariable("name",emailDetail.getReceiver().getEmail());
            context.setVariable("button","Đến HairHarmony");
            context.setVariable("link",emailDetail.getLink());
            String template = templateEngine.process("welcome-template", context);

            //Creating a simple mail message
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

            //Setting up necessary details
            mimeMessageHelper.setFrom("hairharmony@gmail.com");
            mimeMessageHelper.setTo(emailDetail.getReceiver().getEmail());
            mimeMessageHelper.setText(template, true);
            mimeMessageHelper.setSubject(emailDetail.getSubject());

            //Send email
            javaMailSender.send(mimeMessage);
        }catch(MessagingException e) {
            System.out.println("GỬI EMAIL BỊ LỖI!!!");
        }
    }

    public void sendResetPasswordEmail(EmailDetail emailDetail) {
        try {
            Context context = new Context();
            context.setVariable("name",emailDetail.getReceiver().getEmail());
            context.setVariable("button","Đặt lại mật khẩu");
            context.setVariable("link",emailDetail.getLink());
            String template = templateEngine.process("reset-password-template", context);

            //Creating a simple mail message
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

            //Setting up necessary details
            mimeMessageHelper.setFrom("hairharmony@gmail.com");
            mimeMessageHelper.setTo(emailDetail.getReceiver().getEmail());
            mimeMessageHelper.setText(template, true);
            mimeMessageHelper.setSubject(emailDetail.getSubject());

            //Send email
            javaMailSender.send(mimeMessage);
        }catch(MessagingException e) {
            System.out.println("GỬI EMAIL BỊ LỖI!!!");
        }
    }

    public void sendAppointmentEmail(EmailDetail emailDetail) {
        try {
            Context context = new Context();
            context.setVariable("name",emailDetail.getReceiver().getEmail());
            context.setVariable("button","Quản lý lịch hẹn");
            context.setVariable("link",emailDetail.getLink());
            String template = templateEngine.process("appointment-template", context);

            //Creating a simple mail message
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

            //Setting up necessary details
            mimeMessageHelper.setFrom("hairharmony@gmail.com");
            mimeMessageHelper.setTo(emailDetail.getReceiver().getEmail());
            mimeMessageHelper.setText(template, true);
            mimeMessageHelper.setSubject(emailDetail.getSubject());

            //Send email
            javaMailSender.send(mimeMessage);
        }catch(MessagingException e) {
            System.out.println("GỬI EMAIL BỊ LỖI!!!");
        }
    }




}

