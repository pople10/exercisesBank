package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StreamPrinter implements Runnable {
    private final InputStream inputStream;

    private boolean print;

    public StreamPrinter(InputStream inputStream, boolean print) {
        this.inputStream = inputStream;
        this.print = print;
    }

    private BufferedReader getBufferedReader(InputStream is) {
        return new BufferedReader(new InputStreamReader(is));
    }

    @Override
    public void run() {
        BufferedReader br = getBufferedReader(inputStream);
        String ligne = "";
        try {
            while ((ligne = br.readLine()) != null) {
                if (print) {
                    System.out.println(ligne);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}