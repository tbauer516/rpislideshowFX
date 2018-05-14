package info.tylerbauer.rpislideshow.network;

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

import java.io.*;
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
                    Collections.singletonList(DriveScopes.DRIVE_READONLY))
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

    public List<java.io.File> getNewPicList() {
        List<java.io.File> files = getFilesLocal();
        for (int i = files.size() - 1; i >= 1; i--) {
            int j = (int) (Math.random() * (i + 1));
            java.io.File temp = files.get(i);
            files.set(i, files.get(j));
            files.set(j, temp);
        }

        return files;
    }

    public void sync() {
        List<java.io.File> localFiles = getFilesLocal();
        List<File> driveFiles = getFilesInDrive();

        List<java.io.File> toRemove = getFilesToDelete(driveFiles, localFiles);
        List<File> toDownload = getFilesToDownload(driveFiles, localFiles);

        for (java.io.File file : toRemove) {
            file.delete();
        }

        for (File file : toDownload) {
            try {
                OutputStream out = new FileOutputStream(picLoc + "/" + file.getName().toLowerCase());
                service.files().get(file.getId())
                        .executeMediaAndDownloadTo(out);
            } catch (FileNotFoundException e) {
                System.out.println("Could not create file for " + file.getName());
            } catch (IOException e) {
                System.out.println("Download failed for " + file.getName());
                e.printStackTrace();
            }
        }
    }

    private List<File> getFilesToDownload(List<File> drive, List<java.io.File> local) {
        List<File> driveCopy = new ArrayList<>(drive);
        int size = driveCopy.size();

        for (int i = size - 1; i >= 0; i--) {
            File dfile = driveCopy.get(i);
            for (java.io.File lfile : local) {
                if (lfile.getName().equals(dfile.getName())) {
                    driveCopy.remove(i);
                    continue;
                }
            }
        }

        return driveCopy;
    }

    private List<java.io.File> getFilesToDelete(List<File> drive, List<java.io.File> local) {
        List<java.io.File> localCopy = new ArrayList<>(local);
        int size = localCopy.size();

        for (int i = size - 1; i >= 0; i--) {
            java.io.File lfile = localCopy.get(i);
            for (File dfile : drive) {
                if (dfile.getName().equals(lfile.getName())) {
                    localCopy.remove(i);
                    continue;
                }
            }
        }

        return localCopy;
    }

    private List<java.io.File> getFilesLocal() {
        java.io.File folder = new java.io.File(picLoc);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        List<java.io.File> files = Arrays.asList(folder.listFiles());

        List<java.io.File> fileSet = new ArrayList<>(files);

        return fileSet;
    }

    private List<File> getFilesInDrive() {
        List<File> files = new ArrayList<>();

        try {
            FileList dfiles = service.files().list()
                    .setFields("files(id, name)")
                    .setQ("mimeType = 'image/jpeg' or mimeType = 'image/png'")
                    .execute();

            files = new ArrayList<>(dfiles.getFiles());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return files;
    }
}
