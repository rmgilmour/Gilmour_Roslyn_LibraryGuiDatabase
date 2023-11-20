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
import java.sql.*;
import java.util.ArrayList;

public class DBHelper {
	private final String DATABASE_NAME = "C:\\Users\\RMGilmour\\IdeaProjects\\LibraryDB.db";
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;

	public DBHelper() {
		connection = null;
		statement = null;
		resultSet = null;
	}

	private void connect() {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_NAME);
			statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void close() {
		try {
			connection.close();
			statement.close();
			if (resultSet != null)
				resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private Object[][] arrayListTo2DArray(ArrayList<ArrayList<Object>> list) {
		Object[][] array = new Object[list.size()][];
		for (int i = 0; i < list.size(); i++) {
			ArrayList<Object> row = list.get(i);
			array[i] = row.toArray(new Object[row.size()]);
		}
		return array;
	}

	protected void execute(String sql) {
		try {
			connect();
			statement.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			close();
		}
	}

	protected DefaultTableModel executeQueryToTable(String sql) {
		ArrayList<ArrayList<Object>> result = new ArrayList<ArrayList<Object>>();
		ArrayList<Object> columns = new ArrayList<Object>();
		connect();
		try {
			resultSet = statement.executeQuery(sql);
			int columnCount = resultSet.getMetaData().getColumnCount();
			for (int i = 1; i <= columnCount; i++)
				columns.add(resultSet.getMetaData().getColumnName(i));
			while (resultSet.next()) {
				ArrayList<Object> subresult = new ArrayList<Object>();
				for (int i = 1; i <= columnCount; i++)
					subresult.add(resultSet.getObject(i));
				result.add(subresult);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		close();
		return new DefaultTableModel(arrayListTo2DArray(result), columns.toArray());
	}

	protected ArrayList<ArrayList<Object>> executeQuery(String sql) {
		ArrayList<ArrayList<Object>> result = new ArrayList<ArrayList<Object>>();
		connect();
		try {
			resultSet = statement.executeQuery(sql);
			int columnCount = resultSet.getMetaData().getColumnCount();
			while (resultSet.next()) {
				ArrayList<Object> subresult = new ArrayList<Object>();
				for (int i = 1; i <= columnCount; i++) {
					subresult.add(resultSet.getObject(i));
				}
				result.add(subresult);
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
		close();
		return result;
	}

}