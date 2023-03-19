package com.example.odyn;

import android.app.Activity;
import android.content.Context;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

enum Type {picture, video, emergency, data};

public class FileHandler {
    // metoda, podajesz plik, typ i zapisuje pod odpowiednią ścieżką
    private String dir;
    private String pictSubdir = "pictures";
    private String vidSubdir = "videos";
    private String emergSubdir = "emergency_recordings";
    private String dataSubdir = "data";
    FileHandler(Context mainActivity) {
        // TODO ustawianie czy w pamięci telefonu, czy na karcie SD. (pobierane z ustawień)
        dir = mainActivity.getFilesDir().getAbsolutePath();
        dir = removeSlash(dir) + '/' + "Odyn";
    }

    public void saveFile(File file, Type type) {
        // TODO zapis plików
    }

    /*
    private boolean fileExists(URI uri) {
        //
    }
     */
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
