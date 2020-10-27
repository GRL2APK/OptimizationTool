package com.optimization;
import javax.swing.*;
import java.util.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
class HomeScreen extends JFrame{
	JPanel jp;
	JList solutionList, selectedSolutionList;
	JButton btnTransfer, btnNext, btnReset, btnOpmization;
	ArrayList<String>selectedSolutionsArrayList;
	public HomeScreen()
	{
		setSize(600,500);
		setTitle("Optimization Framework");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setLayout(null);
		jp=new JPanel();
		DefaultListModel<Object> model_list_1 = new DefaultListModel<Object>();
		DefaultListModel<Object> model_list = new DefaultListModel<Object>();
		selectedSolutionsArrayList=new ArrayList();
		// Adding NFR Lists
		
		String[]NFRs={"Confidentiality","Authentication","Bandwidth Efficiency", "Usability", "Performance"};
		 for (String value : NFRs) {
		        model_list.addElement(value);
		    }

		solutionList=new JList();
		solutionList.setBounds(5,5,150, 200);
		solutionList.setModel(model_list);
		add(solutionList);
		
		// Adding Transfer Button
		
		btnTransfer=new JButton(">>");
		btnTransfer.setBounds(180,90,100,30);
		add(btnTransfer);
		
		// Adding Selected NFR list
		
		selectedSolutionList=new JList();
		selectedSolutionList.setModel(model_list_1);
		selectedSolutionList.setBounds(300,5,150, 200);
		add(selectedSolutionList);
		
		// Adding Reset Button
		btnReset=new JButton("Reset");
		btnReset.setBounds(5,220,100,30);
		add(btnReset);
		
		// Adding Next Button
		
		btnNext=new JButton("Next");
		btnNext.setBounds(120,220,100,30);
		//add(btnNext);
		
		// Adding Optimization Button
		
		btnOpmization=new JButton("Proceeed");
		//btnOpmization.setBounds(240, 220, 200,30);
		btnOpmization.setBounds(120,220,100,30);
		add(btnOpmization);
		
		// Action Listener of btnTransfer
		
		btnTransfer.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	        	solutionList.getSelectedValuesList().stream().forEach((data) -> {
	                model_list_1.addElement(data);
	                model_list.removeElement(data);
	            });
	        }
	    });
		
		btnNext.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				 for (int i = 0; i < selectedSolutionList.getModel().getSize(); i++) {
			            Object item = selectedSolutionList.getModel().getElementAt(i);
			            //System.out.println("Item = " + item);
			            String s = item.toString();
			            System.out.println(s);
			            selectedSolutionsArrayList.add(s);  
			        }
				 	dispose();
		            PrioritySelection ps=new PrioritySelection(selectedSolutionsArrayList);
		            ps.setVisible(true);
				
			}
		});
		
		btnOpmization.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				 for (int i = 0; i < selectedSolutionList.getModel().getSize(); i++) {
			            Object item = selectedSolutionList.getModel().getElementAt(i);
			            //System.out.println("Item = " + item);
			            String s = item.toString();
			            System.out.println(s);
			            selectedSolutionsArrayList.add(s);  
			        }
				 	dispose();
		            Optimizer oz=new Optimizer(selectedSolutionsArrayList);
		            oz.setVisible(true);
				
			}
		});
	}
}
public class Home {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new HomeScreen().setVisible(true);

	}

}
