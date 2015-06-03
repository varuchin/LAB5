package ok;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Stack;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class SwingGUI5 extends JFrame implements ActionListener, TreeSelectionListener {
	private SwingGUI5Model theAppModel;

	private JMenuBar menuBar;
	private JMenu menu;
	private JMenuItem menuItem;
	private JMenuItem menuItemEdit;
	private JMenuItem menuItemDelete;
	private JTree theTree;
	private JPanel panel;
	private JTextArea theTextArea;
	private JLabel forPicture;
	private JTextField text1;
	private JTextField text2;
	private JTextField text3;
	private JTextField text4;
	private JButton insert;
	private JButton match;
	private JButton addPerson;
	private JButton exit;
	private JButton chooseFindCategory;
	private JButton deleteIcon;
	private JButton find;
	private JButton findNext;
	private JDialog d;
	private JDialog additional;
	private JFrame main;
	private JRadioButton nameButton;
	private JRadioButton surnameButton;
	private JRadioButton perNumButton;
	private JRadioButton pictureButton;
	private JFileChooser fc;
	private JButton insertButton;
	private JButton deleteButton;
	private JButton findButton;
	private JButton editButton;

	private JButton changeLookFeelButton;
	private Stack<DefaultMutableTreeNode> stack = null;
	private char array[] = "0123456789".toCharArray();
	private JTextField theTextField;
	private String pathToPicture;
	private ImageIcon picture;
	private int chooser;
	private UIManager.LookAndFeelInfo installedLF[];

	private int current;

	protected Component buildGUI() {
		fc = new JFileChooser();
		picture = new ImageIcon("empty.jpg");
		main = new JFrame();
		menuBar = new JMenuBar();
		menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_1);
		menuBar.add(menu);
		menuItem = new JMenuItem("Open database");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				ActionEvent.CTRL_MASK));
		menuItem.addActionListener(this);
		menu.add(menuItem);

		menuItem = new JMenuItem("Save database");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				ActionEvent.CTRL_MASK));
		menuItem.addActionListener(this);
		menu.add(menuItem);

		menu.addSeparator();

		menuItem = new JMenuItem("Reset");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
				ActionEvent.CTRL_MASK));
		menuItem.addActionListener(this);
		menu.add(menuItem);

		menu.addSeparator();

		menuItem = new JMenuItem("Exit");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4,
				ActionEvent.ALT_MASK));
		menuItem.addActionListener(this);
		menu.add(menuItem);

		
		menu = new JMenu("Operations");
		menu.setMnemonic(KeyEvent.VK_2);
		menuBar.add(menu);

		menuItem = new JMenuItem("Insert New Person");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
		menuItem.addActionListener(this);
		menu.add(menuItem);

		menuItem = new JMenuItem("Find Existed Person");
		menuItem.addActionListener(this);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F,
				ActionEvent.CTRL_MASK));
		menu.add(menuItem);

		menu.addSeparator();

		menuItemEdit = new JMenuItem("Edit Person");
		menuItemEdit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
		menuItemEdit.setEnabled(false);
		menuItemEdit.addActionListener(this);
		menu.add(menuItemEdit);

		menuItemDelete = new JMenuItem("Delete Person");
		menuItemDelete.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_DELETE, 0));
		menuItemDelete.setEnabled(false);
		menuItemDelete.addActionListener(this);
		menu.add(menuItemDelete);

		menu.addSeparator();

		menuItem = new JMenuItem("Change Look & Feel");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0));
		menuItem.addActionListener(this);
		menu.add(menuItem);

		JPanel menupanel = new JPanel();
		menupanel.setLayout(new GridLayout(1, 1));
		menupanel.add(menuBar);
		main.add(menupanel, "North");
		
		theTree = new JTree(theAppModel.buildDefaultTreeStructure());
		theTree.setEditable(true);
		theTree.addTreeSelectionListener(this);
		int mode = TreeSelectionModel.SINGLE_TREE_SELECTION;
		theTree.getSelectionModel().setSelectionMode(mode);

		theTextArea = new JTextArea();
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 2));
		panel.add(new JScrollPane(theTree));

		JPanel panelR = new JPanel();
		panelR.setLayout(new GridLayout(2, 1));
		panelR.add(new JScrollPane(theTextArea));

		forPicture = new JLabel("A place for photo");
		panelR.add(forPicture);

		panel.add(panelR);
		main.add(panel, "Center");

		JPanel panel2 = new JPanel();

		insertButton = new JButton("Insert New Person");
		insertButton.addActionListener(this);

		findButton = new JButton("Find Existed Person");
		findButton.addActionListener(this);

		deleteButton = new JButton("Delete Person");
		deleteButton.addActionListener(this);
		deleteButton.setEnabled(false);

		editButton = new JButton("Edit Person");
		editButton.addActionListener(this);
		editButton.setEnabled(false);

		changeLookFeelButton = new JButton("Change Look & Feel");
		changeLookFeelButton.addActionListener(this);

		panel2.add(insertButton);
		panel2.add(findButton);
		panel2.add(deleteButton);
		panel2.add(editButton);
		panel2.add(changeLookFeelButton);

		JPanel panel3 = new JPanel();
		panel3.setLayout(new GridLayout(1, 1));

		panel3.add(panel2);

		main.add(panel3, "South");
		main.setVisible(true);
		main.setTitle("The list of employees");
		main.setSize(800, 650);
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		return null;
	}
	
	
	public SwingGUI5(SwingGUI5Model appModel) {

		theAppModel = appModel;

		this.buildGUI();

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		installedLF = UIManager.getInstalledLookAndFeels();
		current = 0;
		try {
			UIManager.setLookAndFeel(installedLF[current].getClassName());
		} catch (Exception ex) {
			System.out.println("Exception 1");
		}
	}

	public void actionPerformed(ActionEvent event) {
		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) theTree
				.getLastSelectedPathComponent();


		if (event.getActionCommand().equals("Open database")) {
			try {
				theAppModel.open(new File("data.txt"));

				Enumeration en = theAppModel.theRoot.children();
				while (en.hasMoreElements()) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) en
							.nextElement();
					TreeNode[] nodes = node.getPath();
					theTree.expandPath(new TreePath(nodes));
				}

				JOptionPane.showMessageDialog(theTree, "Opened!");
			} catch (Exception e) {
			}
		}

		if (event.getActionCommand().equals("Save database")) {
			try {
				theAppModel.save();
				JOptionPane.showMessageDialog(theTree, "Saved!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (event.getActionCommand().equals("Change Look & Feel")) {
			current++;
			if (current > installedLF.length - 1) {
				current = 0;
			}

			System.out.println("New Current Look&Feel:" + current);
			System.out.println("New Current Look&Feel Class:"
					+ installedLF[current].getClassName());

			try {
				UIManager.setLookAndFeel(installedLF[current].getClassName());
				SwingUtilities.updateComponentTreeUI(main);
			} catch (Exception ex) {
				System.out.println("exception");
			}
		}
	
		if (event.getActionCommand().equals("Insert New Person")) {

			d = new JDialog(main, "Insertion",
					Dialog.ModalityType.DOCUMENT_MODAL);
			JPanel p1 = new JPanel();
			JLabel l1 = new JLabel();
			JLabel l2 = new JLabel();
			JLabel l3 = new JLabel();
			JLabel l4 = new JLabel();
			l1.setText("Name: ");
			l2.setText("Surname: ");
			l3.setText("№: ");
			l4.setText("Match the picture");

			text1 = new JTextField(20);
			text2 = new JTextField(20);
			text3 = new JTextField(20);
			text4 = new JTextField(20);
			text4.setEditable(false);

			insert = new JButton("Insert Image");
			insert.addActionListener(this);

			deleteIcon = new JButton("Delete Image");
			deleteIcon.addActionListener(this);
			deleteIcon.setEnabled(false);

			addPerson = new JButton("Add Person");
			addPerson.setMnemonic(KeyEvent.VK_ENTER);
			addPerson.addActionListener(this);

			p1.add(l1);
			p1.add(text1);
			p1.add(l2);
			p1.add(text2);
			p1.add(l3);
			p1.add(text3);
			p1.add(l4);
			p1.add(text4);
			p1.add(insert);
			p1.add(deleteIcon);
			p1.add(addPerson);

			d.add(p1);

			d.setSize(250, 300);
			d.setVisible(true);
		}

			if (event.getActionCommand().equals("Add Person")) {

			Calendar cal = new GregorianCalendar();
			String date = new SimpleDateFormat().format(cal.getTime());

			if (text1.getText().isEmpty() || text2.getText().isEmpty()
					|| text3.getText().isEmpty()) {
				JOptionPane.showMessageDialog(d, "Not all fields are filled!");
			} else {
				int counter = 0;
				String temp1 = text1.getText().replaceFirst(
						text1.getText().substring(0, 1),
						text1.getText().substring(0, 1).toUpperCase());
				temp1 = temp1.replace(" ", "");
				String temp2 = text2.getText().replaceFirst(
						text2.getText().substring(0, 1),
						text2.getText().substring(0, 1).toUpperCase());
				temp2 = temp2.replace(" ", "");
				char[] temp3 = text3.getText().toCharArray();

				for (int i = 0; i < temp3.length; i++) {
					for (char number : array) {
						if (temp3[i] == number) {
							counter++;
						}
					}
				}

				if (counter == temp3.length) {
				} else {
					JOptionPane.showMessageDialog(d,
							"The personnel number consists not of numbers");
					return;
				}

				TreePath path = theAppModel.insertPerson(temp1, temp2, text3
						.getText().replace(" ", ""), date, picture);
				if (path != null) {
					theTree.scrollPathToVisible(path);
				}
				d.setVisible(false);
			}
			picture = null;
		}

			if (event.getActionCommand().equals("Find Existed Person")) {
			stack = null;
			d = new JDialog(main, "Find Existed Person",
					Dialog.ModalityType.DOCUMENT_MODAL);

			JLabel l1 = new JLabel();
			JPanel p1 = new JPanel(new GridLayout(0, 1));
			JPanel p2 = new JPanel();
			l1.setText("Choose the ONE parametr: ");

			nameButton = new JRadioButton("Name");
			surnameButton = new JRadioButton("Surname");
			perNumButton = new JRadioButton("Personnel Number");
			pictureButton = new JRadioButton("Picture");
			ButtonGroup group = new ButtonGroup();
			group.add(nameButton);
			group.add(surnameButton);
			group.add(perNumButton);
			group.add(pictureButton);

			chooseFindCategory = new JButton("Find");
			chooseFindCategory.addActionListener(this);

			p1.add(l1);
			p1.add(nameButton);
			p1.add(surnameButton);
			p1.add(perNumButton);
			p1.add(pictureButton);
			p2.add(chooseFindCategory);
			p1.add(p2);

			d.add(p1);
			d.setSize(300, 250);
			d.setVisible(true);

			if (selectedNode == null)
				return;

			if (event.getSource().equals(deleteButton)) {
				if (selectedNode.getParent() != null)
					theAppModel.deletePerson(selectedNode);
				return;
			}
		}
		if (event.getActionCommand().equals("Delete Person")) {

			if (selectedNode == null)
				JOptionPane.showMessageDialog(theTree,
						"The person isn't selected!");
			else if (selectedNode.getAllowsChildren() == false)
				theAppModel.deletePerson(selectedNode);
			else if (selectedNode.getAllowsChildren())
				JOptionPane.showMessageDialog(theTree,
						"This element has persons");
			else
				JOptionPane.showMessageDialog(theTree,
						"The person isn't selected!");
			return;
		}
		if (event.getActionCommand().equals("Edit Person")) {
			if (selectedNode == null)
				JOptionPane.showMessageDialog(theTree,
						"The person isn't selected!");
			else {
				DictionaryEntry entry = (DictionaryEntry) selectedNode
						.getUserObject();
				ImageIcon forDelete = entry.theIcon;

				d = new JDialog(main, "Edition",
						Dialog.ModalityType.DOCUMENT_MODAL);

				JPanel p1 = new JPanel();
				JLabel l1 = new JLabel();
				JLabel l2 = new JLabel();
				JLabel l3 = new JLabel();
				JLabel l4 = new JLabel();
				l1.setText("New name: ");
				l2.setText("New surname: ");
				l3.setText("New №: ");
				l4.setText("New picture");

				text1 = new JTextField(20);
				text1.setText(entry.theName);
				text2 = new JTextField(20);
				text2.setText(entry.theSurname);
				text3 = new JTextField(20);
				text3.setText(entry.personnelNumber);
				text4 = new JTextField(20);
				text4.setText(entry.theIcon.toString());
				text4.setEditable(false);
				text4.setText(pathToPicture);

				deleteIcon = new JButton("Delete Image");
				deleteIcon.addActionListener(this);
				if (text4.getText().isEmpty())
					deleteIcon.setEnabled(false);
				else
					deleteIcon.setEnabled(true);

				match = new JButton("Match new picture");
				match.addActionListener(this);
				exit = new JButton("Edit the person");
				exit.addActionListener(this);

				p1.add(l1);
				p1.add(text1);
				p1.add(l2);
				p1.add(text2);
				p1.add(l3);
				p1.add(text3);
				p1.add(l4);
				p1.add(text4);
				p1.add(match);
				p1.add(deleteIcon);
				p1.add(exit);

				d.add(p1);

				d.setSize(280, 350);
				d.setVisible(true);
			}
		}
		if (event.getActionCommand().equals("Find")) {
			JLabel l1;
			JLabel l2;
			JLabel l3;
			JLabel l4;

			if (nameButton.isSelected())
				chooser = 1;
			if (surnameButton.isSelected())
				chooser = 2;
			if (perNumButton.isSelected())
				chooser = 3;
			if (pictureButton.isSelected())
				chooser = 4;
			additional = new JDialog(d, "The search");
			d.hide();

			JPanel p1 = new JPanel();
			if (chooser == 1) {
				l1 = new JLabel();
				l1.setText("Name: ");
				text1 = new JTextField(20);
				p1.add(l1);
				p1.add(text1);
			}

			if (chooser == 2) {
				l2 = new JLabel();
				l2.setText("Surname: ");
				text2 = new JTextField(20);
				p1.add(l2);
				p1.add(text2);
			}

			if (chooser == 3) {
				l3 = new JLabel();
				l3.setText("№: ");
				text3 = new JTextField(20);
				p1.add(l3);
				p1.add(text3);
			}

			if (chooser == 4) {
				insert = new JButton("Insert Image");
				insert.addActionListener(this);
				deleteIcon = new JButton("Delete Image");
				deleteIcon.addActionListener(this);
				deleteIcon.setEnabled(false);

				l4 = new JLabel();
				l4.setText("Picture: ");

				text4 = new JTextField(20);
				text4.setEditable(false);
				p1.add(l4);
				p1.add(text4);
				p1.add(insert);
				p1.add(deleteIcon);
			}

			find = new JButton("Find Person");
			find.addActionListener(this);
			findNext = new JButton("Find next");
			findNext.addActionListener(this);
			findNext.setEnabled(false);
			p1.add(find);
			p1.add(findNext);
			if (chooser == 0) {
				JOptionPane.showMessageDialog(null,
						"No any parametr was selected");
				return;
			} else {
				additional.add(p1);
				additional.setSize(250, 300);
				additional.setVisible(true);
			}
		}

		if (event.getActionCommand().equals("Find Person")) {

			if (chooser == 1) {
				if (text1.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Nothing is entered!");
					return;
				} else
					stack = theAppModel.findPerson(text1.getText(), chooser);
			} else if (chooser == 2)
				stack = theAppModel.findPerson(text2.getText(), chooser);
			else if (chooser == 3)
				stack = theAppModel.findPerson(text3.getText(), chooser);
			else if (chooser == 4)
				stack = theAppModel.findPerson(text4.getText(), chooser);

			try {
				if (stack.size() > 1) {
					findNext.setEnabled(true);
					chooser = 1;
					TreePath forPath = new TreePath(stack.elementAt(0)
							.getPath());
					theTree.setSelectionPath(forPath);
				} else if (stack == null) {
					additional.dispose();
					main.setVisible(true);
				} else {
					TreePath forPath = new TreePath(stack.elementAt(0)
							.getPath());
					theTree.setSelectionPath(forPath);
					JOptionPane.showMessageDialog(null, "The search is over");
					additional.dispose();
					main.setVisible(true);
				}
			} catch (Exception e) {

			}

		}
		if (event.getActionCommand().equals("Find next")) {
			if (stack.size() > chooser) {
				TreePath forPath = new TreePath(stack.elementAt(chooser)
						.getPath());
				theTree.setSelectionPath(forPath);
				chooser++;
			} else {
				findNext.setEnabled(false);
				chooser = 0;
				JOptionPane.showMessageDialog(null, "The search is over");
				additional.dispose();
				main.setVisible(true);
			}
		}
		if (event.getActionCommand().equals("Edit the person")) {
			Calendar cal = new GregorianCalendar();
			String date = new SimpleDateFormat().format(cal.getTime());

			DictionaryEntry content = (DictionaryEntry) selectedNode
					.getUserObject();
			DictionaryEntry temp = new DictionaryEntry(null, null, null, date,
					null);

			if (text1.getText().isEmpty())
				temp.theName = content.theName;
			else
				temp.theName = text1.getText();

			if (text2.getText().isEmpty())
				temp.theSurname = content.theSurname;
			else
				temp.theSurname = text2.getText();

			if (text3.getText().isEmpty())
				temp.personnelNumber = content.personnelNumber;
			else
				temp.personnelNumber = text3.getText();

			if (text4.getText().isEmpty())
				temp.theIcon = null;
			else
				temp.theIcon = picture;

			theAppModel.deletePerson(selectedNode);

			TreePath path = theAppModel.insertPerson(temp.theName,
					temp.theSurname, temp.personnelNumber, date, temp.theIcon);
			if (path != null) {
				theTree.scrollPathToVisible(path);
			}

			theTextArea.setText(temp.getContent());

			d.hide();
			picture = null;
		}
		if (event.getActionCommand().equals("Insert Image")
				|| event.getActionCommand().equals("Match new picture")) {
			int returnVal = fc.showOpenDialog(d);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				pathToPicture = fc.getSelectedFile().getAbsolutePath();
				picture = new ImageIcon(pathToPicture);
				deleteIcon.setEnabled(true);
				text4.setText(pathToPicture);
			}
		}

		if (event.getActionCommand().equals("Delete Image")
				|| event.getActionCommand().equals("Delete Existed Image")) {
			text4.setText(null);
			picture = null;
			deleteIcon.setEnabled(false);
		}

		if (event.getActionCommand().equals("Reset")) {
			main.dispose();
			SwingGUI5Model theAppModel = new SwingGUI5Model();
			SwingGUI5 theFrame = new SwingGUI5(theAppModel);
		}
		if (event.getActionCommand().equals("Exit")) {
			System.exit(0);
		}

	}

	public void valueChanged(TreeSelectionEvent event) {
		System.out.println("valueChanged");
		TreePath path = theTree.getSelectionPath();
		if (path == null)
			return;

		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path
				.getLastPathComponent();

		if (selectedNode != null) {
			
			if (selectedNode.getAllowsChildren()) {
				theTextArea.setText(selectedNode.toString());
				forPicture.setIcon(null);
				editButton.setEnabled(false);
				deleteButton.setEnabled(false);
				menuItemEdit.setEnabled(false);
				menuItemDelete.setEnabled(false);
			} else {
				
				DictionaryEntry content = (DictionaryEntry) selectedNode
						.getUserObject();
				theTextArea.setText(content.getContent());
				forPicture.setIcon(content.getPicture());
				editButton.setEnabled(true);
				deleteButton.setEnabled(true);
				menuItemEdit.setEnabled(true);
				menuItemDelete.setEnabled(true);
			}
		}

	}

}