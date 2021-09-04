import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GetBuyerPage implements ActionListener{

    private JFrame mainFrame = new JFrame();
    private UserInterface ui;
    private AuctionInterface auction;
    private int myid;
    private ArrayList<AuctionItem> item = new ArrayList<AuctionItem>();
    private DisplayItem display = new DisplayItem();
    public void actionPerformed(ActionEvent e){
       refresh();
 
    }

    public void refresh(){
        try{
            item=auction.getDatabase();
            ui.deleteCFrame();
            this.displayData();
            SwingUtilities.updateComponentTreeUI(mainFrame);
        }
        catch(java.rmi.RemoteException b){
            System.out.println(b);
        }
    }

    private void displayData(){
            
            JPanel displayItems = new JPanel(new GridBagLayout());
            GridBagConstraints constr = new GridBagConstraints();
            displayItems.setLayout(new BoxLayout(displayItems, BoxLayout.Y_AXIS));
            JPanel labelsPanel = new JPanel();
            labelsPanel.add(display.printLabels());
            displayItems.add(labelsPanel);

        for(AuctionItem i : item){
            constr.insets = new Insets(20,5,20,5);
            JPanel oneItem = new JPanel();
            oneItem.add(display.displayItems(i),constr);


            JButton bid = new JButton("BID");
                    
                bid.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        boolean exitWhile = true;
                        while(exitWhile){
                            String input = JOptionPane.showInputDialog(new JFrame(), "You are about to bid on this item","BID");
                            try{
                                int inputInt = Integer.parseInt(input);
                                if(auction.bidOnItme(i.getId(), inputInt,myid)){
                                    JOptionPane.showMessageDialog(new JFrame(), "The bid was registered","Done",JOptionPane.WARNING_MESSAGE);
                                }
                                else {
                                    JOptionPane.showMessageDialog(new JFrame(), "Someone Outbid you","ERROR",JOptionPane.WARNING_MESSAGE);
                                    
                                }
                                exitWhile=false;
                            }
                            catch(java.lang.NumberFormatException gh){
                                JOptionPane.showMessageDialog(new JFrame(), "Please enter a valid Price","ERROR",JOptionPane.WARNING_MESSAGE);
                            }
                            catch(java.rmi.RemoteException b){
                                System.out.println(b);
                            }
                        } 
                        refresh();
                    }
                });
            
            oneItem.add(bid,constr);
            displayItems.add(oneItem);
        }
        JButton goBack = new JButton("Main Page");
        goBack.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent gg){
                ui.deleteCFrame();
                ui.firstPage();
            }
        });
        GridBagConstraints goBottom = new GridBagConstraints();
        goBottom.anchor = GridBagConstraints.SOUTH;
        displayItems.add(goBack,goBottom);
        mainFrame.add(displayItems);
    }


    public GetBuyerPage(JFrame f,UserInterface u, AuctionInterface a, int i){
        mainFrame = f;
        ui=u;
        auction = a;
        myid = i;
    }
}
