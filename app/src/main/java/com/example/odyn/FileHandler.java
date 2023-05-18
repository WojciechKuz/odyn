package com.example.odyn;

import com.example.odyn.settings.SettingOptions;
import com.example.odyn.settings.SettingsProvider;
import com.example.odyn.settings.SettingNames;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.example.odyn.cam.RecType;

import org.json.JSONException;
import org.json.JSONObject;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;



public class FileHandler {
    // metoda, podajesz plik, typ i zapisuje pod odpowiednią ścieżką i nazwą
    private String dir;
    private String pictSubdir = "pictures";
    private String vidSubdir = "videos";
    private String emergSubdir = "emergency_recordings";
    private String dataSubdir = "data";
    private Context context;

    private SettingsProvider settingsProvider;


    public FileHandler(Context mainActivity) {
        // TODO ustawianie czy w pamięci telefonu, czy na karcie SD. (pobierane z ustawień)
        settingsProvider = new SettingsProvider();
        context = mainActivity;
        //dir = context.getFilesDir().getAbsolutePath();
        dir = context.getExternalMediaDirs()[0].getAbsolutePath();
        dir = removeSlash(dir) + '/' + "Odyn";

        createDirIfNotExists(getDirPath(pictSubdir));
        createDirIfNotExists(getDirPath(vidSubdir));
        createDirIfNotExists(getDirPath(emergSubdir));
        createDirIfNotExists(getDirPath(dataSubdir));


        sprawdzRozmiar();
    }


    private String getDirPath(String subDir) {
        return context.getExternalMediaDirs()[0].getAbsolutePath() + File.separator + "Odyn" + File.separator + subDir;
    }
    private void createDirIfNotExists(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            boolean success = dir.mkdirs();
            if (!success) {
                Log.e("FileHandler", "Nie udalo sie stworzyc katalogu: " + path);
            }
        }
    }

    // TODO przetestuj

    // TODO rozbuduj o zapis do katalogu w zależności od rodzaju pliku

    // TESTOWE:
    public String testPathGetExternal() {
        return context.getExternalMediaDirs()[0].getAbsolutePath(); // sdcard/Android/media/com.example.odyn/<tutaj pliki>
    }
    public String testMyDirPath() {
        return dir;
    }
    public String testPathExperiment() {
        return "XD";
    }




    private long getLimitFromSettings() {
        try {
            int selectedPosition = settingsProvider.getSettingInt(SettingNames.spinners[4]);
            String selectedValue = SettingOptions.SizeVideo[selectedPosition];
            String sizeString = selectedValue.replaceAll("[^0-9]", "");
            long sizeInMB = Long.parseLong(sizeString);
            long sizeInBytes = sizeInMB * 1024 * 1024;
            Log.d("FileHandler", "Aktualna wartość limitu: " + sizeInMB);
            return sizeInBytes;
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
    }



    private long getLimitFromEmergency() {
        try {
            int selectedPosition = settingsProvider.getSettingInt(SettingNames.spinners[5]);
            String selectedValue = SettingOptions.SizeEmergency[selectedPosition];
            String sizeString = selectedValue.replaceAll("[^0-9]", "");
            long sizeInMB = Long.parseLong(sizeString);
            long sizeInBytes = sizeInMB * 1024 * 1024;
            Log.d("FileHandler", "Aktualna wartość limitu: " + sizeInMB);
            return sizeInBytes;
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void sprawdzRozmiar() {
        long rozmiarWideo = getVideoDirSize(); // Pobierz sumaryczny rozmiar wideo
        long rozmiarAwaryjnych = getEmergencyDirSize(); // Pobierz sumaryczny rozmiar wideo awaryjnych
        long limit = getLimitFromSettings(); // limit rozmiaru z ustawień
        long limit2 = getLimitFromEmergency(); // limit rozmiaru z ustawień

        if (rozmiarWideo > limit || rozmiarAwaryjnych > limit2) {
            int numVideosToDelete = 2; // Przykładowa liczba najstarszych nagran do usunięcia
            deleteOldestVideos(numVideosToDelete); // Usuń najstarsze nagrania z vidSubdir
            sprawdzRozmiar(); // Rekurencyjnie sprawdź rozmiar ponownie
        }
    }


    public void deleteOldestVideos(int numVideosToDelete) {
        File videoDir = new File(getDirPath(vidSubdir));
        File[] videoFiles = videoDir.listFiles();

        if (videoFiles != null && videoFiles.length > numVideosToDelete) {
            // Sortuj pliki w kolejności od najstarszego do najnowszego
            Arrays.sort(videoFiles, (file1, file2) -> {
                Long lastModified1 = file1.lastModified();
                Long lastModified2 = file2.lastModified();
                return lastModified1.compareTo(lastModified2);
            });

            // Usuń najstarsze pliki
            for (int i = 0; i < numVideosToDelete; i++) {
                if (videoFiles[i].delete()) {
                    Log.d("FileHandler", "Usunięto plik: " + videoFiles[i].getName());
                } else {
                    Log.e("FileHandler", "Nie udało się usunąć pliku: " + videoFiles[i].getName());
                }
            }
        }
    }




    private long getDirectorySize(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
            return 0;
        }

        long size = 0;

        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    size += file.length();
                } else if (file.isDirectory()) {
                    size += getDirectorySize(file.getAbsolutePath());
                }
            }
        }

        return size;
    }



    public long getPictureDirSize() {
        String pictureDirPath = getDirPath(pictSubdir);
        long dirSize = getDirectorySize(pictureDirPath);
        return dirSize;
    }

    public long getVideoDirSize() {
        String videoDirPath = getDirPath(vidSubdir);
        long dirSize = getDirectorySize(videoDirPath);
        return dirSize;
    }

    public long getEmergencyDirSize() {
        String emergencyDirPath = getDirPath(emergSubdir);
        long dirSize = getDirectorySize(emergencyDirPath);
        return dirSize;
    }

    public long getTotalDirSize() {
        return getPictureDirSize() + getVideoDirSize() + getEmergencyDirSize();
    }



    // TWORZENIE PLIKÓW
    public File createFile(String namePrefix, String format) {
        String fileName = youNameIt(namePrefix, format);
        File file = new File(context.getExternalMediaDirs()[0].getAbsolutePath(), fileName);
        return file;
    }

    @Deprecated
    public File createFile(RecType type) { // bez sensu, nie korzystać
        switch (type) {
            case picture:
                return createPicture();
            case video:
                return createVideo("mp4");
            case emergency:
                return createEmergencyVideo("mp4"); // tymczasowo
            case data:
                return createDataFile("txt"); // nie wiem jaki format
        }
        return null;
    }

    public File createPicture() {
        String fileName = youNameIt("ODYN-img", "jpg");
        File file = new File(getDirPath(pictSubdir), fileName);
        // getExternalMediaDirs()[0] = wylistuj mi zewnętrzne nośniki danych i wybierz pierwszy
        // TODO wybierana ścieżka zapisu
        return file;
    }
    public File createVideo(String format) {
        String fileName = youNameIt("ODYN-vid", format);
        File file = new File(getDirPath(vidSubdir), fileName);
        Log.d("FileHandler", "Ścieżka pliku: " + file.getAbsolutePath());
        // Sprawdź rozmiar katalogu wideo
        long videoDirSize = getVideoDirSize();
        Log.d("FileHandler", "Rozmiar katalogu wideo: " + videoDirSize);

        return file;
    }


    public File createEmergencyVideo(String format) {
        String fileName = youNameIt("ODYN-emr", format);
        File file = new File(getDirPath(emergSubdir), fileName);
        return file;
    }
    public File createDataFile(String format) {
        String fileName = youNameIt("ODYN-dat", format);
        File file = new File(getDirPath(dataSubdir), fileName);
        return file;
    }


    private String youNameIt(String namePrefix, String fileFormat) {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault()).format(new Date());
        return namePrefix + '-' + timeStamp + '.' + fileFormat;
    }

    private void createDir(String path) {
        if(!ifDirExists(path)) {
            new File(path).mkdir();
        }
    }
    private boolean ifDirExists(String path) {
        return new File(path).exists();
    }

    // usuwa '/' jeśli jest na ostatniej pozycji
    private String removeSlash(String path) {
        int lastPos = path.length()-1;
        if(path.charAt(lastPos) == '/') {
            path = path.substring(0,lastPos); // path bez ostatniego znaku '/' jeśli był
        }
        return path;
    }
}



























    /*
    public void createFile(File absFile, Type type) {
        // argument absFile - plik bez podanej ścieżki (abstract pathname)
        File file = createFile(type);
        file = absFile.
    }
    */
    /*
    private File createFileTemplate(File dirPath) {
        //
        return null;
    }
    */

    /*
    private boolean fileExists(URI uri) {
        //
    }
     */
    /* // NIE POTRZEBA CZYTAĆ PLIKÓW
    public File readFile(URI uri) {
        //
        File plik = new File(uri);
        if()
    }
    public File readFile(String path, String filename) throws URISyntaxException {
        path = removeSlash(path);
        path += '/' + filename;
        return readFile(new URI(path));
    }
     */

