import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.io.InputStream;
import javax.crypto.SealedObject;
import javax.sound.midi.Receiver;
import javax.swing.text.View;
import java.util.LinkedList;
import java.util.List;


import org.jgroups.Message.*;
import org.jgroups.JChannel;
import org.jgroups.ObjectMessage;
import org.jgroups.util.*;
import org.jgroups.blocks.cs.ReceiverAdapter;
import org.jgroups.blocks.*;
import org.jgroups.*;


import java.util.*;
import java.io.*;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;


public class Server extends java.rmi.server.UnicastRemoteObject implements AuctionInterface, org.jgroups.Receiver{

    private JChannel channel;
    private RpcDispatcher dispatcher;
    private String props;
    private RequestOptions options = new RequestOptions(ResponseMode.GET_ALL,5000).setTransientFlags(TransientFlag.DONT_LOOPBACK);
    private ArrayList<Integer>users = new ArrayList<>();
    private byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    private IvParameterSpec ivspec = new IvParameterSpec(iv);


    public List getValid(RspList list){

        List ret = list.getResults();
        return ret;

    }

    public AuctionItem getSpecI(int itemId, int clientId) {
        try{
            MethodCall call = new MethodCall(getClass().getMethod("getSpecI",int.class,int.class));
            RspList list= dispatcher.callRemoteMethods(null,"getSpecI",new Object[]{itemId,clientId}, new Class[]{int.class,int.class},options);
            List valid = getValid(list);
            AuctionItem item = (AuctionItem)valid.get(0);
            return item;
            
        }catch(Exception nsme){
            nsme.printStackTrace();
            return null;
        }
    }
    public SealedObject getSpec(int itemId, SealedObject clientRequest){
        try{
            MethodCall call = new MethodCall(getClass().getMethod("getSpec",int.class,int.class));
            RspList list = dispatcher.callRemoteMethods(null,"getSpec",new Object[]{itemId,clientRequest}, new Class[]{int.class,SealedObject.class},options);
            List valid = getValid(list);
            SealedObject spec = (SealedObject)valid.get(0);
            return spec;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    int secretCode;
    int codeAfterFormula;
    public int startAuthentication(){
        Random rand = new Random();
        secretCode = rand.nextInt(9000000)+1000000;
        System.out.println("attempts");
        codeAfterFormula = secretCode*secretCode;
        return secretCode;
    }


    public SealedObject receiveResponse(SealedObject response, int i){
        try{
            if(decryptM(response) == codeAfterFormula){
                System.out.println(decryptM(response) + " | " + codeAfterFormula);
            return encryptM(i*3);
            
            }else return null;
        }catch(BadPaddingException e){
            e.printStackTrace();
            return null;
        } 
    }
    public Integer giveMeUniqueId(){
        Random rand = new Random();
        int uniq= rand.nextInt(900)+100;
        while(!uniqu(uniq)){
            uniq = rand.nextInt(900)+100;
        }
        return uniq;
    }

    public boolean uniqu(int i){
        ArrayList<AuctionItem> items =getDatabase();
        for (AuctionItem auctionItem : items) {
            if(auctionItem.getId() == i){
                return false;
            }
        }
        return true;
    }
    public SealedObject checkID(SealedObject clientid) throws java.rmi.RemoteException{
        try{
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            int checkUnique = decryptM(clientid);
            cipher.init(Cipher.ENCRYPT_MODE, getTheKey(),ivspec);
            for(Integer i:users){
                if(checkUnique == i){
                    SealedObject sendt = new SealedObject(false,cipher);
                    return sendt;
                }
            }
        SealedObject sendf = new SealedObject(true, cipher);
        users.add(checkUnique);
        return sendf;
        }
        catch(NoSuchAlgorithmException | InvalidKeyException | IOException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e){
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
        try{
            RspList list = dispatcher.callRemoteMethods(null,"addItemtoSell",new Object[]{item} , new Class[]{AuctionItem.class},options);
            List valid = getValid(list);
            boolean toSell = (boolean)valid.get(0);
            return toSell;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public SecretKey getTheKey(){
        try{
            RspList list = dispatcher.callRemoteMethods(null,"getTheKey",new Object[]{}, new Class[]{},options);
            List valid = getValid(list);
            SecretKey key = (SecretKey)valid.get(0);
            return key;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
        
    }

    @SuppressWarnings("unchecked")
    public ArrayList<AuctionItem> getDatabase(){
        try{

            RspList list = dispatcher.callRemoteMethods(null,"getDatabase",new Object[]{}, new Class[]{},options);
            List valid = getValid(list);
            ArrayList<AuctionItem> database = (ArrayList<AuctionItem>)valid.get(0);
            return database;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    @SuppressWarnings("unchecked")
    public ArrayList<AuctionItem> getMyItems(int sellerId){
        try{
            RspList list = dispatcher.callRemoteMethods(null,"getMyItems",new Object[]{sellerId},new Class[]{int.class},options);
            List valid = getValid(list);
            ArrayList<AuctionItem> items= (ArrayList<AuctionItem>)valid.get(0);
            return items;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    @SuppressWarnings("unchecked")
    public boolean removeAuctionItem(int iid){
        try{

            RspList list = dispatcher.callRemoteMethods(null,"removeAuctionItem",new Object[]{iid},new Class[]{int.class},options);
            List valid = getValid(list);
            boolean remov = (boolean)valid.get(0);
            return remov;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    @SuppressWarnings("unchecked")
    public boolean bidOnItme(int itemID,double price,int clientID){
        try{

            RspList list = dispatcher.callRemoteMethods(null,"bidOnItme", new Object[]{itemID,price,clientID}, new Class[]{int.class,double.class,int.class},options);
            List valid = getValid(list);
            boolean bid = (boolean)valid.get(0);
            return bid;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public Server() throws java.rmi.RemoteException{
        try{
            start();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void simplyConnect(){
        try{

            JChannel channel = new JChannel(); 
            channel.setDiscardOwnMessages(true);
            channel.setReceiver(this);
            dispatcher = new RpcDispatcher(channel,this); 
            channel.connect("auctionCluster");


        }catch(Exception j){
            j.printStackTrace();
        }    
    }
    public final void start() {
        
        simplyConnect();
        
    }
    public void viewAccepted(View new_view){
        System.out.println("view "+new_view);
    }

    public void simplyDisconnect(){
        channel.disconnect();
        channel.close();
        dispatcher.stop();
    }
}