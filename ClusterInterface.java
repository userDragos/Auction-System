import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.InputStream;

import javax.sound.midi.Receiver;
import javax.swing.text.View;

import java.util.LinkedList;
import java.util.List;

import org.jgroups.Message;
import org.jgroups.JChannel;
import org.jgroups.ObjectMessage;
import org.jgroups.util.*;
import org.jgroups.blocks.*;
import org.jgroups.*;


public class ClusterInterface{
    private JFrame mainframe = new JFrame("Clusters");
    public void generateInterface(){
        mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainframe.setLayout(new BorderLayout());
        mainframe.setMinimumSize(new Dimension(1000,500));

        mainframe.setLocationRelativeTo(null);
        mainframe.setVisible(true);
        addCluster();
        System.out.println("wtf");
    }

    public void showClusters(){

    }

    public void addCluster() {
        JButton button = new JButton("Add Container");
        button.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) throws java.lang.IllegalArgumentException{

                Rpc r = new Rpc();
                try{
                    r.simplyConnect();
                }catch(Exception dsfdf){

                }
                
            }
        });
        mainframe.add(button);
        SwingUtilities.updateComponentTreeUI(mainframe);
    }

    public void refresh(){
        mainframe.getContentPane().removeAll();
        mainframe.invalidate();
        mainframe.validate();
        mainframe.repaint();
        showClusters();
    }
}