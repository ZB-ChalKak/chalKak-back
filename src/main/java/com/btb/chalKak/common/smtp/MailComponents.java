package com.btb.chalKak.common.smtp;

import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailComponents {

    private final JavaMailSender javaMailSender;

    public void sendMail(String email, String subject, String text) {

        MimeMessagePreparator msg = new MimeMessagePreparator() {

            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                mimeMessageHelper.setTo(email);
                mimeMessageHelper.setSubject(subject);
                mimeMessageHelper.setText(text, true);

            }
        };

        try {
            javaMailSender.send(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}