package librarymanagementsystem.assignment.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import librarymanagementsystem.assignment.entity.MailsSent;
import librarymanagementsystem.assignment.entity.User;
import librarymanagementsystem.assignment.model.Mail;
import librarymanagementsystem.assignment.repository.MailsSentRepository;

@Service
public class MailService {
	@Autowired
	private JavaMailSender javaMailSender;

    @Autowired
    private MailsSentRepository mailsSentRepository;
	
	public void sendEmail(Mail mail, User user, String mailType) {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		try {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
			mimeMessageHelper.setSubject(mail.getMailSubject());
			mimeMessageHelper.setFrom(new InternetAddress(mail.getMailFrom()));
			mimeMessageHelper.setTo(mail.getMailTo());
			mimeMessageHelper.setText(mail.getMailContent());
			javaMailSender.send(mimeMessageHelper.getMimeMessage());
            MailsSent mailSent = new MailsSent(user, mail.getMailSubject(), mail.getMailContent(), LocalDateTime.now(), mailType);
            mailsSentRepository.save(mailSent);
		} 
		catch (MessagingException e) {
			e.printStackTrace();
		} catch (Exception e){
			throw new RuntimeException("Failed to send email",e);
		}
	}

}