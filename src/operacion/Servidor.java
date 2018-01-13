package operacion;
import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor extends Thread{
    Socket cl;
    ServidorPrincipal sp;
    public Servidor(Socket c, ServidorPrincipal s,ServMulti2 an){
        cl=c;
        sp=s;
    }
    public boolean trataEliminar(String dato){
        File dir=new File(sp.ruta);
        File eliminado=null;
        String archivos[]=dir.list();
        for(int i=0;i<archivos.length;i++){
            if(archivos[i].toString().toUpperCase().equals(dato.toUpperCase())){
                eliminado=new File(sp.ruta+"//"+dato);
            }
        }
        if(eliminado!=null){
            eliminado.delete();
            return true;
        }else{
            return false;
        }
    }
    public void run(){
        boolean ban=false;
        try{
            ObjectOutputStream oos=new ObjectOutputStream(cl.getOutputStream());
            ObjectInputStream ois=new ObjectInputStream(cl.getInputStream());
            sp.getPermiso();
            if(sp.permiso>sp.permitidos){
                oos.writeBoolean(ban);
                oos.flush();
            }else{
                ban=true;
                oos.writeBoolean(ban);
                oos.flush();
                sp.rl.unlock();
            }
            if(!ban){
                sp.echo();
                cl.close();
                System.exit(0);
            }
            System.out.println("Conexion establecida desde: "+cl.getInetAddress()+":"+cl.getPort());
            int msj= 10;
            while(msj!=0){
                msj=ois.readInt();
                System.out.println("Cliente envio: "+msj);
                switch(msj){
                    case 0:
                        sp.echo();
                    break;
                    case 1:
                        ClienMulti2 lista=new ClienMulti2();
                        lista.run();
                        lista.join();
                        oos.writeObject(lista.lista);
                        oos.flush();
                    break;
                    case 2:
                        recibe_archivo rc=new recibe_archivo(sp.ruta+"/",sp.ss);
                        rc.start();
                    break;
                    case 3:
                        String suprim=ois.readUTF();
                        if(trataEliminar(suprim)){
                            oos.writeBoolean(true);
                            oos.flush();
                        }else{
                            ServPideEliminar spe=new ServPideEliminar(suprim);
                            spe.start();
                            spe.join();
                            if(spe.ban){
                                oos.writeBoolean(true);
                                oos.flush();
                            }else{
                                oos.writeBoolean(false);
                                oos.flush();
                            }
                        }
                    break;
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }finally{
            try{
                cl.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
