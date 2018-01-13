package operacion;
import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cliente extends Thread implements ActionListener{
    public Socket cl=null;
    //Inicializacion de ls componentes de la interface
    private JFrame marco=new JFrame();
    private JPanel vista=new JPanel();
    private JPanel botones=new JPanel();
    private JTextArea area=new JTextArea();
    private JButton subir=new JButton("Subir Archivo");//Sirve para subir un archivo
    private JButton elimina=new JButton("Eliminar");//Sirve para eliminar un archivo
    private JButton lista=new JButton("Actualiza");//Sirve para listar los archivos
    private JLabel titulo=new JLabel("                Repositorio de Axel");
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    public ArrayList array;
    private JTextField eliminar=new JTextField();
    JScrollPane scrollV = new JScrollPane (area);
    JScrollBar scrolB=new JScrollBar();
    
    public Cliente(Socket cl,ObjectInputStream oi,ObjectOutputStream oo){
        this.cl=cl;
        ois=oi;
        oos=oo;
    }
    public void run(){
        creaInterface();
        try {
            Thread.sleep(3600000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void creaInterface(){
        //Configuracion de la interface
        String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        //titulo.setFont(new Font(fontNames[290],Font.PLAIN, 30));
        titulo.setFont(new Font(fontNames[40],Font.PLAIN, 30));
        titulo.setForeground(Color.BLACK);
        marco.setLayout(new BorderLayout());
        botones.setLayout(new GridLayout(1,3));
        lista.addActionListener(this);
        subir.addActionListener(this);
        elimina.addActionListener(this);
        eliminar.setEditable(false);
        botones.add(lista);
        botones.add(subir);
        botones.add(eliminar);
        botones.add(elimina); 
        vista.setLayout(new BorderLayout());
        vista.setSize(100,100);
        area.setText("Archivos Existentes");
        area.setEditable(false);
        area.setBackground(Color.ORANGE);
        area.setColumns(37);
        area.setRows(11);
        area.setForeground(Color.BLACK);
        area.setFont(new Font(fontNames[65],Font.PLAIN, 18));
        vista.add(scrollV,BorderLayout.EAST);//Se agrega el panel de scroll sin agregar el area de texto porque si no no sirve correctamente
        marco.add(titulo,BorderLayout.NORTH);
        marco.add(vista,BorderLayout.CENTER);
        marco.add(botones,BorderLayout.SOUTH);
        marco.setSize(600,400);
        marco.setVisible(true);
        marco.setTitle("Examencito");
        marco.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        marco.pack();
    }
    
    /*public static void main(String[]args){
        try{
            Cliente clien=new Cliente(null,null,null);
            clien.start();
           // clien.join();
        }catch(Exception e){
            e.printStackTrace();
        }
    }*/
    public void comunica(int opc){
        try{
            oos.writeInt(opc);
            oos.flush();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void comunicaUTF(String opc){
        try{
            oos.writeUTF(opc);
            oos.flush();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public Object lee(){
        Object ob=null;
        try{
            ob=ois.readObject();
        }catch(Exception e){
            e.printStackTrace();
        }
        return ob;
    }
    public boolean leeBool(){
        boolean ob=true;
        try{
            ob=ois.readBoolean();
        }catch(Exception e){
            e.printStackTrace();
        }
        return ob;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==lista){
            eliminar.setEditable(true);
            comunica(1);
            area.setText("Archivos Existentes");
            array=(ArrayList)lee();
            for(int i=0;i<array.size();i++){
                area.setText(area.getText()+"\n"+array.get(i).toString());
            }
        }
        if(e.getSource()==elimina){
            boolean ban=false;
            for(int i=0;i<array.size();i++){
                if(array.get(i).toString().equals(eliminar.getText())){
                    ban=true;
                }
            }
            if(ban){
                comunica(3);
                comunicaUTF(eliminar.getText());
                if(leeBool()){
                    JOptionPane.showMessageDialog(marco, "Eliminado con exito");
                    eliminar.setText("");
                    eliminar.setEditable(false);
                }else{
                    JOptionPane.showMessageDialog(marco, "No se elimino por algun dato erroneo o falta de comunicacion");
                }
            }else{
                JOptionPane.showMessageDialog(marco, "No existe el archivo");
            }
        }
        if(e.getSource()==subir){
            comunica(2);
            Envia_archivo ea=new Envia_archivo(cl.getInetAddress().toString(),cl.getPort());
            ea.start();
        }
    }
}
