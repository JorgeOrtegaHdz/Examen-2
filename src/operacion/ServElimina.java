package operacion;
import java.net.*;
import java.io.*; 
import java.util.logging.Level;
import java.util.logging.Logger;
import static operacion.ServMulti2.MCAST_PORT;
 
public class ServElimina extends Thread{
	
    public static final String MCAST_ADDR = "230.0.0.2";//dir clase D valida, grupo al que nos vamos a unir
    public static final int MCAST_PORT = 9015;
    public static final int DGRAM_BUF_LEN = 256;
    String ruta,m="";
    
    public ServElimina(String ruta){
        this.ruta=ruta;
    }
    
    public void run(){
        System.out.println();
    	InetAddress group = null; //Inicia en nulo
	try {
            group = InetAddress.getByName(MCAST_ADDR); //se trata de resolver dir multicast  		
    	} catch(UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
    	}
	int c = 0,envio=1;
	for(;;){
            try{
                MulticastSocket socket = new MulticastSocket(MCAST_PORT);
    		socket.joinGroup(group); // se configura para escuchar el paquete
                byte[] buf = new byte[DGRAM_BUF_LEN];
                DatagramPacket recv = new DatagramPacket(buf,buf.length);//crea el datagram packet a recibir
    		socket.receive(recv);// ya se tiene el datagram packet
		String temp = new String(recv.getData(),0,recv.getLength());
		if(temp!=null){
                    System.out.println("Se trata de eliminar: "+temp);
                    if(trataEliminar(temp)){
                        m="1";
                        System.out.println("Se elimino correctamente");
                        envio=50;
                    }else{
                        m="no";
                    }
                    DatagramPacket packet = new DatagramPacket(m.getBytes(),m.length(),group,MCAST_PORT);
                    for(int i=0;i<envio;i++){
                        socket.send(packet);
                        Thread.sleep(500);
                        System.out.println("Paquete enviado: "+m);
                    }
                }
                socket.close();
    	}catch(IOException e){
    		e.printStackTrace();
    		System.exit(2);
    	}   catch (InterruptedException ex) {
                Logger.getLogger(ServElimina.class.getName()).log(Level.SEVERE, null, ex);
            }
	try{
		Thread.sleep(1000*5);
	}catch(InterruptedException ie){}
        }//for
    }//run
    public boolean trataEliminar(String dato){
        File dir=new File(ruta);
        File eliminado=null;
        String archivos[]=dir.list();
        for(int i=0;i<archivos.length;i++){
            if(archivos[i].toString().toUpperCase().equals(dato.toUpperCase())){
                eliminado=new File(ruta+"//"+dato);
            }
        }
        if(eliminado!=null){
            eliminado.delete();
            return true;
        }else{
            return false;
        }
    }
    /*public static void main(String[] args) {

	try{
	    //ServMulti2 mc = new ServMulti2("D:\\Utilidades de scripts en C");
            ServMulti2 mc = new ServMulti2("C:\\Archivos");
	    mc.start();
	}catch(Exception e){e.printStackTrace();}

    }//main*/
}//class