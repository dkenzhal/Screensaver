package screensaver;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.Timer;
import javax.swing.event.MouseInputListener;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * The canvas where the screen saver is drawn.
 */
public class Canvas extends JComponent {

	// A list of nodes.  Only one is used in the sample, but there's
	// no reason there couldn't be more.
	private ArrayList<SSNode> nodes = new ArrayList<SSNode>();
	private static int FPS = 20;	// How often we update the animation.
	private Timer timer;			// The timer to actually cause the animation updates.
	private SSNode selectedNode = null;		// Which node is selected; null if none
	

	public boolean isFocusable() {
		return true;
	}
	
	public Canvas() {

		/*
		 * The mouse input listener combines MouseListener and MouseMotionListener.
		 * Still need to add it both ways, though.
		 */
		MouseInputListener mil = new MouseHandler();
		KeyListener kil = new KeyHandler();
		this.addMouseListener(mil);
		this.addMouseMotionListener(mil);
		this.addKeyListener(kil);
		
		this.setOpaque(true);	// we paint every pixel; Java can optimize
	}
	

	/**
	 * Paint this component:  fill in the background and then draw the nodes
	 * recursively.
	 */
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		super.paintComponent(g2);
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, this.getWidth(), this.getHeight());

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		for (SSNode n : nodes) {
			n.paintNode((Graphics2D) g);
		}
	}

	/**
	 * Add a new node to the canvas.
	 */
	public void addNode(SSNode n) {
		this.nodes.add(n);
	}

	/**
	 * Get the node containing the point p.  Return null
	 * if no such node exists.
	 */
	public SSNode getNode(Point2D p) {
		SSNode hit = null;
		int i = 0;
		while (hit == null && i < nodes.size()) {
			hit = nodes.get(i).hitNode(p);
			i++;
		}
		return hit;
	}

	/**
	 * Convert p to a Point2D, which has higher precision.
	 */
	private Point2D.Double p2D(Point p) {
		return new Point2D.Double(p.getX(), p.getY());
	}


	class KeyHandler implements KeyListener {
	 	@Override
	 	public void keyTyped(KeyEvent e) {
	
	 	}
	 	@Override
	 	public void keyPressed(KeyEvent e) {
        		if(selectedNode != null) {
	        		int keyCode = e.getKeyCode();
	        		switch(keyCode) {
	        			case KeyEvent.VK_UP:
	        				selectedNode.up();
	        				break;
	        			case KeyEvent.VK_DOWN:
	        				selectedNode.down();
	        				break;
	        			case KeyEvent.VK_LEFT:
	        				selectedNode.changeColor();
	        				break;
	        			case KeyEvent.VK_RIGHT:
	        				selectedNode.addChild();
	        				break;
	        			
	        		}
	        		repaint();
	        	}
		}
		@Override
		public void keyReleased(KeyEvent e) {
		       
		}	 	
	}
	
	/**
	 * Listen for mouse events on the Canvas.  Pass them on to
	 * the selected node (if there is one) in most cases.
	 */
	class MouseHandler implements MouseInputListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			selectedNode = getNode(p2D(e.getPoint()));
			if (selectedNode != null) {
				selectedNode.mouseClicked(e);
				repaint();
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			selectedNode = getNode(p2D(e.getPoint()));
			if (selectedNode != null) {
				selectedNode.mousePressed(e);
				repaint();
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (selectedNode != null) {
				selectedNode.mouseReleased(e);
				repaint();
			}
		}
		
		
		@Override
		public void mouseEntered(MouseEvent e) {			
			if(timer!=null) timer.stop();
			repaint();
		}

		@Override
		public void mouseExited(MouseEvent e) {
			timer = new Timer(FPS, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					for(SSNode n : nodes) {
						n.tick();
					}
					repaint();
				}
			});

			timer.start();
			 		
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if (selectedNode != null) {
				selectedNode.mouseDragged(e);
				repaint();
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
		}

	}
}