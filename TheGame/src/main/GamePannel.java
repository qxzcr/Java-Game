package main;

import entity.Player;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePannel extends JPanel implements Runnable{
    // Screen settings
    final int originalTileSize = 16; //16*16 tile
    final int scale = 3;
    public final int tileSize = originalTileSize * scale; // 48*48 tile
     public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; //768 pixels
    public final int screenHieght = tileSize * maxScreenRow; //576 pixels

    //WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;

    int FPS = 60;
    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler();
    Thread getGameThread;
    Thread gameThread; //Keeps program running
    // Set Player's default position
    public Player player = new Player(this, keyH);

    public GamePannel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHieght));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true); //Rendering performance
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start(); //Automatically called run method
    }
    @Override //We will create a game loop
    public void run() {
        double drawInterval = 1000000000/FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null){
            long currentTime = System.nanoTime();
            // 1 UPDATE: Update information such as character positions
            update();
            // 2 Draw: Draw the screen with the update information
            repaint();
            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;

                if(remainingTime < 0){
                    remainingTime = 0;
                }
                Thread.sleep((long) remainingTime);

                nextDrawTime += drawInterval;
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public void update() {
        player.update();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        tileM.draw(g2);
        player.draw(g2);
        g2.dispose();
    }
}
