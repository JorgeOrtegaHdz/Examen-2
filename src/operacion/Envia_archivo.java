package operacion;
import java.net.*;
import java.io.*;
import javax.swing.*;

public class Envia_archivo extends Thread{
    private String host;
    private int pto;
    public Envia_archivo(String dir,int pto){
        host=dir;
        this.pto=pto;
    }
    public void run(){
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            DataOutputStream dos = null;
            DataInputStream dis = null;
            Socket cl=null;
            JFileChooser jf = new JFileChooser();
            jf.setMultiSelectionEnabled(true);
            int r = jf.showOpenDialog(null);
            if(r == JFileChooser.APPROVE_OPTION) {
            	File[] f = jf.getSelectedFiles();
            	for(int x = 0; x < f.length; x++){
                    cl = new Socket(host,pto);
                    String archivo = f[x].getAbsolutePath();
                    String nombre = f[x].getName();
                    long tam = f[x].length();
                    dos = new DataOutputStream(cl.getOutputStream());
                    dis = new DataInputStream(new FileInputStream(archivo));
                    dos.writeUTF(nombre);
                    dos.flush();
                    dos.writeLong(tam);
                    dos.flush();
                    byte[] b = new byte[100000];
                    long enviados = 0 ;
                    int porcentaje,n;
                    while(enviados<tam){
                        n = dis.read(b);
                        dos.write(b,0,n);
                        dos.flush();
                        enviados = enviados+n;
                        porcentaje = (int)((enviados*100)/tam);
                        System.out.println("Estado del envio: "+porcentaje+"%\r");
                    }//while
                    System.out.println("*** Archivo numero "+(x+1)+" de "+f.length+" con el nombre: "+nombre+" ***");
                    dos.close();
                    dis.close();
                    cl.close();
                }//FOR FILES[]
            }//if
        } catch(Exception e) {
            e.printStackTrace();
	} //catch
    }
    /*public static void main(String[] args) {
        Envia_archivo ea=new Envia_archivo("localhost",7000);
        ea.start();
    } //main*/
} //Envia_archivo