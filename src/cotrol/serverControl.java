/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cotrol;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import model.User;

/**
 *
 * @author admin
 */
public class serverControl {
    private int clientPort=7777;
    private int serverPort=8888;
    private DatagramSocket socket;
    private DatagramPacket receivePacket, sendPacket;
    private Dao dao;
    private String host="localhost";

    public serverControl() {
        open();
        dao= new Dao();
        while (true) {            
            listenning();
        }
    }
    
    public void listenning(){
        Object o= receiveData();
        if(o instanceof Integer){
            int id= (Integer)o;
            boolean delete= dao.deleteUser(id);
            sendData(delete);
        }
        else{
            User u= (User)o;
            boolean update= dao.updateUser(u);
            sendData(update);
        }
    }
    
    public void open(){
        try {
            socket= new DatagramSocket(serverPort);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Object receiveData(){
        Object o= new Object();
        try {
            byte[] dataRe= new byte[1024];
            receivePacket= new DatagramPacket(dataRe, dataRe.length);
            socket.receive(receivePacket);
            ByteArrayInputStream bais= new ByteArrayInputStream(dataRe);
            ObjectInputStream ois= new ObjectInputStream(bais);
            o=ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return o;
    }
    
    public void sendData(Object o){
        // tao bo dem chua du lieu gui
        try {
            ByteArrayOutputStream baos= new ByteArrayOutputStream();
            ObjectOutputStream oos= new ObjectOutputStream(baos);
            oos.writeObject(o);
            oos.flush();
            // tao bo dem chuyen du lieu chuyen di thanh byte
            byte[] dataSe= baos.toByteArray();
            sendPacket= new DatagramPacket(dataSe, dataSe.length, receivePacket.getAddress(), receivePacket.getPort());
            socket.send(sendPacket);
        
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}
