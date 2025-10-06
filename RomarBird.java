import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class RomarBird extends JPanel implements ActionListener, KeyListener{
  int boardWidth = 360;
  int boardHeight = 640;
  
  Image backgroundImg;
  Image romarImg;
  Image topPipeImg;
  Image bottomPipeImg;

  int birdX = boardHeight/8;
  int birdY = boardHeight/2;
  int birdWidth = 54;
  int birdHeight = 44;

    

  class Bird{
     int x = birdX;
     int y = birdY;
     int width = birdWidth;
     int height = birdHeight;
     Image img;

     Bird(Image img){
        this.img = img;
        
     }

  }
 //pipes
 int pipeX = boardWidth;
 int pipeY = 0;
 int pipeWidth = 64;
 int pipeHeight = 512;

 class Pipe{
    int x = pipeX;
    int y = pipeY;
    int width = pipeWidth;
    int height = pipeHeight;
    Image img;
    boolean passed = false;

    Pipe(Image img){
    this.img =img;

    }
    
    }

  Bird bird;
  int velocityX = -4;
  int velocityY = -0;
  int gravity = 1;

  ArrayList<Pipe> pipes;
  Random random = new Random();

  Timer gameLoop;
  Timer placePipesTimer;

  boolean gameOver = false;
  double score = 0;

  RomarBird(){
     setPreferredSize(new Dimension(boardWidth, boardHeight));
     //setBackground(Color.blue);

     setFocusable(true);
     addKeyListener(this);

     backgroundImg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
     romarImg = new ImageIcon(getClass().getResource("./romar.png")).getImage();
     topPipeImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
     bottomPipeImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();
        
     bird = new Bird(romarImg);
     pipes = new ArrayList<Pipe>();

     placePipesTimer = new Timer(1500, new ActionListener(){
        public void actionPerformed(ActionEvent e){
           placePipes();
        }

     });
     placePipesTimer.start();

     gameLoop = new Timer(1000/60, this);
     gameLoop.start();

  }

  void placePipes(){
    int randomPipeY = (int)(pipeY - pipeHeight/4 - Math.random()*(pipeHeight/2));
    int openingSpace = boardHeight/4;

    Pipe topPipe = new Pipe(topPipeImg);
    topPipe.y = randomPipeY;
    pipes.add(topPipe);

    Pipe bottomPipe = new Pipe(bottomPipeImg);
    bottomPipe.y = topPipe.y + pipeHeight + openingSpace;
    pipes.add(bottomPipe);
  
  }
  
  public void paintComponent(Graphics g){
      super.paintComponent(g);
      draw(g);
  }
  public void draw(Graphics g){
      g.drawImage(backgroundImg, 0, 0, boardWidth, boardHeight, null);
      //bird
      g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);
     
      for(int i = 0; i < pipes.size(); i++){
         Pipe p = pipes.get(i);
         g.drawImage(p.img, p.x, p.y, p.width, p.height, null);
    
      }

      g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 32));
        if(gameOver){
            g.drawString("Game Over:  " + String.valueOf((int) score), 10, 35);
        }
        else{
           g.drawString(String.valueOf((int) score), 10 ,35);
        }
  }
public void move(){
    velocityY += gravity;
    bird.y += velocityY;
    bird.y = Math.max(bird.y, 0);

    for(int i = 0; i < pipes.size(); i++){
       Pipe p = pipes.get(i);
       p.x += velocityX;

       if(!p.passed && bird.x >p.width){
        p.passed = true;
        score += 0.5;

       }

       if(collision(bird, p)){
          gameOver = true;
       }
    }

    if(bird.y > boardHeight){
       gameOver = true;
    }
  }

  public void actionPerformed(ActionEvent e){
    move();
    repaint();
    if(gameOver){
        placePipesTimer.stop();
        gameLoop.stop();
    }
  }

  public boolean collision(Bird a, Pipe b){
     return a.x < b.x + b.width && 
            a.x + a.width > b.x &&
            a.y < b.y + b.height &&
            a.y + a.height > b.y;
   

  }
  

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
             velocityY = -9;
             if(gameOver){
                bird.y = birdY;
                pipes.clear();
                score = 0;
                gameOver = false;
                velocityY = 0;
                placePipesTimer.start();
                gameLoop.start();

             }
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}