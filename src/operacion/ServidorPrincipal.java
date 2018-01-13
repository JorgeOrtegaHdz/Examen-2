package operacion;

import java.io.*;
import java.net.*;
import javax.swing.JOptionPane;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class ServidorPrincipal {
    public int permiso;
    public int permitidos;
    public ReentrantLock rl;
    public static String rt,dir;
    public static int pt;
    public String ruta=null,direccion=null;
    public int pto=0;
    public static ServidorPrincipal sp;
    public ServerSocket ss;
    public ServidorPrincipal(int npermiso){
        permiso=0;
        permitidos=npermiso;
        rl=new ReentrantLock();
        direccion=JOptionPane.showInputDialog(null, "Dame la direccion del servidor","127.1.2.3");
        pto=Integer.parseInt(JOptionPane.showInputDialog(null, "Dame el puerto", "4567"));
        ruta=JOptionPane.showInputDialog(null, "Dame la ruta de la carpeta", "C:/Archivos");
        rt=ruta;
        dir=direccion;
        pt=pto;
    }
    
    public void getPermiso(){
        try{
            System.out.println("Cliente trata de tener acceso");
            rl.lock();
            permiso++;
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void echo(){
        try{
            rl.lock();
            permiso--;
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static void main(String[]args){
        sp=new ServidorPrincipal(5);
        ServMulti2 anuncia=new ServMulti2(rt);
        anuncia.start();
        ServElimina se=new ServElimina(sp.ruta);
        se.start();
        try{
            ServerSocket s=new ServerSocket(pt,10,InetAddress.getByName(dir));
            sp.ss=s;
            ServMulti1 anunciaCliente=new ServMulti1(pt,s.getInetAddress().getHostAddress());
            anunciaCliente.start();
            System.out.println("Servidor Iniciado... Esperando Conexion\n");
            for(;;){
                Socket cl=s.accept();
                if(cl!=null){
                    Servidor serv=new Servidor(cl,sp,anuncia);
                    serv.start();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
