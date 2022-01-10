/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cotrol;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.User;

/**
 *
 * @author admin
 */
// UDP: chung DatagramSocket socket, chung host
// khac port: clientPort, serverPort
// gui du loei DatagramPacket clientPacket, serverPacket
public class clientCotrol {
    private int clientPort=7777;
    private int serverPort=8888;
    private String host="localhost";
    private DatagramSocket socket;
    private DatagramPacket receivePacket, sendPacket;

    public clientCotrol() {
        open();
    }
    
    public void open(){
        try {
            socket= new DatagramSocket(clientPort);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void close(){
        try {
            socket.close();
        } catch (Exception e) {
        }
    }
    // UDP chi gui va nhan nhung du lieu dang byte
    public void sendId(int id){
        // bo dem chua du lieu gui di
        try {
            ByteArrayOutputStream baos= new ByteArrayOutputStream();
            ObjectOutputStream oos= new ObjectOutputStream(baos);
            oos.writeObject(id);
            // tao bo dem chuyen du lieu chuyen di thanh dang byte
            byte[] dataSe= baos.toByteArray();
            InetAddress IpAddress= InetAddress.getByName(host);
            sendPacket= new DatagramPacket(dataSe, dataSe.length, IpAddress, serverPort);
            socket.send(sendPacket);
        } catch (IOException ex) {
            Logger.getLogger(clientCotrol.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void sendUser(User u){
        try {
            ByteArrayOutputStream baos= new ByteArrayOutputStream();
            ObjectOutputStream oos=  new ObjectOutputStream(baos);
            oos.writeObject(u);
            byte[] dataSe= baos.toByteArray();
            InetAddress IpAddress= InetAddress.getByName(host);
            sendPacket= new DatagramPacket(dataSe, dataSe.length, IpAddress, serverPort);
            socket.send(sendPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    // vi hai cai nay deu tra ve true, false nghia la co thuc hien duoc hay ko
    public boolean receiveMess(){
        boolean check= false;
        try {
            byte[] dataRe= new byte[1024];
            receivePacket= new DatagramPacket(dataRe, dataRe.length);
            socket.receive(receivePacket);
            ByteArrayInputStream bais= new ByteArrayInputStream(dataRe);
            ObjectInputStream ois= new ObjectInputStream(bais);
            check=(boolean)ois.readObject();
        } catch (Exception e) {
        }
        return check;
    }
}
