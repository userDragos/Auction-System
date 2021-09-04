import java.util.*;
import java.io.*;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class ClientRequest implements java.io.Serializable {
    SecretKey theKey;
    byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    IvParameterSpec ivspec = new IvParameterSpec(iv);

    public void generateaKey(){
        try{
            theKey = KeyGenerator.getInstance("AES").generateKey();
            FileWriter writer = new FileWriter("keys.txt");
            String transformKey = Base64.getEncoder().encodeToString(theKey.getEncoded());
            writer.write(transformKey);
            writer.close();
            System.out.println(theKey);
        }catch(IOException e){
            e.printStackTrace();
        }catch(NoSuchAlgorithmException alg){
            System.out.println(alg.getMessage());
        }
    }

    public SealedObject sealID(int clientID){
       
        try{
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, theKey,ivspec);
            SealedObject sealed = new SealedObject( clientID,cipher);
            return sealed;
        }
        catch(NoSuchAlgorithmException | InvalidKeyException | IOException | NoSuchPaddingException | IllegalBlockSizeException | InvalidAlgorithmParameterException e){
            e.printStackTrace();
            return null;
        }
    }
    public int decryptM(SealedObject sealed) throws BadPaddingException {
        
        try{
            Cipher decipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            decipher.init(Cipher.DECRYPT_MODE, theKey,ivspec);
            int clID = (int)sealed.getObject(decipher);
            return clID;
        }
        catch(NoSuchAlgorithmException | ClassNotFoundException | InvalidKeyException | IOException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e){
            e.printStackTrace();
        }
        return 0;
        
    }

    public AuctionItem getItem(SealedObject sealed){
        
        try{
            Cipher decipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            decipher.init(Cipher.DECRYPT_MODE, theKey,ivspec);
            AuctionItem actualItem = (AuctionItem)sealed.getObject(decipher);
            return actualItem;
        }
        catch(NoSuchAlgorithmException | InvalidKeyException | IOException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException | ClassNotFoundException e){
            e.printStackTrace();
            return null;
        }
    }

    public Boolean checkUniq(SealedObject unid){
        try{
            Cipher decipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            decipher.init(Cipher.DECRYPT_MODE, theKey,ivspec);
            boolean uniq = (boolean)unid.getObject(decipher);
            return uniq;
        }
        catch(NoSuchAlgorithmException | InvalidKeyException | IOException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException |ClassNotFoundException e){
            e.printStackTrace();
            return null;
        }
    }

    public ClientRequest(){

    }   
}