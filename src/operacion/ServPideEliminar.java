package operacion;
import static operacion.ServMulti2.MCAST_PORT;
import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import java.lang.*;
import java.util.logging.Level;
import java.util.logging.Logger;
 
public class ServPideEliminar extends Thread {
    public static final String MCAST_ADDR  = "230.0.0.2"; //dir clase D 
    public static final int MCAST_PORT = 9015; //puerto multicast
    public static final int DGRAM_BUF_LEN = 256; //tamaño del buffer
    int Puerto = 0;
    public ArrayList lista;
    private String arch;
    public boolean ban=false;
    
    public ServPideEliminar(String archivo){
        arch=archivo;
    }

    public void run(){
        ban=false;
    	InetAddress group =null;
    	try{
            group = InetAddress.getByName(MCAST_ADDR);//intenta resolver la direccion
    	}catch(UnknownHostException e){
            e.printStackTrace();
            System.exit(1);
	}
	Vector d = new Vector();
	int c = 1,intentos=0;
        String opc="2";
	boolean salta = true;
    	try{
            lista=new ArrayList();
            MulticastSocket socket = new MulticastSocket(MCAST_PORT); //socket tipo multicast
            socket.joinGroup(group);//se une al grupo
            socket.setTimeToLive(255);
            while(salta){
    		byte[] buf = new byte[DGRAM_BUF_LEN];//crea arreglo de bytes 
                DatagramPacket packet = new DatagramPacket(arch.getBytes(),arch.length(),group,MCAST_PORT);
    		socket.send(packet);// ya se tiene el datagram packet
                DatagramPacket recv = new DatagramPacket(buf,buf.length);//crea el datagram packet a recibir
                System.out.println("tamaño paquete: "+recv.getLength());
		String temp = new String(recv.getData(),0,recv.getLength());
                if(!temp.equals(arch) && !temp.equals("")){
                    System.out.println("Se resivio un packete: "+temp);
                    if(temp.equals("1")){
                        ban=true;
                        c=intentos+1;
                        System.out.println("Ya se resibio confirmacion");
                    }else if(temp.equals("no")){
                        c++;
                    }else{
                        intentos++;
                    }
                }
                Thread.sleep(1000);
		if(intentos>c*5){
                    salta = false;
		}
            }
    	}catch(IOException e){
            e.printStackTrace();
            System.exit(2);
    	} catch (InterruptedException ex) {
            Logger.getLogger(ServPideEliminar.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//run
	
    /*public static void main(String[] args) {
	try{
	    ClienMulti2 mc = new ClienMulti2();
	    mc.start();
	}catch(Exception e){e.printStackTrace();}
	}//main*/

}//class
