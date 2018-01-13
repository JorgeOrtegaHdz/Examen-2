package operacion;
import java.net.*;
import java.io.*; 
 
public class ServMulti1 extends Thread{
	
    public static final String MCAST_ADDR = "230.0.0.1";//dir clase D valida, grupo al que nos vamos a unir
    public static final int MCAST_PORT = 9013;
    public static final int DGRAM_BUF_LEN = 512;
    private int PUERTO=0;
    private String DIR=null;
        public ServMulti1(int pto,String dir){
            PUERTO=pto;
            DIR=dir;
        }

	public void run(){
    	String msg = ""; // se cambiara para poner la ip de la maquina con lo siguiente
    	InetAddress group = null; //Inicia en nulo
    	
		try{
		msg =PUERTO+DIR;//InetAddress.getLocalHost().getHostAddress();
    	group = InetAddress.getByName(MCAST_ADDR); //se trata de resolver dir multicast  		
    	}catch(UnknownHostException e){
    		e.printStackTrace();
    		System.exit(1);
    	}
        MulticastSocket socket=null;
	 /********************inicia loop***************************/
    	try{
            socket = new MulticastSocket(MCAST_PORT);
            socket.joinGroup(group); // se configura para escuchar el paquete
            for(;;){
    		DatagramPacket packet = new DatagramPacket(msg.getBytes(),msg.length(),group,MCAST_PORT);
    		System.out.println("Enviando:"+msg);//"  con un TTL= "+socket.getTimeToLive());
    		socket.send(packet);
                try{
                    Thread.sleep(1000*5);
                }catch(InterruptedException ie){}
            }    		
    	}catch(IOException e){
    		e.printStackTrace();
    		System.exit(2);
    	}finally{
            socket.close();
        }
/*****************termina Loop***************************/    	
}//run
    	
    /*public static void main(String[] args) {

	try{
	    ServMulti1 mc = new ServMulti1(9000,"125.34.5.1");
	    mc.start();
	}catch(Exception e){e.printStackTrace();}

    }//main*/
}//class