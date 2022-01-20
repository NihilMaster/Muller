import java.io.IOException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;

/**
 *
 * @author M
 */
public class Muller extends JFrame{
    ////////////////////////////////////////////////////////////////
    /////////////La función se agrega a continuación////////////////
    ////////////////////////////////////////////////////////////////
    private static final String FUN_STR = "x^3 - 9x^2 + 25x(1+(sen^2(x)/25)) + x/sec^2(x) - 24 ";
    ////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////
    public static float f(float x){
        return (float)(Math.pow(x,3)-(9*Math.pow(x,2))+(25*x+(x*Math.pow(Math.sin(x), 2)))+(x*Math.pow(Math.cos(x), 2))-24); 
    }
    ////////////////////////////////////////////////////////////////
    ////////////// No. Iteraciones para tabulación /////////////////
    private static final int IT = 500;
    ////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////
    
    JFrame frame;
    JPanel panelSuperior, panelInferior;
    JButton tabtt, robtt;
    JLabel descrip, albl, blbl, flbl;
    JTextField atxt, btxt;
    JTable table1, table2;
    
    private static String fin = "", err = "";
    
    public Muller() {
        super("M\u00FCller");
        
        // PANEL SUPERIOR
        panelSuperior = new JPanel ();
        panelSuperior.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelSuperior.setLayout(new GridLayout(2,2,8,8));
        albl = new JLabel("Ingrese el menor valor:");
        atxt = new JTextField("0",20);
        blbl = new JLabel("Ingrese el mayor valor:");
        btxt = new JTextField("0",20);
        panelSuperior.add(albl); panelSuperior.add(atxt);
        panelSuperior.add(blbl); panelSuperior.add(btxt);
        // Estilo
        panelSuperior.setBackground(Color.GRAY); 
    
        // PANEL INFERIOR
        panelInferior= new JPanel();
        panelInferior.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelInferior.setLayout(new BoxLayout(panelInferior, BoxLayout.PAGE_AXIS));
        tabtt = new JButton("Calcular");
        robtt = new JButton("Encontrar ra\u00EDz");
        table1 = new JTable(); table2 = new JTable();
        flbl = new JLabel("");
        JScrollPane scrollPanel1 = new JScrollPane(table1);
        JScrollPane scrollPanel2 = new JScrollPane(table2);
        // Estilo
        scrollPanel2.setVisible(false);
        panelInferior.setBackground(Color.GRAY);
        tabtt.setBackground(Color.YELLOW);
        robtt.setVisible(false); robtt.setBackground(Color.YELLOW);
        // Adici�n
        panelInferior.add(tabtt);
        panelInferior.add(robtt);
        panelInferior.add(scrollPanel1); panelInferior.add(scrollPanel2);
        panelInferior.add(flbl);
        
        // DESCRIPCI�N
        descrip = new JLabel("Introduzca el intervalo");
        descrip.setHorizontalAlignment(SwingConstants.LEFT);
        descrip.setFont(new Font("Arial", Font.BOLD, 20));
        
        // GENERAL
        frame =new JFrame("M\u00FCller     |     Mateo L. - Weimar J.");
        frame.setLayout(new BoxLayout(frame.getContentPane(),BoxLayout.Y_AXIS));
        frame.add(descrip);
        frame.add(panelSuperior);
        frame.add(panelInferior);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        //Escuchadores
        tabtt.addActionListener((ActionEvent e) -> {
            String astr = atxt.getText();
            String bstr = btxt.getText();
            boolean ab=true,bb=true;
            try {Float.parseFloat(astr);} catch (NumberFormatException excepcion) {ab=false;} 
            try {Float.parseFloat(bstr);} catch (NumberFormatException excepcion) {bb=false;}
            if(ab && bb){
                Object[] col = {"No.","xi","xj","xk",FUN_STR};
                Object[][] dat = tab1(Float.parseFloat(astr), Float.parseFloat(bstr));
                DefaultTableModel dmt = new DefaultTableModel(dat,col);
                table1.setModel(dmt);
                // Cambio de estilo
                atxt.setText(""); btxt.setText("");
                descrip.setText("Pruebe con otro intervalo");
                table1.getColumnModel().getColumn(0).setMaxWidth(50);
                table1.getColumnModel().getColumn(1).setMaxWidth(150);
                table1.getColumnModel().getColumn(2).setMaxWidth(150);
                table1.getColumnModel().getColumn(3).setMaxWidth(150);
                //Fin de función
                flbl.setText(err);
                showMessageDialog(null, fin);
            }else{
                showMessageDialog(null, "Ingrese datos v\u00E1lidos por favor");
            }
        });
    }
    
    private static Object[][] tab1(float a, float b){
        Object[][] t = new Object[IT][5];
        float xi,xa=a,xb=b, d0,d1, apar,bpar,cpar;
        int i=0;
        
        while(true){
            t[i][0]=i+1; t[i][1]=xa; t[i][2]=xb;
            // #1 Iteración: xa=x0, xb=x1, xi=x2
            xi = (float)((xa+xb)/2);
            // Aproximaciones Müller
            d0 = (float)(f(xb)-f(xa))/(xb-xa);
            d1 = (float)(f(xi)-f(xb))/(xi-xb);
            // Coeficientes de la parábola
            apar = (float)((d1-d0)/((xi-xb)+(xb-xa)));
            bpar = (float)((apar*(xi-xb))+d1);
            cpar = (float)(f(xi));
            // xi+1
            if(bpar<0){
                xi = (float) (xi + ( (-2*cpar) / (bpar - (Math.sqrt(Math.pow(bpar,2)-4*apar*cpar)) ) ));
            }else if(bpar>0){
                xi = (float) (xi + ( (-2*cpar) / (bpar + (Math.sqrt(Math.pow(bpar,2)-4*apar*cpar)) ) ));
            }else {fin="Intervalo no v\u00E1lido, por favor intente nuevamente."; break;}

            t[i][3]=xi; t[i][4]=del(cpar);
            if(xb==xi || xa==xi || i==IT-1){
                System.out.println(xi+" :x"+i+" == x"+(i-1)+": "+xb); 
                fin="La ra\u00EDz est\u00E1 en "+xi;
                err="Resultado con error del: "+cpar;
                break;
            }
            xa=xb;xb=xi;
            i++;
        }
        return t;
    }
    
    public static String del(double num) {
        String d = "###";
        return new DecimalFormat("#." + d + d + d).format(num);
    }
    
    public static void main(String[] args) throws IOException{
        Muller run = new Muller();
        System.out.println(run);
    }
}