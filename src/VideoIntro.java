import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javax.swing.*;

public class VideoIntro extends JPanel {

    private JFXPanel fxPanel;

    public VideoIntro(String videoPath) {
        fxPanel = new JFXPanel();
   //     setLayout(new BorderLayout());
     //   add(fxPanel, BorderLayout.CENTER);
        
        Platform.runLater(() -> {
            Media media = new Media(videoPath);
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            MediaView mediaView = new MediaView(mediaPlayer);

            Scene scene = new Scene(new javafx.scene.Group(mediaView));
            fxPanel.setScene(scene);

            mediaPlayer.setAutoPlay(true);
            mediaPlayer.setOnEndOfMedia(() -> {
                SwingUtilities.invokeLater(() -> {
                    JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                    topFrame.dispose();  // Cerrar la ventana de video
                });
            });
        });
    }
}
