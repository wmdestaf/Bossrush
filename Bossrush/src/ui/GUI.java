package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D.Double;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import actor.DummyActor;
import actor.PlayerActor;
import engine.Engine;
import engine.Engine.AFFILIATION;
import physics.Vec2;
import ui.GUIUtils.SCREEN_BEHAVIOR;

public class GUI extends JComponent implements KeyListener {

	private static final int MAJOR = 0, MINOR = 2, REVISION = 0;
	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 500; //750; //1200;
    private static final int HEIGHT = 300; //750;
    public static final int UPDATE_INTERVAL = 17;  // in milliseconds

    private static Engine engine;
    
    public static void main(String[] args) {
    	
    	//setup the game
    	engine = new Engine();
    	engine.configureStageSize(900, 500); //(int)(HEIGHT * (5. / 6)));
    	engine.configureScreenSize(WIDTH, HEIGHT);
    	engine.configureScreenBehavior(SCREEN_BEHAVIOR.SCROLL_XY);
    	engine.configureScale(1.5, null);
    	engine.addActor(new PlayerActor(new Vec2(250, 0), new Vec2(50, 50), AFFILIATION.BLUE, "one"));
    	
    	//prey
    	//engine.addActor(new DummyActor(new Vec2(300, 0), new Vec2(50, 100), AFFILIATION.RED,  "two"));
    	
    	engine.addActor(new DummyActor(new Vec2(195,75), new Vec2(150,25), AFFILIATION.TERRAIN, "gnd"));
    	engine.addActor(new DummyActor(new Vec2(95,0), new Vec2(25,150), AFFILIATION.TERRAIN, "gnd2"));
    	engine.addActor(new DummyActor(new Vec2(415,115), new Vec2(25,100), AFFILIATION.TERRAIN, "gnd3"));
    	engine.addActor(new DummyActor(new Vec2(450,115), new Vec2(200,50), AFFILIATION.TERRAIN, "gnd4"));
    	engine.addActor(new DummyActor(new Vec2(205,115), new Vec2(25,125), AFFILIATION.TERRAIN, "gnd4"));
    	
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
        component.setFocusable(true);

        frame.add(component);
        frame.pack();
        frame.setLocationRelativeTo(null);
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
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);
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
}