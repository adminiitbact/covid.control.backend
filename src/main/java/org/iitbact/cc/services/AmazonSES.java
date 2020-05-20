package org.iitbact.cc.services;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AmazonSES {

    // Replace sender@example.com with your "From" address.
    // This address must be verified.
    static final String FROM = "admin@cov2.in";
    static final String FROMNAME = "Admin";
	
    // Replace recipient@example.com with a "To" address. If your account 
    // is still in the sandbox, this address must be verified.
    //static final String TO = "recipient@example.com";
    
    // Replace smtp_username with your Amazon SES SMTP user name.
    @Value("${SMTP_USERNAME}")
    private String SMTP_USERNAME;
    
    // Replace smtp_password with your Amazon SES SMTP password.
    @Value("${SMTP_PASSWORD}")
    private String SMTP_PASSWORD;
    
    // The name of the Configuration Set to use for this message.
    // If you comment out or remove this variable, you will also need to
    // comment out or remove the header below.
    //static final String CONFIGSET = "ConfigSet";
    
    // Amazon SES SMTP host name. This example uses the US West (Oregon) region.
    // See https://docs.aws.amazon.com/ses/latest/DeveloperGuide/regions.html#region-endpoints
    // for more information.
    static final String HOST = "email-smtp.ap-south-1.amazonaws.com";
    
    // The port you will connect to on the Amazon SES SMTP endpoint. 
    static final int PORT = 587;
    
    static final String SUBJECT = "Reset Password for cov2.in";
    
    static final String BODY = String.join(
    	    System.getProperty("line.separator"),
    	    "<h1>Link to reset password</h1>",
    	    "<p>Please follow the link to reset your password for cov2.in. After resetting,please login to cov2.in using new credentials. ", 
    	    "<br><b><a href='LINK'>Reset Password</a></b>"
    	);

    public void sendResetPasswordEmail(String email,String link) throws MessagingException, UnsupportedEncodingException {

        // Create a Properties object to contain connection configuration information.
    	Properties props = System.getProperties();
    	props.put("mail.transport.protocol", "smtp");
    	props.put("mail.smtp.port", PORT); 
    	props.put("mail.smtp.starttls.enable", "true");
    	props.put("mail.smtp.auth", "true");

        // Create a Session object to represent a mail session with the specified properties. 
    	Session session = Session.getDefaultInstance(props);

        // Create a message with the specified information. 
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(FROM,FROMNAME));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
        msg.setSubject(SUBJECT);
        msg.setContent(BODY.replace("LINK", link),"text/html");
        
        // Add a configuration set header. Comment or delete the 
        // next line if you are not using a configuration set
       // msg.setHeader("X-SES-CONFIGURATION-SET", CONFIGSET);
            
        // Create a transport.
        Transport transport = session.getTransport();
                    
        // Send the message.
        try
        {
            System.out.println("Sending...");
            
            // Connect to Amazon SES using the SMTP username and password you specified above.
            transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
        	
            // Send the email.
            transport.sendMessage(msg, msg.getAllRecipients());
            System.out.println("Email sent!");
        }
        catch (MessagingException ex) {
        	ex.printStackTrace();
            System.out.println("The email was not sent.");
            System.out.println("Error message: " + ex.getMessage());
        }
        finally
        {
            // Close and terminate the connection.
            transport.close();
        }
    }
}