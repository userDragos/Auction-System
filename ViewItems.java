import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class ViewItems implements ActionListener{

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
        ui.deleteCFrame();
        this.buildPage();
        SwingUtilities.updateComponentTreeUI(mainFrame);
    }

    public void buildPage(){
        try{
            item = auction.getMyItems(myid);

            JPanel displayItems = new JPanel(new GridBagLayout());
            GridBagConstraints constr = new GridBagConstraints();
            displayItems.setLayout(new BoxLayout(displayItems, BoxLayout.Y_AXIS));
            JPanel labelsPanel = new JPanel();
            labelsPanel.add(display.printLabels());
            displayItems.add(labelsPanel);

            for(AuctionItem i : item){

                constr.insets = new Insets(10,5,10,5);
                JPanel oneItem = new JPanel();
                oneItem.add(display.displayItems(i),constr);


                JButton closeAuction = new JButton("Close");
                    
                closeAuction.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        try{
                            if(i.getReservedPrice() > i.getPrice()){
                                JOptionPane.showMessageDialog(new JFrame(), "The Reserved Price is Higher than the current bidding price","No Winner", JOptionPane.WARNING_MESSAGE);
                            }
                            else{
                                JOptionPane.showMessageDialog(new JFrame(), "The item was removed from the Auction","Winner Stored",JOptionPane.WARNING_MESSAGE);
                            }
                            auction.removeAuctionItem(i.getId());
                            mainFrame.remove(oneItem);
                            refresh();
                        }
                        catch(java.rmi.RemoteException b){
                            System.out.println(b);
                        }
                    }
                });

                oneItem.add(closeAuction,constr);


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
        catch(java.rmi.RemoteException b){
            System.out.println(b);
        }
    }

    public ViewItems(JFrame f,UserInterface u, AuctionInterface a, int i){
        mainFrame = f;
        ui=u;
        auction = a;
        myid = i;
    }   
}

////not secure