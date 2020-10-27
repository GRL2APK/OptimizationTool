package com.optimization;
import javax.swing.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import javax.swing.table.DefaultTableModel;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.sql.*;
public class Optimizer extends JFrame{
	JComboBox jcSolution, jcOperationalization;
	JLabel lblNFR, lblPrio;
	private JButton btnConfirm, btnContinueWithAll;
	private JTable jtSolutions;
	JSpinner jsPriority;
	Connection con=null;
	Statement stmt=null;
	ResultSet rs=null;
	int prevprio=0,id=0;
	int noofnfrs = 0;
	
	CopyOnWriteArrayList<Solution>unorderedSolution, orderedSolution;
	CopyOnWriteArrayList<ParametersOfOrderedPairs>paramOrderedpairlist;
	CopyOnWriteArrayList<OptimalSolutionClass>optimizedsolutionlist;
	public Optimizer(ArrayList<String>selectedSolutionArrayList) {
		setSize(600,600);
		setTitle("Optimization Framework");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setLayout(null);
		DefaultComboBoxModel nfrmodel= new DefaultComboBoxModel();
		unorderedSolution=new CopyOnWriteArrayList<>();
		paramOrderedpairlist=new CopyOnWriteArrayList<>();
		optimizedsolutionlist=new CopyOnWriteArrayList<>();
		noofnfrs = selectedSolutionArrayList.size();
		// DB Connection
		
		String drivername="com.microsoft.sqlserver.jdbc.SQLServerDriver";
		try {
			Class.forName(drivername);
			String db="jdbc:sqlserver://localhost:1433;user=sa;password=cmsa019;databaseName=OptimizationDB";
			con=DriverManager.getConnection(db);
			System.out.println("Driver loaded successfully");
			stmt=con.createStatement();
			stmt.executeUpdate("delete from Tblgivenpriority");
			stmt.executeUpdate("delete from Tbluniquesolution");
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		lblNFR=new JLabel("Select NFR");
		lblNFR.setBounds(5,10,100,30);
		add(lblNFR);
		// Adding Solution Combobox
		
				jcSolution=new JComboBox();
				for (int i = 0; i < selectedSolutionArrayList.size(); i++) 
				{
					nfrmodel.addElement(selectedSolutionArrayList.get(i));
				}
				jcSolution.setModel(nfrmodel);
				jcSolution.setBounds(110,10, 150,30);
				add(jcSolution);
				
				
				// Adding Spinner priority
				lblPrio=new JLabel("Select Priority");
				lblPrio.setBounds(270,10,100,30);
				add(lblPrio);
				
				SpinnerModel value =  
			             new SpinnerNumberModel(5, //initial value  
			                1, //minimum value  
			                10, //maximum value  
			                1); //step  
				jsPriority=new JSpinner(value);
				jsPriority.setBounds(380,10,50,30);
				add(jsPriority);
				
				// Adding btnConfirm
				
				btnConfirm=new JButton("Confirm");
				btnConfirm.setBounds(440,10,100,30);
				add(btnConfirm);
				
				// Adding Table
				
				JPanel jp=new JPanel();
				jp.setBounds(30,200,500,200);
				String []col={"NFR","Priority"};
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
				
				// Adding btnContinueWithAll
				
				btnContinueWithAll=new JButton("Generate Solution");
				btnContinueWithAll.setBounds(200,420,200,30);
				add(btnContinueWithAll);
				
				// Add item to table row event
				
				btnConfirm.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						
						DefaultTableModel model = (DefaultTableModel)jtSolutions.getModel();
						String NFR=jcSolution.getSelectedItem().toString();
						String p = jsPriority.getValue().toString();
						
						int pr=Integer.parseInt(p);
							
							prevprio=pr;
							Object []o = new Object[3];
							o[0]=NFR;
							o[1]=p;
							
							model.addRow(o);
							nfrmodel.removeElement(NFR);
							Solution sl=new Solution(); // SL
							id++;
							sl.setId(id);
							sl.setNFR(NFR);
							sl.setPriority(pr);
							
							unorderedSolution.add(sl);
							
							
							try {
								
								stmt=con.createStatement();
								
								stmt.executeUpdate("insert into Tblgivenpriority(NFR,priority)values('"+NFR+"',"+pr+")");
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
						
							//					JOptionPane.showMessageDialog(null, "Please give priority greater than "+prevprio, "Failure", JOptionPane.ERROR_MESSAGE);

						
					}
				});
				
				
				//Adding btnContinueWithAll event
				
				btnContinueWithAll.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						
						findAllOperationalizations(unorderedSolution);
						

						
					}
				});
				
				
	}
	
	// Finding all Operationalizations of each NFR
	
	public void findAllOperationalizations(CopyOnWriteArrayList<Solution>unorderedSolList)
	{
		try {
			CopyOnWriteArrayList<AllOperationalization>allopslist=new  CopyOnWriteArrayList();
			stmt=con.createStatement();
			stmt.executeUpdate("delete from TblOrderedPairs");
			
			for(Solution s:unorderedSolList)
			{
				String NFR=s.getNFR();
				int priority=s.getPriority();
				ArrayList<String>oplist=new ArrayList();
				
					AllOperationalization aos=new AllOperationalization();
					aos.setNFR(NFR);
					aos.setPriority(priority);
					stmt=con.createStatement();
					rs=stmt.executeQuery("select Operationalization from TblOperationalization where NFRName='"+NFR+"' ");
					while(rs.next())
					{
						String op=rs.getString(1);
						oplist.add(op);
					}
					aos.setOperationalizationList(oplist);
					allopslist.add(aos);
	
			}
			
			for(AllOperationalization aos:allopslist)
			{
				System.out.println("NFR name "+aos.getNFR()+" Priority is "+aos.getPriority());
				ArrayList<String>oplist=new ArrayList();
				oplist=aos.getOperationalizationList();
				System.out.println("Operationalizations are......");
				for(String s:oplist)
				{
					System.out.println(s);
				}
			}
			
			for(AllOperationalization ao:allopslist)
			{
				int prio1=ao.getPriority();
				String NFR1=ao.getNFR();
				ArrayList<String>oplist1=new ArrayList();
				oplist1=ao.getOperationalizationList();
				for(AllOperationalization ao2:allopslist)
				{
					int prio2=ao2.getPriority();
					String NFR2=ao2.getNFR();
					ArrayList<String>oplist2=new ArrayList();
					oplist2=ao2.getOperationalizationList();
					if(prio1<prio2)
					{
						generateCombination(NFR1,NFR2,prio1,prio2,oplist1,oplist2);
					}
					
				}
			}
			// Run the python script
			try {
				Process process = Runtime.getRuntime().exec("python E:\\combinations.py");
				//process = Runtime.getRuntime().exec("dir");
				Thread.sleep(3000);
			}
			catch(Exception ex)
			{
				
			}
			
			ArrayList<String>validSolutionList=null;
			String solutionname="";
			stmt=con.createStatement();
			rs=stmt.executeQuery("select solution from Tbluniquesolution");
			while(rs.next())
			{
				validSolutionList = new ArrayList();
				String res=rs.getString(1);
				solutionname = res;
				StringTokenizer st=new StringTokenizer(res);
				while(st.hasMoreTokens())
				{
					String s = st.nextToken();
					validSolutionList.add(s);
				}
				
				// fetch rows which have valid solutions and store them in tempdb 
				String op1 = "";
				String op2 = "";
				String nFR1 = "";
				String nFR2 = "";
				double cc = 0.00;
				int prio1 = 0;
				int prio2 = 0;
				double del = 0.00;
				double bigdel = 0.00;
				int sumprio=0;
				Statement stmt1=con.createStatement();
				ResultSet rs1=stmt1.executeQuery("select * from TblOrderedPairs");
				while(rs1.next())
				{
					int count=0;
					op1 = rs1.getString(2);
					op2 = rs1.getString(3);
					 nFR1 = rs1.getString(4);
					 nFR2 = rs1.getString(5);
					 cc = rs1.getDouble(6);
					 prio1 = rs1.getInt(7);
					 prio2 = rs1.getInt(8);
					 del = rs1.getDouble(9);
					 bigdel = rs1.getDouble(10);
					 
					if(validSolutionList.contains(op1))
					{
						count++;
					}
					if(validSolutionList.contains(op2))
					{
						count++;
					}
					if(count == 2)
					{
						// store in tempdb
						sumprio=sumprio+prio1+prio2;
						Statement stmttemp=con.createStatement();
						stmttemp.executeUpdate("insert into TbltempOrderedPairs (op1,op2,nfr1,nfr2,combinedcontribution,op1prio,op2prio,delta,bigdel)values('"+op1+"','"+op2+"','"+nFR1+"','"+nFR2+"',"+cc+","+prio1+","+prio2+","+del+","+bigdel+")");

					}
					
				}
				double sp = (double)(sumprio/2);
				System.out.println("Sp is "+sp);
				double div=noofnfrs*10;
				double np= (double)(sp/div);
				np = 1-np;
				double cc1 = 0.00;
				double bigdel1 = 0.00;
				Statement stmttemp=con.createStatement();
				ResultSet rstemp = stmttemp.executeQuery("select * from  TbltempOrderedPairs");
				while(rstemp.next())
				{
					cc1 = cc1+rstemp.getDouble(6);
					bigdel1 = bigdel1 + rstemp.getDouble(10);
				}
				double sigma1 = cc1 - bigdel1;
				double roh1 = sigma1 * np;
				 DecimalFormat df = new DecimalFormat("#.###");
			     df.setRoundingMode(RoundingMode.CEILING);
			     sigma1 = Double.valueOf(df.format(sigma1));
			   roh1 = Double.valueOf(df.format(roh1));
			   
				System.out.println(solutionname+" "+sigma1+" "+roh1+" "+np);
				OptimalSolutionClass opc = new OptimalSolutionClass();
				opc.setSolution(solutionname);
				opc.setSigma(sigma1);
				opc.setRoh(roh1);
				opc.setNp(np);
				opc.setCc(cc1);
				optimizedsolutionlist.add(opc);
				//Statement stmtperm=con.createStatement();
				//stmtperm.executeUpdate("insert into Tblpermsolution(solution,roh, normalizedprio, sigma)values('"+solutionname+"',"+roh1+","+np+","+sigma1+")");
				Statement stmtdel=con.createStatement();
				stmtdel.executeUpdate("delete from TbltempOrderedPairs");
			}
			System.out.println("Valid Solutions");
//			for(String s:validSolutionList)
//			{
//				//System.out.println(s);
//			}
			
			
			dispose();
			//new OptimizedList(paramOrderedpairlist).setVisible(true);
			new OptimizedList(optimizedsolutionlist).setVisible(true);
		}
		catch(Exception e)
		{
			System.err.println(e.toString());
		}
		
	}

	private void generateCombination(String nFR1, String nFR2, int prio1, int prio2, ArrayList<String> oplist1,
			ArrayList<String> oplist2) 
	{
		
		try
		{
			int priorities[]= {prio1,prio2};
			
			// mean normalized priority
			double w=calculateMeanNormalizedPriority(priorities);
			
			// find threshold 
			
			double threshold=0.00;
			stmt=con.createStatement();
			rs=stmt.executeQuery("select Threshold from TblThreshold where NFR1='"+nFR1+"' and NFR2='"+nFR2+"'");
			while(rs.next())
			{
				threshold=rs.getDouble(1);
			}
			rs.close();
			//System.out.println("Threshold of "+nFR1+" and "+nFR2+" is "+threshold);
			double combinedcontribution=0.00;
			for(String ol:oplist1)
			{
				for(String ol2:oplist2)
				{
					combinedcontribution=0.00;
					stmt=con.createStatement();
					rs=stmt.executeQuery("select CombinedContribution from TblCombinedContribution where Operationalization1='"+ol+"' and Operationalization2='"+ol2+"'");
					while(rs.next())
					{
						combinedcontribution=rs.getDouble(1);
					}
					//System.out.println("<"+ol+","+ol2+"> => "+combinedcontribution);
					rs.close();
					double del=threshold-combinedcontribution;
					double bigdel=0.00;
					if(del>0)
					{
						bigdel=w*del;
					}
					else
					{
						bigdel=0.00;
					}
					ParametersOfOrderedPairs pop=new ParametersOfOrderedPairs();
					pop.setNFR1(nFR1);
					pop.setNFR2(nFR2);
					pop.setOp1(ol);
					pop.setOp2(ol2);
					pop.setThreshold(threshold);
					pop.setCombinedcontribution(combinedcontribution);
					pop.setDelta(del);
					pop.setBigdel(bigdel);
					pop.setOp1prio(prio1);
					pop.setOp2prio(prio2);
					
					
					double sigma=combinedcontribution-bigdel;
					double roh=sigma*w;
					//System.out.println("<"+ol+","+ol2+"> PHI => "+combinedcontribution+" BIGDEL =>"+bigdel+" Sigma => "+sigma+" ROH => "+roh);
					pop.setSigma(sigma);
					pop.setRoh(roh);
					paramOrderedpairlist.add(pop);
					
					//Inserting all information to DB
					stmt=con.createStatement();
					stmt.executeUpdate("insert into TblOrderedPairs (op1,op2,nfr1,nfr2,combinedcontribution,op1prio,op2prio,delta,bigdel)values('"+ol+"','"+ol2+"','"+nFR1+"','"+nFR2+"',"+combinedcontribution+","+prio1+","+prio2+","+del+","+bigdel+")");
				
				
				
				
				}
			}
		}
		catch(Exception e)
		{
			System.err.println(e.toString());
		}
		
	}
	
	// Calculating Normalized priority
	
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
}
