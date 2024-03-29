import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;

public class App extends JFrame implements Runnable {
    public static final int WIDTH = 360, HEIGHT = 640;

    private Canvas canvas;
    private Thread thread;
    private boolean running;
    private BufferStrategy bs;
    private Graphics g;
    private final int FPS = 60;
    private double TARGET_TIME = 1000000000 / FPS;
    private double delta = 0;
    private int AVERAGE_FPS = FPS;

    // Constructor de la clase
    public App() {
        window(); // Configura la ventana al ser instanciada
        initializeCanvas(); // Configura el canvas
    }

    // Configura la ventana principal
    private void window() {
        setTitle("Flappy Bird");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Inicializa el canvas en la ventana principal
    private void initializeCanvas() {
        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        canvas.setMaximumSize(new Dimension(WIDTH, HEIGHT));
        canvas.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        canvas.setFocusable(true);
        add(canvas);
    }

    // Método principal de la aplicación
    public static void main(String[] args) {
        new App().start();
    }

    // Actualiza el estado del juego
    private void update() {
    }

    // Dibuja los gráficos del juego
    private void draw() {
        // Se obtiene la estrategia de buffer del canvas
        bs = canvas.getBufferStrategy();

        // Si la estrategia de buffer es nula, se crea una nueva
        if (bs == null) {
            canvas.createBufferStrategy(3);
            return;
        }

        // Se obtiene el objeto Graphics de la estrategia de buffer
        g = bs.getDrawGraphics();

        try {
            // Limpia la pantalla
            g.setColor(Color.GRAY);
            g.fillRect(0, 0, WIDTH, HEIGHT);
            // -------------------------------------------
            // Dibuja otros elementos del juego aquí

            // Dibuja el FPS
            g.setColor(Color.WHITE);
            g.drawString("FPS: " + AVERAGE_FPS, 10, 20);
            // -------------------------------------------

        } finally {
            // Se libera el objeto Graphics y se muestra el buffer
            g.dispose();
            bs.show();
        }
    }

    // Método principal del hilo de ejecución
    @Override
    public void run() {
        long lastTime = System.nanoTime();
        long timer = System.nanoTime();
        int frames = 0;

        while (running) {
            long now = System.nanoTime();
            long elapsedTime = now - lastTime;
            lastTime = now;

            delta += elapsedTime;

            if (delta >= TARGET_TIME) {
                update();
                draw();
                delta -= TARGET_TIME;
                frames++;
            }

            if (System.nanoTime() - timer > 1000000000) {
                timer = System.nanoTime();
                AVERAGE_FPS = frames;
                frames = 0;
            }
        }

        stop();
    }

    // Inicia el hilo de ejecución
    private void start() {
        thread = new Thread(this);
        thread.start();
        running = true;
    }

    // Detiene el hilo de ejecución
    private void stop() {
        try {
            thread.join();
            running = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}