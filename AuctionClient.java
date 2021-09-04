import java.rmi.Naming;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.util.*;
import java.awt.GridBagConstraints;
import java.io.*;
import java.security.*;
import javax.crypto.*;
import javax.swing.*;
import java.awt.*;

public class AuctionClient extends JPanel{
    public static void main(String[] args){
        try{

            AuctionInterface transit = (AuctionInterface)Naming.lookup("rmi://localhost/AuctionService");
            Random rand = new Random();
            ClientRequest client = new ClientRequest();
            client.generateaKey();
            int secretCode = rand.nextInt(9000000)+1000000;
            int codeAfterFormula = secretCode*3;

            //5 way challenge
            int codeReceived = transit.startAuthentication();
            SealedObject challengeResponse = client.sealID(codeReceived*codeReceived);
            
            SealedObject receiveChallenge = transit.receiveResponse(challengeResponse,secretCode);
            int receivedCode = client.decryptM(receiveChallenge);
            System.out.println(codeAfterFormula +" | "+ receivedCode);
            

            if(codeAfterFormula == receivedCode){
                int clientID=0;
                
                boolean checkuniq = false;
                
                
                while (!checkuniq){ 
                    clientID = rand.nextInt(900)+100;
                    System.out.println(clientID);
                    SealedObject checkResponse = transit.checkID(client.sealID(clientID));
                    checkuniq = client.checkUniq(checkResponse);
                }

                UserInterface uinterface = new UserInterface(transit,clientID);
                uinterface.generateInterface();
            }
            
            
        }
        catch(MalformedURLException | RemoteException | NotBoundException | java.lang.ArithmeticException | BadPaddingException murle) {
            System.out.println(murle.getMessage());
        }
    }
}