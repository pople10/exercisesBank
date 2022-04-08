package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import beans.Exercise;
import gui.messages.Dialogs;
import gui.utils.ButtonColumn;
import services.ExerciseService;
import utils.Callback;
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
		this.setTitle("ExoBank");
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
		JLabel welcomingLabel = new JLabel("Bienvenue sur notre application ExoBank V1.0", SwingConstants.CENTER);
		welcomingLabel.setFont(new Font("Times New Roman", Font.PLAIN, 42));
		this.add(welcomingLabel,BorderLayout.CENTER);
		setSizing();
		this.setVisible(true);
	}
	
	private JMenuBar getMenu()
	{
		JMenuBar mb=new JMenuBar();
		JMenu menu = new JMenu("Acceuil"); 
		JMenu menu1 = new JMenu("Exercice"); 
		JMenuItem add = new JMenuItem("Ajouter");    
		JMenuItem all = new JMenuItem("Tous les exercices");
		JMenuItem extract = new JMenuItem("Extracter");
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
			extracting();
		});
		all.addActionListener(e->{
			managing();
		});
		menu1.add(add);
		menu1.add(extract);
		menu1.add(all);
		return mb;
	}
	private void extracting()
	{
		List<String> cats = new ArrayList<String>();
		cats.add("");
		try {
			cats.addAll(this.exoService.listAllCategories());
		} catch (SQLException e) {
			home();
			Dialogs.showErrorMessage("Erreur : "+e.getMessage());
		}
		this.getContentPane().removeAll();
		this.repaint();
		this.setVisible(false);
		this.setLayout( new GridLayout(0,1));
		JLabel ComoboLabel = new JLabel("Matiere");
		JLabel categoryLabel = new JLabel("Matiere");
		JComboBox<String> categoryInput = new JComboBox<String>();
		for(String cat : cats)
		{
			categoryInput.addItem(cat);
		}
		JButton button = new JButton("Extracter");
		this.add(ComoboLabel);
		this.add(categoryInput);
		button.addActionListener(e->{
			List<Exercise> list = new ArrayList<Exercise>();
			String catSelected = categoryInput.getSelectedItem().toString();
				button.setEnabled(false);
				button.setText("Extractant...");
				try {
					if(catSelected.trim().isEmpty())
						list=exoService.findAllExercises();
					else
						list=exoService.findAllExercisesByCategory(catSelected);
				
					generating(list);
				}
				catch(SQLException ex)
				{
					Dialogs.showErrorMessage("Erreur : "+ex.getMessage());
				}
				finally
				{
					button.setEnabled(true);
					button.setText("Extract");
				}
		});
		this.add(button);
		for(int i=0;i<29;i++)
		{
			this.add(new JLabel());
		}
		setSizing();
		this.setVisible(true);
	}
	
	private void generating(List<Exercise> exos)
	{
		this.getContentPane().removeAll();
		this.repaint();
		this.setVisible(false);
		this.setLayout(new CardLayout());
		String[] columnNames = {"#", "Question", "Réponse", "Matiere","Professeur"};
		Object[][] data = null;
		try {
			data = this.exoService.getForDatatable(exos);
		} catch (SQLException e) {
			home();
			Dialogs.showErrorMessage("Erreur : "+e.getMessage());
		}
		DefaultTableModel model = new DefaultTableModel(data, columnNames);
		JTable table = new JTable(model){
			  public boolean isCellEditable(int row,int column){
				    return false;
				  }
				};
		table.setPreferredScrollableViewportSize(table.getPreferredSize());
		table.setFillsViewportHeight(true);
		table.setSelectionMode( ListSelectionModel.SINGLE_INTERVAL_SELECTION );
		table.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER)
				{
					List<Exercise> listExos = new ArrayList<Exercise>();
					for(Integer i : table.getSelectedRows())
					{
						Exercise tmp = new Exercise();
						tmp.setId((Long) table.getValueAt(i, 0));
						tmp.setQuestion((String) table.getValueAt(i, 1));
						tmp.setAnswer((String) table.getValueAt(i, 2));
						tmp.setMatiere((String) table.getValueAt(i, 3));
						tmp.setProf((String) table.getValueAt(i, 4));
						listExos.add(tmp);
					}
					try {
						exoService.generatePDF(listExos,new Callback() {

							@Override
							public void call() {
								extracting();
							}
							
						});
					} catch (Exception e1) {
						Dialogs.showErrorMessage("Erreur : "+e1.getMessage());
					}
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		JScrollPane scrollPane = new JScrollPane(table);
		this.add(scrollPane);
		setSizing();
		this.setVisible(true);
	}
	
	private void managing()
	{
		Exercise exo = new Exercise();
		this.getContentPane().removeAll();
		this.repaint();
		this.setVisible(false);
		this.setLayout(new CardLayout());
		String[] columnNames = {"#", "Question", "Réponse", "Matiere","Professeur","",""};
		Object[][] data = null;
		try {
			data = this.exoService.getForDatatable();
		} catch (SQLException e) {
			home();
			Dialogs.showErrorMessage("Erreur : "+e.getMessage());
		}
		DefaultTableModel model = new DefaultTableModel(data, columnNames);
		JTable table = new JTable(model){
			  public boolean isCellEditable(int row,int column){
				    if(column == 0) return false;
				    if(column == 4) return false;
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
		        	Dialogs.showSuccessMessage("Supprimé avec succès");
				} catch (SQLException ex) {
					Dialogs.showErrorMessage("Erreur : "+ex.getMessage());
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
		        exoTmp.setMatiere(categorySelected);
		        exoTmp.setId(idSelected);
		        exoTmp.setQuestion(questionSelected);
		        try {
					exoService.updateExercise(exoTmp);
		        	Dialogs.showSuccessMessage("Modifé avec succès");
				} catch (SQLException ex) {
					Dialogs.showErrorMessage("Erreur : "+ex.getMessage());
				}
		    }
		};
		 
		ButtonColumn buttonColumn = new ButtonColumn(table, delete, 6,"Supprimer");
		buttonColumn.setMnemonic(KeyEvent.VK_D);
		
		ButtonColumn buttonColumnUpdate = new ButtonColumn(table, update, 5,"Modifier");
		buttonColumnUpdate.setMnemonic(KeyEvent.VK_D);
		JScrollPane scrollPane = new JScrollPane(table);
		this.add(scrollPane);
		setSizing();
		this.setVisible(true);
	}
	
	private void adding()
	{
		Exercise exo = new Exercise();
		this.getContentPane().removeAll();
		this.repaint();
		this.setVisible(false);
		this.setLayout( new GridLayout(5,2,30,30));
		JLabel questionLabel = new JLabel("Question");
		JLabel answerLabel = new JLabel("Réponse");
		JLabel categoryLabel = new JLabel("Matiere");
		JLabel profLabel = new JLabel("Professeur");
		JTextArea questionInput = new JTextArea(5, 20);
		JTextArea answerInput = new JTextArea(5, 20);
		JTextField categoryInput = new JTextField();
		JTextField profInput = new JTextField();
		JButton button = new JButton("Ajouter");
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
		JPanel pane2 = new JPanel();
		pane2.setLayout(null);
		button.setBounds(0,20,100,30);
		button.addActionListener(e->{
			exo.setAnswer(answerInput.getText());
			exo.setMatiere(categoryInput.getText());
			exo.setQuestion(questionInput.getText());
			exo.setProf(profInput.getText());
			if(Validator.validateExercise(exo))
			{
				button.setEnabled(false);
				button.setText("Ajoutant...");
				try {
					exoService.createExercise(exo);
					answerInput.setText("");
					categoryInput.setText("");
					questionInput.setText("");
					profInput.setText("");
					Dialogs.showSuccessMessage("Ajouté avec succès");
				} catch (SQLException e1) {
					Dialogs.showErrorMessage("Erreur : "+e1.getMessage());
				}
				finally {
					button.setEnabled(true);
					button.setText("Add");
				}
			}
		});
		this.add(profLabel);
		JPanel pane3 = new JPanel();
		pane3.setLayout(null);
		profInput.setBounds(0,20,250,30);
		pane3.add(profInput);
		this.add(pane3);
		pane2.add(button);
		this.add(new JLabel());
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
