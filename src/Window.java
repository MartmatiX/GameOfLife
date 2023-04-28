import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Window extends JFrame implements Runnable {

    private final int sizeX = 800;
    private final int sizeY = 600;

    private final int COLS = sizeX;
    private final int ROWS = sizeY;
    private boolean[][] cells;
    private boolean[][] nextCells;

    private final BufferedImage image = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);
    private final JLabel label = new JLabel(new ImageIcon(image));

    public Window() {
        this.setSize(new Dimension(sizeX, sizeY));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setBackground(Color.WHITE);
        this.setTitle("Game Of Life");
        this.setVisible(true);
        assignCells();
        this.add(label);
        new Thread(this).start();
    }

    public void assignCells() {
        cells = new boolean[COLS][ROWS];
        nextCells = new boolean[COLS][ROWS];
        Random random = new Random();
        for (int x = 0; x < COLS; x++) {
            for (int y = 0; y < ROWS; y++) {
                cells[x][y] = random.nextBoolean();
            }
        }
    }

    public void updateImage() {
        int color;
        for (int x = 0; x < COLS; x++) {
            for (int y = 0; y < ROWS; y++) {
                if (cells[x][y]) {
                    color = 0xffffff;
                } else {
                    color = 0x0;
                }
                image.setRGB(x, y, color);
            }
        }
        label.repaint();
    }

    @Override
    public void run() {
        while (true) {
            for (int x = 1; x < COLS - 1; x++) {
                for (int y = 1; y < ROWS - 1; y++) {
                    int neighbourCount = 0;
                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dy = -1; dy <= 1; dy++) {
                            if (dx == 0 && dy == 0) continue;
                            if (cells[x + dx][y + dy]) neighbourCount++;
                        }
                    }
                    if (cells[x][y]) {
                        nextCells[x][y] = neighbourCount >= 2 && neighbourCount <= 3;
                    } else {
                        nextCells[x][y] = neighbourCount == 3;
                    }
                }
            }
            for (int x = 1; x < COLS - 1; x++) {
                System.arraycopy(nextCells[x], 1, cells[x], 1, ROWS - 1 - 1);
            }
            updateImage();
            try {
                Thread.sleep(1000 / 60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}