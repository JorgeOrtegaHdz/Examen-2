package operacion;
import java.io.*;
import java.net.*;

public class ClientePrincipal{
    public static void main(String args[]){
        int puer=0;
        String direc="";
        ClienMulti1 mult=new ClienMulti1();
        mult.start();
        Socket cl=null;
        try{
            while(cl==null){
                for(;;){
                    Thread.sleep(10);//Con esto forzamos el cambio de contexto
                    System.out.println("Buscando...");
                    if((mult.ban) && (mult.Puerto!=puer || direc.equals(mult.dir))){
                        puer=mult.Puerto;
                        direc=mult.dir;
                        break;
                    }
                }
                cl=new Socket(direc,puer);
                System.out.println("Buscando un servidor disponible");
            }
            if(cl!=null){
                System.out.println("Se conecto al servidor");
                ObjectOutputStream oos=new ObjectOutputStream(cl.getOutputStream());
                ObjectInputStream ois=new ObjectInputStream(cl.getInputStream());
                System.out.println("Se creo una conexion");
                if(ois.readBoolean()){
                    Cliente clien=new Cliente(cl,ois,oos);
                    clien.start();
                    clien.join();
                    oos.writeInt(0);
                    oos.flush();
                    cl.close();
                    //System.exit(0);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
