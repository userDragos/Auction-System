
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class DisplayItem{
    Insets inset = new Insets(10,5,10,5);
    public JPanel printLabels(){
            
            JPanel displayItems = new JPanel(new GridBagLayout());
            
            JTextArea id = new JTextArea("Seller ID");
            id.setColumns(10);
            JTextArea iID = new JTextArea("Item ID");
            iID.setColumns(10);
            JTextArea iTit = new JTextArea("Title");
            iTit.setColumns(10);
            JTextArea iDes = new JTextArea("Description");
            iDes.setColumns(10);
            JTextArea iCon = new JTextArea("Condition");
            iCon.setColumns(10);
            JTextArea iPrice = new JTextArea("Current Price");
            iPrice.setColumns(10);
            JTextArea iLast = new JTextArea("Last Bid ID");
            iLast.setColumns(10);

            JPanel labels = new JPanel();
            labels.add(id, inset);
            labels.add(iID,inset);
            labels.add(iTit,inset);
            labels.add(iDes,inset);
            labels.add(iCon,inset);
            labels.add(iPrice, inset);
            labels.add(iLast,inset);

            displayItems.add(labels);
        return displayItems;
    }

    public JPanel displayItems(AuctionItem i){

                JPanel oneItem = new JPanel(new GridBagLayout());
                GridBagConstraints constr = new GridBagConstraints();
                constr.insets = inset;
                
                JTextArea sellerId = new JTextArea(String.valueOf(i.getSellerId()));
                sellerId.setColumns(10);
                JTextArea itemID = new JTextArea(String.valueOf(i.getId()));
                itemID.setColumns(10);
                JTextArea itemTitle = new JTextArea(i.getTitle());
                itemTitle.setColumns(10);
                JTextArea itemDescription = new JTextArea(i.getDescription());
                itemDescription.setColumns(10);
                JTextArea itemCondition = new JTextArea(i.getCondition());
                itemCondition.setColumns(10);
                JTextArea currentPrice = new JTextArea(String.valueOf(i.getPrice()));
                currentPrice.setColumns(10);
                JTextArea lastBidId = new JTextArea(String.valueOf(i.getLastBidID()));
                lastBidId.setColumns(10);

                oneItem.add(sellerId,constr);
                oneItem.add(itemID,constr);
                oneItem.add(itemTitle,constr);
                oneItem.add(itemDescription,constr);
                oneItem.add(itemCondition,constr);
                oneItem.add(currentPrice,constr);
                oneItem.add(lastBidId,constr);
                
                int color=0;
                if(color%2==0){
                    oneItem.setForeground(Color.GRAY);
                }
                else{
                    oneItem.setForeground(Color.WHITE);
                }
            return oneItem;

    }
}