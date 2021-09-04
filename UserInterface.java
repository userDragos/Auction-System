import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UserInterface{

    private JFrame mainframe = new JFrame("Auction");
    AuctionInterface auction;
    int clid;
    public void generateInterface(){

        mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainframe.setLayout(new BorderLayout());
        mainframe.setMinimumSize(new Dimension(1000,500));
        
        mainframe.setLocationRelativeTo(null);
        mainframe.setVisible(true);
        this.firstPage();   
    }
    public void firstPage(){
        JPanel panel = new JPanel(new GridBagLayout());
        JButton sellerButton = new JButton("Add Item to Sell");
        JButton buyerButton = new JButton("Buy Items");
        JButton viewItems = new JButton("View my Items");
        sellerButton.addActionListener(new GetSellerPage(mainframe,this,auction,clid));
        buyerButton.addActionListener(new GetBuyerPage(mainframe,this,auction,clid));
        viewItems.addActionListener(new ViewItems(mainframe,this,auction,clid));


        GridBagConstraints constrWEST = new GridBagConstraints();
        constrWEST.anchor = GridBagConstraints.WEST;
        constrWEST.insets = new Insets(10,10,10,10);
        GridBagConstraints constrEast = new GridBagConstraints();
        constrEast.anchor=GridBagConstraints.EAST;
        constrEast.insets = new Insets(10,10,10,10);
        GridBagConstraints constrNorth = new GridBagConstraints();
        constrNorth.anchor=GridBagConstraints.NORTH;
        constrNorth.insets = new Insets(10,10,10,10);

        panel.add(sellerButton,constrWEST);
        panel.add(buyerButton,constrEast);
        panel.add(viewItems,constrNorth);
        mainframe.add(panel);
        SwingUtilities.updateComponentTreeUI(mainframe);
    }
    public void deleteCFrame(){
        mainframe.getContentPane().removeAll();
        mainframe.invalidate();
        mainframe.validate();
        mainframe.repaint();
    }
    public UserInterface(AuctionInterface a, int i){
        auction = a;
        clid = i;
    }
}