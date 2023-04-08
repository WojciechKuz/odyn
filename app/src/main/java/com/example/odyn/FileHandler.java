package com.example.odyn;

import android.content.Context;

import com.example.odyn.types.RecType;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;



public class FileHandler {
    // metoda, podajesz plik, typ i zapisuje pod odpowiednią ścieżką
    private String dir;
    private String pictSubdir = "pictures";
    private String vidSubdir = "videos";
    private String emergSubdir = "emergency_recordings";
    private String dataSubdir = "data";
    private Context context;

    FileHandler(Context mainActivity) {
        // TODO ustawianie czy w pamięci telefonu, czy na karcie SD. (pobierane z ustawień)
        context = mainActivity;
        dir = context.getFilesDir().getAbsolutePath();
        dir = removeSlash(dir) + '/' + "Odyn";

    }

    // TODO przetestuj

    // TODO rozbuduj o zapis do katalogu w zależności od rodzaju pliku

    // TESTOWE:
    public String testPathGetExternal() {
        return context.getExternalMediaDirs()[0].getAbsolutePath();
    }
    public String testMyDirPath() {
        return dir;
    }
    public String testPathExperiment() {
        return "XD";
    }


    // TWORZENIE PLIKÓW
    public File createFile(String namePrefix, String format) {
        String fileName = youNameIt(namePrefix, format);
        File file = new File(context.getExternalMediaDirs()[0].getAbsolutePath(), fileName);
        return file;
    }
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
        File file = new File(context.getExternalMediaDirs()[0].getAbsolutePath(), fileName);
        // getExternalMediaDirs()[0] = wylistuj mi zewnętrzne nośniki danych i wybierz pierwszy
        // TODO wybierana ścieżka zapisu
        return file;
    }
    public File createVideo(String format) {
        String fileName = youNameIt("ODYN-vid", format);
        File file = new File(context.getExternalMediaDirs()[0].getAbsolutePath(), fileName);
        return file;
    }
    public File createEmergencyVideo(String format) {
        String fileName = youNameIt("ODYN-emr", format);
        File file = new File(context.getExternalMediaDirs()[0].getAbsolutePath(), fileName);
        return file;
    }
    public File createDataFile(String format) {
        String fileName = youNameIt("ODYN-dat", format);
        File file = new File(context.getExternalMediaDirs()[0].getAbsolutePath(), fileName);
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
