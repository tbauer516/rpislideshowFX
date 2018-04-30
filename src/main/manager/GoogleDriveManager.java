package manager;

import network.GoogleDriveRequest;

import java.io.File;
import java.util.List;

public class GoogleDriveManager {

    private GoogleDriveRequest service;
    private List<File> pictures;
    private int index;

    public GoogleDriveManager(GoogleDriveRequest gdrive) {
        service = gdrive;
        pictures = service.getNewPicList();
        index = 0;
    }

    public void sync() {
        service.sync();
    }

    public File getPic() {
        index++;
        if (index >= pictures.size()) {
            index = 0;
            pictures = service.getNewPicList();
        }

        if (pictures.size() == 0)
            return null;

        return pictures.get(index);
    }
}
