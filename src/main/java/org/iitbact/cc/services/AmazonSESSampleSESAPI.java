package org.iitbact.cc.services;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
// AWS SDK libraries. Download the AWS SDK for Java 
// from https://aws.amazon.com/sdk-for-java
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.RawMessage;
import com.amazonaws.services.simpleemail.model.SendRawEmailRequest;

public class AmazonSESSampleSESAPI {

	// Replace sender@example.com with your "From" address.
	// This address must be verified with Amazon SES.
	private static String SENDER = "Admin <admin@cov2.in>";

	// Specify a configuration set. If you do not want to use a configuration
	// set, comment the following variable, and the 
	// ConfigurationSetName=CONFIGURATION_SET argument below.
	///private static String CONFIGURATION_SET = "ConfigSet";

	// The subject line for the email.
	private static String SUBJECT = "Customer service contact info";

	// The full path to the file that will be attached to the email.
	// If you're using Windows, escape backslashes as shown in this variable.
	//private static String ATTACHMENT = "C:\\Users\\sender\\customers-to-contact.xlsx";

	// The email body for recipients with non-HTML email clients.
	private static String BODY_TEXT = "Hello,\r\n"
                                        + "Please see the attached file for a list "
                                        + "of customers to contact.";

	// The HTML body of the email.
	private static String BODY_HTML = "<html>"
                                        + "<head></head>"
                                        + "<body>"
                                        + "<h1>Hello!</h1>"
                                        + "<p>Please see the attached file for a "
                                        + "list of customers to contact.</p>"
                                        + "{link}"
                                        + "</body>"
                                        + "</html>";
	
	
	public void sendResetPasswordEmail(String email,String resetLink) throws MessagingException {
		Session session = Session.getDefaultInstance(new Properties());
        
        // Create a new MimeMessage object.
        MimeMessage message = new MimeMessage(session);
        
        // Add subject, from and to lines.
        message.setSubject(SUBJECT, "UTF-8");
        message.setFrom(new InternetAddress(SENDER));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));

        // Create a multipart/alternative child container.
        MimeMultipart msg_body = new MimeMultipart("alternative");
        
        // Create a wrapper for the HTML and text parts.        
        MimeBodyPart wrap = new MimeBodyPart();
        
        // Define the text part.
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setContent(BODY_TEXT, "text/plain; charset=UTF-8");
                
        // Define the HTML part.
        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(BODY_HTML.replace("{link}", resetLink),"text/html; charset=UTF-8");
                
        // Add the text and HTML parts to the child container.
        msg_body.addBodyPart(textPart);
        msg_body.addBodyPart(htmlPart);
        
        // Add the child container to the wrapper object.
        wrap.setContent(msg_body);
        
        // Create a multipart/mixed parent container.
        MimeMultipart msg = new MimeMultipart("mixed");
        
        // Add the parent container to the message.
        message.setContent(msg);
        
        // Add the multipart/alternative part to the message.
        msg.addBodyPart(wrap);
        
        // Define the attachment
		/*
		 * MimeBodyPart att = new MimeBodyPart(); DataSource fds = new
		 * FileDataSource(ATTACHMENT); att.setDataHandler(new DataHandler(fds));
		 * att.setFileName(fds.getName());
		 */
        
        // Add the attachment to the message.
        //msg.addBodyPart(att);

        // Try to send the email.
        try {
            System.out.println("Attempting to send an email through Amazon SES "
                              +"using the AWS SDK for Java...");
            AWSCredentialsProvider awsCreds = new EnvironmentVariableCredentialsProvider();
            // Instantiate an Amazon SES client, which will make the service 
            // call with the supplied AWS credentials.
            AmazonSimpleEmailService client = 
                    AmazonSimpleEmailServiceClientBuilder.standard()
                    .withCredentials(awsCreds)
                    // Replace US_WEST_2 with the AWS Region ysou're using for
                    // Amazon SES.
                    .withRegion(Regions.AP_SOUTH_1).build();
            
            // Print the raw email content on the console
            PrintStream out = System.out;
            message.writeTo(out);

            // Send the email.
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            message.writeTo(outputStream);
            RawMessage rawMessage = 
            		new RawMessage(ByteBuffer.wrap(outputStream.toByteArray()));

            SendRawEmailRequest rawEmailRequest = 
            		new SendRawEmailRequest(rawMessage);
            
            client.sendRawEmail(rawEmailRequest);
            System.out.println("Email sent!");
        // Display an error if something goes wrong.
        } catch (Exception ex) {
          System.out.println("Email Failed");
            System.err.println("Error message: " + ex.getMessage());
            ex.printStackTrace();
        }
	}
}