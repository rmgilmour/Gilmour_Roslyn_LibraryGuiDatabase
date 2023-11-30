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

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;


public class MainFrame extends JFrame {

    //Table labels and fields
    private JPanel mainPanel;
    private JPanel titlePanel;
    private JLabel listDbase;
    private JButton btnList;
    private JButton btnExit;
    private JButton btnDisplay;
    private JComboBox comboBox1;
    private JTextField txtComboField;
    private JTextField tfDbaseResult;
    private JPanel selectDbasePanel;
    private JPanel tabs;
    private JTabbedPane tabbedPane1;
    private JPanel addPanel;
    private JPanel deletePanel;
    private JPanel outPanel;
    private JPanel inPanel;
    private JTextField tfAddBcode;
    private JLabel addBcodeLabel;
    private JTextField tfAddStatus;
    private JLabel addStatusLabel;
    private JTextField tfAddDate;
    private JLabel addDateLabel;
    private JTextField tfAddTitle;
    private JTextField tfAddAuthor;
    private JButton btnAddSubmit;
    private JLabel addTitleLabel;
    private JLabel addAuthorLabel;
    private JLabel deleteBcodeLabel;
    private JLabel deleteTitleLabel;
    private JTextField tfDeleteBcode;
    private JTextField tfDeleteTitle;
    private JButton btnDelCode;
    private JTextField tfCheckout;
    private JButton btnCheckOut;
    private JLabel outBcodeLabel;
    private JLabel checkInLabel;
    private JTextField tfCheckIn;
    private JButton btnCheckIn;
    private JTextField textField1;
    private JPanel tablePanel;
    private JTable tableList;
    private JButton btnDelTitle;

    /**
     * Instance to access the bookList class
     */
    bookList bl1 = new bookList();

    /**
     * Arraylist object to store the data
     */
    ArrayList<ArrayList<Object>> data = new ArrayList<ArrayList<Object>>();

    /**
     * 2dArray to store the data
     */
     Object[][] dataList = {};

    /**
     * String array to store the column headings
     */
    String[] columns = {"Barcode", "Title", "Author", "Status", "Due Date"};


    /** MainFrame
     *  Purpose:    Creates the GUI
     *  Arguments:  parameters to create the GUI
     *  Return Type: void
     */
    public MainFrame() {
        setContentPane(mainPanel);
        setTitle("Library Management System");
        setSize(900, 750);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        createTable();
        data = bl1.getExecuteResult("select * from bookList;");

        btnList.addActionListener(new ActionListener() {
            /**
             * Purpose: Method to list the database
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {

                int count = 0;

                try {
                    Connection con = DriverManager.getConnection("jdbc:sqlite:C:/Users/RMGilmour/IdeaProjects/LibraryDB.db");
                    Statement st=con.createStatement();
                    String query = "select * from bookList";
                    ResultSet rs = st.executeQuery(query);

                    DefaultTableModel model = (DefaultTableModel)tableList.getModel();
                    String barcode, title, author, status, dueDate;
                    while (rs.next()) {
                        barcode=rs.getString(1);
                        title=rs.getString(2);
                        author=rs.getString(3);
                        status=rs.getString(4);
                        dueDate=rs.getString(5);
                        String[] row = {barcode,title,author,status,dueDate};
                        model.addRow(row);
                        count++;
                    }
                    textField1.setText("Number of records: " + count);
                    st.close();
                    con.close();

                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }); // end btnList

        btnAddSubmit.addActionListener(new ActionListener() {
            /**
             * Purpose: Method to add an item
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {

                int code = Integer.parseInt(tfAddBcode.getText());
                String title = tfAddTitle.getText();
                String author = tfAddAuthor.getText();
                String status = tfAddStatus.getText();
                String dueDate = tfAddDate.getText();

                bl1.insert(code,title,author,status,dueDate);
                printDatabase();

                tfAddBcode.setText("");
                tfAddTitle.setText("");
                tfAddAuthor.setText("");
                tfAddStatus.setText("");
                tfAddDate.setText("");
            }
        }); //end add an item

        btnDelCode.addActionListener(new ActionListener() {
            /**
             * Purpose: Method to delete an item
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                String code = tfDeleteBcode.getText();
                String tle = tfDeleteTitle.getText();
                if(tfDeleteBcode.getText() != null) {
                    bl1.delete("barcode", code);
                } else if (tfDeleteTitle.getText() != null) {
                    bl1.delete("title", tle);
                }
                tfDeleteBcode.setText("");
                tfDeleteTitle.setText("");
                printDatabase();
            }
        }); // end delete by barcode

        btnCheckOut.addActionListener(new ActionListener() {
            /**
             * Purpose: Method to check out an item
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalDate date = LocalDate.now();
                LocalDate newDate = date.plusDays(30);
                String toDate = String.valueOf(newDate);
                String code = tfCheckout.getText();
                bl1.update(bookList.status, "Checked out", "barcode", code);
                bl1.update(bookList.dueDate, toDate, "barcode", code);
                tfCheckout.setText("");
                printDatabase();
            }
        }); // end check out

        btnCheckIn.addActionListener(new ActionListener() {
            /**
             * Purpose: Method to check in an item
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                String code = tfCheckIn.getText();
                bl1.update(bl1.status, "In", "barcode", code);
                bl1.update(bl1.dueDate, "null", "barcode", code);
                tfCheckIn.setText("");
                printDatabase();
            }
        }); // end check in

        // Action Listener method to exit the program
        btnExit.addActionListener(new ActionListener() {
            /**
             * Purpose: Method to Exit the program
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                MainFrame frame = null;
                int response = JOptionPane.showConfirmDialog(frame, "Do you want to exit?", "Confirm", JOptionPane.YES_NO_OPTION);

                if(response == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        }); // end exit

    } // end mainFrame

    /** Void method to create the table
     * Purpose:     Method to create the table
     * Arguments:   Set the column data, width, and format
     * Return Type: void
     */
    private void createTable() {

        tableList.setModel(new DefaultTableModel(dataList, columns));
        TableColumnModel columns = tableList.getColumnModel();
        columns.getColumn(1).setMinWidth(250);
        columns.getColumn(2).setMinWidth(150);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        columns.getColumn(0).setCellRenderer(centerRenderer);
        columns.getColumn(3).setCellRenderer(centerRenderer);
        columns.getColumn(4).setCellRenderer(centerRenderer);

    } // end createTable

    /**
     * printDatabase
     * Purpose:     Method to print the database to the table GUI
     * Arguments:   connection to the database
     *              barcode, title, author, status, date data
     *              to set the table row contents.
     *              Textfield string to print the number of records
     *              in the database.
     * Return type: Void
     *
     */
    public void printDatabase() {

        tableList.setModel(new DefaultTableModel(null, columns));
        createTable();

        int count = 0;

        try {
            Connection con = DriverManager.getConnection("jdbc:sqlite:C:/Users/RMGilmour/IdeaProjects/LibraryDB.db");
            Statement st=con.createStatement();
            String query = "select * from bookList";
            ResultSet rs = st.executeQuery(query);

            DefaultTableModel model = (DefaultTableModel)tableList.getModel();
            String barcode, title, author, status, dueDate;
            while (rs.next()) {
                barcode=rs.getString(1);
                title=rs.getString(2);
                author=rs.getString(3);
                status=rs.getString(4);
                dueDate=rs.getString(5);
                String[] row = {barcode,title,author,status,dueDate};
                model.addRow(row);
                count++;
            }
            textField1.setText("Number of records: " + count);
            st.close();
            con.close();

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    } // end print database

    /**
     * Main
     * Purpose:     To create a connection to the database
     * @param args connect to the database
     * @throws java.sql.SQLException
     * Return Type: void
     */

    public static void main(String[] args) throws SQLException {
        MainFrame myFrame = new MainFrame();

        String url = "jdbc:sqlite:C:/Users/RMGilmour/IdeaProjects/LibraryDB.db";

        String query1 = "select * from bookList";

        Connection con = null;

        try {
            con = DriverManager.getConnection(url);
            System.out.println("Connection established.");
            Statement statement = con.createStatement();
            ResultSet result = statement.executeQuery(query1);
        } catch(SQLException e) {
            e.printStackTrace();
        }
        con.close();
    } // end main

} // end Class MainFrame

