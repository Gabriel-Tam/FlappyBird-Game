import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class Column {
    // Representa una imagen de columna
    private BufferedImage column;
    // Coordenadas x e y de la columna
    private int x;
    private int y;
    private int width;
    private int height;

    // Generador de números aleatorios
    private Random random;

    // Espacio entre columnas
    private int gap = 109;

    public int getGap() {
        return gap;
    }

    public void setGap(int gap) {
        this.gap = gap;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Crea una nueva columna en la posición x especificada.
     * @param x La posición x inicial de la columna
     * @throws IOException Si hay un error al leer la imagen de la columna
     */
    public Column(int x) throws IOException{
        // Lee la imagen de la columna
        column = ImageIO.read(getClass().getResource("/res/img/column.png"));
        width = column.getWidth();
        height = column.getHeight();
        random = new Random();
        this.x = x;
        
        // Calcula una posición y aleatoria dentro de un rango
        y = 140 + random.nextInt(140);
    }

    /**
     * Mueve la columna hacia la izquierda.
     */
    public void step(){
        x--;
        // Si la columna sale de la pantalla, se reposiciona a la derecha
        if(x <= -width){
            x = 320;
            // Calcula una nueva posición y aleatoria dentro del rango
            y = 140 + random.nextInt(140);
        }
    }

    /**
     * Dibuja la columna en el lienzo de juego.
     * @param g El contexto de gráficos en el que se dibuja la columna
     */
    public void paint(Graphics g){
        // Dibuja la imagen de la columna en la posición correspondiente
        g.drawImage(column, x-width/2, y-height/2, null);
    }
}
