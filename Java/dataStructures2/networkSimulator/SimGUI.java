package project5;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;

import edu.uci.ics.jung.algorithms.generators.random.ErdosRenyiGenerator;
import edu.uci.ics.jung.algorithms.generators.random.ErdosRenyiGeneratorDirected;

import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;

import java.util.*;

import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.FlowLayout;

import java.awt.geom.Ellipse2D;
import java.awt.Rectangle;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.ToolTipManager;
import javax.swing.JOptionPane;

/**
 * GUI for the simulation.
 * 
 * @author Katherine (Raven) Russell
 */
class SimGUI {
	/**
	 * Frame for the GUI.
	 */
	private JFrame frame;

	/**
	 * Current simulation.
	 */
	private ThreeTenNetwork alg = null;

	/**
	 * The panel containing the graph display.
	 */
	private Network graph = null;

	/**
	 * The panel containing the graph display.
	 */
	private VisualizationViewer<Host, Connection> visServer = null;

	/**
	 * Editing model for mouse.
	 */
	private EditingModalGraphMouse<Host, Connection> gm;

	/**
	 * The panel containing the step, reset, and play buttons.
	 */
	private JPanel buttonPanel = null;

	/**
	 * Whether or not we are currently routing.
	 */
	private boolean routing = false;

	/**
	 * Whether or not a simulation is currently playing with the play button
	 * (i.e. automatically playing).
	 */
	private boolean playing = false;

	/**
	 * The seed to use for the random number generator associated with the
	 * algorithm simulation.
	 */
	private final Random rand;

	/**
	 * The probability of an edge existing between two nodes.
	 */
	private final double prob;

	/**
	 * The number of nodes in the simulation.
	 */
	private final int numNodes;

	/**
	 * Load up the GUI.
	 * 
	 * @param numNodes
	 *            the number of nodes for the simulation
	 * @param prob
	 *            the probability of a connection between two nodes
	 * @param seed
	 *            seed for the random number generator in Alg
	 */
	public SimGUI(int numNodes, double prob, int seed) {
		this.rand = new Random(seed);
		this.numNodes = numNodes;
		this.prob = prob;

		frame = new JFrame("Algorithm Simulation");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600, 700);
		frame.getContentPane().setLayout(new FlowLayout());

		resetAlg();
		makeMenu(); // needs to go after so gm is set

		frame.setVisible(true);
	}

	/**
	 * Makes the menu for the simulation.
	 */
	public void makeMenu() {
		frame.setJMenuBar(null);
		JMenuBar menuBar = new JMenuBar();

		// exit option
		JMenu simMenu = new JMenu("Simulation");
		simMenu.setPreferredSize(new Dimension(80, 20)); // Change the size

		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});
		simMenu.add(exit);
		menuBar.add(simMenu);

		// graph editing options
		JMenu modeMenu = gm.getModeMenu();
		modeMenu.setText("Mode");
		modeMenu.setIcon(null); // I'm using this in a main menu
		modeMenu.setPreferredSize(new Dimension(50, 20)); // Change the size
		menuBar.add(modeMenu);

		frame.setJMenuBar(menuBar);
	}

	/**
	 * Makes the graph components.
	 */
	public void makeGraphPanel() {
		if (alg == null)
			return;
		if (visServer != null)
			frame.remove(visServer);

		// Layout (ISOMLayout also looks good)
		Layout<Host, Connection> layout = new KKLayout<>(graph);
		// Layout<Host, Connection> layout = new ISOMLayout<>(graph);
		layout.setSize(new Dimension(600, 600));
		visServer = new VisualizationViewer<Host, Connection>(layout);
		visServer.setPreferredSize(new Dimension(600, 600));

		visServer.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
		RenderContext<Host, Connection> context = visServer.getRenderContext();

		// label edges with toString()
		context.setEdgeLabelTransformer(new Transformer<Connection, String>() {
			/**
			 * Transforms the connection into a string.
			 * 
			 * @param e
			 *            the connection to print
			 * @return string representation
			 */
			public String transform(Connection e) {
				return e.toString();
			}
		});

		// color arrows with edge color
		context.setArrowFillPaintTransformer(new Transformer<Connection, Paint>() {
			/**
			 * Transforms the connection into a color.
			 * 
			 * @param e
			 *            the connection to print
			 * @return color representation
			 */
			public Paint transform(Connection e) {
				return e.getColor();
			}
		});

		// color lines with edge color
		context.setEdgeDrawPaintTransformer(new Transformer<Connection, Paint>() {
			/**
			 * Transforms the connection into a color.
			 * 
			 * @param e
			 *            the connection to print
			 * @return color representation
			 */
			public Paint transform(Connection e) {
				return e.getColor();
			}
		});

		// set edge line stroke to bolder
		context.setEdgeStrokeTransformer(new Transformer<Connection, Stroke>() {
			/**
			 * Transforms the connection into a line.
			 * 
			 * @param e
			 *            the connection to print
			 * @return line representation
			 */
			public Stroke transform(Connection e) {
				return new BasicStroke(2);
			}
		});

		// move edge labels off the lines
		context.setLabelOffset(-5);

		// make nodes bigger
		context.setVertexShapeTransformer(new Transformer<Host, Shape>() {
			/**
			 * Transforms the host into a shape.
			 * 
			 * @param v
			 *            the host to print
			 * @return shape representation
			 */
			public Shape transform(Host v) {
				// int s = 30;
				// return new Ellipse2D.Double(-s/2.0, -s/2.0, s, s);
				int w = 80;
				int h = 20;
				return new Rectangle(-w / 2, -h / 2, w, h);
			}
		});

		// label vertices with toString()
		context.setVertexLabelTransformer(new Transformer<Host, String>() {
			/**
			 * Transforms the host into a string.
			 * 
			 * @param v
			 *            the host to transform
			 * @return string representation
			 */
			public String transform(Host v) {
				return v.toString();
				// return v.toString() + "\n" + v.getRoutingTable().toString();
			}
		});

		// color vertices with node color
		context.setVertexFillPaintTransformer(new Transformer<Host, Paint>() {
			/**
			 * Transforms the host into a color.
			 * 
			 * @param v
			 *            the host to transform
			 * @return color representation
			 */
			public Paint transform(Host v) {
				return v.getColor();
			}
		});

		// deal with tooltips...
		ToolTipManager.sharedInstance().setDismissDelay(60000);
		visServer.setVertexToolTipTransformer(new Transformer<Host, String>() {
			/**
			 * Transforms the host into a string for a tooltip.
			 * 
			 * @param v
			 *            the host to transform
			 * @return string representation
			 */
			public String transform(Host v) {
				return "<html>" + v.toString() + "\n" + v.getRoutingTable().toString() + "</html>";
			}
		});

		// Add user interactions
		gm = new EditingModalGraphMouse<>(context, Host.getFactory(), Connection.getFactory());
		gm.setMode(ModalGraphMouse.Mode.EDITING);
		visServer.setGraphMouse(gm);

		frame.add(visServer, 0);
		frame.revalidate();
	}

	/**
	 * Makes the panel containing the step, reset, and play buttons.
	 */
	public void makeBottomButtons() {
		if (alg == null)
			return;
		if (buttonPanel != null)
			frame.remove(buttonPanel);

		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 2));

		// play button
		JButton play = new JButton("Route");
		play.addActionListener(new ActionListener() {
			/**
			 * Toggles routing on and off.
			 */
			private void toggle() {
				// toggle routing and not routing
				routing = !routing;
				buttonPanel.getComponent(0).setEnabled(!routing);
				buttonPanel.getComponent(1).setEnabled(!routing);
				buttonPanel.getComponent(2).setEnabled(!routing);
			}

			/**
			 * Handler for a button click.
			 * 
			 * @param event
			 *            the event that occurred
			 */
			public void actionPerformed(ActionEvent event) {
				toggle();
				if (routing) {
					start();
					toggle();
				}
			}
		});
		buttonPanel.add(play);

		// send button
		JButton send = new JButton("Send");
		send.addActionListener(new ActionListener() {
			/**
			 * Toggle playing on and off.
			 */
			private void toggle() {
				// toggle playing and not playing
				playing = !playing;
				buttonPanel.getComponent(0).setEnabled(!playing);
				buttonPanel.getComponent(2).setEnabled(!playing);
				((JButton) buttonPanel.getComponent(1)).setText((playing ? "Stop" : "Send"));
			}

			/**
			 * Handler for a button click.
			 * 
			 * @param event
			 *            the event that occurred
			 */
			public void actionPerformed(ActionEvent event) {
				toggle();

				// if playing, kick off a timer
				if (playing) {
					final Host source = (Host) JOptionPane.showInputDialog(frame, "Send from which host?",
							"Send from...", JOptionPane.PLAIN_MESSAGE, null, graph.getVertices().toArray(), null);

					if (source == null) {
						toggle();
						return;
					}

					final Host dest = (Host) JOptionPane.showInputDialog(frame, "Send to which host?", "Send to...",
							JOptionPane.PLAIN_MESSAGE, null, graph.getVertices().toArray(), null);

					if (dest == null) {
						toggle();
						return;
					}

					try {
						Thread.sleep(750);
					} catch (Exception e) {
					}
					if (!step(source, dest)) {
						toggle();
						return;
					} else {
						new javax.swing.Timer(1000, new ActionListener() {
							/**
							 * Handler for a button click.
							 * 
							 * @param event
							 *            the event that occurred
							 */
							public void actionPerformed(ActionEvent event) {
								// someone hit the stop button
								if (!playing) {
									((javax.swing.Timer) event.getSource()).stop();
									return;
								} else {
									if (!step(dest))
										toggle();
								}
							}
						}).start();
					}
				}
			}
		});
		send.setEnabled(false);
		buttonPanel.add(send);

		// reset button
		JButton reset = new JButton("Reset");
		reset.addActionListener(new ActionListener() {
			/**
			 * Handler for a button click.
			 * 
			 * @param event
			 *            the event that occurred
			 */
			public void actionPerformed(ActionEvent event) {
				buttonPanel.getComponent(1).setEnabled(false);
				resetAlg();
			}
		});
		buttonPanel.add(reset);

		frame.add(buttonPanel, 1);
		frame.revalidate();
	}

	/**
	 * Calls the step button on the simulation and updates the GUI to display
	 * the result.
	 * 
	 * @param source
	 *            the source for the message
	 * @param dest
	 *            the destination for the message
	 * @return whether or not the simulation was able to step
	 */
	public boolean step(Host source, Host dest) {
		boolean ret = alg.step(source, dest);
		visServer.repaint();
		return ret;
	}

	/**
	 * Calls the step button on the simulation and updates the GUI to display
	 * the result.
	 * 
	 * @param dest
	 *            the destination for the message
	 * @return whether or not the simulation was able to step
	 */
	public boolean step(Host dest) {
		boolean ret = alg.step(dest);
		visServer.repaint();
		return ret;
	}

	/**
	 * Starts the simulation.
	 */
	public void start() {
		alg.start();
		visServer.repaint();
	}

	/**
	 * Generates a new graph.
	 */
	public void genGraph() {
		int nodeCount = 0;
		int edgeCount = 0;

		Factory<Host> nodeFactory = Host.getFactory();
		Factory<Connection> edgeFactory = Connection.getFactory();

		ErdosRenyiGeneratorDirected<Host, Connection> gen = new ErdosRenyiGeneratorDirected<Host, Connection>(
				Network.getFactory(), nodeFactory, edgeFactory, this.numNodes, this.prob);
		gen.setSeed(this.rand.nextInt());
		graph = (Network) gen.create();
	}

	/**
	 * Load a new simulation.
	 */
	public void resetAlg() {
		if (alg == null)
			alg = new ThreeTenNetwork();

		Host.LAST_ID = -1;
		Connection.LAST_ID = -1;
		genGraph();
		alg.reset(graph);

		makeGraphPanel();
		makeMenu();
		makeBottomButtons();
	}

	/**
	 * A main method to run the simulation with GUI.
	 * 
	 * @param args
	 *            [0] = the seed for the alg's random number generator
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			new SimGUI(6, 0.4, 0);
		} else if (args.length == 1) {
			new SimGUI(Integer.parseInt(args[0]), 0.4, 0);
		} else if (args.length == 2) {
			new SimGUI(Integer.parseInt(args[0]), Double.parseDouble(args[1]), 0);
		} else if (args.length == 3) {
			new SimGUI(Integer.parseInt(args[0]), Double.parseDouble(args[1]), Integer.parseInt(args[2]));
		} else {
			System.out.println(
					"Call with one of the following:\njava SIMGui\njava SIMGui [numNodes]\njava SIMGui [numNodes] [connectProb]\njava SIMGui [numNodes] [connectProb] [seed]");
		}
	}
}