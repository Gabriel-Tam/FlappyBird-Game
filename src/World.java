
//import java.awt.Color;
//import java.awt.Font;
//import java.awt.FontMetrics;
import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Clase que representa el mundo del juego Flappy Bird, inicializa los elementos
 * del juego y gestiona su funcionamiento.
 */
public class World extends JPanel {
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
    private BufferedImage backgroundDay;
    private BufferedImage backgroundNight;
    private long startTime;

    /**
     * Constructor que inicializa el mundo del juego.
     */
    public World() throws IOException {
        startImg = ImageIO.read(getClass().getResource("/res/img/start.png"));
        backgroundDay = ImageIO.read(this.getClass().getResource("/res/img/bg-day.png"));
        backgroundNight = ImageIO.read(this.getClass().getResource("/res/img/bg-night.png"));
        this.startTime = System.currentTimeMillis();
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
                    // No llamar ground.step() una vez que el juego haya terminado.
                }
                if (bird.pass(column1, column2)) {
                    score++;
                    AudioPlayWave pointSound = new AudioPlayWave("/res/sound/hit.wav");
                    pointSound.start();
                }
            }
            // Actualizar el movimiento del suelo solo si el juego no ha terminado.
            if (!gameover) {
                ground.step();
            }
            repaint();
            Thread.sleep(1000 / 60);
        }
    }
    

    // Este método retorna un factor [0, 1] que represente el ciclo de día a noche.
    private float getDayFraction() {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - this.startTime; // Usa this.startTime para referenciar la variable de instancia
        return (elapsedTime % 80000) / 80000f; // Asumiendo un ciclo de 1 minuto para simplificar
    }

    /**
     * Método para dibujar los elementos del juego en la pantalla.
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g); 
        // Dibujar siempre el fondo de día primero
        g.drawImage(backgroundDay, 0, 0, this.getWidth(), this.getHeight(), null);
    
        float fraction = getDayFraction();
        // Ahora, calcular la opacidad para la transición día/noche
        float opacity = Math.abs(fraction - 0.5f) * 2; // Oscila entre 0 y 1 a lo largo del día
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
        g2d.drawImage(backgroundNight, 0, 0, this.getWidth(), this.getHeight(), null);
        g2d.dispose();
    
        // Resto de elementos del juego
        ground.paint(g);
        column1.paint(g);
        column2.paint(g);
        bird.paint(g);
    
        // Dibujar el score con imágenes de los números
        int scoreDigits = score == 0 ? 1 : (int) Math.log10(score) + 1; // Número de dígitos del score
        int digitWidth = numbers[0].getWidth(); // Ancho de un dígito
        int totalWidth = scoreDigits * digitWidth;
        int x = (getWidth() - totalWidth) / 2; // Posición x inicial para centrar el score
    
        for (int i = 0; i < scoreDigits; i++) {
            int digit = (int) (score / Math.pow(10, scoreDigits - i - 1)) % 10;
            g.drawImage(numbers[digit], x + (digitWidth * i), 50, null);
        }
    
        // Centrar y dibujar la imagen de "Game Over" si el juego ha terminado
        if (gameover) {
            try {
                end = ImageIO.read(getClass().getResource("/res/img/gameover.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            x = (getWidth() - end.getWidth()) / 2;
            int y = (getHeight() - end.getHeight()) / 2;
            g.drawImage(end, x, y, null);
        } else if (!start) {
            // Asegurarse de que la imagen de "Start" solo se dibuje al principio
            // y no después de que el juego ha terminado.
            x = (getWidth() - startImg.getWidth()) / 2;
            int y = (getHeight() - startImg.getHeight()) / 5;
            g.drawImage(startImg, x, y, null);
        }
    }
    
    

    public static void main(String[] args) throws IOException, InterruptedException {
        JFrame frame = new JFrame("FlappyBird");
        World world = new World();
        frame.add(world);
        frame.setSize(288, 512);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        world.action();
    }
}
