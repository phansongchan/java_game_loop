import javax.swing.*; 
import java.awt.*; 


class Main
{
	static void main( String[] args )
	{
		def m_frame = new JFrame( "My JFrame" );
		def m_p 	= new LocalPanel();


		m_frame.add( m_p );
		m_frame.pack();
		m_frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		m_frame.setResizable( false );
		m_frame.setLocationRelativeTo( null );
		m_frame.setVisible( true );

		m_p.startThread();
	}
}


class LocalPanel extends JPanel implements Runnable
{
	def m_thread;

	def m_x = 0, m_y = 0;

	final FPS = 30;


	def startThread()
	{
		m_thread = new Thread( this );
		m_thread.start();
	}

	def update()
	{
		m_x++;
		m_y++;
	}

	@Override void paint( Graphics g )
	{
		super.paint( g );

		g.setColor( Color.BLUE );
		g.fillRect( m_x, m_y, 50, 50 );
		g.dispose();
	}

	@Override void run()
	{
		def m_amountTime = 10E8.div( FPS );
		def m_nextTime = System.nanoTime() + m_amountTime;


		while ( m_thread != null ) {
			update();
			repaint();

			try {
				def m_remainTime = m_nextTime - System.nanoTime();
				m_remainTime = m_remainTime.div( 10E5 );

				if ( m_remainTime < 0 ) m_remainTime = 0;

				Thread.sleep( (long)m_remainTime );

				m_nextTime = m_nextTime + m_amountTime;
			} catch ( Exception e ) {
				System.out.println( e );
			}
		}
	}


	// CONSTRUCTOR
	def LocalPanel()
	{
		this.setPreferredSize( new Dimension( 400, 400 ) );
		this.setBackground( Color.BLACK );
		this.setDoubleBuffered( true );
	}
}