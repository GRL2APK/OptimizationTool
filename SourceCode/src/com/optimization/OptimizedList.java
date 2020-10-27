package com.optimization;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import java.awt.*;
public class OptimizedList extends JFrame{
	JTable jtSolutions;
	JButton btnReduceSpace;
	Connection con=null;
	Statement stmt=null;
	ResultSet rs=null;
	//CopyOnWriteArrayList<ParametersOfOrderedPairs>dupparamOrderedpairlist;
	CopyOnWriteArrayList<OptimalSolutionClass>dupparamOrderedpairlist;
		public OptimizedList(CopyOnWriteArrayList<OptimalSolutionClass>paramOrderedpairlist)
		{
			setSize(700,700);
			setTitle("Optimization Framework");
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			setResizable(false);
			setLayout(null);
			//dupparamOrderedpairlist=new CopyOnWriteArrayList<ParametersOfOrderedPairs>();
			dupparamOrderedpairlist=new CopyOnWriteArrayList<OptimalSolutionClass>();
			dupparamOrderedpairlist=paramOrderedpairlist;
			
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
			
			// Adding btn reduced space
			
			btnReduceSpace=new JButton("Reduce Solution Space");
			btnReduceSpace.setBounds(150,530,200,30);
			add(btnReduceSpace);
			
			// Adding btnClick Event
			
			btnReduceSpace.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					ArrayList<String>NFRList=new ArrayList();
					ArrayList<String>uNFRList=new ArrayList();
					try
					{
						for(OptimalSolutionClass po:dupparamOrderedpairlist)
						{
							//String nfr1=po.getNFR1();
							//String nfr2=po.getNFR2();
							//NFRList.add(nfr1);
							//NFRList.add(nfr2);
							break;     // only for two solutions
						}
						
//						for(String s:NFRList)
//						{
//							if(!uNFRList.contains(s))		Removal of duplicates (required for more than 2 solutions)
//							{
//								uNFRList.add(s);
//							}
//						}
						
						
						
					}
					catch(Exception e)
					{
						System.err.println(e.toString());
					}
					
					dispose();
					new ReducedSpace().setVisible(true);
					
				}
			});
			
			// Adding Table
			
			JPanel jp=new JPanel();
			jp.setBounds(10,30,600,600);
			String []col={"Solution","Combined Contribution","Normalized Priority","Sigma","Quality Function"};
			
			String [][]data=null;
			
			
			
			DefaultTableModel model = new DefaultTableModel(data,col);  
			jtSolutions=new JTable(model);
			jtSolutions.setBounds(10,30, 600,600);
			//jtSolutions.setPreferredScrollableViewportSize(new Dimension(450,363));
			jtSolutions.setFillsViewportHeight(true);
			JScrollPane js=new JScrollPane(jtSolutions);
			js.setVisible(true);
			jp.add(js);
			
			add(jp);
			DefaultTableModel dumodel = (DefaultTableModel)jtSolutions.getModel();
			double max=0.0;
			int rownum=0;
			int c=0;
			for(OptimalSolutionClass po:paramOrderedpairlist)
			{
//				String op1=po.getOp1();
//				String op2=po.getOp2();
//				String sol="<"+op1+","+op2+">";
				String sol = po.getSolution();
				double cc=po.getCc();
				double np=po.getNp();
				double sigma=po.getSigma();
				double qf=po.getRoh();
				Object []o = new Object[5];
				o[0]=sol;
				o[1]=cc;
				o[2]=np;
				o[3]=sigma;
				o[4]=qf;
				dumodel.addRow(o);
				if(max<qf)
				{
					max=qf;
					rownum=c;
				}
				c++;
				
			}
			
			//jtSolutions.getColumnModel().getColumn(4).setCellRenderer(new CustomRenderer());
//			TableColumn coll = jtSolutions.getColumnModel().getColumn(4);
//			DefaultTableModel model3 = (DefaultTableModel)jtSolutions.getModel();
//			coll.setCellRenderer(new CustomRenderer());
			changeTable(jtSolutions,4,rownum);
			
			
			
			
			
		}
		
		 public void changeTable(JTable table, int column_index,int maxrow) {
		        table.getColumnModel().getColumn(column_index).setCellRenderer(new DefaultTableCellRenderer() {
		            @Override
		            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		                final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		                double st_val = Double.valueOf(table.getValueAt(row, 4).toString());
		                double req_val = 0.4;
		               
		                System.out.println("Hello this is outside test "+st_val);
		                if(row==maxrow)
		                {
		                	c.setBackground(Color.GREEN);
		                	c.setForeground(Color.BLACK);
		                }
		               
		                else if (st_val < req_val) 
		                {
		                	 if( st_val < 0.000)
				                {
			                		 System.out.println("Inside block "+st_val);
			                		 c.setBackground(Color.RED);
					                 c.setForeground(Color.WHITE);
				                }
		                	 else {
		                		 c.setBackground(Color.MAGENTA);
				                    c.setForeground(Color.BLACK);
		                	 }
		                   
		                    
		                }
		               
		                else 
		                {
		                	
		                    c.setBackground(Color.BLUE);
		                    c.setForeground(Color.WHITE);
		                }
		                return c;
		            }
		        });
		    }
		
		
		
		
}

class CustomRenderer extends DefaultTableCellRenderer
{
   

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, 5, column);
        setForeground(Color.blue); 
        return c;
    }
}

 