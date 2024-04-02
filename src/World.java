import java.awt.Color;
import java.awt.Font;
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
    private int score;

    /**
     * Constructor que inicializa el mundo del juego.
     */
    public World() throws IOException {
        background = ImageIO.read(this.getClass().getResource("/res/img/bg.png"));
        startImg = ImageIO.read(getClass().getResource("/res/img/start.png"));
        init();
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
		Font font = new Font(Font.MONOSPACED, Font.BOLD, 30);
		g.setFont(font);
		g.setColor(Color.white);
		g.drawString("Puntuación: " + score, 30, 50); // Cambiar "Puntuación" a "Puntaje"
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
