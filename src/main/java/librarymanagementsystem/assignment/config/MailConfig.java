// package librarymanagementsystem.assignment.config;

// // import java.util.Properties;

// // import org.springframework.context.annotation.Bean;
// // import org.springframework.context.annotation.Configuration;
// // import org.springframework.mail.javamail.JavaMailSender;
// // import org.springframework.mail.javamail.JavaMailSenderImpl;

// // @Configuration
// // public class MailConfig {

// //     @Bean
// //     public JavaMailSender javaMailSender() {
// //         JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
// //         mailSender.setHost("smtp.gmail.com");
// //         mailSender.setPort(587);

// //         mailSender.setUsername("kunwar1devansh@gmail.com");
// //         mailSender.setPassword("pass");

// //         Properties props = mailSender.getJavaMailProperties();
// //         props.put("mail.transport.protocol", "smtp");
// //         props.put("mail.smtp.auth", "true");
// //         props.put("mail.smtp.starttls.enable", "true");
// //         props.put("mail.debug", "true");

// //         return mailSender;
// //     }
// // }


// import java.util.Properties;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.core.env.Environment;
// import org.springframework.mail.javamail.JavaMailSender;
// import org.springframework.mail.javamail.JavaMailSenderImpl;

// @Configuration
// public class MailConfig 
// {
// 	@Autowired
// 	private Environment environment;
	
// 	@Bean
// 	public JavaMailSender getMailSender()
// 	{
// 		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
// 		javaMailSender.setHost(environment.getProperty("spring.mail.host"));
// 		javaMailSender.setPort(Integer.valueOf(environment.getProperty("spring.mail.port")));
// 		javaMailSender.setUsername(environment.getProperty("spring.mail.username"));
// 		javaMailSender.setPassword(environment.getProperty("spring.mail.password"));
		
// 		Properties javaMailProperties = new Properties();
// 		javaMailProperties.put("mail.smtp.starttls.enable", "true");
// 		javaMailProperties.put("mail.smtp.auth", "true");
// 		javaMailProperties.put("mail.transport.protocol", "smtp");
// 		javaMailProperties.put("mail.debug", "true");
// 		javaMailProperties.put("mail.smtp.ssl.trust", "*");
		
// 		javaMailSender.setJavaMailProperties(javaMailProperties);
// 		return javaMailSender;
// 	}
// }
