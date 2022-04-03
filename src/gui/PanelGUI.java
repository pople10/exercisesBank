package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import beans.Exercise;
import gui.messages.Dialogs;
import gui.utils.ButtonColumn;
import services.ExerciseService;
import utils.Validator;

public class PanelGUI extends JFrame {
	
	private ExerciseService exoService = ExerciseService.getInstance();

	public PanelGUI()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	    int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
	    int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
	    this.setLocation(x,y);
		this.setTitle("Exercises Bank");
		home();
		this.setIconImage((new ImageIcon("images/logo.png")).getImage());
		this.setJMenuBar(getMenu());
		this.setVisible(true);
	}
	
	private void home()
	{
		this.setVisible(false);
		this.getContentPane().removeAll();
		this.repaint();
		this.setLayout(new BorderLayout());
		JLabel welcomingLabel = new JLabel("Welcome to our application", SwingConstants.CENTER);
		welcomingLabel.setFont(new Font("Times New Roman", Font.PLAIN, 42));
		this.add(welcomingLabel,BorderLayout.CENTER);
		setSizing();
		this.setVisible(true);
	}
	
	private JMenuBar getMenu()
	{
		JMenuBar mb=new JMenuBar();
		JMenu menu = new JMenu("Home"); 
		JMenu menu1 = new JMenu("Exercise"); 
		JMenuItem add = new JMenuItem("Add");    
		JMenuItem all = new JMenuItem("All exercises");
		JMenuItem extract = new JMenuItem("Extract");
		menu.addMouseListener(new MouseListener() {
			  @Override
			  public void mouseClicked(MouseEvent e) {
				  home();
			  }

			  @Override
			  public void mousePressed(MouseEvent e) {
			  }

			  @Override
			  public void mouseReleased(MouseEvent e) {
			  }

			  @Override
			  public void mouseEntered(MouseEvent e) {
			  }

			  @Override
			  public void mouseExited(MouseEvent e) {
			  }
			});
		mb.add(menu);
		mb.add(menu1);
		add.addActionListener(e->{
			adding();
		});
		extract.addActionListener(e->{
			System.out.println("Extract");
		});
		all.addActionListener(e->{
			managing();
		});
		menu1.add(add);
		menu1.add(extract);
		menu1.add(all);
		return mb;
	}
	
	private void managing()
	{
		Exercise exo = new Exercise();
		this.getContentPane().removeAll();
		this.repaint();
		this.setVisible(false);
		String[] columnNames = {"#", "Question", "Answer", "Category","",""};
		Object[][] data = null;
		try {
			data = this.exoService.getForDatatable();
		} catch (SQLException e) {
			home();
			Dialogs.showErrorMessage("Error : "+e.getMessage());
		}
		DefaultTableModel model = new DefaultTableModel(data, columnNames);
		JTable table = new JTable(model){
			  public boolean isCellEditable(int row,int column){
				    if(column == 0) return false;
				    return true;
				  }
				};
		table.setPreferredScrollableViewportSize(table.getPreferredSize());
		table.setFillsViewportHeight(true);
		Action delete = new AbstractAction()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		        JTable table = (JTable)e.getSource();
		        int modelRow = Integer.valueOf( e.getActionCommand() );
		        Long idSelected = (Long) ((DefaultTableModel)table.getModel()).getValueAt(modelRow,0);
		        try {
		        	exoService.deleteExercise(idSelected);
		        	Dialogs.showSuccessMessage("Deleted with success");
				} catch (SQLException ex) {
					Dialogs.showErrorMessage("Error : "+ex.getMessage());
				}
		        ((DefaultTableModel)table.getModel()).removeRow(modelRow);
		    }
		};
		
		Action update = new AbstractAction()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		        JTable table = (JTable)e.getSource();
		        int modelRow = Integer.valueOf( e.getActionCommand() );
		        Long idSelected = (Long) ((DefaultTableModel)table.getModel()).getValueAt(modelRow,0);
		        String questionSelected = (String) ((DefaultTableModel)table.getModel()).getValueAt(modelRow,1);
		        String answerSelected = (String) ((DefaultTableModel)table.getModel()).getValueAt(modelRow,2);
		        String categorySelected = (String) ((DefaultTableModel)table.getModel()).getValueAt(modelRow,3);
		        Exercise exoTmp = new Exercise();
		        exoTmp.setAnswer(answerSelected);
		        exoTmp.setCategory(categorySelected);
		        exoTmp.setId(idSelected);
		        exoTmp.setQuestion(questionSelected);
		        try {
					exoService.updateExercise(exoTmp);
		        	Dialogs.showSuccessMessage("Updated with success");
				} catch (SQLException ex) {
					Dialogs.showErrorMessage("Error : "+ex.getMessage());
				}
		    }
		};
		 
		ButtonColumn buttonColumn = new ButtonColumn(table, delete, 5,"Delete");
		buttonColumn.setMnemonic(KeyEvent.VK_D);
		
		ButtonColumn buttonColumnUpdate = new ButtonColumn(table, update, 4,"Update");
		buttonColumnUpdate.setMnemonic(KeyEvent.VK_D);
		this.add(table);
		setSizing();
		this.setVisible(true);
	}
	
	private void adding()
	{
		Exercise exo = new Exercise();
		this.getContentPane().removeAll();
		this.repaint();
		this.setVisible(false);
		this.setLayout( new GridLayout(4,2,30,30));
		JLabel questionLabel = new JLabel("Question");
		JLabel answerLabel = new JLabel("Answer");
		JLabel categoryLabel = new JLabel("Category");
		JTextArea questionInput = new JTextArea(5, 20);
		JTextArea answerInput = new JTextArea(5, 20);
		JTextField categoryInput = new JTextField();
		JButton button = new JButton("Add");
		button.setBounds(10, 10, 200,30);
		this.add(questionLabel);
		this.add(questionInput);
		this.add(answerLabel);
		this.add(answerInput);
		this.add(categoryLabel);
		JPanel pane = new JPanel();
		pane.setLayout(null);
		categoryInput.setBounds(0,20,250,30);
		pane.add(categoryInput);
		this.add(pane);
		this.add(new JLabel());
		JPanel pane2 = new JPanel();
		pane2.setLayout(null);
		button.setBounds(0,20,100,30);
		button.addActionListener(e->{
			exo.setAnswer(answerInput.getText());
			exo.setCategory(categoryInput.getText());
			exo.setQuestion(questionInput.getText());
			if(Validator.validateExercise(exo))
			{
				button.setEnabled(false);
				button.setText("Adding...");
				try {
					exoService.createExercise(exo);
					answerInput.setText("");
					categoryInput.setText("");
					questionInput.setText("");
					Dialogs.showSuccessMessage("Added with success");
				} catch (SQLException e1) {
					Dialogs.showErrorMessage("Error : "+e1.getMessage());
				}
				finally {
					button.setEnabled(true);
					button.setText("Add");
				}
			}
		});
		pane2.add(button);
		this.add(pane2);
        this.setVisible(true);
	}
	
	private void setSizing()
	{
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(dimension);
	    int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
	    int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
	    this.setLocation(x,y);
        setMinimumSize(getSize());
	}
}
