package screensaver;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Represent one Screen Save Node.
 */
public class SSNode implements MouseMotionListener, MouseListener {

	private AffineTransform trans = new AffineTransform();
	private Shape shape;
	private ArrayList<SSNode> children = new ArrayList<SSNode>();
	private SSNode parent = null;
	private String id; // for debugging printf statements
	private Point2D lastPoint = null;
	private Color color = Color.RED;
	private int shapeInt;
	//boolean clickedOnce;
	

	/**
	 * Create a new SSNode, given a shape and a colour.
	 */
	public SSNode(Shape s, Color color) {
		this.id = "id";
		this.shape = s;
		this.color = color;
		this.shapeInt=0;
		//clickedOnce = false;
		
	}

	/**
	 * Set this node's shape to a new shape.
	 */
	public void setShape(Shape s) {
		this.shape = s;
	}

	/**
	 * Add a child node to this node.
	 */
	public void addChild(SSNode child) {
		child.id = this.id + "." + (this.children.size());
		this.children.add(child);
		child.parent = this;
	}

	/**
	 * Is this node the root node? The root node doesn't have a parent.
	 */
	private boolean isRoot() {
		return this.parent == null;
	}

	/**
	 * Get this node's parent node; null if there is no such parent.
	 */
	public SSNode getParent() {
		return this.parent;
	}

	/**
	 * One tick of the animation timer. What should this node do when a unit of
	 * time has passed?
	 */
	public void tick() { ///
		// Because the root node doesn't rotate, it'll be a special case.
		if(!this.isRoot()) { //if not the root
			AffineTransform t = getLocalTransform();
			AffineTransform r = AffineTransform.getRotateInstance(Math.toRadians(1));
			t.preConcatenate(r);
			trans = t;
			for(SSNode c : children) {
				c.tick();
			}
		} else {
			for(SSNode n : children) {
				n.tick();
			}
			//setTranform(t);
		}
	}

	/**
	 * Does this node contain the given point (which is in window coordinates)?
	 */
	public boolean containsPoint(Point2D p) {
		AffineTransform inverseTransform = this.getFullInverseTransform();
		Point2D pPrime = inverseTransform.transform(p, null);

		return this.shape.contains(pPrime);
	}

	/**
	 * Return the node containing the point. If nodes overlap, child nodes take
	 * precedence over parent nodes.
	 */
	public SSNode hitNode(Point2D p) {
		for (SSNode c : this.children) {
			SSNode hit = c.hitNode(p);
			if (hit != null)
				return hit;
		}
		if (this.containsPoint(p)) {
			return this;
		} else {
			return null;
		}
	}

	/**
	 * Transform this node's transformation matrix by concatenating t to it.
	 */
	public void transform(AffineTransform t) {
		this.trans.concatenate(t);
	}

	/**
	 * Convert p to a Point2D.
	 */
	private Point2D.Double p2D(Point p) {
		return new Point2D.Double(p.getX(), p.getY());
	}

	/*************************************************************
	 * 
	 * Handle mouse events directed to this node.
	 * 
	 *************************************************************/

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getClickCount() == 2) {
			if(shapeInt == 0){	
				setShape(Main.star);
				shapeInt=1;
			} else  if(shapeInt == 1) {
				setShape(Main.rectangle);
				shapeInt=2;
			} else if(shapeInt == 2) {
				setShape(Main.circle);
				shapeInt = 3;
			} else if(shapeInt == 3) {
				setShape(Main.heart);
				shapeInt=0;
			}
			//color = getPrevColor(color);
		}
	}
	
	//helper functions:
	public void changeColor() {
			color = getNextColor(color);
	}
	
	public void addChild() {
			SSNode ch = new SSNode(shape, color);
			ch.trans = this.getFullTransform();
			AffineTransform translateBy = AffineTransform.getTranslateInstance(0,-70);
			
			/*double chx, chy;
			if(this.children.size() == 0) {
				chx = trans.getScaleX()/1.5;
				chy = trans.getScaleY()/1.5;
			} else {
				chx = children.get(0).trans.getScaleX();
				chy = children.get(0).trans.getScaleY();
			}
			AffineTransform scaleBy = AffineTransform.getScaleInstance(chx, chy);
			*/
			
			ch.trans = translateBy;
			//ch.transform(scaleBy);
			addChild(ch);
	}
	
	public Color getNextColor(Color c) {
		Color ret = c;
		if(c == Color.RED) {
			ret = Color.GREEN;
		} else if(c == Color.GREEN) {
			ret = Color.BLUE;
		} else if(c == Color.BLUE) {
			ret = Color.RED;
		}
		return ret;
	}
	
	
	@Override
	public void mousePressed(MouseEvent e) {
		this.lastPoint = p2D(e.getPoint());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		this.lastPoint = null;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

    /**
     * Handle mouse drag event, with the assumption that we have already
     * been "selected" as the sprite to interact with.
     * This is a very simple method that only works because we
     * assume that the coordinate system has not been modified
     * by scales or rotations. You will need to modify this method
     * appropriately so it can handle arbitrary transformations.
     */
	@Override
	public void mouseDragged(MouseEvent e) {
		// first, we take care of translation:
		Point2D mouse = p2D(e.getPoint());
		double dx = mouse.getX() - this.lastPoint.getX();
		double dy = mouse.getY() - this.lastPoint.getY();
		
		if(this.isRoot()) {
			this.trans.translate(dx,dy);
		} else {
			dx = mouse.getX();
			dy = mouse.getY();
			Point2D p = new Point2D.Double(dx, dy);
			AffineTransform inv = getFullInverseTransform();
			Point2D pPrime = inv.transform(p,null);
			double x= pPrime.getX();
			double y = pPrime.getY();
			
			AffineTransform prev = trans;
			double theta1 = Math.atan2(prev.getTranslateY(), prev.getTranslateX());
								
			this.trans.translate(x ,y);
			
			//now, we take care of rotation:
			double theta2 = Math.atan2(trans.getTranslateY(), trans.getTranslateX());
			
			double theta = theta2 - theta1;
			
			AffineTransform r = AffineTransform.getRotateInstance(theta);
			this.trans.concatenate(r);
		}
		
		lastPoint = mouse;
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	/**
	 * Paint this node and its children.
	 */
	public void paintNode(Graphics2D g2) {
		/*
		 * You can change this code if you wish. Based on an in-class example
		 * it's going to be really tempting. You are advised, however, not to
		 * change it. Doing so will likely bring you hours of grief and much
		 * frustration.
		 */

		// Remember the transform being used when called
		AffineTransform t = g2.getTransform();

		g2.transform(this.getFullTransform());
		g2.setColor(this.color);
		g2.fill(this.shape);
		// Restore the transform.
		g2.setTransform(t);

		// Paint each child
		for (SSNode c : this.children) {
			c.paintNode(g2);
		}

		// Restore the transform.
		g2.setTransform(t);
	}

	/*
	 * There are a number of ways in which the handling of the transforms could
	 * be optimized. That said, don't bother. It's not the point of the
	 * assignment.
	 */

	/**
	 * Returns our local transform. Copy it just to make sure it doesn't get
	 * messed up.
	 */
	public AffineTransform getLocalTransform() {
		return new AffineTransform(this.trans);
	}

	/**
	 * Returns the full transform to this node from the root.
	 */
	public AffineTransform getFullTransform() {
		// Start with an identity matrix. Concatenate on the left
		// every local transformation matrix from here to the root.
		AffineTransform at = new AffineTransform();
		SSNode curNode = this;
		while (curNode != null) {
			at.preConcatenate(curNode.getLocalTransform());
			curNode = curNode.getParent();
			
			/*if(curNode!=null && curNode.isRoot()) {
				AffineTransform R = AffineTransform.getRotateInstance(Math.toRadians(10));
				at.preConcatenate(R);
			}*/
		}
		return at;
	}

	/**
	 * Return the full inverse transform, starting with the root. That is, get
	 * the full transform from here to the root and then invert it, catching
	 * exceptions (there shouldn't be any).
	 */
	private AffineTransform getFullInverseTransform() {
		try {
			AffineTransform t = this.getFullTransform();
			AffineTransform tp = t.createInverse();
			return tp;
		} catch (NoninvertibleTransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new AffineTransform();
		}
	}
	
	public void up() {
		AffineTransform scale = AffineTransform.getScaleInstance(1.1,1.1);
		
		double x = trans.getTranslateX();
		double y = trans.getTranslateY();
		
		for(SSNode s : children) {
			AffineTransform childtrans = s.getLocalTransform();
			try {
				childtrans.invert();
			} catch (NoninvertibleTransformException e) {
				e.printStackTrace();
			}
			
			childtrans.concatenate(AffineTransform.getScaleInstance(1/1.1, 1/1.1));
			childtrans.concatenate(s.getLocalTransform());
			s.transform(childtrans);
		}
		
		this.trans.concatenate(scale);
		
		double x2 = trans.getTranslateX();
		double y2 = trans.getTranslateY();
		
		AffineTransform inst = AffineTransform.getTranslateInstance(x-x2, y-y2);
		this.transform(inst);

	}
	
	public void down() {
		AffineTransform scale = AffineTransform.getScaleInstance(0.9, 0.9);
		
		double x = trans.getTranslateX();
		double y = trans.getTranslateY();
		
		for(SSNode s : children) {
			AffineTransform childtrans = s.getLocalTransform();
			try {
				childtrans.invert();
			} catch (NoninvertibleTransformException e) {
				e.printStackTrace();
			}
			
			childtrans.concatenate(AffineTransform.getScaleInstance(1/0.9, 1/0.9));
			childtrans.concatenate(s.getLocalTransform());
			s.transform(childtrans);
		}
		
		this.trans.concatenate(scale);
		
		double x2 = trans.getTranslateX();
		double y2 = trans.getTranslateY();
		
		AffineTransform inst = AffineTransform.getTranslateInstance(x2-x, y2-y);
		this.transform(inst);
	}

}
