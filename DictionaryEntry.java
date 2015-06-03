import javax.swing.ImageIcon;

public class DictionaryEntry extends DictionaryElem {
	protected String theName;
	protected String theSurname;
	protected String personnelNumber;
	protected String theDate;
	protected ImageIcon theIcon;

	public DictionaryEntry(String name, String surname, String number,
			String date, ImageIcon icon) {
		theName = name;
		theSurname = surname;
		personnelNumber = number;
		theDate = date;
		theIcon = icon;
		if (theIcon == null) {
			ImageIcon empty = new ImageIcon("empty.jpg");
			theIcon = empty;
		}
	}

	public String getType() {
		return "Entry";
	}

	public String getContent() {
		return "Name: " + "\t" + theName + "\n" + "Surname: " + "\t"
				+ theSurname + "\n" + "Per. Number: " + "\t" + personnelNumber
				+ "\n" + "Date: " + "\t" + theDate + "\n";
	}

	public ImageIcon getPicture() {
		return theIcon;
	}

	public String getValue() {
		return theName;
	}

	public String getSurname() {
		return theSurname;
	}

	public String toString() {
		return theName + " " + theSurname;
	}

	public String getLine() {
		return theName + " " + theSurname + " " + personnelNumber;
	}
}