package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class Main extends JFrame {

	private static final long serialVersionUID = 1635339098293442318L;
	private JButton button1;
	private JLabel label1;
	private JPanel panel1;
	private JTextField task;
	private JTextArea tasks;
	private JButton button2;
	private static Date date;
	private JButton confirmButton;
	private JLabel soundLabel;

	
	Xml1 xml = new Xml1();

	// Components of clock placed in a
	//label
	private JLabel clockLabel;
	private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
	private static LocalDateTime now;
	private static String time;


	AudioInputStream audio;
	private static Clip clip;
	private static TreeMap<Integer, String> textTree = new TreeMap<Integer, String>();
	StringBuilder sb = new StringBuilder();
	TreeMap<Integer, TimerTask> taskTree = new TreeMap<Integer, TimerTask>();
	private static TreeMap<Integer, SoundEffect> soundTree = new TreeMap<Integer, SoundEffect>();
	private static SoundEffect se;
	private static URL alarmMusic;
	Timer last;
	TimerTask lastTask;
	private int count = 0;
	Timer timer;
	private static int keyTime;
	// Files in which xml data about txt of tasks is stored
	File fileTxt = new File("Tasktxt.xml");
	File fileTask = new File("Task.xml");
	JComboBox box;
	

	public Main() {

	}

	public Main(Clip clip) {

		super("Simple task manager v.1.0");
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(new Dimension(700, 500));
		// Setting icon
		URL iconURL = getClass().getResource("/main/clock.png");
		ImageIcon icon = new ImageIcon(iconURL);
		setIconImage(icon.getImage());
		tasks = new JTextArea(30, 30);
		button1 = new JButton("         Add          ");
		button2 = new JButton("      Remove     ");
		confirmButton = new JButton("Confirm alarm");
		clockLabel = new JLabel();
		getClock(clockLabel, dtf);
		label1 = new JLabel("Input task you plan to do : ");
		label1.setForeground(Color.BLUE);
		task = new JTextField(15);
		panel1 = new JPanel(new GridBagLayout());
		// Adding JComboBox enabling user to choose Sound of alarm
		String[]nameOfSounds = {"       Sound1       ", "       Sound2       ", "       Sound3       "};
		box = new JComboBox(nameOfSounds);
		box.setSelectedIndex(0);
		soundLabel = new JLabel("Choose sound of alarm");
		soundLabel.setForeground(Color.BLUE);
		
	

		// READING OLD TASKS AND THEIR TEXT WHEN STARTING THE APP
		try {
			textTree = xml.readTasksTxt(fileTxt);
			taskTree = xml.readTasks(fileTask);

			for (Integer key : textTree.keySet()) {
				String value = textTree.get(key);
				tasks.append(value + "\n");
			}
		}

		catch (IOException e2) {
			e2.printStackTrace();
		}

		
		
		// ADDING A TASK AND ITS TEXT
		button1.addActionListener(new ActionListener() {

			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == button1) {

					
					// Set date of event by inputing in input dialog
					date = new Date(JOptionPane.showInputDialog("Input date in format MM/dd/yyyy HH:mm"));
					int time = (int) -(date.getTime());
					count++;
					SimpleDateFormat newFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm");
					String formatedDate = newFormat.format(date);
					textTree.put(time,  "Task no." + count + ": " + task.getText()+ "                  " + formatedDate );
					
					
					//Choose sound
					String path;
					if(box.getSelectedIndex()==0) {
						path = "/main/sound1.wav";
					}
					else if(box.getSelectedIndex()==1)
						path = "/main/sound2.wav";
					
					else {
						path="/main/sound2.wav";
					}
					
					setSound(path);													
					soundTree.put(keyTime, se);
				
					
					
					//Set a timer
					Timer timer = new Timer();
					taskTree.put(time, new TimerTask() {
						
						public void run() {
			
							playSound(se, time, textTree);		
						
						}});
					
					timer.schedule(taskTree.get(time), date);
					
					
		
					// Clear the text area
					tasks.setText("");
					
					// Display new order of events
					for (Integer key : textTree.keySet()) {
						String value = textTree.get(key);
						tasks.append(value +  "\n");
					}
					

					// Saving tasks and their txt to xml
					xml.saveTasks(taskTree, fileTask);
					xml.saveTxt(textTree, fileTxt);

				}
			}

		});
		
		
		
		
		
		
		// REMOVING LAST TASK AND ITS TEXT
		button2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == button2) {

					// Remove the soonest task text
					textTree.remove(textTree.lastKey());
					xml.saveTxt(textTree, fileTxt);

					// Clear text area
					tasks.setText("");

					// Text area will display new set of tasks(without the last that was deleted in
					// order oldest to soonest
					for (Integer key : textTree.keySet()) {

						String value = textTree.get(key);
						tasks.append(value + "\n");
					}
					// Remove the last task
					taskTree.get(taskTree.lastKey()).cancel();
					taskTree.remove(taskTree.lastKey());
					xml.saveTasks(taskTree, fileTask);
					// Save changed tasks

				}

			}

		});

		
		//CONFIRMIG THE ALARM
		confirmButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == confirmButton) {

					soundTree.get(soundTree.lastKey()).close();
					soundTree.remove(soundTree.lastKey());

				}

			}

		});

		
		
		
		
		//SETTING LAYOUT
		GridBagConstraints c = new GridBagConstraints();

		c.weightx = 0.5;
		c.weighty = 0.5;

		c.anchor = GridBagConstraints.NORTH;
		c.gridx = 0;
		c.gridy = 0;
		panel1.add(label1, c);

		c.weighty = 10;
		c.anchor = GridBagConstraints.NORTH;
		c.gridx = 0;
		c.gridy = 3;
		panel1.add(task, c);

		c.weighty = 10;
		c.anchor = GridBagConstraints.NORTH;
		c.gridx = 0;
		c.gridy = 38;
		panel1.add(button1, c);

		c.anchor = GridBagConstraints.NORTH;
		c.weighty = 10;
		c.gridx = 0;
		c.gridy = 39;
		panel1.add(button2, c);

		c.weighty = 1000;
		c.anchor = GridBagConstraints.NORTH;
		c.gridx = 0;
		c.gridy = 40;
		panel1.add(confirmButton, c);
		
		
		c.anchor = GridBagConstraints.SOUTH;
		c.gridx = 0;
		c.gridy = 41;
		panel1.add(soundLabel, c);
		
		// Placing JComboBox
		c.anchor = GridBagConstraints.NORTH;
		c.gridx = 0;
		c.gridy = 42;
		panel1.add(box, c);
		
		

		c.anchor = GridBagConstraints.SOUTH;
		c.gridx = 0;
		c.gridy = 43;
		panel1.add(clockLabel, c);

		add(panel1, BorderLayout.WEST);

		add(tasks, BorderLayout.CENTER);

		setVisible(true);

	}
	
	
	
	

	private void getClock(JLabel label, DateTimeFormatter dtf) {

		Timer Timer = new Timer();

		TimerTask tim = new TimerTask() {

			@Override
			public void run() {

				Font f = new Font("Arial", Font.PLAIN, 20);
				label.setFont(f);
				label.setForeground(Color.BLUE);
				LocalDateTime now = LocalDateTime.now();
				String time = dtf.format(now);
				label.setText(time);

			}
		};
		Timer.schedule(tim, 1000, 1000);

	}

	private void playSound(SoundEffect se, int time, TreeMap<Integer, String>tTree) {

		se.play();
		JOptionPane.showMessageDialog(null, tTree.get(time));

	}
	
	private void setSound(String name) {
		
		se = new SoundEffect();
		alarmMusic = Main.class.getResource(name);
		se.setURL(alarmMusic);
	
		
	}

	public static void main(String[] args) throws InterruptedException {

		Main main = new Main(clip);
	}

}
