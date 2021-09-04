import java.rmi.Naming;

public class AuctionServer{
    public AuctionServer(){
        try{
            AuctionInterface a = new Server();
            Naming.rebind("rmi://localhost/AuctionService",a);
        }
        catch(Exception e){
            System.out.println("Server Error:" + e);
        }
    }

    public static void main(String args[]){
        new AuctionServer();
    }
}