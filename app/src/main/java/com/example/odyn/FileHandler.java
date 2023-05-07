package com.example.odyn;

import android.content.Context;
import android.util.Log;

import com.example.odyn.cam.RecType;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Jest to klasa odpowiedzialna za obsługę plików.
 */
public class FileHandler {
    // metoda, podajesz plik, typ i zapisuje pod odpowiednią ścieżką i nazwą
    private String dir;
    private String pictSubdir = "pictures";
    private String vidSubdir = "videos";
    private String emergSubdir = "emergency_recordings";
    private String dataSubdir = "data";
    private Context context;

    public FileHandler(Context mainActivity) {
        // TODO ustawianie czy w pamięci telefonu, czy na karcie SD. (pobierane z ustawień)
        context = mainActivity;
        //dir = context.getFilesDir().getAbsolutePath();
        dir = context.getExternalMediaDirs()[0].getAbsolutePath();
        dir = removeSlash(dir) + '/' + "Odyn";

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


    // TWORZENIE PLIKÓW
    /**
     * Jest to metoda służąca do tworzenia plików.
     */
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

    /**
     * Jest to metoda służąca do tworzenia plików obrazów.
     */
    public File createPicture() {
        String fileName = youNameIt("ODYN-img", "jpg");
        File file = new File(context.getExternalMediaDirs()[0].getAbsolutePath(), fileName);
        // getExternalMediaDirs()[0] = wylistuj mi zewnętrzne nośniki danych i wybierz pierwszy
        // TODO wybierana ścieżka zapisu
        return file;
    }

    /**
     * Jest to metoda służąca do tworzenia plików video.
     */
    public File createVideo(String format) {
        String fileName = youNameIt("ODYN-vid", format);
        File file = new File(context.getExternalMediaDirs()[0].getAbsolutePath(), fileName);
        return file;
    }

    /**
     * Jest to metoda służąca do tworzenia plików video nagrywanych w tle.
     */
    public File createEmergencyVideo(String format) {
        String fileName = youNameIt("ODYN-emr", format);
        File file = new File(context.getExternalMediaDirs()[0].getAbsolutePath(), fileName);
        return file;
    }

    /**
     * Jest to metoda służąca do tworzenia plików związanych z danymi.
     */
    public File createDataFile(String format) {
        String fileName = youNameIt("ODYN-dat", format);
        File file = new File(context.getExternalMediaDirs()[0].getAbsolutePath(), fileName);
        return file;
    }

    /**
     * Jest to metoda służąca do nazywania plików wraz z podanym przez użytkownika formatem pliku.
     */
    private String youNameIt(String namePrefix, String fileFormat) {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault()).format(new Date());
        return namePrefix + '-' + timeStamp + '.' + fileFormat;
    }

    /**
     * Jest to metoda służąca do tworzenia katalogu.
     */
    private void createDir(String path) {
        if(!ifDirExists(path)) {
            new File(path).mkdir();
        }
    }

    /**
     * Jest to metoda służąca do sprawdzenia czy dany katalog istnieje.
     */
    private boolean ifDirExists(String path) {
        return new File(path).exists();
    }

    // usuwa '/' jeśli jest na ostatniej pozycji
    /**
     * Jest to metoda służąca do usuwania "/" ze ścieżki jeżeli znajduje się na ostatniej pozycji.
     */
    private String removeSlash(String path) {
        int lastPos = path.length()-1;
        if(path.charAt(lastPos) == '/') {
            path = path.substring(0,lastPos); // path bez ostatniego znaku '/' jeśli był
        }
        return path;
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
}
