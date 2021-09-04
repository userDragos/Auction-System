
public class AuctionItem implements java.io.Serializable{ 

    private int itemId;
    private String itemTitle;
    private String itemDescription;
    private String itemCondition;
    private double price;
    private int sellerID;
    private int lastBidID;
    private int reservedPrice;
    private String winnerEmail;
    private String winnerName;

    public String getWinnerEmail(){
        return winnerEmail;
    }
    public String getWinnerName(){
        return winnerName;
    }

    public int getId(){
        return itemId;
    }

    public String getTitle(){
        return itemTitle;
    }

    public String getDescription(){
        return itemDescription;
    }

    public String getCondition(){
        return itemCondition;
    }

    public double getPrice(){
        return price;
    }
    public void setPrice(double price2){
        price = price2;
    }

    public int getSellerId(){
        return sellerID;
    }

    public int getLastBidID(){
        return lastBidID;
    }
    public void setLastBidId(int i){
        lastBidID = i;
    }

    public int getReservedPrice(){
        return reservedPrice;
    }

    public AuctionItem(int id, String title, String description, String condition, double p,int seller,int res){
        itemId = id;
        itemTitle = title;
        itemDescription = description;
        itemCondition = condition;
        price = p;
        sellerID = seller;
        reservedPrice = res;
    } 
}