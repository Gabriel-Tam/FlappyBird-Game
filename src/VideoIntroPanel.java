import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javax.swing.JPanel;
import java.net.URL;

public class VideoIntroPanel extends JPanel {
    private static final String VIDEO_PATH = "/res/vid/lv_0_20240602113802.mp4";

    public VideoIntroPanel(Runnable onVideoEnd) {
        JFXPanel fxPanel = new JFXPanel();
        this.add(fxPanel);
        
        javafx.application.Platform.runLater(() -> {
            URL videoUrl = getClass().getResource(VIDEO_PATH);
            if (videoUrl == null) {
                System.err.println("No se pudo encontrar el video en la ruta: " + VIDEO_PATH);
                return;
            }
            Media media = new Media(videoUrl.toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            MediaView mediaView = new MediaView(mediaPlayer);

            mediaView.setFitWidth(512);  // Establece el ancho del video
            mediaView.setFitHeight(288); // Establece la altura del video
            mediaView.setPreserveRatio(true); // Mantén la relación de aspecto del video

            StackPane root = new StackPane();
            root.getChildren().add(mediaView);

            Scene scene = new Scene(root, 512, 288);
            fxPanel.setScene(scene);

            mediaPlayer.play();

            // Al terminar el video, se llama al callback para iniciar el juego
            mediaPlayer.setOnEndOfMedia(onVideoEnd);
        });
    }
}

