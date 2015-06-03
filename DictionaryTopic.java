import java.awt.event.*;
import java.awt.*;
import java.util.Enumeration;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

class DictionaryTopic extends DictionaryElem {
	private String theTopic;

	public DictionaryTopic(String topic) {
		theTopic = topic;
	}

	public String getType() {
		return "Topic";
	}

	public String getValue() {
		return theTopic;
	}

	public String toString() {
		return theTopic;
	}

}
