
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Stack;

import javax.swing.*;
import javax.swing.tree.*;

// ------------------------------------------------------------------
class SwingGUI5Model {

	private DefaultTreeModel theModel;
	private static String alphabet = new String("ABCDEFGIJKLMNOPQRSTUVWXYZ");
	DefaultMutableTreeNode theRoot;
	private String[] AA_AM = alphabet.substring(0, 13).split("");
	private String[] AN_AZ = alphabet.substring(13).split("");
	static PrintWriter os;
	static FileInputStream fis;
	static DataInputStream dis;

	public SwingGUI5Model() {

	}

	public Stack<DefaultMutableTreeNode> findPerson(String parametr, int chooser) {
		DictionaryAnchor anchor = new DictionaryAnchor();
		Stack<DefaultMutableTreeNode> stack = new Stack<DefaultMutableTreeNode>();
		anchor.topic = null;
		anchor.entry = null;

		if (chooser == 1) {
			DictionaryEntry new_entry = new DictionaryEntry(parametr, null,
					null, null, null);

			if (this.findName(new_entry, anchor, stack)) {
			} else {
				stack = null;
				JOptionPane.showMessageDialog(null, "Not found!");
			}
		}

		else if (chooser == 2) {
			DictionaryEntry new_entry = new DictionaryEntry(null, parametr,
					null, null, null);

			if (this.findSurname(new_entry, anchor, stack)) {
			} else {
				stack = null;
				JOptionPane.showMessageDialog(null, "Not found!");
			}
		}

		else if (chooser == 3) {
			DictionaryEntry new_entry = new DictionaryEntry(null, null,
					parametr, null, null);

			if (this.findNumber(new_entry, anchor, stack)) {
			} else {
				stack = null;
				JOptionPane.showMessageDialog(null, "Not found!");
			}
		}

		else if (chooser == 4) {
			DictionaryEntry new_entry = new DictionaryEntry(null, null, null,
					null, new ImageIcon(parametr));

			if (this.findPicture(new_entry, anchor, stack)) {
			} else {
				stack = null;
				JOptionPane.showMessageDialog(null, "Not found!");
			}
		}

		return stack;
	}

	public void deletePerson(DefaultMutableTreeNode selectedNode) {
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) selectedNode
				.getParent();
		DefaultMutableTreeNode grandParent = (DefaultMutableTreeNode) parent
				.getParent();

		if ((parent.toString().equals("AA_AM") || parent.toString().equals(
				"AN_AZ"))
				&& parent.getChildCount() == 1) {
			theModel.removeNodeFromParent(parent);
			parent = (DefaultMutableTreeNode) grandParent
					.getChildAt(grandParent.getChildCount() - 1);

			int counter = parent.getChildCount() - 1;
			while (counter > -1) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) parent
						.getChildAt(counter);
				DefaultMutableTreeNode copyNode = node;

				theModel.removeNodeFromParent((MutableTreeNode) node);
				theModel.insertNodeInto(copyNode, grandParent, 0);

				counter--;
			}

			theModel.removeNodeFromParent(parent);
		} else
			theModel.removeNodeFromParent(selectedNode);
	}

	public TreePath insertPerson(String name, String surname,
			String personnelNumber, String date, ImageIcon icon) {
		TreePath path = null;
		DictionaryAnchor anchor = new DictionaryAnchor();

		anchor.topic = null;
		anchor.entry = null;

		DictionaryEntry new_entry = new DictionaryEntry(name, surname,
				personnelNumber, date, icon);

		if (this.findEntry(new_entry, anchor)) {
			// found such a person
			DefaultMutableTreeNode new_node = new DefaultMutableTreeNode(
					new_entry);
			new_node.setAllowsChildren(false);
			theModel.insertNodeInto(new_node, anchor.topic,
					anchor.topic.getChildCount());
			TreeNode[] nodes = theModel.getPathToRoot(new_node);
			path = new TreePath(nodes);
		} else {
			// not found - insert the person
			if (anchor.topic != null) {
				// the proper topic has been found
				DefaultMutableTreeNode new_node = new DefaultMutableTreeNode(
						new_entry);
				new_node.setAllowsChildren(false);
				theModel.insertNodeInto(new_node, anchor.topic,
						anchor.topic.getChildCount());
				TreeNode[] nodes = theModel.getPathToRoot(new_node);
				path = new TreePath(nodes);
				if (anchor.topic.getChildCount() > 1
						&& (check(anchor.topic.getChildAt(0).toString())) != check(anchor.topic
								.getChildAt(anchor.topic.getChildCount() - 1)
								.toString())) {
					path = rebuild(anchor);
				}

			} else {
				path = null;
			}

		}
		return path;
	}

	protected void open(File f) throws IOException {

		TreePath path = null;
		Stack<String> stackElem = new Stack<String>();
		;
		try {
			fis = new FileInputStream(f);
			dis = new DataInputStream(fis);
		} catch (Exception e) {
		}

		String line;
		String temp[];
		String forAdding[];

		while ((line = dis.readLine()) != null) {
			stackElem.add(line);
		}

		Enumeration en = theRoot.children();
		while (en.hasMoreElements()) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) en
					.nextElement();
			Enumeration en1 = node.children();
			while (en1.hasMoreElements())
				removeNodeFromParent((DefaultMutableTreeNode) en1.nextElement());
		}

		for (int i = 0; i < stackElem.size(); i++) {
			temp = stackElem.elementAt(i).split(" ");

			if (temp[5].charAt(0) == 'C')
				for (int j = 6; j < temp.length; j++)
					temp[5] = temp[5] + " " + temp[j];

			temp[3] = temp[3] + " " + temp[4];
			insertPerson(temp[0], temp[1], temp[2], temp[3], new ImageIcon(
					temp[5]));
		}

		dis.close();
		fis.close();
	}

	protected void save() throws IOException {

		os = new PrintWriter("data.txt");

		Enumeration en = theRoot.children();

		while (en.hasMoreElements()) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) en
					.nextElement();
			if ((node.getChildCount() > 1)
					&& (node.getChildAt(0).toString().equals("AA_AM"))) {

				Enumeration en1 = node.getChildAt(0).children();
				Enumeration en2 = node.getChildAt(1).children();

				while (en1.hasMoreElements()) {
					DefaultMutableTreeNode nodeTemp = (DefaultMutableTreeNode) en1
							.nextElement();
					DictionaryEntry entryTemp = (DictionaryEntry) nodeTemp
							.getUserObject();
					os.println(entryTemp.theName + " " + entryTemp.theSurname
							+ " " + entryTemp.personnelNumber + " "
							+ entryTemp.theDate + " "
							+ entryTemp.theIcon.toString());
				}

				while (en2.hasMoreElements()) {
					DefaultMutableTreeNode nodeTemp = (DefaultMutableTreeNode) en2
							.nextElement();
					DictionaryEntry entryTemp = (DictionaryEntry) nodeTemp
							.getUserObject();
					os.println(entryTemp.theName + " " + entryTemp.theSurname
							+ " " + entryTemp.personnelNumber + " "
							+ entryTemp.theDate + " "
							+ entryTemp.theIcon.toString());

				}
			} else {
				Enumeration en3 = node.children();

				while (en3.hasMoreElements()) {
					DefaultMutableTreeNode nodeTemp = (DefaultMutableTreeNode) en3
							.nextElement();
					DictionaryEntry entryTemp = (DictionaryEntry) nodeTemp
							.getUserObject();
					os.println(entryTemp.theName + " " + entryTemp.theSurname
							+ " " + entryTemp.personnelNumber + " "
							+ entryTemp.theDate + " "
							+ entryTemp.theIcon.toString());
				}
			}
		}
		os.flush();
	}

	protected boolean check(String letter) {
		String array[] = letter.split(" ");
		letter = array[1].substring(0, 1);
		boolean toReturn = false;

		for (String temp : AA_AM) {
			if (letter.equals(temp))
				toReturn = true;
		}
		return toReturn;
	}

	protected TreePath rebuild(DictionaryAnchor anchor) {
		DefaultMutableTreeNode new_node0 = new DefaultMutableTreeNode("AA_AM");
		DefaultMutableTreeNode new_node1 = new DefaultMutableTreeNode("AN_AZ");

		int counter = anchor.topic.getChildCount() - 1;

		while (counter > -1) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) anchor.topic
					.getChildAt(counter);
			DefaultMutableTreeNode copyNode = node;

			theModel.removeNodeFromParent((MutableTreeNode) node);

			DictionaryEntry entry = (DictionaryEntry) copyNode.getUserObject();
			String letter = entry.getSurname().substring(0, 1);

			for (String temp : AA_AM) {
				if (letter.equals(temp))
					new_node0.add(copyNode);
			}

			for (String temp : AN_AZ) {
				if (letter.equals(temp))
					new_node1.add(copyNode);
			}
			counter--;

		}

		theModel.insertNodeInto(new_node0, anchor.topic, 0);
		theModel.insertNodeInto(new_node1, anchor.topic, 1);

		TreeNode[] nodes = theModel.getPathToRoot(new_node1);
		return new TreePath(nodes);
	}

	protected boolean findEntry(DictionaryEntry new_entry,
			DictionaryAnchor anchor) {

		String nameLetter = new_entry.getValue().substring(0, 1);
		String surnameLetter = new_entry.getSurname().substring(0, 1);
		boolean result = false;

		if (anchor == null)
			return false;
		anchor.topic = null;

		Enumeration en = theRoot.children();

		while (en.hasMoreElements()) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) en
					.nextElement();
			if (nameLetter.equalsIgnoreCase(node.toString())) {
				anchor.topic = node;
				break;
			}
		}

		if ((anchor.topic.getChildCount() > 1)
				&& (anchor.topic.getChildAt(0).toString().equals("AA_AM"))) {

			int counter = 0;
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) anchor.topic
					.getChildAt(counter);

			for (String temp : AA_AM) {
				if (temp.equals(surnameLetter)) {
					anchor.topic = node;
					counter++;
					break;
				}
			}

			if (counter == 0) {
				for (String temp : AN_AZ) {
					if (temp.equals(surnameLetter)) {
						anchor.topic = (DefaultMutableTreeNode) anchor.topic
								.getChildAfter(node);
						break;
					}
				}
			}
		}

		if (anchor.topic != null) {
			en = anchor.topic.children();
			anchor.entry = null;

			while (en.hasMoreElements()) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) en
						.nextElement();
				if (new_entry.toString().equalsIgnoreCase(node.toString())) {
					anchor.entry = node;
					result = true;
					break;
				}
			}
		}
		return result;
	}

	protected boolean findName(DictionaryEntry entry, DictionaryAnchor anchor,
			Stack<DefaultMutableTreeNode> stack) {
		String name = entry.theName;
		String firstLetter = name.substring(0, 1);
		boolean result = false;

		if (anchor == null)
			return false;
		anchor.topic = null;

		Enumeration en = theRoot.children();

		while (en.hasMoreElements()) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) en
					.nextElement();
			String temp = node.toString();
			if (firstLetter.equals(temp)) {
				anchor.topic = node;
				break;
			}
		}

		if ((anchor.topic.getChildCount() > 1)
				&& (anchor.topic.getChildAt(0).toString().equals("AA_AM"))) {

			Enumeration en1 = anchor.topic.getChildAt(0).children();
			Enumeration en2 = anchor.topic.getChildAt(1).children();

			while (en1.hasMoreElements()) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) en1
						.nextElement();
				String temp[] = node.toString().split(" ");
				if (name.equals(temp[0])) {
					stack.push(node);
					anchor.entry = node;
					result = true;
				}
			}

			while (en2.hasMoreElements()) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) en2
						.nextElement();
				String temp[] = node.toString().split(" ");
				if (name.equals(temp[0])) {
					stack.push(node);
					anchor.entry = node;
					result = true;
				}
			}

		}

		else {
			en = anchor.topic.children();
			anchor.entry = null;

			while (en.hasMoreElements()) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) en
						.nextElement();
				String temp[] = node.toString().split(" ");
				if (name.equals(temp[0])) {
					stack.push(node);
					anchor.entry = node;
					result = true;
				}
			}
		}
		return result;
	}

	protected boolean findSurname(DictionaryEntry entry,
			DictionaryAnchor anchor, Stack<DefaultMutableTreeNode> stack) {
		String surname = entry.theSurname;
		boolean result = false;

		if (anchor == null)
			return false;
		anchor.topic = null;

		Enumeration en = theRoot.children();

		while (en.hasMoreElements()) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) en
					.nextElement();

			if ((node.getChildCount() > 1)
					&& (node.getChildAt(0).toString().equals("AA_AM"))) {
				Enumeration en1 = node.getChildAt(0).children();
				Enumeration en2 = node.getChildAt(1).children();

				while (en1.hasMoreElements()) {
					DefaultMutableTreeNode nodeTemp = (DefaultMutableTreeNode) en1
							.nextElement();
					String temp[] = nodeTemp.toString().split(" ");
					if (surname.equals(temp[1])) {
						stack.push(nodeTemp);
						anchor.topic = node;
						anchor.entry = nodeTemp;
						result = true;
					}
				}

				while (en2.hasMoreElements()) {
					DefaultMutableTreeNode nodeTemp = (DefaultMutableTreeNode) en2
							.nextElement();
					String temp[] = nodeTemp.toString().split(" ");
					if (surname.equals(temp[1])) {
						stack.push(nodeTemp);
						anchor.topic = node;
						anchor.entry = nodeTemp;
						result = true;
					}
				}
			} else {
				Enumeration en3 = node.children();
				anchor.entry = null;

				while (en3.hasMoreElements()) {
					DefaultMutableTreeNode nodeTemp = (DefaultMutableTreeNode) en3
							.nextElement();
					String temp[] = nodeTemp.toString().split(" ");
					if (surname.equals(temp[1])) {
						stack.push(nodeTemp);
						anchor.topic = node;
						anchor.entry = nodeTemp;
						result = true;
					}
				}
			}
		}

		return result;
	}

	protected boolean findNumber(DictionaryEntry entry,
			DictionaryAnchor anchor, Stack<DefaultMutableTreeNode> stack) {
		String number = entry.personnelNumber;
		boolean result = false;

		if (anchor == null)
			return false;
		anchor.topic = null;

		Enumeration en = theRoot.children();

		while (en.hasMoreElements()) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) en
					.nextElement();

			if ((node.getChildCount() > 1)
					&& (node.getChildAt(0).toString().equals("AA_AM"))) {
				Enumeration en1 = node.getChildAt(0).children();
				Enumeration en2 = node.getChildAt(1).children();

				while (en1.hasMoreElements()) {
					DefaultMutableTreeNode nodeTemp = (DefaultMutableTreeNode) en1
							.nextElement();
					DictionaryEntry entryTemp = (DictionaryEntry) nodeTemp
							.getUserObject();
					String temp = entryTemp.personnelNumber;
					if (number.equals(temp)) {
						stack.push(nodeTemp);
						anchor.topic = node;
						anchor.entry = nodeTemp;
						result = true;
					}
				}

				while (en2.hasMoreElements()) {
					DefaultMutableTreeNode nodeTemp = (DefaultMutableTreeNode) en2
							.nextElement();
					DictionaryEntry entryTemp = (DictionaryEntry) nodeTemp
							.getUserObject();
					String temp = entryTemp.personnelNumber;
					if (number.equals(temp)) {
						stack.push(nodeTemp);
						anchor.topic = node;
						anchor.entry = nodeTemp;
						result = true;
					}
				}
			} else {
				Enumeration en3 = node.children();
				anchor.entry = null;

				while (en3.hasMoreElements()) {
					DefaultMutableTreeNode nodeTemp = (DefaultMutableTreeNode) en3
							.nextElement();
					DictionaryEntry entryTemp = (DictionaryEntry) nodeTemp
							.getUserObject();
					String temp = entryTemp.personnelNumber;
					if (number.equals(temp)) {
						stack.push(nodeTemp);
						anchor.topic = node;
						anchor.entry = nodeTemp;
						result = true;
					}
				}
			}
		}
		return result;
	}

	protected boolean findPicture(DictionaryEntry entry,
			DictionaryAnchor anchor, Stack<DefaultMutableTreeNode> stack) {
		String picture = entry.theIcon.toString();
		boolean result = false;

		if (anchor == null)
			return false;
		anchor.topic = null;

		Enumeration en = theRoot.children();

		while (en.hasMoreElements()) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) en
					.nextElement();

			if ((node.getChildCount() > 1)
					&& (node.getChildAt(0).toString().equals("AA_AM"))) {
				Enumeration en1 = node.getChildAt(0).children();
				Enumeration en2 = node.getChildAt(1).children();

				while (en1.hasMoreElements()) {
					DefaultMutableTreeNode nodeTemp = (DefaultMutableTreeNode) en1
							.nextElement();
					DictionaryEntry entryTemp = (DictionaryEntry) nodeTemp
							.getUserObject();
					String temp = entryTemp.theIcon.toString();
					if (picture.equals(temp)) {
						stack.push(nodeTemp);
						anchor.topic = node;
						anchor.entry = nodeTemp;
						result = true;
					}
				}

				while (en2.hasMoreElements()) {
					DefaultMutableTreeNode nodeTemp = (DefaultMutableTreeNode) en2
							.nextElement();
					DictionaryEntry entryTemp = (DictionaryEntry) nodeTemp
							.getUserObject();
					String temp = entryTemp.theIcon.toString();
					if (picture.equals(temp)) {
						stack.push(nodeTemp);
						anchor.topic = node;
						anchor.entry = nodeTemp;
						result = true;
					}
				}
			} else {
				Enumeration en3 = node.children();
				anchor.entry = null;

				while (en3.hasMoreElements()) {
					DefaultMutableTreeNode nodeTemp = (DefaultMutableTreeNode) en3
							.nextElement();
					DictionaryEntry entryTemp = (DictionaryEntry) nodeTemp
							.getUserObject();
					String temp = entryTemp.theIcon.toString();
					if (picture.equals(temp)) {
						stack.push(nodeTemp);
						anchor.topic = node;
						anchor.entry = nodeTemp;
						result = true;
					}
				}
			}
		}
		return result;
	}

	public void removeNodeFromParent(MutableTreeNode selectedNode) {
		theModel.removeNodeFromParent(selectedNode);
	}

	public TreeNode[] getPathToRoot(MutableTreeNode newNode) {

		return theModel.getPathToRoot(newNode);
	}

	public DefaultTreeModel buildDefaultTreeStructure() {
		theRoot = new DefaultMutableTreeNode("Employees");
		theRoot.setAllowsChildren(true);

		for (int i = 0; i < alphabet.length(); i++) {
			DictionaryElem nodeElem = new DictionaryTopic(alphabet.substring(i,
					i + 1));
			DefaultMutableTreeNode topic = new DefaultMutableTreeNode(nodeElem);
			topic.setAllowsChildren(true);

			theRoot.add(topic);
		}
		theModel = new DefaultTreeModel(theRoot);
		theModel.setAsksAllowsChildren(true);

		return theModel;
	}

}