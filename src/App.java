import javax.swing.*;

public class App {
    public static final int WIDTH = 360;
    public static final int HEIGHT = 640;
    public static void main(String[] args) throws Exception {


        JFrame frame = new JFrame("Flappy Bird");
        // frame.setVisible(true);
		frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Logic flappyBird = new Logic();
        frame.add(flappyBird);
        frame.pack();
        flappyBird.requestFocus();
        frame.setVisible(true);
    }
}