package lab12;

/*
 * Java Classes:  Variable and Jasthi
 * File Name: JasthiGUI.java
 * Author: Siva Jasthi
 * Date: 10/15/2003 (Version 1.0)
 * Purpose: A program to generate the java code for simple java classes given an input file
 *          JasthiGUI in turn makes use of two more classes called Jasthi.java and Variable.java
 */

import java.awt.*;
import java.awt.event.*;
import java.util.StringTokenizer;
import javax.swing.*;
import java.io.*;

/**
 * Variable class encapsulates a java variable.
 */
class Variable {
	// name represents the name of the variable
	public String name;

	// type represents the data type of the variable
	// It can be any of the 8 primitive types
	// (boolean, char, byte, short, int, long, float, double)
	public String type;

	// qualifier represents the access qualifier
	// For eg. (private, public, protected, package, final, static)
	// These can be combined as well - eg public static final
	public String qualifier;

	// documentation outlines the purpose of the variable
	public String documentation;

	/**
	 * Given a variable name, returns the string representing the argument name as
	 * per the coding conventions
	 */
	public static String argument(String input_string) {
		char[] char_array = input_string.toCharArray();

		String temp_string = "a_";

		for (int i = 0; i < char_array.length; i++) {
			if (Character.isUpperCase(char_array[i])) {
				temp_string += "_";
				temp_string += Character.toLowerCase(char_array[i]);
			} else {
				temp_string += char_array[i];
			}
		}

		return temp_string;

	}

	/**
	 * Constructor that sets up a variable given a comma-separated string outlining
	 * [name, type, qualifier, documentation]
	 */
	Variable(String in_line) {
		StringTokenizer new_string = new StringTokenizer(in_line, ",");

		name = new_string.nextToken().trim();
		type = new_string.nextToken().trim();
		qualifier = new_string.nextToken().trim();
		documentation = new_string.nextToken().trim();
	}

	/**
	 * Returns 'variable declaration' code
	 */
	public String getVariableDeclarationCode() {
		String temp = "\n\n\t//" + documentation + "\n\t" + qualifier + " " + type + " " + name + ";";
		return temp;
	}

	/**
	 * Returns the string for the setMethod (modifier) code
	 */

	public String getSetMethodCode() {
		String argument_name = Variable.argument(name);
		String first_character = name.substring(0, 1);
		String rest_of_string = name.substring(1);
		String first_character_upper_case = first_character.toUpperCase();
		String set_method_javadoc = "\n\n\t/**" + "\n\t * Set method for the variable " + name + "\n\t */";

		String set_method_code = set_method_javadoc + "\n\tpublic void set" + first_character_upper_case
				+ rest_of_string + "(" + type + " " + argument_name + ")" + "\n\t{" + "\n\t\t" + name + " = "
				+ argument_name + ";" + "\n\t}";
		return set_method_code;
	}

	/**
	 * Returns the string representing a setMethod invocation
	 */

	public String getInvokeSetMethodCode() {
		String first_character = name.substring(0, 1);
		String rest_of_string = name.substring(1);
		String first_character_upper_case = first_character.toUpperCase();

		String dummy_value = "";
		if ((type.equals("byte")) || (type.equals("short")) || (type.equals("int")) || (type.equals("long"))
				|| (type.equals("float")) || (type.equals("double"))) {
			dummy_value = "100";
		} else if (type.equals("String")) {
			dummy_value = "\"" + name + "_dummy_string\"";
		} else if (type.equals("char")) {
			dummy_value = "\'X\'";
		} else if (type.equals("boolean")) {
			dummy_value = "false";
		} else {
			dummy_value = "new " + type + "( )";
		}

		String invoke_set_method_code = ".set" + first_character_upper_case + rest_of_string + "(" + dummy_value + ");";
		return invoke_set_method_code;
	}

	/**
	 * Returns the string for the getMethod (accessor) code
	 */

	public String getGetMethodCode() {

		String first_character = name.substring(0, 1);
		String rest_of_string = name.substring(1);
		String first_character_upper_case = first_character.toUpperCase();
		String get_method_javadoc = "\n\n\t/**" + "\n\t * Get method for the variable " + name + "\n\t */";

		String get_method_code = get_method_javadoc + "\n\tpublic " + type + " get" + first_character_upper_case
				+ rest_of_string + "( )" + "\n\t{" + "\n\t\treturn " + name + ";" + "\n\t}";
		return get_method_code;
	}

}

/**
 * Jasthi class encapsulates the details of a Class
 */
class Jasthi {
	// className holds the name of the Class
	private String className;

	// variableArray holds the details of the instance variables
	private Variable[] variableArray;

	/**
	 * This constructor takes the className and the Variable[ ] as the inputs
	 *
	 */
	Jasthi(String a_class_name, Variable[] a_variable_array) {
		className = a_class_name;
		variableArray = a_variable_array;
	}

	/**
	 * This constructor takes "in_file_name" as the input and populates the instance
	 * variables of Jasthi. The first line in the input file contains the class name
	 * Rest of the lines in the input file contains the details of the instance
	 * varialbes as follows. variable name, variable type, variable qualifier,
	 * variable documentation variable name, variable type, variable qualifier,
	 * variable documentation variable name, variable type, variable qualifier,
	 * variable documentation
	 */
	Jasthi(String in_file_name) {
		try {
			FileReader in_stream = new FileReader(in_file_name);
			BufferedReader ins = new BufferedReader(in_stream);

			// read a single line of text
			String line = ins.readLine().trim();
			setClassName(line);

			int number_of_variables = -1;

			// find out how many lines are in the file
			while (line != null) {
				line = ins.readLine();
				number_of_variables++;
			}
			ins.close();

			// specify the size of the variableArray
			variableArray = new Variable[number_of_variables];

			// open the file again
			in_stream = new FileReader(in_file_name);
			ins = new BufferedReader(in_stream);

			line = ins.readLine();
			for (int i = 0; i < number_of_variables; i++) {
				line = ins.readLine();
				variableArray[i] = new Variable(line);
			}
			ins.close();

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Set method for the ClassName
	 */
	public void setClassName(String a_class_name) {
		className = a_class_name;
	}

	/**
	 * Method to generate the variable declaration code
	 */
	private String generateVariableDeclarationCode() {
		String temp = "";
		for (int i = 0; i < variableArray.length; i++) {
			Variable var = variableArray[i];
			temp += "\t" + var.getVariableDeclarationCode();
		}
		return temp;
	}

	/**
	 * Method to generate the default constructor
	 */
	private String generateDefaultConstructor() {
		String temp = "\n\n\t/**" + "\n\t * Default Constructor For " + this.className + " Class" + "\n\t */"
				+ "\n\t public " + this.className + "()" + "\n\t {\n " + "\n\t };";
		return temp;
	}

	/**
	 * Method to generate the set methods
	 */
	private String generateSetMethods() {
		String temp = "";

		for (int i = 0; i < variableArray.length; i++) {
			Variable var = variableArray[i];
			temp += var.getSetMethodCode();
		}
		return temp;
	}

	/**
	 * Method to generate the get methods
	 */
	private String generateGetMethods() {
		String temp = "";

		for (int i = 0; i < variableArray.length; i++) {
			Variable var = variableArray[i];
			temp += var.getGetMethodCode();
		}
		return temp;
	}

	/**
	 * Method to generate the toString method
	 */
	private String generateToStringMethod() {
		String tostring_java_doc = "\n\n\t/** " + "\n\t * Returns the String representation of " + className
				+ " object " + "\n\t */";

		String tostring_method_code = tostring_java_doc + "\n\t public String toString()" + "\n\t{"
				+ "\n\t\t String temp = ";

		for (int i = 0; i < variableArray.length - 1; i++) {
			Variable var = variableArray[i];
			tostring_method_code += "\n\t\t\t\"" + "\\n" + var.name + " = \" + " + var.name + " +";
		}
		Variable var = variableArray[variableArray.length - 1];
		tostring_method_code += "\n\t\t\t\"" + "\\n" + var.name + " = \" + " + var.name + ";" + "\n\n\t\t return temp;"
				+ "\n\t}";

		return tostring_method_code;
	}

	/**
	 * Method to generate the Argument String to be used in the overloaded
	 * constructor
	 */
	private String getArgumentString() {
		String temp = "";

		for (int i = 0; i < variableArray.length - 1; i++) {
			Variable var = variableArray[i];
			temp += var.type + " " + Variable.argument(var.name) + ", ";
		}
		Variable var = variableArray[variableArray.length - 1];
		temp += var.type + " " + Variable.argument(var.name);
		return temp;
	}

	/**
	 * Method to generate the variable assignments inside the overloaded constructor
	 */
	private String getVariableAssignmentString() {
		String temp = "";

		for (int i = 0; i < variableArray.length; i++) {
			Variable var = variableArray[i];
			temp += "\n\t\t" + var.name + " = " + Variable.argument(var.name) + ";";
		}
		return temp;
	}

	/**
	 * Method to generate the overloaded constructor
	 */
	private String generateOverloadedConstructor() {
		String temp = "\n\n\t/**" + "\n\t* Overloded Constructor For " + this.className + " Class" + "\n\t*/"
				+ "\n\t public " + this.className + "(" + getArgumentString() + ")" + "\n\t { "
				+ getVariableAssignmentString() + "\n\t }";
		return temp;
	}

	/**
	 * Method to generate the main() method
	 */
	private String generateMainMethod() {
		String instance_1 = className.toLowerCase() + "_1";

		String instance_1_string = className + " " + instance_1 + " = new " + className + "( );";

		String instance_creation = "";
		for (int i = 0; i < variableArray.length; i++) {
			Variable var = variableArray[i];
			instance_creation += "\n\t\t" + instance_1 + var.getInvokeSetMethodCode();
		}

		String temp = "\n\n\t/**" + "\n\t* main( ) method for  " + this.className + " Class" + "\n\t*/"
				+ "\n\tpublic static void main(String args[])" + "\n\t{ \n\t\t" + instance_1_string + instance_creation
				+ "\n\t\tSystem.out.println(" + instance_1 + ");" + "\n\t}";
		return temp;

	}

	/**
	 * Method to generate the code for the whole class
	 */
	public void generate() throws java.io.IOException {
		// set up the file for writing
		String file_name = className + ".java";
		FileWriter output_file_writer = new FileWriter(file_name);
		PrintWriter output_writer = new PrintWriter(output_file_writer);

		// 1. write the string representation of the class
		output_writer.println(this);

		// 2. close the opened file
		output_writer.close();

		// print the informative message
		String temp = "\n\n==============================" + "\n See " + className + ".java"
				+ "\n for the generated code " + "\n as per ICS141 (Jasthi) Coding Conventions "
				+ "\n=================================\n";
		System.out.println(temp);
	}

	/**
	 * Method to return the String representation of Jasthi. It basically tells the
	 * name of the java file which contains the code generated.
	 */
	public String toString() {
		// 0. Code header for the java code; Need to be adjusted
		String code_string = "\n\n/*================================" + "\n Author     : Jasthi.java Program"
				+ "\n Class Name : " + className + "\n Date       :       "
				+ "\n Course     : ICS141 Programming with Objects (Siva Jasthi)" + "\n Purpose    : Lab/Program Number"
				+ "\n=================================*/\n" +

				// 1. write the class name and opening brace
				("\n\npublic class " + className + "\n{") +

				// 2. start spinning the variables along with the documentation
				(generateVariableDeclarationCode()) +

				// 3. write the the default constructor
				(generateDefaultConstructor()) +

				// 4. write the the overloaded constructor
				(generateOverloadedConstructor()) +

				// 5. write the set methods for all the variables
				(generateSetMethods()) +

				// 6. write the get methods for all the variables
				(generateGetMethods()) +

				// 7. write the toString() method
				(generateToStringMethod()) +

				// 8. write the main() method
				(generateMainMethod()) +

				// 9. write the closing brace for the whole class and a comment
				("\n}");

		// finally, return the String
		return code_string;
	}

	/**
	 * Main method that takes the file name as the input and generates the code for
	 * the class.
	 */
	public static void main(String[] args) throws java.io.IOException {
		/*
		 * if (args.length == 0) { System.out.println("Error: Missing input file");
		 * System.out.println("Usage:  java Jasthi input_file_name"); System.exit(0); }
		 */

		Jasthi object_code = new Jasthi(args[0]);

		object_code.generate();
		System.out.println(object_code);
	}
}

/**
 * Siva Jasthi (sjathi@msn.com) A GUI-based application
 *
 * Title: JasthiGUIUI Description: Compares the contents of two arrays and
 * describes how their contents differ.
 */

public class JasthiGUI extends JFrame implements ActionListener {
	private JTextField classNameText = new JTextField(50);
	private JTextArea classDescriptionText = new JTextArea(2, 10);
	private JTextArea variableListText = new JTextArea(5, 50);
	private JTextArea outputCodeText = new JTextArea(15, 50);

	private Font buttonFont = new Font("SansSerif", Font.BOLD, 12);
	private Font labelFont = new Font("SansSerif", Font.BOLD, 11);

	private JButton generateButton = new JButton("Generate Code");
	private JButton saveButton = new JButton("Save");

	private JButton ex1Button = new JButton("EX.1");
	private JButton ex2Button = new JButton("EX.2");
	private JButton ex3Button = new JButton("EX.3");

	private JButton clearButton = new JButton("Clear");
	private JButton quitButton = new JButton("Quit");
	private JButton helpButton = new JButton("Help");

	private Container c;
	private GridBagLayout layout = new GridBagLayout();
	private GridBagConstraints constraints = new GridBagConstraints();

	/**
	 * Constructor method which defines the GUI widgets and sets the layout
	 */
	public JasthiGUI() {
		super("ICS141 - Programming with Objects (Siva R Jasthi)");

		c = getContentPane();
		c.setLayout(layout);

		// Generate Code Button
		buildConstraints(constraints, 0, 0, 1, 1, 15, 2);
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.WEST;
		generateButton.setFont(buttonFont);
		layout.setConstraints(generateButton, constraints);
		c.add(generateButton);
		generateButton.addActionListener(this);

		// Save Code Button
		buildConstraints(constraints, 1, 0, 1, 1, 15, 2);
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.WEST;
		saveButton.setFont(buttonFont);
		layout.setConstraints(saveButton, constraints);
		c.add(saveButton);
		saveButton.addActionListener(this);

		// Example 1 code button
		buildConstraints(constraints, 2, 0, 1, 1, 15, 2);
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.WEST;
		ex1Button.setFont(buttonFont);
		layout.setConstraints(ex1Button, constraints);
		c.add(ex1Button);
		ex1Button.addActionListener(this);

		// Example 2 code button
		buildConstraints(constraints, 3, 0, 1, 1, 15, 2);
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.WEST;
		ex2Button.setFont(buttonFont);
		layout.setConstraints(ex2Button, constraints);
		c.add(ex2Button);
		ex2Button.addActionListener(this);

		// Example 3 code button
		buildConstraints(constraints, 4, 0, 1, 1, 15, 2);
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.WEST;
		ex3Button.setFont(buttonFont);
		layout.setConstraints(ex3Button, constraints);
		c.add(ex3Button);
		ex3Button.addActionListener(this);

		// Clear Button
		buildConstraints(constraints, 5, 0, 1, 1, 15, 2);
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.WEST;
		clearButton.setFont(buttonFont);
		layout.setConstraints(clearButton, constraints);
		c.add(clearButton);
		clearButton.addActionListener(this);

		// Quit Button
		buildConstraints(constraints, 6, 0, 1, 1, 15, 2);
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.WEST;
		quitButton.setFont(buttonFont);
		layout.setConstraints(quitButton, constraints);
		c.add(quitButton);
		quitButton.addActionListener(this);

		// Help Button
		buildConstraints(constraints, 7, 0, 1, 1, 15, 2);
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.WEST;
		helpButton.setFont(buttonFont);
		layout.setConstraints(helpButton, constraints);
		c.add(helpButton);
		helpButton.addActionListener(this);

		// Label for taking the Class Name
		buildConstraints(constraints, 0, 1, 1, 1, 0, 24);
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.WEST;
		Label label1 = new Label("Enter Java Class Name:", Label.LEFT);
		label1.setFont(labelFont);
		layout.setConstraints(label1, constraints);
		c.add(label1);

		// TextField for taking the java class name
		buildConstraints(constraints, 1, 1, 2, 1, 85, 0);
		constraints.fill = GridBagConstraints.NONE;
		layout.setConstraints(classNameText, constraints);
		classNameText.setText("Student");
		c.add(classNameText);

		// Variable Details Label
		buildConstraints(constraints, 0, 2, 1, 1, 0, 24);
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.WEST;
		Label label2 = new Label("Enter Variable Details:", Label.LEFT);
		label2.setFont(labelFont);
		layout.setConstraints(label2, constraints);
		c.add(label2);

		// TextArea for taking the Variable Details
		buildConstraints(constraints, 1, 2, 2, 1, 0, 0);
		constraints.fill = GridBagConstraints.NONE;
		layout.setConstraints(variableListText, constraints);
		variableListText.setText("name, String, private, for representing the name of Student"
				+ "\nmarks, int, private, for representing the marks of Student");
		JScrollPane scrollingResult2 = new JScrollPane(variableListText);
		layout.setConstraints(scrollingResult2, constraints);
		c.add(scrollingResult2);

		// Label to show the Generated Code
		buildConstraints(constraints, 0, 3, 1, 1, 0, 24);
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.WEST;
		Label label3 = new Label("Generated Code:", Label.LEFT);
		label3.setFont(labelFont);
		layout.setConstraints(label3, constraints);
		c.add(label3);

		// Text Area to show the Generated Code
		buildConstraints(constraints, 1, 3, 2, 1, 0, 0);
		constraints.fill = GridBagConstraints.NONE;
		outputCodeText.setEditable(true);
		outputCodeText.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
		JScrollPane scrollingResult = new JScrollPane(outputCodeText);
		layout.setConstraints(scrollingResult, constraints);
		c.add(scrollingResult);

		// Display GUI
		setSize(540, 400);
		show();

	}

	/**
	 * Method for saving the generated code. Users can modify the generated code.
	 */
	private void saveTheCode() {
		// 1. get the file name
		String file_name = classNameText.getText().trim() + ".java";

		// 2. verify that generated output code has some code there
		if (outputCodeText.getText().equals("")) {
			String warning_string = "Make sure that you have generated code first";
			JOptionPane.showMessageDialog(new Frame(), warning_string, "JasthiGUI Warning",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		try {
			// 3. get the file handle
			FileWriter output_file_writer = new FileWriter(file_name);
			// 4. get the Print Writer handle
			PrintWriter output_writer = new PrintWriter(output_file_writer);
			// 5. Write something to the file
			output_writer.print(outputCodeText.getText());
			// 6. finally, close the opened file
			output_writer.close();
		}

		catch (IOException e) {
			String warning_string = "Error while creating the " + file_name;

			JOptionPane.showMessageDialog(new Frame(), warning_string, "JasthiGUI Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		String message_string = file_name + " is created successfuly. \n " + "You can try compiling and running  "
				+ file_name;
		JOptionPane.showMessageDialog(new Frame(), message_string, "JasthiGUI Information",
				JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Method for generate the Java Code and for showing it in the Text Area.
	 */
	private void generateCode() {
		// 1. get the class name
		String class_name = classNameText.getText();

		// 2. get the variables
		String variable_list_string = variableListText.getText();

		// 3. verify that the user has entered values
		if (class_name.trim().equals("") || variable_list_string.trim().equals("")) {
			String warning_string = "You must enter the Class Name and Variable details \n"
					+ "    to generate the Java Code. ";
			JOptionPane.showMessageDialog(new Frame(), warning_string, "JasthiGUI Warning",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		// 4. Construct the variables
		Variable[] variable_array = null;
		StringTokenizer new_string = new StringTokenizer(variable_list_string, "\n");
		int i = 0;
		while (new_string.hasMoreTokens()) {
			new_string.nextToken();
			i++;
		}
		variable_array = new Variable[i];

		i = 0;
		new_string = new StringTokenizer(variable_list_string, "\n");
		while (new_string.hasMoreTokens()) {
			Variable v = new Variable(new_string.nextToken().trim());
			variable_array[i] = v;
			i++;
		}

		// 5 set the class name and the variables through the constructor.
		Jasthi jasthi_java_class = new Jasthi(class_name, variable_array);

		// 6.get the code
		String code_string = jasthi_java_class.toString();

		// 7. set this String to the text area
		outputCodeText.setText(code_string);

	}

	/**
	 * For showing the help on how the students can generate Java Code for simple
	 * java classes using JasthiGUI program.
	 */
	private void showTheHelp() {
		String help_string = "JasthiGUI.java can be used to generate Java Code for simple Java Classes.\n"
				+ "You can enter the Java Class Name in \"Enter Java Class Name\" field \n"
				+ "and the variable details in \"Enter Variable Details\" field. \n\n"
				+ "The varialbe details need the following values \n" + "\t\t Variable name (eg. name) \n"
				+ "\t\t Variable Type (eg. String) \n" + "\t\t Variable Access (eg. private) \n"
				+ "\t\t Variable description (eg. for representing the name of the student) \n\n"
				+ "When JasthiGUI is first launched, it shows some default values for \n"
				+ "the Java Class name and for the Variables to guide the user. \n\n"
				+ "\"Generate Code\" button generates the corresponding Java Code \n "
				+ "and shows generated code in the Text Area. \n"
				+ "\"Save\" button saves the Java (generated + modified) code to a file (class_name.java).\n "
				+ "\"Clear\" button clears all the entries in the GUI \n " + "\"Quit\" button quits the system \n\n "
				+ "For any further questions on this program, please contact.\n"
				+ "Siva.Jasthi@metrostate.edu or 651 482 2160\n";

		JOptionPane.showMessageDialog(new Frame(), help_string, "JasthiGUI Help", JOptionPane.INFORMATION_MESSAGE);

	}

	/**
	 * actionPerformed method for "compare" and "clear" buttons
	 */
	public void actionPerformed(ActionEvent event) {
		// Action when 'Generate Code' button is pushed
		if (event.getSource() == generateButton) {
			generateCode();
		} // end if for "Generate Code" button

		// Action when 'Save' button is pushed
		if (event.getSource() == saveButton) {
			saveTheCode();
		} // end if for "Save" button

		// Action when "EX.1" button is pushed.
		else if (event.getSource() == ex1Button) {
			classNameText.setText("Student");
			variableListText.setText("name, String, private, for representing the name of Student\"\r\n"
					+ "marks, int, private, for representing the marks of Student\"\r");
		} // end if for "EX1: button

		else if (event.getSource() == ex2Button) {
			classNameText.setText("Book");
			variableListText.setText("title, String, private, for representing the title of Book\"\r\n"
					+ "author, String, private, for representing the author of the Book\"\r\n"
					+ "cost, double, private, for representing the cost of the Book\"\r");
		} // end if for "EX2: button

		else if (event.getSource() == ex3Button) {
			classNameText.setText("Computer");
			variableListText.setText("make, String, private, for representing the make of the Computer\"\r\n"
					+ "model, String, private, for representing the model of the Computer\"\r\n"
					+ "cost, double, private, for representing the cost of the Computer\"\r\n"
					+ "memory, int, private, for representing the RAM in GB\"\r");
		} // end if for "EX3: button
		
		
		// Action when 'Help' button is pushed
		if (event.getSource() == helpButton) {
			showTheHelp();
		} // end if for "Help" button

		// Action when 'Quit' button is pushed
		if (event.getSource() == quitButton) {
			System.exit(0);// showTheHelp();
		} // end if for "Quit" button

		// Action when "Clear" button is pushed.
		else if (event.getSource() == clearButton) {
			classNameText.setText("");
			variableListText.setText("");
			outputCodeText.setText("");
		} // end if for "Clear: button

	} // end Action Performed

	/**
	 * method to set the GUI align with the GridBag layout
	 */
	private void buildConstraints(GridBagConstraints gbc, int gx, int gy, int gw, int gh, int wx, int wy) {
		gbc.gridx = gx;
		gbc.gridy = gy;
		gbc.gridwidth = gw;
		gbc.gridheight = gh;
		gbc.weightx = wx;
		gbc.weighty = wy;
	} // end contsraint builder

	// instantiate the GUI
	public static void main(String args[]) {
		JasthiGUI myGUI = new JasthiGUI();
		myGUI.setSize(1000, 700);
		myGUI.show();

	} // end main

} // end class JasthiGUI
