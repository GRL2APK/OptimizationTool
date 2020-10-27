package com.optimization;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.awt.event.ActionListener;
import java.sql.DriverManager;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.sql.*;
public class PrioritySelection extends JFrame{
	JComboBox jcSolution, jcOperationalization;
	JButton btnSetParameters,btnConfirm,btnGenerateOrderedPairs,btnContinueWithAll;
	JSpinner jsPriority;
	JTable jtSolutions;
	int prevprio=0,id=0;
	CopyOnWriteArrayList<Solution>unorderedSolution, orderedSolution;
	CopyOnWriteArrayList<OrderedPairs>orderedPairsList;
	DefaultComboBoxModel opmodel;
	ArrayList<String>dupselectedSolutionArrayList;
	Connection con=null;
	Statement stmt=null;
	ResultSet rs=null;
	public PrioritySelection(ArrayList<String>selectedSolutionArrayList) {
		setSize(600,600);
		setTitle("Optimization Framework");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setLayout(null);
		unorderedSolution=new CopyOnWriteArrayList<>();
		orderedSolution=new CopyOnWriteArrayList<>();
		orderedPairsList=new CopyOnWriteArrayList<>();
		DefaultComboBoxModel nfrmodel= new DefaultComboBoxModel();
		opmodel= new DefaultComboBoxModel();
		dupselectedSolutionArrayList=new ArrayList();
		
		for(String s:selectedSolutionArrayList)
		{
			dupselectedSolutionArrayList.add(s);
		}
		// DB Connection String
		
		String drivername="com.microsoft.sqlserver.jdbc.SQLServerDriver";
		try {
			Class.forName(drivername);
			String db="jdbc:sqlserver://localhost:1433;user=sa;password=cmsa019;databaseName=OptimizationDB";
			con=DriverManager.getConnection(db);
			System.out.println("Driver loaded successfully");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
//		String[]ops={"Operationalization 1","Operationalization 2","Operationalization 3", "Operationalization 4", "Operationalization 5"};
//		 for (String value : ops) {
//			 opmodel.addElement(value);
//		    }
		// Adding Solution Combobox
		
		jcSolution=new JComboBox();
		for (int i = 0; i < selectedSolutionArrayList.size(); i++) 
		{
			nfrmodel.addElement(selectedSolutionArrayList.get(i));
		}
		jcSolution.setModel(nfrmodel);
		jcSolution.setBounds(10,10, 150,30);
		add(jcSolution);
		
		// Adding Spinner priority
		SpinnerModel value =  
	             new SpinnerNumberModel(5, //initial value  
	                1, //minimum value  
	                10, //maximum value  
	                1); //step  
		jsPriority=new JSpinner(value);
		jsPriority.setBounds(180,10,50,30);
		add(jsPriority);
		
		// Adding Operationalization Combobox
		
		// Collect Operationalizations from database and add to opmodel
		
		jcOperationalization=new JComboBox();
		jcOperationalization.setBounds(250,10,150,30);
		//jcOperationalization.setModel(opmodel);
		add(jcOperationalization);
		
		// Adding btnConfirm
		
		btnConfirm=new JButton("Confirm");
		btnConfirm.setBounds(415,10,100,30);
		add(btnConfirm);
		
		// Adding btnGenerateOrderedPairs
		System.out.println("Generate Solution");
		btnGenerateOrderedPairs=new JButton("Generate Solution");
		btnGenerateOrderedPairs.setBounds(100,520,200,30);
		add(btnGenerateOrderedPairs);
		
//		// Adding btnContinueWithAll
//		
//		btnContinueWithAll=new JButton("Find Optimal Solution");
//		btnContinueWithAll.setBounds(320, 520,200,30);
//		add(btnContinueWithAll);
		
		// Adding Table
		
		JPanel jp=new JPanel();
		jp.setBounds(30,300,500,200);
		String []col={"NFR","Priority","Operationalization"};
		String [][]data=null;
		
		DefaultTableModel model = new DefaultTableModel(data,col);  
		jtSolutions=new JTable(model);
		jtSolutions.setBounds(30,300, 500,200);
		//jtSolutions.setPreferredScrollableViewportSize(new Dimension(450,363));
		jtSolutions.setFillsViewportHeight(true);
		JScrollPane js=new JScrollPane(jtSolutions);
		js.setVisible(true);
		jp.add(js);
		
		add(jp);
		
		// Adding Onselect event of NFR combobox
		
		jcSolution.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try
				{
					String item=jcSolution.getSelectedItem().toString();
					System.out.println(item +" is selected");
					if(item!=null)
					{
						populateFromDB(item);
					}
				}
				catch(Exception ex)
				{
					System.err.println("Error Occurred!! "+ex.toString());
				}
				
				
				
			}
		});
		
		
		// Add item to table row event
		
		btnConfirm.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				DefaultTableModel model = (DefaultTableModel)jtSolutions.getModel();
				String NFR=jcSolution.getSelectedItem().toString();
				String p = jsPriority.getValue().toString();
				String op=jcOperationalization.getSelectedItem().toString();
				int pr=Integer.parseInt(p);
					
					prevprio=pr;
					Object []o = new Object[3];
					o[0]=NFR;
					o[1]=p;
					o[2]=op;
					model.addRow(o);
					nfrmodel.removeElement(NFR);
					Solution sl=new Solution(); // SL
					id++;
					sl.setId(id);
					sl.setNFR(NFR);
					sl.setPriority(pr);
					sl.setOperationalization(op);
					unorderedSolution.add(sl);
					
				
					//					JOptionPane.showMessageDialog(null, "Please give priority greater than "+prevprio, "Failure", JOptionPane.ERROR_MESSAGE);

				
			}
		});
		
		// Adding event to Generate Ordered pairs Button
		
		btnGenerateOrderedPairs.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				makeOrderedSolution(unorderedSolution);
				
			}
		});
		
//		btnContinueWithAll.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				findOperationalizations();
//				
//			}
//		});
		
	} // End of constructor
	
	// Convert Unordered solution to Ordered Solution
	
			public void makeOrderedSolution(CopyOnWriteArrayList<Solution> solutionlist)
			{
				int status=0;
				int id=0;
				CopyOnWriteArrayList<Solution>orderedsolutionlist=new CopyOnWriteArrayList<>();
				while(!solutionlist.isEmpty())
				{
					for(Solution s : solutionlist)
					{
						status=0;
						int prio1=s.getPriority();
						
						for(Solution s2: solutionlist)
						{
							int prio2=s2.getPriority();
							
							if(prio1<=prio2)
							{
								
							}
							else
							{
								status=1;
							}
						}
						if(status==0)
						{
							id++;
							s.setId(id);
							orderedsolutionlist.add(s);
							solutionlist.remove(s);
						}
					}
				}
				for(Solution s:orderedsolutionlist)
				{
					System.out.println(s.getId()+" "+s.getNFR()+" "+s.getPriority()+" "+s.getOperationalization());
				}
				createOrderedPairs(orderedsolutionlist);
			}
			
			// Creating Ordered Pairs
			
			public void createOrderedPairs(CopyOnWriteArrayList<Solution> solutionlist)
			{
				int id=0;
				for(Solution s : solutionlist)
				{
					int firstid=s.getId();
					for(Solution s2:solutionlist)
					{
						int secondid=s2.getId();
						if(firstid<secondid)
						{
							id++;
							OrderedPairs op=new OrderedPairs();
							op.setId(id);
							op.setOp1prio(s.getPriority());
							op.setOp2prio(s2.getPriority());
							op.setOperationalization1(s.getOperationalization());
							op.setOperationalization2(s2.getOperationalization());
							orderedPairsList.add(op);
							
						}
					}
				}
				System.out.println("=================== Ordered Pairs========================");
				for(OrderedPairs op:orderedPairsList)
				{
					System.out.println(op.getOperationalization1()+" "+op.getOperationalization2());
				}
				
				calculateQualityFunction(orderedPairsList,solutionlist);
				// calling mean normalized priority
				
//				int a[]={4,5};
//				double w=calculateMeanNormalizedPriority(a);
//				System.out.println("Mean normalized priority of "+4+" and "+5+" is "+w);
			}
			
			// populate Operationalizations from DB
			
			public void populateFromDB(String NFRName)
			{
				try
				{
					stmt=con.createStatement();
					rs=stmt.executeQuery("select Operationalization from TblOperationalization where NFRName='"+NFRName+"' ");
					int i=0;
					ArrayList<String>opslist=new ArrayList<>();
					while(rs.next())
					{
						opslist.add(rs.getString(1));
						//System.out.println(rs.getString(1));
					}
					opmodel=new DefaultComboBoxModel<>();
					 for (String value : opslist) {
						 opmodel.addElement(value);
					    }
					 jcOperationalization.setModel(opmodel);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			
			public double calculateMeanNormalizedPriority(int []priorities)
			{
				int priosum=0;
				for(int i=0;i<priorities.length;i++)
				{
					priosum=priosum+priorities[i];
				}
				int p=priorities.length*10;
				double k=((double)priosum/((double)p));
				//System.out.println(k);
				double w=1-k;
				return w;
			}
			
			public void calculateQualityFunction(CopyOnWriteArrayList<OrderedPairs> orderedpairlist, CopyOnWriteArrayList<Solution> solutionlist)
			{
				CopyOnWriteArrayList<ParametersOfOrderedPairs>paramOrderedpairlist=new CopyOnWriteArrayList<>();
				for(OrderedPairs op:orderedpairlist)
				{
					String op1=op.getOperationalization1();
					String op2=op.getOperationalization2();
					
					try
					{
						// finding NFR for op1
						
						String NFR1="";
						stmt=con.createStatement();
						rs=stmt.executeQuery("select NFRName from TblOperationalization where Operationalization='"+op1+"' ");
						while(rs.next())
						{
							NFR1=rs.getString(1);
						}
						rs.close();
						
						
						// finding NFR for op2
						
						String NFR2="";
						stmt=con.createStatement();
						rs=stmt.executeQuery("select NFRName from TblOperationalization where Operationalization='"+op2+"' ");
						while(rs.next())
						{
							NFR2=rs.getString(1);
						}
						rs.close();
						
						System.out.println("NFR1 is "+NFR1+" NFR2 is "+NFR2);
						
						// finding threshold for NFR(op1) and NFR(op2)
						
						double threshold=0.00;
						stmt=con.createStatement();
						rs=stmt.executeQuery("select Threshold from TblThreshold where NFR1='"+NFR1+"' and NFR2='"+NFR2+"'");
						while(rs.next())
						{
							threshold=rs.getDouble(1);
						}
						System.out.println("Threshold is "+threshold);
						
						// Finding combined contribution
						
						double combinedcontribution=0.00;
						stmt=con.createStatement();
						rs=stmt.executeQuery("select CombinedContribution from TblCombinedContribution where Operationalization1='"+op1+"' and Operationalization2='"+op2+"'");
						while(rs.next())
						{
							combinedcontribution=rs.getDouble(1);
						}
						rs.close();
						System.out.println("Combined contribution is "+combinedcontribution);
						
						
						// Calculating Mean Normalized priority
						
						int prio1=op.getOp1prio();
						int prio2=op.getOp2prio();
						int p[]={prio1,prio2};
						double mnp=calculateMeanNormalizedPriority(p);
						System.out.println("MNP is "+mnp);
						
						// Calculating Del conflict deviation
						
						double del=threshold-combinedcontribution;
						
						// Calculating BigDel  Conflict function 
						double bigdel=0.00;
						if(del>0)
						{
							bigdel=mnp*del;
						}
						else
						{
							bigdel=0.00;
						}
						
						ParametersOfOrderedPairs pop=new ParametersOfOrderedPairs();
						pop.setNFR1(NFR1);
						pop.setNFR2(NFR2);
						pop.setOp1(op1);
						pop.setOp2(op2);
						pop.setThreshold(threshold);
						pop.setCombinedcontribution(combinedcontribution);
						pop.setDelta(del);
						pop.setBigdel(bigdel);
						pop.setOp1prio(prio1);
						pop.setOp2prio(prio2);
						
						paramOrderedpairlist.add(pop);
						
					}
					catch(Exception e)
					{
						System.err.println(e.toString());
					}
				}
				
				QualityFunction(paramOrderedpairlist,solutionlist);
			}
			
			
			// Quality function implementation
			
			public void QualityFunction(CopyOnWriteArrayList<ParametersOfOrderedPairs>poplist, CopyOnWriteArrayList<Solution>solutionlist)
			{
				double w3=0.00;
				int sumprio=0;
				int no_of_sol=solutionlist.size();
				
				for(Solution s:solutionlist)
				{
					sumprio=sumprio+s.getPriority();
					
				}
				
				w3 = 1-((double)sumprio/((double)no_of_sol*10));
				System.out.println("W3 is "+w3);
				double sigma3=0.00;
				
				for(ParametersOfOrderedPairs pop:poplist)
				{
					double cc=pop.getCombinedcontribution();
					double bigdel=pop.getBigdel();
					sigma3=sigma3+(cc-bigdel);
				}
				
				double roh=sigma3*w3;
				 System.out.println("===============Quality function value is =========== "+roh);
				 JOptionPane.showMessageDialog(null, "Quality function value is "+roh, "Success", JOptionPane.INFORMATION_MESSAGE);
				
			}
			
			// Finding all operationalizations for list of NFRs
			
//			public void findOperationalizations()
//			{
//				
//				for(String s:dupselectedSolutionArrayList)
//				{
//					System.out.println(s);
//				}
//			}
			

}
