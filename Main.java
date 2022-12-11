import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class Main
{
	public static void main( String[] args )
	{
		JFrame m_window = new JFrame( "My JFrame" );
		GamePanel m_gp  = new GamePanel();


		m_window.add( m_gp );
		m_window.pack();
		m_window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		m_window.setResizable( false );
		m_window.setLocationRelativeTo( null );
		m_window.setVisible( true );

		m_gp.startThread();
	}
}


class GamePanel extends JPanel implements Runnable
{
	public final int ORIGINAL_TILE_SIZE = 16; 	// 16x16
	public final int SCALE 				= 3;
	public final int TILE_SIZE 			= SCALE * ORIGINAL_TILE_SIZE; 	// 48x48
	public final int WND_COLUMN 		= 16;
	public final int WND_ROW 			= 12;
	public final int WND_WIDTH 			= WND_COLUMN * TILE_SIZE;
	public final int WND_HEIGHT			= WND_ROW	 * TILE_SIZE;
	public final int FPS				= 30;


	public Thread m_thread;

	public MyKeyListener m_keyL = new MyKeyListener();


	public int m_iX = TILE_SIZE, m_iY = TILE_SIZE;


	public void startThread()
	{
		System.out.println( "USE ARROW KEYS TO MOVE" );

		m_thread = new Thread( this );
		m_thread.start();
	}

	public void update()
	{
		if ( m_keyL.upPressed 	 ) m_iY -= 2; 	// m_keyL.upPressed == true
		if ( m_keyL.downPressed  ) m_iY += 2;
		if ( m_keyL.leftPressed  ) m_iX -= 2;
		if ( m_keyL.rightPressed ) m_iX += 2;
	}

	@Override public void paintComponent( Graphics g )
	{
		super.paintComponent( g );
		Graphics2D g2 = (Graphics2D)g;
		
		g2.setColor( Color.YELLOW );
		g2.fillRect( m_iX, m_iY, TILE_SIZE, TILE_SIZE );
		g2.dispose();
	} 

	@Override public void run()
	{
		/*
			new m_dDrawInterval = 1 000 000 000 / 30 = 33333333.3333333333
			new m_dNextDrawTime = System.nanoTime() + m_dDrawInterval = 11146771260200 + 33333333.3333333333 = 11168349793933.3333333333


			LOOP {
				new m_dRemainTime = m_dNextDrawTime - System.nanoTime() = -71156414466.6666666667
				m_dRemainTime / 1000000 = -71156.4144666666666667 
				
				if m_dRemainTime < 0 then m_dRemainTime = 0
				
				SLEEP( m_dRemainTime )
				
				m_dNextDrawTime += m_dDrawInterval = 11168383127266.6666666666
			}
		*/


		double m_dDrawInterval = 1000000000 / FPS;
		double m_dNextDrawTime = System.nanoTime() + m_dDrawInterval;


		while ( m_thread != null ) {
			update();
			repaint();
			
			try {
				double m_dRemainTime = m_dNextDrawTime - System.nanoTime();
				m_dRemainTime /= 1000000;

				if ( m_dRemainTime < 0 ) m_dRemainTime = 0;

				m_thread.sleep( (long)m_dRemainTime );
				
				m_dNextDrawTime += m_dDrawInterval;
			} catch ( Exception e ) {
				System.out.println( e );
			}
		}
	} 


	// CONSTRUCTOR
	public GamePanel()
	{
		this.setPreferredSize( new Dimension( WND_WIDTH, WND_HEIGHT ) );
		this.setBackground( Color.BLACK );
		this.setDoubleBuffered( true );
		this.addKeyListener( m_keyL );
		this.setFocusable( true );
	}
}


class MyKeyListener implements KeyListener
{
	public boolean upPressed, downPressed, leftPressed, rightPressed; 


	@Override public void keyPressed( KeyEvent e )
	{
		if ( e.getKeyCode() == KeyEvent.VK_UP 	 ) upPressed 	= true;
		if ( e.getKeyCode() == KeyEvent.VK_DOWN  ) downPressed 	= true;
		if ( e.getKeyCode() == KeyEvent.VK_LEFT  ) leftPressed 	= true;
		if ( e.getKeyCode() == KeyEvent.VK_RIGHT ) rightPressed = true;
	}

	@Override public void keyReleased( KeyEvent e )
	{
		if ( e.getKeyCode() == KeyEvent.VK_UP 	 ) upPressed 	= false;
		if ( e.getKeyCode() == KeyEvent.VK_DOWN  ) downPressed 	= false;
		if ( e.getKeyCode() == KeyEvent.VK_LEFT  ) leftPressed 	= false;
		if ( e.getKeyCode() == KeyEvent.VK_RIGHT ) rightPressed = false;
	}

	@Override public void keyTyped( KeyEvent e )
	{
	}
}