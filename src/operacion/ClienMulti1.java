package operacion;
import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import java.lang.*;
import java.util.logging.Level;
import java.util.logging.Logger;
 
public class ClienMulti1 extends Thread {
	
 	public static final String MCAST_ADDR  = "230.0.0.1"; //dir clase D 
 	public static final int MCAST_PORT = 9013; //puerto multicast
 	public static final int DGRAM_BUF_LEN = 512; //tamaño del buffer
	int Puerto = 0;
        public String dir="";
        public boolean ban=false;

	public void run(){
   	InetAddress group =null;
    	try{
    		group = InetAddress.getByName(MCAST_ADDR);//intenta resolver la direccion
    	}catch(UnknownHostException e){
    		e.printStackTrace();
    		System.exit(1);
	}
	
	Vector d = new Vector();
	
	boolean salta = true;	
    	try{
    		MulticastSocket socket = new MulticastSocket(MCAST_PORT); //socket tipo multicast
    		socket.joinGroup(group);//se une al grupo
			int cd = 0;
    		
			while(salta){
    			byte[] buf = new byte[DGRAM_BUF_LEN];//crea arreglo de bytes 
    			DatagramPacket recv = new DatagramPacket(buf,buf.length);//crea el datagram packet a recibir
    			socket.receive(recv);// ya se tiene el datagram packet
    			byte [] data = recv.getData(); //aqui no se entienden los datos
				String temp = new String(data);
				String temp3 = temp.substring(0,4);
                                int temp2=Integer.parseInt(temp3);
                                String temp4 =temp.substring(4,14);
				if((Puerto != temp2) || (!dir.equals(temp4))){
					actualizaPuerto(temp2);
                                        actualizaDir(temp4);
                                        System.out.println("Puerto Actual = "+Puerto);
                                        System.out.println("Direccion Actual = "+dir);
                                        ban=true;
                                        Thread.sleep(1000);
				}else{
                                    ban=false;
                                }
		}
    	}catch(IOException e){
    		e.printStackTrace();
    		System.exit(2);
    	}    catch (InterruptedException ex) {
                 Logger.getLogger(ClienMulti1.class.getName()).log(Level.SEVERE, null, ex);
             }

	}//run
 	public void actualizaDir(String x){
            dir = x;
	}
        
	public void actualizaPuerto(int x){
		Puerto = x;
	}
	
    /*public static void main(String[] args) {
 
	try{
	    ClienMulti1 mc = new ClienMulti1();
	    mc.start();
	}catch(Exception e){e.printStackTrace();}
    }//main*/

}//class