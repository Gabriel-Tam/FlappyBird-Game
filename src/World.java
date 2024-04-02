import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
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
    private int score;
    private Font scoreFont; // Nueva fuente para la puntuación

    /**
     * Constructor que inicializa el mundo del juego.
     */
    public World() throws IOException {
        background = ImageIO.read(this.getClass().getResource("/res/img/bg.png"));
        startImg = ImageIO.read(getClass().getResource("/res/img/start.png"));
        init();

        // Cargar la nueva fuente
        try {
            InputStream fontStream = getClass().getResourceAsStream("/res/font/numers-font/flappy-bird-font.ttf");
            scoreFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(Font.BOLD, 30);
        } catch (Exception e) {
            e.printStackTrace();
            scoreFont = new Font("Arial", Font.BOLD, 30); // En caso de error, usa una fuente predeterminada
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
     */
    @Override
    public void paint(Graphics g) {
        g.drawImage(background, 0, 0, null);
        ground.paint(g);
        column1.paint(g);
        column2.paint(g);
        bird.paint(g);
    
        // Aplicar la nueva fuente para la puntuación
        g.setFont(scoreFont);
        
        // Obtener métricas de la fuente para calcular el ancho del texto
        FontMetrics metrics = g.getFontMetrics(scoreFont);
        int scoreWidth = metrics.stringWidth(Integer.toString(score));
        
        // Calcular las coordenadas para centrar el número en la parte superior de la pantalla
        int x = (getWidth() - scoreWidth) / 2;
        int y = 60; // Altura ajustada para que el número esté en la parte superior
    
        // Dibujar el texto con el efecto de borde resaltado
        g.setColor(Color.black); // Fondo blanco
        g.drawString(Integer.toString(score), x, y);
        
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
