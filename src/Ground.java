import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Clase que representa el suelo del juego, cargando la imagen "ground.png".
 */
public class Ground {
    private BufferedImage ground;
    private int x; // Posición horizontal
    private int y; // Posición vertical (altura de "ground.png" es 400)
    private int width; // Ancho de la imagen ground.png
     //private int height; // Altura de la imagen ground.png
    
    // Constructor que inicializa el suelo con una posición vertical dada
    public Ground(int y) throws IOException {
        this.y = y; // Inicializa la posición vertical
        x = 0;
        
        // Carga la imagen ground.png
        ground = ImageIO.read(this.getClass().getResource("/res/img/ground.png"));
        
        // Obtiene el ancho y la altura de la imagen
        width = ground.getWidth(); // Ancho: 497
        //height = ground.getHeight(); // Altura: 80
    }
    
    // Método getter para la posición horizontal
    public int getX() {
        return x;
    }

    // Método setter para la posición horizontal
    public void setX(int x) {
        this.x = x;
    }

    // Método getter para la posición vertical
    public int getY() {
        return y;
    }

    // Método setter para la posición vertical
    public void setY(int y) {
        this.y = y;
    }
    
    // Método para avanzar el suelo
    public void step() {
        x--;
        
        // Si la posición horizontal es menor o igual al ancho menos 360 (320 de ventana y 40 de margen),
        // se reinicia la posición horizontal a 0
        if (x <= -(width - 360)) {
            x = 0;
        }
    }
    
    // Método para dibujar el suelo
    public void paint(Graphics g) {
        // Dibuja la imagen del suelo en la posición actual
        g.drawImage(ground, x, y, null); // Posición: (0,400)
    }
    
    // Método principal para probar la clase Ground
    public static void main(String[] args) throws IOException {
        // Crea un nuevo objeto Ground con una posición vertical de 400
        new Ground(400);
    }
}
