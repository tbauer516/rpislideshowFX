package network;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.FileList;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public class GoogleDriveRequest {

    public GoogleDriveRequest(String configPath) {
        try {
            InputStream in = new FileInputStream(configPath + "/client_secret.json");
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            NetHttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
            GoogleClientSecrets secrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(in));

            File datastore = new File(configPath);

            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                    transport,
                    jsonFactory,
                    secrets,
                    Collections.singletonList(DriveScopes.DRIVE_METADATA_READONLY))
                    .setDataStoreFactory(new FileDataStoreFactory(datastore))
                    .setAccessType("offline")
                    .build();

            Credential cred = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");

            Drive service = new Drive.Builder(transport, jsonFactory, cred)
                    .setApplicationName("RPISlideshow")
                    .build();

            FileList result = service.files().list()
                    .setFields("nextPageToken, files(id, name)")
                    .execute();
            List<com.google.api.services.drive.model.File> files = result.getItems();
            if (files == null || files.isEmpty()) {
                System.out.println("No files found.");
            } else {
                System.out.println("Files:");
                for (com.google.api.services.drive.model.File file : files) {
                    System.out.printf("%s (%s)\n", file.getTitle(), file.getId());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        } catch (GeneralSecurityException e) {
            System.out.println("TrustedTransport had an error");
        }
    }
}
