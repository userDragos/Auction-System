import java.util.*;
import java.io.*;
import java.security.*;
import javax.crypto.*;
import java.lang.Object;

public interface AuctionInterface extends java.rmi.Remote {
    public AuctionItem getSpecI(int itemId, int clientId) throws java.rmi.RemoteException;

    public SealedObject getSpec(int itemId, SealedObject clientRequest) throws java.rmi.RemoteException;

    public SealedObject checkID(SealedObject clientid) throws java.rmi.RemoteException;

    public boolean addItemtoSell(AuctionItem item) throws java.rmi.RemoteException;
    public ArrayList<AuctionItem> getDatabase() throws java.rmi.RemoteException;
    public ArrayList<AuctionItem> getMyItems(int sellerId) throws java.rmi.RemoteException;
    public boolean removeAuctionItem(int iid) throws java.rmi.RemoteException;
    public boolean bidOnItme(int itemID,double price,int clientID) throws java.rmi.RemoteException;
    public Integer giveMeUniqueId() throws java.rmi.RemoteException;
    public int startAuthentication() throws java.rmi.RemoteException;
    public SealedObject receiveResponse(SealedObject response, int i) throws java.rmi.RemoteException;
}