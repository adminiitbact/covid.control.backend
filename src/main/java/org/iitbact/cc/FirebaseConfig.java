package org.iitbact.cc;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.iitbact.cc.constants.SYSTEM_ENVIRONMENT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.Base64.Decoder;

@Configuration
public class FirebaseConfig {

    @Value("${firebase}")
    private String credentials;

    @Value("${firebase.dburl}")
    private String dbUrl;

    @Value("${env}")
    private String env;

    @PostConstruct
    public void initFirebase() {
        if (SYSTEM_ENVIRONMENT.DEV.toString().equalsIgnoreCase(env)) {
            GoogleCredentials credentials = readCredentialsForDev();
            FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(credentials).setDatabaseUrl(dbUrl)
                    .build();
            FirebaseApp.initializeApp(options);
        } else {
            GoogleCredentials credentials = readCredentialsFromProperties();
            FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(credentials).setDatabaseUrl(dbUrl)
                    .build();
            FirebaseApp.initializeApp(options);
        }
    }

    // Google credentials from firebase property from system
    private GoogleCredentials readCredentialsFromProperties() {
        try {
            // use ByteArrayInputStream to get the bytes of the String and
            // convert them to InputStream.
            Decoder decoder = Base64.getDecoder();
            InputStream inputStream = new ByteArrayInputStream(decoder.decode(credentials));
            return GoogleCredentials.fromStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Google credentials for Dev using GOOGLE_APPLICATION_CREDENTIALS
    private GoogleCredentials readCredentialsForDev() {
        try {
            return GoogleCredentials.getApplicationDefault();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Google credentials from firebase property from system
    @SuppressWarnings("unused")
    private GoogleCredentials readCredentialsFromFile() {
        try {
            File file = ResourceUtils.getFile("classpath:firebase.json");
            FileInputStream serviceAccount = new FileInputStream(file);
            return GoogleCredentials.fromStream(serviceAccount);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
