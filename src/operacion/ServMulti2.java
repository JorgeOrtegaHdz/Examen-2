package operacion;
import java.net.*;
import java.io.*; 
 
public class ServMulti2 extends Thread{
	
    public static final String MCAST_ADDR = "230.0.0.2";//dir clase D valida, grupo al que nos vamos a unir
    public static final int MCAST_PORT = 9014;
    public static final int DGRAM_BUF_LEN = 256;
	
    //public static final int DGRAM_BUF_LEN = 512;
        //File dir =new File("C:\\Users\\jorge\\Downloads");
        //File dir =new File("D:\\Utilidades de scripts en C");
	File dir;
	int NumeroDeArchivos;
        String msg[];
        
    public ServMulti2(String ruta){
        dir=new File(ruta);
        NumeroDeArchivos = dir.list().length;
    }
	public void run(){
    	InetAddress group = null; //Inicia en nulo
		try {
    	group = InetAddress.getByName(MCAST_ADDR); //se trata de resolver dir multicast  		
    	} catch(UnknownHostException e) {
    		e.printStackTrace();
    		System.exit(1);
    	}
	 /********************inicia loop***************************/
	int c = 0;
	for(;;) {
    	try{
                msg= dir.list();
    		MulticastSocket socket = new MulticastSocket(MCAST_PORT);
    		socket.joinGroup(group); // se configura para escuchar el paquete
                byte[] buf = new byte[DGRAM_BUF_LEN];
                DatagramPacket recv = new DatagramPacket(buf,buf.length);//crea el datagram packet a recibir
    		socket.receive(recv);// ya se tiene el datagram packet
		String temp = new String(recv.getData(),0,recv.getLength());
		for(c = 0;c < msg.length;c++) {
			DatagramPacket packet = new DatagramPacket(msg[c].getBytes(),msg[c].length(),group,MCAST_PORT);
    		System.out.println("Archivo["+c+"]: "+msg);//"  con un TTL= "+socket.getTimeToLive());
                    if(temp.equals("1"))
			socket.send(packet);
		}
		    socket.close();
    	}catch(IOException e){
    		e.printStackTrace();
    		System.exit(2);
    	}	
	try{
		Thread.sleep(1000*5);
	}catch(InterruptedException ie){}
    }//for;; 
		
/*****************termina Loop***************************/    	
	}//run
    	
    /*public static void main(String[] args) {

	try{
	    //ServMulti2 mc = new ServMulti2("D:\\Utilidades de scripts en C");
            ServMulti2 mc = new ServMulti2("C:\\Archivos");
	    mc.start();
	}catch(Exception e){e.printStackTrace();}

    }//main*/
}//class