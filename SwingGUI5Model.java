import java.awt.event.*;
import java.awt.*;
import java.util.Enumeration;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

class SwingGUI5Model {

	private DefaultTreeModel theModel;
	private static String alphabet = new String("ABCDEFGIJKLMNOPRSTUVWXYZ");
	private DefaultMutableTreeNode theRoot;

	public SwingGUI5Model() {

	}

	public TreePath findPerson(String data) {
		TreePath path;
		DictionaryAnchor anchor = new DictionaryAnchor();

		anchor.topic = null;
		anchor.entry = null;

		DictionaryEntry new_entry = new DictionaryEntry(data);

		if (this.findEntry(new_entry, anchor)) {
			TreeNode[] nodes = theModel.getPathToRoot(anchor.entry);
			path = new TreePath(nodes);

		} else {
			// not found
			path = null;
		}

		return path;
	}

	public void deletePerson(DefaultMutableTreeNode selectedNode) {
		if (selectedNode != theRoot) {
			DictionaryElem elem = (DictionaryElem) selectedNode.getUserObject();
			if ("Entry".equals(elem.getType())) {
				theModel.removeNodeFromParent(selectedNode);
			}
		}
	}

	public TreePath insertPerson(String data) {
		TreePath path;
		DictionaryAnchor anchor = new DictionaryAnchor();

		anchor.topic = null;
		anchor.entry = null;

		DictionaryEntry new_entry = new DictionaryEntry(data);

		if (this.findEntry(new_entry, anchor)) {
			// found such a person
			TreeNode[] nodes = theModel.getPathToRoot(anchor.entry);
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
			} else {
				path = null;
			}
		}

		return path;
	}

	protected boolean findEntry(DictionaryEntry new_entry,
			DictionaryAnchor anchor) {

		String firstLetter = new_entry.getValue().substring(0, 1);
		boolean result = false;

		if (anchor == null)
			return false;
		anchor.topic = null;

		Enumeration en = theRoot.children();

		while (en.hasMoreElements()) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) en
					.nextElement();

			DictionaryElem elem = (DictionaryElem) node.getUserObject();
			if ("Topic".equals(elem.getType())) {
				if (firstLetter.equalsIgnoreCase(elem.getValue())) {
					anchor.topic = node;
					break;
				}
			} else {
				break;
			}
		}

		if (anchor.topic != null) {
			en = anchor.topic.children();
			anchor.entry = null;

			while (en.hasMoreElements()) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) en
						.nextElement();

				DictionaryElem elem = (DictionaryElem) node.getUserObject();
				if ("Entry".equals(elem.getType())) {
					if (new_entry.getValue().equalsIgnoreCase(elem.getValue())) {
						anchor.entry = node;
						result = true;
						break;
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
		theRoot = new DefaultMutableTreeNode("Dictionary");
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
