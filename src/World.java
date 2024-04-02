import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Clase que representa el mundo del juego Flappy Bird, inicializa los elementos del juego y gestiona su funcionamiento.
 */
public class World extends JPanel {
    private BufferedImage background;
    private BufferedImage startImg;
    private Ground ground;
    private Column column1;
    private Column column2;
    private boolean start;
    private Bird bird;
    private boolean gameover;
    private BufferedImage end;
    private BufferedImage[] numbers; // Array para almacenar las imágenes de los números
    private int score;

    /**
     * Constructor que inicializa el mundo del juego.
     */
    public World() throws IOException {
        background = ImageIO.read(this.getClass().getResource("/res/img/bg.png"));
        startImg = ImageIO.read(getClass().getResource("/res/img/start.png"));
        init();

        // Cargar las imágenes de los números
        numbers = new BufferedImage[10];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = ImageIO.read(getClass().getResource("/res/img/numbers/" + i + ".png"));
        }
    }

    /**
     * Método privado para inicializar los elementos del juego.
     */
    private void init() throws IOException {
        ground = new Ground(400);
        column1 = new Column(320 + 100);
        column2 = new Column(320 + 100 + 180);
        bird = new Bird(140, 225);
        start = false;
        gameover = false;
        score = 0;
    }

    /**
     * Método que gestiona las acciones del juego.
     */
    public void action() throws InterruptedException {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (gameover) {
                    try {
                        init();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    return;
                }
                start = true;
                bird.flappy();
            }
        });

        while (true) {
            if (start) {
                column1.step();
                column2.step();
                bird.step();
                if (bird.hit(column1, column2, ground)) {
                    AudioPlayWave audioPlayWave = new AudioPlayWave("/res/sound/death.wav");
                    audioPlayWave.start();
                    start = false;
                    gameover = true;
                }
                if (bird.pass(column1, column2)) {
                    score++;
                    AudioPlayWave pointSound = new AudioPlayWave("/res/sound/hit.wav");
                    pointSound.start();
                }
            }
            ground.step();
            repaint();
            Thread.sleep(1000 / 60);
        }
    }

    /**
     * Método para dibujar los elementos del juego en la pantalla.
     */@Override
public void paint(Graphics g) {
    g.drawImage(background, 0, 0, null);
    ground.paint(g);
    column1.paint(g);
    column2.paint(g);
    bird.paint(g);

    // Dibujar el score con imágenes de los números
    int scoreDigits = score == 0 ? 1 : (int)Math.log10(score) + 1; // Número de dígitos del score, asegurándonos de que sea al menos 1
    int digitWidth = numbers[0].getWidth(); // Ancho de un dígito

    // Calcular la posición x inicial para centrar los números del score
    int totalWidth = scoreDigits * digitWidth;
    int x = (getWidth() - totalWidth) / 2;

    // Si el score es cero, dibujar el número 0
    if (score == 0) {
        g.drawImage(numbers[0], x, 50, null);
    } else {
        // Dibujar los dígitos del score de derecha a izquierda
        int tempScore = score;
        while (tempScore > 0) {
            int digit = tempScore % 10; // Obtener el último dígito
            g.drawImage(numbers[digit], x + totalWidth - digitWidth, 50, null);
            tempScore /= 10; // Eliminar el último dígito
            totalWidth -= digitWidth; // Mover a la izquierda para el siguiente dígito
        }
    }

    if (gameover) {
        try {
            end = ImageIO.read(getClass().getResource("/res/img/gameover.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        g.drawImage(end, 0, 0, null);
        return;
    }
    if (!start) {
        g.drawImage(startImg, 0, 0, null);
    }
}

    public static void main(String[] args) throws IOException, InterruptedException {
        JFrame frame = new JFrame("FlappyBird");
        World world = new World();
        frame.add(world);
        frame.setSize(320, 480);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        world.action();
    }
}
