import java.util.*;
import java.io.*;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.util.*;
import java.lang.Object;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.io.InputStream;
import javax.crypto.SealedObject;
import javax.sound.midi.Receiver;
import javax.swing.text.View;
import java.util.LinkedList;
import java.util.List;
import java.util.*;
import java.io.*;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import org.jgroups.blocks.cs.ReceiverAdapter;
import org.jgroups.protocols.*;
import org.jgroups.Message.*;
import org.jgroups.JChannel;
import org.jgroups.ObjectMessage;
import org.jgroups.blocks.*;
import org.jgroups.util.Util;
import org.jgroups.util.RspList;
import org.jgroups.*;


public class Auction implements org.jgroups.Receiver{



    private ArrayList<AuctionItem> items = new ArrayList<AuctionItem>();
    private ArrayList<Integer>users = new ArrayList<>();
    private byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    private IvParameterSpec ivspec = new IvParameterSpec(iv);
    int secretCode;
    int codeAfterFormula;
    
    public AuctionItem getSpecI(int itemId, int clientId) throws java.rmi.RemoteException{
           
        for(AuctionItem auc : items){
            if(auc.getId() == itemId){
                return auc;
            }
        }
        return null;
    }

    public ArrayList<AuctionItem> getDatabase(){
        return items;
    }

    public ArrayList<AuctionItem> getMyItems(int sellerId){
        ArrayList<AuctionItem> myitems = new ArrayList<AuctionItem>();
        for (AuctionItem auctionItem : items) {
            if(auctionItem.getSellerId() == sellerId){
                myitems.add(auctionItem);
            }
        }   
        return myitems;
    }
    public synchronized boolean bidOnItme(int itemID,double price,int clientID){
        for (AuctionItem auctionItem : items) {
            if(auctionItem.getId()==itemID){
                if(price>=auctionItem.getPrice()){
                    auctionItem.setPrice(price);
                    auctionItem.setLastBidId(clientID);
                    return true;
                } 
                else{
                    return false;
                }
            }
        }
        return false;
    }

    public boolean removeAuctionItem(int iid){

        for (AuctionItem auctionItem : items) {
            if(auctionItem.getId()==iid){
                items.remove(auctionItem);
                return true;
            }
        }
        return false;
    }

    public SealedObject getSpec(int itemId, SealedObject clientRequest) throws java.rmi.RemoteException{
        try{
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            int userId = decryptM(clientRequest);
            AuctionItem sendItem = this.getSpecI(itemId, userId);
              
            cipher.init(Cipher.ENCRYPT_MODE, getTheKey(),ivspec);
            SealedObject sealed = new SealedObject( sendItem,cipher);
            return sealed;
        }
        catch(NoSuchAlgorithmException | InvalidKeyException | IOException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e){
            e.printStackTrace();
            return null;
        }
        
    }

    public SecretKey getTheKey(){
        try{
            SecretKey originalKey;
            StringBuilder build = new StringBuilder();
            BufferedReader buff = new BufferedReader(new FileReader("keys.txt"));
            String data;
            
            while((data=buff.readLine()) !=null){
                build.append(data);
                
            }
            buff.close();
            String encodedKey = build.toString();
            byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
            originalKey =  new SecretKeySpec(decodedKey,0,decodedKey.length,"AES");
            return originalKey;
        }catch(FileNotFoundException e){
            e.printStackTrace();
            return null;
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }

    private int decryptM(SealedObject sealed) throws BadPaddingException {
        
        try{
            Cipher decipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            decipher.init(Cipher.DECRYPT_MODE, getTheKey(),ivspec);
            int clID = (int)sealed.getObject(decipher);
            return clID;
        }
        catch(NoSuchAlgorithmException | ClassNotFoundException | InvalidKeyException | IOException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e){
            e.printStackTrace();
        }
        return 0;
        
    }
    public SealedObject encryptM(int i){
       
        try{
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, getTheKey(),ivspec);
            SealedObject sealed = new SealedObject( i,cipher);
            return sealed;
        }
        catch(NoSuchAlgorithmException | InvalidKeyException | IOException | NoSuchPaddingException | IllegalBlockSizeException | InvalidAlgorithmParameterException e){
            e.printStackTrace();
            return null;
        }
    }

    public boolean addItemtoSell(AuctionItem item){
        if(items.add(item))
        return true;
        else return false;
    }

    public void viewAccepted(View new_view){
        System.out.println("view "+new_view);
    }
    public void getState(OutputStream output) throws Exception{
        synchronized(items){
            Util.objectToStream(items,new DataOutputStream(output));
        }
    }
    @SuppressWarnings("unchecked")
    public void setState(InputStream input)throws Exception{
        ArrayList<AuctionItem> list = (ArrayList<AuctionItem>)Util.objectFromStream(new DataInputStream(input));
        synchronized(items){
            items.clear();
            items.addAll(list);
            System.out.println(items);
        }
    }

    public Auction(){
        AuctionItem test = new AuctionItem(124,"keyboard", "whatever","new",50,404,44);
        items.add(test);

        try{

            JChannel channel = new JChannel(); 
            channel.setDiscardOwnMessages(true);
            channel.setReceiver(this);
            RpcDispatcher dis = new RpcDispatcher(channel,this);
            channel.connect("auctionCluster");
            channel.getState(null,10000);
        }catch(Exception j){
            j.printStackTrace();
        }  
    }

    public static void main(String[] args) {
        new Auction();
        
    }
}