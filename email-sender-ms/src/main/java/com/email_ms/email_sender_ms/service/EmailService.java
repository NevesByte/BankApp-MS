package main.java.com.email_ms.email_sender_ms.service;

import java.time.LocalDateTime;

import main.java.com.email_ms.email_sender_ms.dtos.EmailRecordDto;

@Service
public class EmailService {
    @Autowired
    EmailRepository emailRepository;
    JavaMailSender eMailSender;

    @Value(value = "${spring.mail.username}")
    private String emailFrom;

    @Transactional
    public EmailRecordDto sendEmail(EmailRecordDto emailDto) {
        try{
            emailDto.setSendDateEmail(LocalDateTime.now());
            emailDto.setEmailFrom(emailFrom);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(emailDto.getEmailTo());
            message.setSubject(emailDto.getSubject());
            message.setText(emailDto.getText());
            eMailSender.send(message);
        } catch (MailException e){
            emailDto.setStatusEmail(StatusEmail.ERROR);
        }finally{
            return emailDto;
        }
    }

}