package com.edupitr.api;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.edupitr.models.ContactUsModel;

@RestController
public class ContactUsController {
	
	final String username = "svcedupitr@gmail.com";
	final String password = "svc-Edup!tr";
	
	static final String FROM = "svcedupitr@gmail.com";
    static final String FROMNAME = "svc-edupitr";
	
    // Replace recipient@example.com with a "To" address. If your account 
    // is still in the sandbox, this address must be verified.
    static final String TO = "ambarish.v.rao@gmail.com";
    
    static final String CC = "suyash.bhangale48@gmail.com";
    
    // Replace smtp_username with your Amazon SES SMTP user name.
    static final String SMTP_USERNAME = "AKIAIOYH6SW5SJI5GHDA";
    
    // Replace smtp_password with your Amazon SES SMTP password.
    static final String SMTP_PASSWORD = "AiDT6STeK0hueZ5EiSssq6cmKa7y1lQ9jFPksZg2WeKE";
    
    // The name of the Configuration Set to use for this message.
    // If you comment out or remove this variable, you will also need to
    // comment out or remove the header below.
    //static final String CONFIGSET = "ConfigSet";
    
    // Amazon SES SMTP host name. This example uses the US West (Oregon) region.
    // See http://docs.aws.amazon.com/ses/latest/DeveloperGuide/regions.html#region-endpoints
    // for more information.
    static final String HOST = "email-smtp.eu-west-1.amazonaws.com";
    
static final int PORT = 587;
    
    static final String SUBJECT = "[Testing] Edupitr-> New potential customer";
    
    static final String BODY = String.join(
    	    System.getProperty("line.separator"),
    	    "<h3>Edupitr has a new potential customer</h3>",
    	    "<h1>{name}</h1>",
    	    "<p>Subject: {subject}</p>",
    	    "<p>Service availed: {service}</p>",
    	    "<p>Email id: {emailid}</p>",
    	    "<p>Documents: {documents}</p>",
    	    "<p>Phone number: {phonenumber}</p>",
    	    "<p>Message: {message}</p>"
    	);
	
	@PostMapping("/contactus")
    public String contactUs(@RequestBody ContactUsModel model) {
        
		Properties props = System.getProperties();
    	props.put("mail.transport.protocol", "smtp");
    	props.put("mail.smtp.port", PORT); 
    	props.put("mail.smtp.starttls.enable", "true");
    	props.put("mail.smtp.auth", "true");

        // Create a Session object to represent a mail session with the specified properties. 
    	Session session = Session.getDefaultInstance(props);

        // Create a message with the specified information. 
        MimeMessage msg = new MimeMessage(session);
        try {
			msg.setFrom(new InternetAddress(FROM,FROMNAME));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(TO));
			msg.setRecipient(Message.RecipientType.CC, new InternetAddress(CC));
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			msg.setSubject(SUBJECT);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
        	
        	String mailBody=BODY.replace("{name}", model.name);
        	mailBody=mailBody.replace("{emailid}", model.emailId);
        	mailBody=mailBody.replace("{service}", model.service);
        	mailBody=mailBody.replace("{documents}", model.documents);
        	mailBody=mailBody.replace("{message}", model.message);
        	mailBody=mailBody.replace("{subject}", model.subject);
        	mailBody=mailBody.replace("{phonenumber}", model.phoneNumber);
			
        	msg.setContent(mailBody,"text/html");
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        // Add a configuration set header. Comment or delete the 
        // next line if you are not using a configuration set
        try {
			//msg.setHeader("X-SES-CONFIGURATION-SET", CONFIGSET);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
            
        // Create a transport.
        Transport transport=null;
		try {
			transport = session.getTransport();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
                    
        // Send the message.
        try
        {
            System.out.println("Sending...");
            
            // Connect to Amazon SES using the SMTP username and password you specified above.
            transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
            System.out.println("connected");
            
            // Send the email.
            transport.sendMessage(msg, msg.getAllRecipients());
            System.out.println("Email sent!");
        }
        catch (Exception ex) {
            System.out.println("The email was not sent.");
            System.out.println("Error message: " + ex.getMessage());
        }
        finally
        {
            // Close and terminate the connection.
            try {
				transport.close();
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		
        return "greeting";
    }
}
