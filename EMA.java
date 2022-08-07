//Rafael M. dos Santos
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;


public class EMA extends JFrame {
    Connection con;
    Statement stmt;
    JDesktopPane desktop;

    JMenuItem mnuCreate, mnuSelect, mnuUpdate, mnuDelete, mnuExit, mnuAbout, mnuInsert, mnuClose;

    SelectWindow selectWindow;
    InsertWindow insertWindow;
    CreateWindow createWindow;
    DeleteWindow deleteWindow;
    UpdateWindow updateWindow;


    public EMA() {

        super("Employee Management Application"); // set title of the frame
        setBounds(50, 50, 700, 450); // setting the bounds of the frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // setting the default close operation of the frame

        desktop = new JDesktopPane(); // creating a desktop pane
        add(desktop); // adding the desktop pane to the frame

        setJMenuBar(createMnu()); //setting the menu bar childs

        setLookAndFeelForWindow(); // setting the look and feel for the window

        initializeDataBase(); // initializing the database

        selectWindow = new SelectWindow(desktop, con); // creating a select window
        desktop.add(selectWindow); // adding the select window to the desktop pane
        selectWindow.setVisible(false); // setting the select window to visible

        insertWindow = new InsertWindow(desktop, con); // creating an insert window
        desktop.add(insertWindow); // adding the insert window to the desktop pane
        insertWindow.setVisible(false); // setting the insert window to visible

        createWindow = new CreateWindow(desktop, con); // creating a create window
        desktop.add(createWindow); // adding the create window to the desktop pane
        createWindow.setVisible(false); // setting the create window to visible

        deleteWindow = new DeleteWindow(desktop, con); // creating a delete window
        desktop.add(deleteWindow); // adding the delete window to the desktop pane
        deleteWindow.setVisible(false); // setting the delete window to visible

        updateWindow = new UpdateWindow(desktop, con); // creating an update window
        desktop.add(updateWindow); // adding the update window to the desktop pane
        updateWindow.setVisible(false); // setting the update window to visible

        this.setLocationRelativeTo(null); //setting the location of the frame to the center of the screen
        setVisible(true);
    }

    public static void main(String [] args){
        new EMA();
    }

    void setLookAndFeelForWindow() {
        //sets the look and feel for the application, i chose nimbus over the default for the windows
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    JMenuBar createMnu () {
        JMenuBar mnuBar = new JMenuBar(); // creating a menu bar

        JMenu mnuFile = new JMenu("File"); // creating a menu


        // menu items for the file menu
        mnuCreate = new JMenuItem("Create", KeyEvent.VK_C); // creating a menu item
        mnuSelect = new JMenuItem("Select", KeyEvent.VK_S); // creating a menu item
        mnuUpdate = new JMenuItem("Update", KeyEvent.VK_U); // creating a menu item
        mnuDelete = new JMenuItem("Delete", KeyEvent.VK_D); // creating a menu item
        mnuInsert = new JMenuItem("Insert", KeyEvent.VK_I); // creating a menu item
        mnuClose = new JMenuItem("Close All", KeyEvent.VK_W); // creating a menu item
        mnuExit = new JMenuItem("Exit", KeyEvent.VK_E); // creating a menu item
        mnuAbout = new JMenuItem("About", KeyEvent.VK_A); // creating a menu item
        
        // adding the menu item to the menu
        mnuFile.add(mnuCreate); 
        mnuFile.add(mnuSelect);
        mnuFile.add(mnuUpdate);
        mnuFile.add(mnuDelete);
        mnuFile.add(mnuInsert);

        // adding the menus to the menu bar
        mnuBar.add(mnuFile);
        mnuBar.add(mnuAbout);
        mnuBar.add(mnuClose);
        mnuBar.add(mnuExit);


        // actions listeners for the menu items
        mnuInsert.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                insertWindow.setVisible(true);
            }
        });

        mnuCreate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createWindow.setVisible(true);
            }
        });

        mnuSelect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectWindow.setVisible(true);
            }
        });

        mnuUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateWindow.setVisible(true);
            }
        });

        mnuDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteWindow.setVisible(true);
            }
        });

        mnuClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectWindow.setVisible(false);
                insertWindow.setVisible(false);
                createWindow.setVisible(false);
                deleteWindow.setVisible(false);
                updateWindow.setVisible(false);
            }
        });

        // action listener for the exit menu item
        mnuExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("mnuExit called");
                int opt = JOptionPane.showConfirmDialog(EMA.this, "Are you sure you want to exit?", "Confirm", JOptionPane.YES_NO_OPTION);
                if(opt == JOptionPane.YES_OPTION){
                    System.exit(0);
                } else {
                    return;
                };
            }
        });

        // action listener for the about menu item
        mnuAbout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("mnuAbout called");
                JTextPane txt = new JTextPane(); // using JTextPane to display html text on the screen
                txt.setContentType("text/html");
                txt.setText("<html><b><i>Employee Management Application</i></b><br>Version 1.0<br>" +
                "Developed by:<b>Rafael Maestro</b><br><br><b><u>Copyright (c) 2022</u></b></html>");
                JOptionPane.showMessageDialog(EMA.this, txt, "About", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        return mnuBar;

    }
    void initializeDataBase() {
        //getting the connection to the database
        System.out.println("initializing database");
        try{
            Class.forName("org.hsql.jdbcDriver");
            con = DriverManager.getConnection("jdbc:HypersonicSQL:hsql://localhost:8080", "sa", "");
            stmt = con.createStatement();
        } catch(ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Database driver not found.\n"+e, "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "Error in initializing database.\n"+e, "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        create(stmt); // trying to create the employee table when the application starts
    }

    void create(Statement s){
        // creating the employee table if it doesn't exist
        System.out.println("creating table");
        this.setLocationRelativeTo(null);
        try{
            String createQuery = "CREATE TABLE employee (ssn INTEGER, name VARCHAR(255), salary FLOAT, dept VARCHAR(255))";
            s.executeUpdate(createQuery);
            JOptionPane.showMessageDialog(null, "Table EMPLOYEE has been successfully created", "Everything is right!", JOptionPane.INFORMATION_MESSAGE);
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(null, "Error in creating table.\n"+e, "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(EMA.this, "Table employee already exists on the schema!", "Table already exists!", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
    }
}

class DeleteWindow extends JInternalFrame {

    JDesktopPane desktop;
    Connection con;
    Statement stmt;
    JButton btnDelete;
    JLabel lbl;

    DeleteWindow(JDesktopPane d, Connection con){

        super("Delete Window", true, true, false, true);
        desktop = d;

        try{
            stmt = con.createStatement();
            JPanel p1 = new JPanel();
            JPanel p2 = new JPanel();
            p2.setLayout(new GridLayout(1,1));
            p1.setLayout(new BorderLayout());

            lbl = new JLabel("Delete EMPLOYEE table?");
            lbl.setFont(new Font("Arial", Font.BOLD, 20));
            btnDelete = new JButton("Delete Table");

            p1.add(lbl, BorderLayout.CENTER);
            p2.add(btnDelete, BorderLayout.CENTER);
            p1.add(p2, BorderLayout.SOUTH);
            add(p1, BorderLayout.CENTER);
            
            // this.setMaximum(true);
            pack();

            btnDelete.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    delete(stmt);
                }
            });
            setDefaultCloseOperation(HIDE_ON_CLOSE);
        } catch (Exception e) {
            System.out.println("Error in creating statement");
        }
    }

    
    protected void delete(Statement s) {
        System.out.println("deleting table");
        int opt = JOptionPane.showConfirmDialog(this, "Are you sure you want to DELETE the table?\nData can be lost!", "Confirm", JOptionPane.YES_NO_OPTION);
        if(opt == JOptionPane.YES_OPTION){
            try{
                String deleteQuery = "DROP TABLE employee";
                s.executeUpdate(deleteQuery);
                JOptionPane.showMessageDialog(null, "Table EMPLOYEE has been successfully deleted!", "Everything is right!", JOptionPane.INFORMATION_MESSAGE);
            } catch (NullPointerException e) {
                JOptionPane.showMessageDialog(null, "Error in deleting table.\n"+e, "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Something went wrong.\n"+e, "Table already exists!", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        } else {
            return;
        };
    }

}

class CreateWindow extends JInternalFrame {

    JDesktopPane desktop;
    Connection con;
    Statement stmt;
    JButton btnCreate;
    JLabel lbl;

    CreateWindow(JDesktopPane d, Connection con){

        super("Create Window", true, true, false, true);
        desktop = d;

        try{
            stmt = con.createStatement();
            JPanel p1 = new JPanel();
            JPanel p2 = new JPanel();
            p2.setLayout(new GridLayout(1,1));
            p1.setLayout(new BorderLayout());

            lbl = new JLabel("Create EMPLOYEE table?");
            lbl.setFont(new Font("Arial", Font.BOLD, 20));
            btnCreate = new JButton("Create Table");

            p1.add(lbl, BorderLayout.CENTER);
            p2.add(btnCreate, BorderLayout.CENTER);
            p1.add(p2, BorderLayout.SOUTH);

            add(p1, BorderLayout.CENTER);
            // this.setMaximum(true);
            pack();

            btnCreate.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    create(stmt);
                }
            });
            setDefaultCloseOperation(HIDE_ON_CLOSE);
        } catch (Exception e) {
            System.out.println("Error in creating statement");
        }
    }

    protected void create(Statement s) {
        System.out.println("creating table");
        try{
            String createQuery = "CREATE TABLE employee (ssn INTEGER, name VARCHAR(255), salary FLOAT, dept VARCHAR(255))";
            s.executeUpdate(createQuery);
            JOptionPane.showMessageDialog(null, "Table EMPLOYEE has been successfully created", "Everything is right!", JOptionPane.INFORMATION_MESSAGE);
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(null, "Error in creating table.\n"+e, "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Table employee already exists on the schema!", "Table already exists!", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
    }
}

class InsertWindow extends JInternalFrame {

    JDesktopPane desktop;
    Connection con;
    Statement stmt;
    JTextField txtSSN, txtName, txtSalary, txtDept;
    JButton btnInsert, btnClear;
    JLabel lblSSN, lblName, lblSalary, lblDept, lblResult;

    InsertWindow(JDesktopPane d, Connection con){
        super("Insert Window", true, true, false, true);
        System.out.println("insertWindow selected!");
        desktop = d;
        try{
            stmt = con.createStatement();

            JPanel p1 = new JPanel();
            p1.setLayout(new GridLayout(10,1));

            p1.add(lblSSN = new JLabel("SSN:"));
            p1.add(txtSSN = new JTextField());
            p1.add(lblName = new JLabel("Name:"));
            p1.add(txtName = new JTextField());
            p1.add(lblSalary = new JLabel("Salary:"));
            p1.add(txtSalary = new JTextField());
            p1.add(lblDept = new JLabel("Department:"));
            p1.add(txtDept = new JTextField());

            
            add(p1, BorderLayout.NORTH);

            p1 = new JPanel();
            p1.add(lblResult = new JLabel(""));
            add(p1, BorderLayout.CENTER);

            p1 = new JPanel();
            p1.setLayout(new FlowLayout());
            btnInsert = new JButton("Insert");
            btnClear = new JButton("Clear");
            p1.add(btnInsert);
            p1.add(btnClear);

            add(p1, BorderLayout.SOUTH);
            this.setMaximum(true);
            // pack();

            btnClear.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    clear();
                }
            });

            btnInsert.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Integer ssn = Integer.parseInt(txtSSN.getText());
                    String name = txtName.getText().trim();
                    String dept = txtDept.getText().trim();
                    Float salary = Float.parseFloat(txtSalary.getText());
                    String query = "INSERT INTO employee VALUES ("+ssn+",'"+name+"',"+salary+",'"+dept+"')";
                    try{
                        stmt.executeUpdate(query);
                        lblResult.setText("Record inserted successfully!");
                        txtSSN.setText("");
                        txtName.setText("");
                        txtSalary.setText("");
                        txtDept.setText("");
                    } catch (SQLException e1) {
                        lblResult.setText("Error in inserting record!");
                    }
                }
            });
            setDefaultCloseOperation(HIDE_ON_CLOSE);
        } catch(Exception e) {
            JOptionPane.showMessageDialog(null, "Error in creating statement.\n"+e, "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    protected void clear() {
        txtSSN.setText("");
        txtName.setText("");
        txtSalary.setText("");
        txtDept.setText("");
        lblResult.setText("");
    }
}

class UpdateWindow extends JInternalFrame {

    JDesktopPane desktop;
    Connection con;
    Statement stmt;
    JTextField txtSSN, txtName, txtSalary, txtDept, txtSSNToUpdate;
    JButton btnUpdate, btnSearch, btnClear;
    JLabel lblSSN, lblName, lblSalary, lblDept, lblSSNToUpdate;
    JTextArea ta;

    UpdateWindow(JDesktopPane d, Connection con){
        super("Update Window", true, true, false, true);
        System.out.println("updateWindow selected!");
        desktop = d;
        try{
            stmt = con.createStatement();

            JPanel p1 = new JPanel();
            p1.setLayout(new GridLayout(9,1));

            p1.add(lblSSNToUpdate = new JLabel("SSN to update:"));
            p1.add(txtSSNToUpdate = new JTextField());
            p1.add(new JSeparator(JSeparator.HORIZONTAL));
            p1.add(new JSeparator(JSeparator.HORIZONTAL));
            p1.add(lblSSN = new JLabel("Update SSN:"));
            p1.add(txtSSN = new JTextField());
            p1.add(lblName = new JLabel("Update Name:"));
            p1.add(txtName = new JTextField());
            p1.add(lblSalary = new JLabel("Update Salary:"));
            p1.add(txtSalary = new JTextField());
            p1.add(lblDept = new JLabel("Update Department:"));
            p1.add(txtDept = new JTextField());

            
            add(p1, BorderLayout.NORTH);

            p1 = new JPanel();
            JScrollPane scrollPane = new JScrollPane(ta = new JTextArea(13, 90));
            ta.setFont(new Font("Monospaced", Font.PLAIN, 12));
            p1.add(scrollPane);
            add(p1, BorderLayout.CENTER);

            p1 = new JPanel();
            p1.setLayout(new FlowLayout());
            btnUpdate = new JButton("Update");
            btnSearch = new JButton("Search");
            btnClear = new JButton("Clear");
            p1.add(btnSearch);
            p1.add(btnUpdate);
            p1.add(btnClear);

            add(p1, BorderLayout.SOUTH);
            this.setMaximum(true);

            btnSearch.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Integer ssn = Integer.parseInt(txtSSNToUpdate.getText());
                    String query = "SELECT * FROM employee WHERE ssn="+ssn;
                    try{
                        ResultSet rs = stmt.executeQuery(query);
                        if(rs.next()){
                            txtSSN.setText(rs.getString("ssn"));
                            txtName.setText(rs.getString("name"));
                            txtSalary.setText(rs.getString("salary"));
                            txtDept.setText(rs.getString("dept"));
                            ta.setText("");

                            ta.setFont(new Font("Monospaced", Font.PLAIN, 12));
                            ta.setText("SSN\t\t\tNAME\t\t\tSALARY\t\t\tDEPARTMENT\n");

                            String s = rs.getString(1);
                            String name = rs.getString(2);
                            String salary = rs.getString(3);
                            String dept = rs.getString(4);
                            ta.append(s + "\t\t\t" + name + "\t\t\t" + salary + "\t\t\t" + dept + "\n");
                        } else {
                            ta.setText("No record found!");
                        }
                    } catch (SQLException e1) {
                        ta.setText("Error in searching record!");
                    }
                }
            });

            btnClear.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    txtSSNToUpdate.setText("");
                    txtSSN.setText("");
                    txtName.setText("");
                    txtSalary.setText("");
                    txtDept.setText("");
                    ta.setText("");
                }
            });

            btnUpdate.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Integer ssn = Integer.parseInt(txtSSN.getText());
                    String name = txtName.getText().trim();
                    String dept = txtDept.getText().trim();
                    Float salary = Float.parseFloat(txtSalary.getText());
                    Integer ssnToUpdate = Integer.parseInt(txtSSNToUpdate.getText());
                    String query = "UPDATE employee SET ssn="+ssn+",name='"+name+"',salary="+salary+",dept='"+dept+"' WHERE ssn="+ssnToUpdate;
                    try{
                        stmt.executeUpdate(query);
                        txtSSN.setText("");
                        txtName.setText("");
                        txtSalary.setText("");
                        txtDept.setText("");
                        txtSSNToUpdate.setText("");
                        ta.setText("Record updated successfully!");
                    } catch (SQLException e1) {
                        ta.setText("Error in updating record!");
                    }
                }
            });
            setDefaultCloseOperation(HIDE_ON_CLOSE);
        } catch(Exception e) {
            JOptionPane.showMessageDialog(null, "Error in creating statement.\n"+e, "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
}

class SelectWindow extends JInternalFrame {
    Statement pStmt;
    JDesktopPane desktop;
    JButton btn;
    JTextField tf;
    JTextArea ta;
    JComboBox<String> cb;

    /**
     * @param d
     * @param con
     */
    public SelectWindow(JDesktopPane d, Connection con) {
        super("Select on EMPLOYEE table", true, true, false, true);
        System.out.println("selectWindow selected!");
        desktop = d;

        try{
            pStmt = con.createStatement();

            JPanel l1 = new JPanel();
            JPanel l2 = new JPanel();

            l1.setLayout(new BorderLayout());
            l2.setLayout(new FlowLayout());

            l2.add(new JLabel("Get by: "));

            // creating the combo box for the user to select the field to get the data from
            String[] options = {"SSN", "NAME", "SALARY", "DEPARTMENT"};
            cb = new JComboBox<String>(options);
            l2.add(cb);

            l1.add(l2, BorderLayout.NORTH);
            
            l1.add(tf = new JTextField(15), BorderLayout.CENTER);
            l1.add(btn = new JButton("Search"), BorderLayout.SOUTH);
            
            add(l1, BorderLayout.NORTH);
            
            l1 = new JPanel();
            JScrollPane scrollPane = new JScrollPane(ta = new JTextArea(13, 90));
            ta.setFont(new Font("Monospaced", Font.PLAIN, 12));
            l1.add(scrollPane);
            add(l1, BorderLayout.CENTER);

            this.setMaximum(true);
            // pack();

            //set the button action listener
            
            btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.out.println("query button clicked");
                    String query = "";
                    try {
                        ta.setFont(new Font("Monospaced", Font.PLAIN, 12));
                        ta.setText("SSN\t\t\tNAME\t\t\tSALARY\t\t\tDEPARTMENT\n");
                        String opt = cb.getSelectedItem().toString();
                        switch(opt){

                            case "SSN":
                                System.out.println("SSN consult selected");
                                Integer valueInteger = Integer.valueOf(tf.getText().toString());
                                System.out.println(valueInteger);
                                query = "SELECT * FROM employee WHERE " + "SSN" + " LIKE  " + valueInteger + "";
                                break;

                            case "NAME":
                                System.out.println("NAME consult selected");
                                query = "SELECT * FROM employee WHERE " + "NAME"  + " LIKE '" + tf.getText() + "'";
                                break;

                            case "SALARY":
                                System.out.println("SALARY consult selected");
                                float valueFloat = Float.valueOf(tf.getText().toString());
                                System.out.println(valueFloat);
                                query = "SELECT * FROM employee WHERE " + "SALARY" + " LIKE  " + valueFloat + "";
                                break;

                            case "DEPARTMENT":
                                System.out.println("DEPT consult selected");
                                query = "SELECT * FROM employee WHERE " + "DEPT"  + " LIKE '" + tf.getText() + "'";
                                break;
                        }
                        System.out.println(query);
                        ResultSet rs = pStmt.executeQuery(query);
                        while (rs.next()) {
                            String ssn = rs.getString(1);
                            String name = rs.getString(2);
                            String salary = rs.getString(3);
                            String dept = rs.getString(4);
                            ta.append(ssn + "\t\t\t" + name + "\t\t\t" + salary + "\t\t\t" + dept + "\n");
                        }
                        tf.selectAll();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(desktop, "Something went wrong!.\n"+ex, "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            setDefaultCloseOperation(HIDE_ON_CLOSE);
        } catch (Exception e){
            JOptionPane.showMessageDialog(desktop, "Problema interno.\n"+e, "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
