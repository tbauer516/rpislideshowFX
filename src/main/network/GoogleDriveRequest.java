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
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.*;

public class GoogleDriveRequest {

    private Drive service;
    private String picLoc;

    public GoogleDriveRequest(String configPath, String picLoc) {
        this.picLoc = picLoc;

        try {
            InputStream in = new FileInputStream(configPath + "/client_secret.json");
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            NetHttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
            GoogleClientSecrets secrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(in));

            java.io.File datastore = new java.io.File(configPath);

            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                    transport,
                    jsonFactory,
                    secrets,
                    Collections.singletonList(DriveScopes.DRIVE_METADATA_READONLY))
                    .setDataStoreFactory(new FileDataStoreFactory(datastore))
                    .setAccessType("offline")
                    .build();

            Credential cred = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");

            service = new Drive.Builder(transport, jsonFactory, cred)
                    .setApplicationName("RPISlideshow")
                    .build();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        } catch (GeneralSecurityException e) {
            System.out.println("TrustedTransport had an error");
        }
    }

    public void sync() {
        Set<String> localFiles = getFilesLocal();
        Set<String> driveFiles = getFilesInDrive();

        Set<String> toRemove = getFilesToDelete(driveFiles, localFiles);
        Set<String> toDownload = getFilesToDownload(driveFiles, localFiles);
    }

    public Set<String> getFilesToDownload(Set<String> drive, Set<String> local) {
        Set<String> driveCopy = new HashSet<>(drive);

        for (String name : driveCopy) {
            if (local.contains(name))
                driveCopy.remove(name);
        }

        return driveCopy;
    }

    public Set<String> getFilesToDelete(Set<String> drive, Set<String> local) {
        Set<String> localCopy = new HashSet<>(local);

        for (String name : localCopy) {
            if (!drive.contains(name))
                localCopy.remove(name);
        }

        return localCopy;
    }

    public Set<String> getFilesInDrive() {
        Set<String> fileSet = new HashSet<>();

        try {
            FileList files = service.files().list()
                    .setFields("files(id, name)")
                    .setQ("mimeType = 'image/jpeg' or mimeType = 'image/png'")
                    .execute();

            for (File file : files.getFiles()) {
                fileSet.add(file.getName());
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return fileSet;
    }

    public Set<String> getFilesLocal() {
        java.io.File folder = new java.io.File(picLoc);
        List<java.io.File> files = Arrays.asList(folder.listFiles());

        Set<String> fileSet = new HashSet<>();
        for (java.io.File file : files) {
            fileSet.add(file.getName());
        }

        return fileSet;
    }
}
