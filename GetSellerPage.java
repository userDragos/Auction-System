import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GetSellerPage implements ActionListener{

    private JFrame mainFrame = new JFrame();
    private JPanel panel = new JPanel(new GridBagLayout());
    private UserInterface ui;
    private AuctionInterface auction;
    private int myid;
    
    public void actionPerformed(ActionEvent e){
        
        ui.deleteCFrame();

        GridBagConstraints constr = new GridBagConstraints();
        constr.anchor = GridBagConstraints.NORTH;

        JLabel titleLabel = new JLabel("Product Tite");
        JTextField itemTitle = new JTextField(10);

        JLabel descripLabel = new JLabel("Description");
        JTextField itemDescription = new JTextField(10);

        JLabel conditionLabel = new JLabel("Product Condition");
        JTextField itemCondition = new JTextField(10);

        JLabel priceLabel = new JLabel("Product Min Price");
        JTextField price = new JTextField(10);

        JLabel reseredPriceLabel = new JLabel("Reserved Price");
        JTextField reservedPrice = new JTextField(10);
        constr.gridx=-1;
        constr.gridy=1;
        panel.add(titleLabel,constr);
        constr.gridy=2;
        panel.add(descripLabel,constr);
        constr.gridy=3;
        panel.add(conditionLabel,constr);
        constr.gridy=4;
        panel.add(priceLabel,constr);
        constr.gridy=5;
        panel.add(reseredPriceLabel,constr);

        constr.gridx=1;
        constr.gridy=1;
        panel.add(itemTitle,constr);
        constr.gridy=2;
        panel.add(itemDescription,constr);
        constr.gridy=3;
        panel.add(itemCondition,constr);
        constr.gridy=4;
        panel.add(price,constr);
        constr.gridy=5;
        panel.add(reservedPrice,constr);

        JButton sendData = new JButton("Add item");


        sendData.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) throws java.lang.IllegalArgumentException{

                if( itemTitle.getText().isEmpty() || itemDescription.getText().isEmpty() || itemCondition.getText().isEmpty() || price.getText().isEmpty() || reservedPrice.getText().isEmpty()){
                    JOptionPane.showMessageDialog(new JFrame(), "Please complete all the fields","Error", JOptionPane.ERROR_MESSAGE);
                }
                else{
                    
                    String storedTitle = itemTitle.getText().trim();
                    String storedItemDescription = itemDescription.getText().trim();
                    String storedItemCoondition = itemCondition.getText().trim();
                    String storedStartingPrice = price.getText().trim();
                    int storedPrice = Integer.parseInt(storedStartingPrice);
                    String srz = reservedPrice.getText().trim();
                    int storedRezerved = Integer.parseInt(srz);


                    //this needs to be secured
                    try{
                        AuctionItem item = new AuctionItem(auction.giveMeUniqueId(), storedTitle, storedItemDescription, storedItemCoondition, storedPrice, myid,storedRezerved);
                        auction.addItemtoSell(item);
                    }
                    catch(java.rmi.RemoteException b){
                        System.out.println(b);
                    }


                    ui.deleteCFrame();
                    ui.firstPage();
                }
            }
        });
        constr.gridx=0;
        constr.gridy=6;
        panel.add(sendData,constr);
        mainFrame.add(panel);
        SwingUtilities.updateComponentTreeUI(mainFrame);
    }

    public GetSellerPage(JFrame f,UserInterface u, AuctionInterface a, int i){
        mainFrame = f;
        ui=u;
        auction = a;
        myid = i;
    }
}