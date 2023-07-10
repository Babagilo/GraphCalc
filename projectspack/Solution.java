package projectspack;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import static projectspack.Util.specialfunctions;
import static projectspack.Util.round2NearestInterval;

public class Solution extends JPanel implements ActionListener, MouseWheelListener, MouseListener,MouseMotionListener, KeyListener {


	/**
	 * 
	 */
	private static final long serialVersionUID = 7732570952062313116L;


	String[] verSpecial= {//Very special functions that skip a bunch
			"plusminus",
			"sin",
			"cos",
			"tan",
			"floor",
			"sum"




	};

	private static final DecimalFormat lbf = new DecimalFormat("0.00");
	private double zoomFactor =250;
	public String equation = new String();;
	public JLabel equationLabel;
	public JTextField graphFunctionBox;
	public JTextField graphFunctionBox1;
	public JTextField graphFunctionBox2;
	public JButton submitButton;
	public JButton calcButton;
	public JPanel menu;
	private JFrame graph;
	private Point mouse;
	private double targetZoom ;
	private double xOffset, yOffset=0;
	public float[] dataPoints ; // DataPoints on Screen with length of w * n   N being how precise the data is.
	private Timer time = new Timer(10,this);
	public Solution(){

		 menu = new JPanel();
		JPanel UI = new JPanel();
		UI.setBounds(0,0,400,150);;
		graph = new JFrame();
		graph.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		calcButton = new JButton(new ImageIcon("C:/Users/ammer/Downloads/calculator.png"));
		calcButton.setSize(getMaximumSize());
		

		setFocusable(true);
		setBounds(0, 20, 400, 200);
		graphFunctionBox = new JTextField(30);
		graphFunctionBox1 = new JTextField(30);
		graphFunctionBox2 = new JTextField(30);
		equationLabel = new JLabel("Enter the equation within this box");
		graph.addMouseWheelListener(this);
		graph.setTitle("GraphIT");
		graph.setSize(300,300);
		//graph.setLayout((LayoutManager) new FlowLayout());
		menu.add(calcButton);
		menu.add(equationLabel);
		menu.add(graphFunctionBox);
		menu.add(graphFunctionBox1);
		menu.add(graphFunctionBox2);
		graph.add(menu);
		graph.add(this);
		this.add(UI);
		repaint();
		addMouseListener(this);
		addMouseMotionListener(this);
		targetZoom = zoomFactor;
		addMouseWheelListener(this);
		addKeyListener(this);
		grabFocus();
		calcButton.addActionListener(this);
		graph.setVisible(true);
		setFocusable(true);
		time.start();
	}
	public void paintComponent(Graphics g){
		menu.setBounds(0,0,350,graph.getHeight());
		this.setBounds(350,7,graph.getWidth()-350, graph.getHeight()-40);
		zoomFactor =lerp(zoomFactor ,targetZoom , 0.3 );
		if(targetZoom> zoomFactor) {

			if(targetZoom - zoomFactor < Math.abs(500 *Math.pow(1.1,zoom-1) -targetZoom)/10) {
				zoomFactor = targetZoom;
			}
		}else {
			if(zoomFactor-targetZoom  < Math.abs(500 *Math.pow(1.1,zoom+1) -targetZoom)/10) {
				zoomFactor = targetZoom;
			}
		}
		super.paintComponent(g);
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		if(mouse==null) {
			mouse = new Point(0,0);
		}
		Equation mouseX =new Equation(graphFunctionBox.getText().trim() ,Double.parseDouble(lbf.format((mouse.x -xOffset*-zoomFactor   )/zoomFactor))) ;
		g.setColor(Color.BLACK);
		int at =0;
		g.drawString("x :" +  lbf.format((mouse.x +xOffset*zoomFactor   )/zoomFactor ), 200, 200);
		if(mouse!=null)

			for(Map.Entry<Double, Boolean> entry : mouseX.value.entrySet()) {
				at++;
				g.drawString("y :" + entry.getKey() , 200, 200+at*20);

			}

		g.setColor(Color.pink);
		try{

			g.setColor(Color.BLACK);
			//y axis
			g.drawLine( (int)(xOffset*-zoomFactor) , this.getHeight(),(int)(xOffset*-zoomFactor) , 0   );
			//x axis
			g.drawLine( 0,this.getHeight()+ (int)(yOffset*-zoomFactor) , this.getWidth() ,  this.getHeight()+(int)(yOffset*-zoomFactor));
			//double[][] data =loadDataPoints( graphFunctionBox.getText()  , xOffset , xOffset + (this.getWidth())*(1/zoomFactor) , 1/zoomFactor);
			double y2;

			double a =((0 - (yOffset*-zoomFactor)-(this.getHeight() ))/(- zoomFactor))- ((0 - (yOffset*-zoomFactor) ))/(- zoomFactor) ;
			double scale= Math.pow(10,(double)((int)(Math.log10(a)-1)));
			int incre =  this.getHeight()/scale/zoomFactor >10? 5:1 ;
			for(int i = 0 ; scale*i *zoomFactor< this.getHeight()-zoomFactor*yOffset ; i+= incre) {
				g.drawString( lbf.format(scale * i) , (int)(xOffset*-zoomFactor)  + 20 , (int)(this.getHeight() - i*scale* zoomFactor+ (int)(yOffset*-zoomFactor)));
			}
			for(int i = 0 ; -scale*i *zoomFactor< this.getHeight()-zoomFactor*yOffset ; i-= incre) {
				g.drawString( lbf.format(scale * i) , (int)(xOffset*-zoomFactor)  + 20 , (int)(this.getHeight() - i*scale* zoomFactor+ (int)(yOffset*-zoomFactor)));
			}


			for(int i = 0 ; scale*i *zoomFactor< this.getWidth() -zoomFactor*xOffset; i+= incre) {
				g.drawString( lbf.format(scale * i) , (int)(i*scale* zoomFactor+ (int)(xOffset*-zoomFactor)) ,this.getHeight()+(int)(yOffset*-zoomFactor) +20 ) ;
			}
			for(int i = 0 ; -scale*i *zoomFactor< this.getWidth() -zoomFactor*xOffset  ; i-= incre) {
				g.drawString( lbf.format(scale * i) , (int)(i*scale* zoomFactor+ (int)(xOffset*-zoomFactor)) ,this.getHeight()+(int)(yOffset*-zoomFactor) +20 ) ;
			}

			drawFunction(g,graphFunctionBox.getText().trim() ,Color.red);
			drawFunction(g,graphFunctionBox1.getText().trim() ,Color.green);
			drawFunction(g,graphFunctionBox2.getText().trim() ,Color.blue);

		}catch(Exception e ){
			e.printStackTrace();
			// Equation Provided is Invalid
		}

	}
	public void drawFunction(Graphics g , String function, Color color) {
		function = function.trim();
		if(function.length() == 0) return;

		int brack = 0;
		if(function.charAt(0) == '(')
		for(int i =0 ; i < function.length();i++) {
			if( i == -1) {
				break;
			}
			if(function.charAt(i) == '(') {
				brack++;
			}if(function.charAt(i) == ')') {
				brack--;
			}
			if(brack == 1 &&function.charAt(i)  == ',' ) {
				
				
			}
			if(brack ==0 ) {
				System.out.println("Is point" +function.substring(1, function.length()-1));
				
				
				break;
			}
		}
		Color initColor = g.getColor();
		g.setColor(color);
		
		functionDraw(g, function );	
		
		
		
		
		g.setColor(initColor);

	}
	
	public void drawPoints(String functionA, String functionB , double start , double end , double inc) {
		double x,y;
		Equation temp , temp1;
		
		if(functionA.contains(specialfunctions[0] )||functionB.contains(specialfunctions[0] ))return;
		for( start = start ; start < end ; start++) {
			temp  = new Equation (functionA , start);
			temp1 = new Equation (functionB , start);
			for(Map.Entry<Double, Boolean> entryA : temp.value.entrySet()) {

				for(Map.Entry<Double, Boolean> entryB : temp1.value.entrySet()) {
					
					double y1 =((int)(this.getHeight() - entryB.getKey()*zoomFactor + (int)(yOffset*-zoomFactor)));
					
							
							
							
					break;
				}
				break;
			}
		}
	}
	public void functionDraw(Graphics g , String function) {

		Graphics2D g2 =((Graphics2D) g);
		boolean special = false;
		for(int i = 0 ; i < verSpecial.length; i++) {
			if(function.contains(verSpecial[i])) {
				special = true;
				break;
			}
		}
		if(function.contains("plusminus")) {

			for(int x = 0 ; x < this.getWidth(); x++){
				Equation x2 =new Equation(function,round2Interval((x -xOffset*-zoomFactor   )/zoomFactor ,0.005)) ;
				for(Map.Entry<Double, Boolean> entry : x2.value.entrySet()) {
					double y2 =entry.getKey();

					double y =((int)(this.getHeight() - y2*zoomFactor + (int)(yOffset*-zoomFactor)));
					if( y > 0 && y < this.getHeight() ) {

						g.fillOval(x-2, (int) (y-2), 4, 4);

					}
				}

			}
		}else if(!special ) {

			double py=0;
			for(int x = 0 ; x < this.getWidth(); x++){
				Equation x2 =new Equation(function,round2Interval((x -xOffset*-zoomFactor   )/zoomFactor ,0.005)) ;
				
				for(Map.Entry<Double, Boolean> entry : x2.value.entrySet()) {
					double y2 =entry.getKey();
					if(x==0) {
						py = y2;
					}
					
					
					double y =((int)(this.getHeight() - y2*zoomFactor + (int)(yOffset*-zoomFactor)));
					
					if( y > -100 && y < this.getHeight()+100 ) {
						g2.setStroke(new  BasicStroke(3));
						g2.drawLine(x-1,  (int)py,x,(int) y);
					}
					py=y;
					break;
				}

			}
		}
		else {
			double py=0;
			for(int x = 0 ; x < this.getWidth(); x++){
				Equation x2 =new Equation(function,round2Interval((x -xOffset*-zoomFactor   )/zoomFactor ,0.005)) ;
				
				for(Map.Entry<Double, Boolean> entry : x2.value.entrySet()) {
					double y2 =entry.getKey();
					if(x==0) {
						py = y2;
					}
					
					
					double y =((int)(this.getHeight() - y2*zoomFactor + (int)(yOffset*-zoomFactor)));
					if(Math.abs(py-y)>5) {
						precisedraw(g,function, (double)x, 0.4 );
					}
					
					py=y;
					if( y > 0 && y < this.getHeight() ) {

						g.fillOval(x-2, (int) (y-2), 4, 4);

					}
					break;
				}

			}
		}
	}
	public void precisedraw(Graphics g , String function, double x, double inc ) {
		double end = x+1;
		for( x = x+inc ; x < end; x+=inc){
			Equation x2 =new Equation(function,round2Interval((x -xOffset*-zoomFactor   )/zoomFactor ,0.005)) ;
			for(Map.Entry<Double, Boolean> entry : x2.value.entrySet()) {
				double y2 =entry.getKey();
				
				
				
				double y =((int)(this.getHeight() - y2*zoomFactor + (int)(yOffset*-zoomFactor)));
				if( y > 0 && y < this.getHeight() ) {

					g.fillOval((int) (x-2), (int) (y-2), 4, 4);

				}
			}

		}
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() ==time)
			repaint();
		if(e.getSource()==calcButton)
		{
			CalcGUI s1 = new CalcGUI();
		}
	}

	public static void main(String[]args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException
	{
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		Solution s1 = new Solution();


	}
	public void mousePressed(MouseEvent e) {
	}
	public void mouseReleased(MouseEvent e)
	{
		p1 = null;
	}
	Point p1 = null;
	public void mouseMoved(MouseEvent e)
	{
		mouse = e.getPoint();
	}
	double zoom =0;
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		zoom += e.getPreciseWheelRotation();
		targetZoom =500 *Math.pow(1.1,zoom);
	}
	static String previous = " ";
	public static double lerp(double a, double b, double f) {

		return a + f * (b - a);
	}


	public static double[][] run(double min , double max , double inc) throws IOException {
		double[][] result = new double[2][ (int) ((max - min)/inc )];
		Runtime rt = Runtime.getRuntime();
		Process pr = rt.exec("java Snippet " +  min + " " + max + " " +inc );
		BufferedWriter pr_writer = new BufferedWriter(
				new OutputStreamWriter(pr.getOutputStream()));

		BufferedReader br = new BufferedReader(new InputStreamReader(pr.getInputStream()));
		String ch;
		int data =0;
		for(double i = min ; i <=max ; i+= inc) {

			if ((ch = br.readLine()) != null)
			{
				if(data >= result[0].length)break;
				String [] a = ch.trim().split("\\s");
				result[0][data] = Double.parseDouble(a[0]);
				result[1][data] = Double.parseDouble(a[1]);

				data++;
			}

		}
		br.close();
		pr_writer.close();
		return result;
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

		if(p1 == null) {
			p1 = e.getPoint();

		}
		else {
			xOffset -= (e.getPoint().getX() - p1.getX() )/ zoomFactor;
			yOffset -= (e.getPoint().getY() - p1.getY() )/ zoomFactor;

			p1 = e.getPoint();
		}

	}
	public double expoNeg(double a) {
		a =round2NearestInterval(a,0.0001);
		a = a%1;
		if(a==0 )return 0;
		a = 1/a;
		if(a %2 ==0) {

		}
		return 0;

	}
	DecimalFormat smdf = new DecimalFormat("0.00000");
	public double round2Interval(double n , double interval) {

		n -=n%interval;
		n=  Double.parseDouble(smdf.format(n));
		return n;
	}



	public int getLastDigitinc0(double a) {
		a =round2NearestInterval(a,0.0001);
		String temp =(a+"");
		int c =temp.length()-1;
		while(c>=0) {
			if(temp.charAt(c) == '0' ||temp.charAt(c) == '.' ||temp.charAt(c) == '-' ) {
				c--;
				continue;
			}else {
				return Integer.parseInt(temp.charAt(c) +""); 
			}
		}
		return 0;

	}


	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		System.out.println("e :"+e.getExtendedKeyCode());
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		System.out.println("e :"+e.getExtendedKeyCode());

	}  

}












