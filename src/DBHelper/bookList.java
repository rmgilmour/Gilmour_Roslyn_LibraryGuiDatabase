package DBHelper;
/* File Name: 	LibraryGuiDatabase.java
 * Author:     	Roslyn Gilmour
 * Course:		CEN-3024C
 * Professor:	Mary Walauskis
 * Description:	This program will provide a GUI tabbed menu panel to obtain
 *               the user's menu choice.
 *               Choice Options:   List Database:
 *                                       Initial button to list the database.
 *                                 Exit Program::
 *                                       Button with a confirm option to exit
 *                                       the program.
 *                                 Select Items:
 *                                       Select a search term to list the
 *                                       related records.
 *                                 Add an Item:
 *                                       Tabbed panel option to add a new
 *                                       record to the database.
 *                                 Delete an item:
 *                                       Tabbed panel option to Delete a
 *                                       record from the database.
 *                                 Check out an item:
 *                                       Update the item's status to
 *                                       "Checked out", and update the
 *                                       due date to reflect a date 30
 *                                       days from the current date.
 *                                   Check in an item:
 *                                       Update the item's status to
 *                                       "In", and update the due date
 *                                       to reflect null.
 * Date:		11/19/23
 */

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class bookList extends DBHelper {
	private final String TABLE_NAME = "bookList";
	public static final String barcode = "barcode";
	public static final String title = "title";
	public static final String author = "author";
	public static final String status = "status";
	public static final String dueDate = "dueDate";

	/*
	 * prepareSQL
	 * Purpose: 		prepares the text of a SQL "select" command.
	 * Return Type: 	String
	 * Arguments: 	fields: the fields to be displayed in the output
	 * 				whatField: field to search for
	 * 				whatValue: value to search for within whatField
	 * 				sort: use ABC or DESC to specify the sorting order
	 * 				sortField: the field that the data will be sorted by
	 */
	private String prepareSQL(String fields, String whatField, String whatValue, String sortField, String sort) {
		String query = "SELECT ";
		query += fields == null ? " * FROM " + TABLE_NAME : fields + " FROM " + TABLE_NAME;
		query += whatField != null && whatValue != null ? " WHERE " + whatField + " = \"" + whatValue + "\"" : "";
		query += sort != null && sortField != null ? " order by " + sortField + " " + sort : "";
		return query;
	}

	/*
	 * Insert
	 * Purpose:		insert a new recrd into the database
	 * Return Type:	void
	 * Arguments:	each field listed in the table from the database, in order
	 * Notes:		Due to inheritance, this executes the execute method found in the parent class
	 */
	public void insert(Integer barcode, String title, String author, String status, String dueDate) {
		title = title != null ? "\"" + title + "\"" : null;
		author = author != null ? "\"" + author + "\"" : null;
		status = status != null ? "\"" + status + "\"" : null;
		dueDate = dueDate != null ? "\"" + dueDate + "\"" : null;
		
		Object[] values_ar = {barcode, title, author, status, dueDate};
		String[] fields_ar = {bookList.barcode, bookList.title, bookList.author, bookList.status, bookList.dueDate};
		String values = "", fields = "";
		for (int i = 0; i < values_ar.length; i++) {
			if (values_ar[i] != null) {
				values += values_ar[i] + ", ";
				fields += fields_ar[i] + ", ";
			}
		}
		if (!values.isEmpty()) {
			values = values.substring(0, values.length() - 2);
			fields = fields.substring(0, fields.length() - 2);
			super.execute("INSERT INTO " + TABLE_NAME + "(" + fields + ") values(" + values + ");");
		}
	}

	/*
	 * delete
	 * Purpose:		remove a record from the database
	 * Return Type:	void
	 * Arguments:	the field (key) used to determine if a row should be deleted,and the value that should be removed
	 * Notes: 		Due to inheritance, this executes the execute method found in the parent class
	 */
	public void delete(String whatField, String whatValue) {
		super.execute("DELETE from " + TABLE_NAME + " where " + whatField + " = " + whatValue + ";");
	}

	/*
	 * update
	 * Purpose:		update a record in the database
	 * Return Type:	void
	 * Arguments:	the field (key) used to determine if a row should be updated, and the value that should be updated
	 * Notes:		Due to inheritance, this executes the execute method found in the parent class
	 **/
	public void update(String whatField, String whatValue, String whereField, String whereValue) {
		super.execute("UPDATE " + TABLE_NAME + " set " + whatField + " = \"" + whatValue + "\" where " + whereField + " = \"" + whereValue + "\";");
	}

	/*
	 * select
	 * Purpose:		completes a SQL "select command
	 * Return Type: ArrayList<ArrayList<Object>> - this means it returns a 2D array of objects, so that the data returned
	 * 	can be any type.
	 * Arguments: 	fields: the fields to be displayed in the output
	 * 				whatField: field to search within
	 * 				whatValue: value to search for within whatField
	 * 				sort: use ABC or DESC to specify the sorting order
	 * 				sortField: the field that the data will be sorted by
	 * Notes:		this method calls the private method prepareSQL within this class.
	 **/
	public ArrayList<ArrayList<Object>> select(String fields, String whatField, String whatValue, String sortField, String sort) {
		return super.executeQuery(prepareSQL(fields, whatField, whatValue, sortField, sort));
	}

	/*
	 * getExecuteResult
	 * Purpose:		performs a search of the database, where String "query" is the SQL command that would be entered
	 * on the command line
	 * Return Type:	ArrayList<ArrayList<Object>> - this means it returns a 2D array of objects, so that the data returned
	 * 				can be any type.
	 * Arguments:	query - this is the SQL command that would be entered at the command line for a sQL query
	 * Notes:		Due to inheritance, this executes the executeQuery method found in the parent class
	 */
	public ArrayList<ArrayList<Object>> getExecuteResult(String query) {
		return super.executeQuery(query);
	}

	/*
	 * execute
	 * Purpose:		performs a SQL command, where String query is the SQL command that would be entered
	 * on the command line
	 * Return Type:	void
	 * Arguments:	query - this is the SQL command that would be entered at the command line for a sQL query
	 * Notes:		Due to inheritance, this executes the executeQuery method found in the parent class
	 *
	 */
	public void execute(String query) {
		super.execute(query);
	}

	/*
	 * DefaultTableModel
	 * Purpose:		performs a search of the database, where String query is the SQL command that would be entered
	 * on the command line.
	 * Return Type:	DefaultTableModel - uses a vector of vectors (i.e a table) to store data
	 * Arguments:	fields: the fields to be displayed in the output
	 * 				whatField: field to search within
	 * 				whatValue: value to search for within whatField
	 * 				sort: use ABC or DESC to specify the sorting order
	 * 				sortField: the field that the data will be sorted by
	 * Notes:		Due to inheritance, this method calls the method "executeQueryToTable method from the parent class
	 */
	public DefaultTableModel selectToTable(String fields, String whatField, String whatValue, String sortField, String sort) {
		return super.executeQueryToTable(prepareSQL(fields, whatField, whatValue, sortField, sort));
	}

}