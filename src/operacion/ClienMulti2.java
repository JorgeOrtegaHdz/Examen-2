package operacion;
import static operacion.ServMulti2.MCAST_PORT;
import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import java.lang.*;
 
public class ClienMulti2 extends Thread {
	
 	public static final String MCAST_ADDR  = "230.0.0.2"; //dir clase D 
 	public static final int MCAST_PORT = 9014; //puerto multicast
 	public static final int DGRAM_BUF_LEN = 256; //tamaño del buffer
 	//public static final int DGRAM_BUF_LEN = 512; //tamaño del buffer
	int Puerto = 0;
        public ArrayList lista;

	public void run(){
   	InetAddress group =null;
    	try{
    		group = InetAddress.getByName(MCAST_ADDR);//intenta resolver la direccion
    	}catch(UnknownHostException e){
    		e.printStackTrace();
    		System.exit(1);
	}
	
	Vector d = new Vector();
	int c = 0,intentos=0;
        String opc="1";
	
	boolean salta = true;
	
    	try{
                lista=new ArrayList();
    		MulticastSocket socket = new MulticastSocket(MCAST_PORT); //socket tipo multicast
    		socket.joinGroup(group);//se une al grupo
			int cd = 0;
			while(salta){
    			byte[] buf = new byte[DGRAM_BUF_LEN];//crea arreglo de bytes 
                        DatagramPacket packet = new DatagramPacket(opc.getBytes(),opc.length(),group,MCAST_PORT);
                        socket.send(packet);
    			DatagramPacket recv = new DatagramPacket(buf,buf.length);//crea el datagram packet a recibir
    			socket.receive(recv);// ya se tiene el datagram packet
    			
				//byte [] data = recv.getData(); //aqui no se entienden los datos
				String temp = new String(recv.getData(),0,recv.getLength());
                                if(!temp.equals("1")){
                                    System.out.println("Archivos del servidor x: "+temp);
                                    if(noExiste(temp)){
                                        lista.add(temp);
                                        c++;
                                    }else{
                                        intentos++;
                                    }
                                }
				if(intentos>c*2){
					salta = false;
				} 
				
		}
    	}catch(IOException e){
    		e.printStackTrace();
    		System.exit(2);
    	}

	}//run
    public boolean noExiste(String var){
        boolean ban=true;
        for(int i=0;i<lista.size();i++){
            if(lista.get(i).toString().equals(var)){
                ban=false;
            }
        }
        return ban;
    }
	
    /*public static void main(String[] args) {
 
	try{
	    ClienMulti2 mc = new ClienMulti2();
	    mc.start();
	}catch(Exception e){e.printStackTrace();}
	}//main*/

}//class