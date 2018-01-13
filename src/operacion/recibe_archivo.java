package operacion;
import java.net.*;
import java.io.*;
import javax.swing.*;

public class recibe_archivo extends Thread{
    String ruta=null;
    ServerSocket s=null;
    public recibe_archivo(String r,ServerSocket s){
        this.s=s;
        ruta=r;
    }
    public void run(){
        try{ 			
            System.out.println("Servidor de archivos iniciado... Esperando Conexion\n");
            //for(;;) {
                Socket cl = s.accept();
                System.out.println("Conexion establecida desde: "+cl.getInetAddress()+":"+cl.getPort());
                DataInputStream dis = new DataInputStream(cl.getInputStream());
                byte[] b = new byte[100000];
                String nombre = dis.readUTF();
                System.out.println("Recibiendo el archivo"+nombre);
                long tam = dis.readLong();
                DataOutputStream dos = new DataOutputStream(new FileOutputStream(new File(ruta+nombre)));
                long recibidos = 0;
                int n,porcentaje;
                while(recibidos<tam) {
                    n=dis.read(b);
                    dos.write(b,0,n);
                    dos.flush();
                    recibidos = recibidos+n;
                    porcentaje = (int)((recibidos*100)/tam);
                    System.out.println("\r Completado"+porcentaje+"%");
                } //while
                System.out.println("\n Archivo recibido");
                dos.close();
                dis.close();
                cl.close();
            //} //for
        } catch(Exception e) {
            e.printStackTrace();
        } //catch
    }
    
    /*public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(7000);
        recibe_archivo ra=new recibe_archivo("",ss);
        ra.start();
    } //main*/
} //recibe_archivo