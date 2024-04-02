
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Clase que implementa la reproducción de archivos de audio en formato WAV en Java.
 * Utiliza hilos para reproducir el audio en segundo plano.
 */
public class AudioPlayWave extends Thread {
    private String fileName; // Nombre del archivo de audio a reproducir
    private Position curPosition; // Posición de reproducción del audio
    private final int EXTERNAL_BUFFER_SIZE = 524288; // Tamaño del buffer externo (128k)

    enum Position { // Enumeración que define las posiciones de reproducción del audio
        LEFT, RIGHT, NORMAL
    };

    // Constructor
    public AudioPlayWave(String wavFile) {
        this.fileName = wavFile;
        curPosition = Position.NORMAL;
    }

    public void run() {
        AudioInputStream audioInputStream = null; // Flujo de entrada de audio
        try {
            // Obtiene el flujo de entrada de audio a partir del archivo
            audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource(fileName));
        } catch (UnsupportedAudioFileException | IOException e1) {
            e1.printStackTrace();
            return;
        }

        AudioFormat format = audioInputStream.getFormat(); // Formato de audio
        SourceDataLine line = null; // Línea de datos de origen
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

        try {
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(format);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            return;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if (line.isControlSupported(FloatControl.Type.PAN)) {
            FloatControl pan = (FloatControl) line.getControl(FloatControl.Type.PAN);
            if (curPosition == Position.RIGHT)
                pan.setValue(1.0f);
            else if (curPosition == Position.LEFT)
                pan.setValue(-1.0f);
        }

        line.start();
        int bytesRead = 0;
        byte[] data = new byte[EXTERNAL_BUFFER_SIZE];

        try {
            while (bytesRead != -1) {
                bytesRead = audioInputStream.read(data, 0, data.length);
                if (bytesRead >= 0)
                    line.write(data, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        } finally {
            line.drain();
            line.close();
        }
    }
}
