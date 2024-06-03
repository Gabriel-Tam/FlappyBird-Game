import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import java.io.IOException;
import javax.swing.JFrame;
import java.nio.file.Paths;

public class VideoIntroGame extends Application {

    @Override
    public void start(Stage primaryStage) {
        String video1Path = Paths.get("src/res/vid/GaboCorpsCompany.mp4").toUri().toString();
        String video2Path = Paths.get("src\\res\\vid\\IntroFlappyBird_v2.mp4").toUri().toString();

        MediaPlayer mediaPlayer1 = new MediaPlayer(new Media(video1Path));
        MediaPlayer mediaPlayer2 = new MediaPlayer(new Media(video2Path));
        mediaPlayer2.setCycleCount(MediaPlayer.INDEFINITE);

        MediaView mediaView = new MediaView(mediaPlayer1);
        mediaView.setFitWidth(300);
        mediaView.setFitHeight(520);
        mediaView.setPreserveRatio(true);

        BorderPane root = new BorderPane(mediaView);
        Scene scene = new Scene(root, 300, 520);

        mediaPlayer1.setOnEndOfMedia(() -> {
            mediaView.setMediaPlayer(mediaPlayer2);
            mediaPlayer2.play();
        });

        mediaView.setOnMouseClicked(event -> {
            mediaPlayer2.stop();
            startGame(primaryStage);
        });

        primaryStage.setTitle("Video Intro");
        primaryStage.setScene(scene);
        primaryStage.show();

        mediaPlayer1.play();
    }

    private void startGame(Stage primaryStage) {
        // Aquí inicializamos y mostramos el juego
        JFrame frame = new JFrame("FlappyBird");
        try {
            Main world = new Main();
            frame.add(world);
            frame.setSize(288, 512);
            frame.setResizable(false);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            world.action();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
