import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Bird {
    // Coordenadas del pájaro
    private int x;
    private int y;
    private BufferedImage bird; // Imagen del pájaro
    
    // Objetos para la animación del pájaro
    private BufferedImage[] birds;
    private int index;
    
    // Variables de física para el movimiento del pájaro
    private int g;
    private double t;
    private double v0;
    private double vt;
    private double s;
    
    // Ángulo de rotación del pájaro
    private double angle;
    
    // Tamaño del pájaro
    private int size = 26;
    
    // Constructor que inicializa la posición del pájaro y carga las imágenes
    public Bird(int x , int y) throws IOException {
        this.x = x;
        this.y = y;
        birds = new BufferedImage[3];
        for(int i = 0; i < 3; i++) {
            birds[i] = ImageIO.read(getClass().getResource("/res/img/" + i + ".png"));
        }
        bird = birds[0];
        this.g = 4;
        this.t = 0.25;
        this.v0 = 20;
    }
    
    // Método para dibujar el pájaro en la pantalla
    public synchronized void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.rotate(angle, this.x, this.y);
        int x = this.x - bird.getWidth() / 2;
        int y = this.y - bird.getHeight() / 2;
        g.drawImage(bird, x, y, null);
        g2d.rotate(-angle, this.x, this.y);
    }
    
    // Método para actualizar la posición del pájaro en cada iteración del juego
    public void step() {
        double vt1 = vt;
        double v = vt1 - g * t;
        vt = v;
        s = vt1 * t - 0.5 * g * t * t;
        y = y - (int)s;
        angle = -Math.atan(s / 15);
        index++;
        bird = birds[index / 8 % 3];
    }
    
    // Método para hacer que el pájaro "vole" cuando el jugador lo activa
    public void flappy() {
        AudioPlayWave audioPlayWave = new AudioPlayWave("/res/sound/Fly.wav");
        audioPlayWave.start();
        vt = v0;
    }
    
    // Método para detectar colisiones con los tubos y el suelo
    public boolean hit(Column column1, Column column2, Ground ground) {
        if(y - size / 2 >= ground.getY() || y - size / 2 <= 0) {
            return true;
        }
        return hit(column1) || hit(column2);
    }
    
    public boolean hit(Column col) {
        if(x > col.getX() - col.getWidth() / 2 - size / 2 && x < col.getX() + col.getWidth() / 2 + size / 2) {
            if(y > col.getY() - col.getGap() / 2 + size / 2  && y < col.getY() + col.getGap() / 2 - size / 2) {
                return false;
            }
            return true;
        }
        return false;
    }
    
    // Método para verificar si el pájaro pasa por los tubos
    public boolean pass(Column col1, Column col2) {
        return col1.getX() == x || col2.getX() == x;
    }
}
