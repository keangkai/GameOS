import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Logger;
import acm.util.Animator;
import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import acm.program.GraphicsProgram;

public class Controller extends GraphicsProgram implements KeyListener{

	//private static final long serialVersionUID = 1L;
	private static final int Window_Width = 1000;
	private static final int Window_Height = 600;
	private static final int Refresh = 10;
	private static final int paddle1_Height = 120;
	private static final int paddle1_Width = 15;
	private static final int paddle2_Height = 120;
	private static final int paddle2_Width = 15;
	private static final int BALL_RADIUS = 20;
	private static final double FRICTION = 0.94;
	
	int ballPositionX = Window_Width / 2;
	int ballPositionY = Window_Height / 2;
	int ballSpeedX = 5;
	int ballSpeedY = 5;
	double paddle1SpeedY = 0;
	double paddle2SpeedY = 0;
	double paddle1PositionY = Window_Height / 2 - paddle1_Height / 2;
	double paddle2PositionY = Window_Height / 2 - paddle2_Height / 2;
	int scorePlayer1 = 0;
	int scorePlayer2 = 0;
	boolean paddle1Up = false, paddle1Down = false;
	boolean paddle2Up = false, paddle2Down = false;
	
	GImage bg =  new GImage("BG.jpg");
	GOval ball = new GOval(BALL_RADIUS, BALL_RADIUS);
	GRect paddle1 = new GRect(paddle1_Width,paddle1_Height);
	GRect paddle2 = new GRect(paddle2_Width,paddle2_Height);
	GLabel score1 = new GLabel(Integer.toString(scorePlayer1));
	GLabel score2 = new GLabel(Integer.toString(scorePlayer2));
	
	public void init() {
		//Window
		setSize(Window_Width, Window_Height);
		addKeyListeners();
		
		//Background
			
		//Ball
		ball.setColor(Color.MAGENTA);
		ball.setLocation(ballPositionX, ballPositionY);
		ball.setFilled(true);
		
		//Left paddle
		paddle1.setLocation(30, (int)paddle1PositionY);
		paddle1.setColor(Color.RED);
		paddle1.setFilled(true);
		
		//Right paddle
		paddle2.setLocation(Window_Width-50, Window_Height/2 - paddle2_Height/2);
		paddle2.setColor(Color.BLUE);
		paddle2.setFilled(true);
		
		//score of Player 1
		score1.setLocation(Window_Width / 4,50);
		
		//score of Player 1
		score2.setLocation(3 * Window_Width / 4,50);
	
		//GImage background =  new GImage("BG2.jpg");
		//add(background);
		add(bg);
		add(ball);
		add(paddle1);
		add(paddle2);
		add(score1);
		add(score2);
	}
	
	//Running
	public void run() {
		//send object from class ThreadPause
		Thread time = new Thread(new ThreadPause());
		time.start();
		while (true) {
			pause(Refresh);
			moveBall();
			checkBallCollision();
			moveFirstPaddle();
			moveSecondPaddle();
		}
	}
	
	//Control Paddle 1
	public void moveFirstPaddle() {
		if (paddle1Up) {
			paddle1SpeedY -= 5;
		}
		if (paddle1Down) {
			paddle1SpeedY += 5;
		}
		if (!paddle1Up && !paddle1Down) {
			paddle1SpeedY *= FRICTION;
		}
		
		if(paddle1PositionY < 0) {
			paddle1PositionY = 0;
		}
		else if(paddle1PositionY > Window_Height - paddle1_Height) {
			paddle1PositionY = Window_Height - paddle1_Height;
		}
		if(paddle1SpeedY >= 9) {
			paddle1SpeedY = 9;
		}
		else if(paddle1SpeedY <= -9) {
			paddle1SpeedY = -9;
		}
		
		paddle1PositionY += paddle1SpeedY;
		paddle1.setLocation(30, paddle1PositionY);
	}
	
	//Control Paddle 2
	public void moveSecondPaddle() {
		if (paddle2Up) {
			paddle2SpeedY -= 5;
		}
		if (paddle2Down) {
			paddle2SpeedY += 5;
		}
		if (!paddle2Up && !paddle2Down) {
			paddle2SpeedY *= FRICTION;
		}
		
		if(paddle2PositionY < 0) {
			paddle2PositionY = 0;
		}
		else if(paddle2PositionY > Window_Height - paddle1_Height) {
			paddle2PositionY = Window_Height - paddle1_Height;
		}
		if(paddle2SpeedY >= 9) {
			paddle2SpeedY = 9;
		}
		else if(paddle2SpeedY <= -9) {
			paddle2SpeedY = -9;
		}
		
		paddle2PositionY += paddle2SpeedY;
		paddle2.setLocation(Window_Width-50, paddle2PositionY);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_W:
			paddle1Up = true;
			break;
		case KeyEvent.VK_S:
			paddle1Down = true;
			break;
		case KeyEvent.VK_UP:
			paddle2Up = true;
			break;
		case KeyEvent.VK_DOWN:
			paddle2Down = true;
			break;
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_W:
			paddle1Up = false;
			break;
		case KeyEvent.VK_S:
			paddle1Down = false;
			break;
		case KeyEvent.VK_UP:
			paddle2Up = false;
			break;
		case KeyEvent.VK_DOWN:
			paddle2Down = false;
			break;
		}
	}
	
	public void moveBall() {
		ballPositionX += ballSpeedX;
		ballPositionY += ballSpeedY;
		
		ball.setLocation(ballPositionX, ballPositionY);	
	}
	public void checkBallCollision() {
		if(ballPositionX < 0) {
			ballPositionX = Window_Width / 2;
			ballPositionY = Window_Height / 2;
			ballSpeedX = ballSpeedX * -1;
			ballSpeedY = ballSpeedY * -1;
			
			//Increase score of Player 2
			updateScore(2);
		}
		if(ballPositionX > Window_Width - BALL_RADIUS) {
			ballPositionX = Window_Width / 2;
			ballPositionY = Window_Height / 2;
			ballSpeedX = ballSpeedX * -1;
			ballSpeedY = ballSpeedY * -1;
			
			//Increase score of Player 1
			updateScore(1);
		}
		if(ballPositionY > Window_Height - BALL_RADIUS || ballPositionY < 0) {
			ballSpeedY = ballSpeedY * -1;
		}
		
		//Check collision of paddle 1
		if(ballPositionY > paddle1.getLocation().getY() - BALL_RADIUS / 2 && ballPositionY < paddle1.getLocation().getY() + paddle1_Height && ballPositionX < 30 + paddle1_Width) {
			ballSpeedX = ballSpeedX * -1;
		}

		//Check collision of paddle 2
		if(ballPositionY > paddle2.getLocation().getY() && ballPositionY < paddle2.getLocation().getY() + paddle2_Height && ballPositionX > Window_Width - 40 - BALL_RADIUS) {
			ballSpeedX = ballSpeedX * -1;
		}
	}
	
	public void updateScore(int player) {	
		if(player == 1) {
			scorePlayer1++;
			score1.setLabel(Integer.toString(scorePlayer1));
		}
		else if(player == 2) {
			scorePlayer2++;
			score2.setLabel(Integer.toString(scorePlayer2));
		}
	}
	//implement class thread
	public class ThreadPause implements Runnable {

		@Override
		public void run() {
			int count = 0 ;
			while(true) {
				System.out.println("Count time : " + count);
				count++;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
	}
}
