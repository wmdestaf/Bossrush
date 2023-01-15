package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D.Double;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import actor.DummyActor;
import actor.MovingPlatformActor;
import actor.PlayerActor;
import engine.Engine;
import engine.Engine.AFFILIATION;
import physics.ParametricFactory;
import physics.Vec2;
import ui.GUIUtils.SCREEN_BEHAVIOR;

public class GUI extends JComponent implements KeyListener, MouseWheelListener {

	private static final int MAJOR = 0, MINOR = 4, REVISION = 0;
	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 500 ; //500; 750; //1200;
    private static final int HEIGHT = 300; //300; //450; //750;
    public static final int UPDATE_INTERVAL = 17;  // in milliseconds

    public static final Vec2 INITIAL_SCALE = new Vec2(0.7, 0.7);
    private static Engine engine;
    
    public static void main(String[] args) {
    	
    	//setup the game
     	engine = new Engine();
    	engine.configureStageSize(1000,1000);
    	engine.configureScreenSize(WIDTH, HEIGHT);
    	engine.configureMinimapEnabled(false);
    	engine.configureScreenBehavior(SCREEN_BEHAVIOR.SCROLL_XY);
    	engine.addActor(new PlayerActor(new Vec2(0, 0), new Vec2(50, 50), AFFILIATION.BLUE, "guy"));

    	//bottom 2 + connector
    	engine.addActor(new DummyActor(new Vec2(125,125), new Vec2(75,75), AFFILIATION.TERRAIN, ""));
    	engine.addActor(new DummyActor(new Vec2(750,125), new Vec2(75,75), AFFILIATION.TERRAIN, ""));
    	
    	
    	engine.addActor(
    		new MovingPlatformActor(new Vec2(300,175), new Vec2(75,25), AFFILIATION.TERRAIN, "terrain",
    			ParametricFactory.createOscillation(new Vec2(300,175), new Vec2(575,175), 300, false)
    		)
    	);

    	//top 2 + connector
    	engine.addActor(new DummyActor(new Vec2(125,750), new Vec2(75,75), AFFILIATION.TERRAIN, ""));
    	engine.addActor(new DummyActor(new Vec2(750,750), new Vec2(75,75), AFFILIATION.TERRAIN, ""));
    	engine.addActor(
    		new MovingPlatformActor(new Vec2(200,725), new Vec2(75,25), AFFILIATION.TERRAIN, "",
    			ParametricFactory.createOscillation(new Vec2(200,800), new Vec2(675,800), 300, false)
    		)
    	);
    	
    	engine.addActor(
    		new MovingPlatformActor(new Vec2(200,0), new Vec2(75,75), AFFILIATION.TERRAIN, "",
    			ParametricFactory.createOscillation(new Vec2(200,0), new Vec2(675,0), 300, false)
    		)
    	);
    	
    	
    	//vertical connectors
    	engine.addActor(
    		new MovingPlatformActor(new Vec2(50,175), new Vec2(75,25), AFFILIATION.TERRAIN, "",
    			ParametricFactory.createOscillation(new Vec2(50,175), new Vec2(50,800), 300, false)
    		)
    	);
    	engine.addActor(
    		new MovingPlatformActor(new Vec2(825,175), new Vec2(75,25), AFFILIATION.TERRAIN, "",
    			ParametricFactory.createOscillation(new Vec2(825,175), new Vec2(825,800), 300, false)
    		)
    	);
    	
    	//circles
    	engine.addActor(
			new MovingPlatformActor(new Vec2(825,175), new Vec2(75,25), AFFILIATION.TERRAIN, "",
	    		ParametricFactory.createCircle(new Vec2(500,500), 200, 0, 300)
	    	)	
    	);
    	engine.addActor(
			new MovingPlatformActor(new Vec2(825,175), new Vec2(75,25), AFFILIATION.TERRAIN, "",
	    		ParametricFactory.createCircle(new Vec2(500,500), 200, Math.PI / 2, 300)
	    	)	
    	);
    	engine.addActor(
			new MovingPlatformActor(new Vec2(825,175), new Vec2(75,25), AFFILIATION.TERRAIN, "",
	    		ParametricFactory.createCircle(new Vec2(500,500), 200, Math.PI, 300)
	    	)	
    	);
    	engine.addActor(
			new MovingPlatformActor(new Vec2(825,175), new Vec2(75,25), AFFILIATION.TERRAIN, "",
	    		ParametricFactory.createCircle(new Vec2(500,500), 200, 3 * Math.PI / 2, 300)
	    	)	
    	);
    	
    	//centrico
    	engine.addActor(new DummyActor(new Vec2(450,475), new Vec2(100, 50), AFFILIATION.TERRAIN, ""));
    	
    	Runtime.getRuntime().addShutdownHook(new Thread() {
    		public void run() {
    			System.out.println("goodbye");
  	      	}
	    });
  	
    	
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {    	
        JFrame frame = new JFrame(String.format("Bossrush v%d.%d.%d", MAJOR, MINOR, REVISION));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JComponent component = new GUI();
        component.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        component.addKeyListener((KeyListener) component);
        component.addMouseWheelListener((MouseWheelListener)component);
        component.setFocusable(true);
        component.addComponentListener(new ComponentAdapter() {
        	
	        @Override
	        public void componentResized(ComponentEvent e) {
	        	Vec2 newSize  = new Vec2(e.getComponent().getSize());
	        	Vec2 newScale = new Vec2(
	        			INITIAL_SCALE.getX() * newSize.getX() / WIDTH, 
	        			INITIAL_SCALE.getY() * newSize.getY() / HEIGHT
	        	);
	        	engine.configureScale(newScale.getX(), newScale.getY());
	        	engine.configureScreenSize(newSize.getXi(), newSize.getYi());
	        }
	        
        });

        
        frame.add(component);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
        frame.setVisible(true);
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    
                	engine.tick();
                	component.repaint();
                    
                    try {
                        Thread.sleep(UPDATE_INTERVAL);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        engine.render(g);
    }
    
    @Override
	public void keyPressed(KeyEvent e) {
    	
    	engine.onKeyPress(e);
    	
    	switch(e.getKeyCode()) {
    		case KeyEvent.VK_ESCAPE: System.exit(0);
    	}
    }
    
    @Override
	public void keyReleased(KeyEvent e) {
    	engine.onKeyRelease(e);
    }

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		engine.changeZoom(e.getWheelRotation());
	}
}