/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prototipohopfield;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileWriter;
import java.io.IOException;


/**
 *
 * @author LoreyFaby
 */


public class PrototipoHopfield{
    private JFrame marco;
    private JPanel panelPrincipal;
    private JPanel[] panelSecundario;
    private JButton ejecutar;
    private int[][] patrones;
    private int[][] matrizDePeso;
    private boolean[] patronEntrada;
    private int[] patron1;
    private int[] patron2;
 

    public PrototipoHopfield() {
        // Configuro y defino interfaz Grafica
        marco = new JFrame("Trabajo Practico N° 3");
        marco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        marco.setSize(400, 400);

        panelPrincipal = new JPanel(new GridLayout(10, 10));
        panelSecundario = new JPanel[100];

        // Define los patrones
        patron1 = new int[]{
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, 1, 1, -1, -1, -1, -1,
            -1, -1, -1, 1, -1, -1, 1, -1, -1, -1,
            -1, -1, 1, -1, -1, -1, -1, 1, -1, -1,
            -1, 1, -1, -1, -1, -1, -1, -1, 1, -1,
            -1, 1, -1, -1, -1, -1, -1, -1, 1, -1,
            -1, -1, 1, -1, -1, -1, -1, 1, -1, -1,
            1, -1, -1, 1, -1, -1, 1, -1, -1, -1,
            1, -1, -1, -1, 1, 1, -1, -1, -1, -1,
            1, 1, 1, -1, -1, -1, -1, -1, -1, -1
        };  // patron de imagen1
        patron2 = new int[]{
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, 1, 1, -1, -1, -1, -1,
            -1, -1, -1, 1, -1, -1, 1, -1, -1, -1,
            -1, -1, 1, -1, -1, -1, -1, 1, -1, -1,
            -1, -1, 1, -1, -1, -1, -1, 1, -1, -1,
            -1, -1, -1, 1, -1, -1, 1, -1, -1, -1,
            1, -1, -1, -1, 1, 1, -1, -1, -1, -1,
            1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            1, 1, 1, -1, -1, -1, -1, -1, -1, -1
        }; // patron de imagen2
             
        
    
        patrones = new int[][]{patron1, patron2}; // ingresa los dos patrones en una matriz 
       
 
        patronEntrada = new boolean[100]; 

        crearMatrizDePesos();

        for (int i = 0; i < 100; i++) {
            panelSecundario[i] = new JPanel();
            panelSecundario[i].setBackground(Color.LIGHT_GRAY);
            panelSecundario[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            panelSecundario[i].addMouseListener(new CuadranteMouse(i));
            panelPrincipal.add(panelSecundario[i]);
        }

        ejecutar = new JButton("Buscar Imagen");
        ejecutar.addActionListener(new EjecutarButton());
        ejecutar.setPreferredSize(new Dimension(ejecutar.getPreferredSize().width, ejecutar.getPreferredSize().height*2));
        ejecutar.setBackground(Color.cyan);
         
        marco.setLayout(new BorderLayout());
        marco.add(panelPrincipal, BorderLayout.CENTER);
        marco.add(ejecutar, BorderLayout.SOUTH);
        marco.setVisible(true);
    }
     
  
    private void crearMatrizDePesos() {
        // Se realiza la matriz de pesos correspondiente a los´patrones almacenados.
        int patronTamano = patrones[0].length;
        matrizDePeso = new int[patronTamano][patronTamano]; // tamaño de la matriz de peso 

        for (int i = 0; i < patronTamano; i++) {
            for (int j = 0; j < patronTamano; j++) {
                if (i != j) { // donde el valor de i es igual a J asegura hacer la diagonal con valores en cero
                    for (int[] patron : patrones) {
                        matrizDePeso[i][j] += patron[i] * patron[j]; // itera los dos patrones alamacenados donde  realiza multiplicacion de cada vectro para luego sumarlos
                    }
                }
            }
        }
    }

    private class CuadranteMouse extends MouseAdapter {
        private int indice;

        public CuadranteMouse(int indice) {
            this.indice = indice;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            // cambiar los estados y colores con el clic
            if (!patronEntrada[indice]) {
                panelSecundario[indice].setBackground(Color.BLUE); // asignacion de color al cuadrado seleccionado
                patronEntrada[indice] = true;
            } else {
                panelSecundario[indice].setBackground(Color.LIGHT_GRAY); // asigancion de color al cuadrado no seleccionado
                patronEntrada[indice] = false;
            }
        }
    }

    private class EjecutarButton implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // Convierte la seleccion de las celdas en el nuevo vector de imagen.
            int[] entrada = new int[100];
            for (int i = 0; i < 100; i++) {
                if (patronEntrada[i]) {
                    entrada[i] = 1;  //cuadrado seleccionado
                } else {
                    entrada[i] = -1; // cuadrado no seleccionado
                }

            }

            // Verificar si hay convergencia y procesar la entrada

            boolean convergente = esConvergente(entrada);
            int[] salida = procesoEntrada(entrada);
            String convergencia;
            if (convergente) {
                convergencia = "Hay Convergencia!!!.\nSe encontro la imagen Buscada:\nEl vector de entrada es similar a los patrones almacenados.";
            } else {
                convergencia = "No hay Convergencia.\nNo se encontro la imagen Buscada:\nEl vector de entrada difiere de los patrones almacenados.";
            }

            StringBuilder respuestaRed = new StringBuilder();
            respuestaRed.append("Respuesta de la red a la selección:\n");

            // Convertir el vector de la imagen consultada en una matriz para mostrarse en pantalla 
            int[][] salidaMatriz = new int[10][10];
            int index = 0;
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    salidaMatriz[i][j] = salida[index];
                    index++;
                }
            }
  
            // Construir la respuesta de la red a la entrada
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    respuestaRed.append(salidaMatriz[i][j]).append(" , ");
                }
                respuestaRed.append("\n");
            }

            // Mostrar el resultado en un cuadro de diálogo
            JOptionPane.showMessageDialog(marco, convergencia + "\n" + respuestaRed.toString());
            
            // Exportar resultado a archivo de texto
            
            try {
                FileWriter escribir = new FileWriter("TP3.txt"); // crear archivo TXT
                escribir.write(convergencia + "\n" + respuestaRed.toString());
                escribir.close();
                JOptionPane.showMessageDialog(marco, "El resultado se ha exportado exitosamente a TP3.txt");
                
            } catch (IOException ex) {
                ex.printStackTrace();
                
                JOptionPane.showMessageDialog(marco, "Error al exportar el resultado a TP3.txt");
            }
        }
        

        private int[] procesoEntrada(int[] entrada) {
            // Procesar la entrada utilizando la matriz de pesos
            int patronTamano = patrones[0].length;
            int[] salida = new int[patronTamano];

            for (int i = 0; i < patronTamano; i++) {
                int sum = 0;
                for (int j = 0; j < patronTamano; j++) {
                    sum += matrizDePeso[i][j] * entrada[j]; // multiplica  matriz de pesos correspondiente con a los patrones almacenados con el vector de entrada de la imagen ingresada
                }
                salida[i] = (sum >= 0) ? 1 : -1; // si el valor es mayor e igual a 0 se le pone 1, sino -1
            }

            return salida;
        }
        
        
          private boolean esConvergente(int[] entrada) {
            // Verificar si la imagen ingresa converge en la con las alamacenadas
           // se verifica si el vector ingresado de la imagen es igual al vector resultante al multiplicarlo por la matriz de pesos con el vector de entrada
            int[] salida = procesoEntrada(entrada); 
            for (int i = 0; i < salida.length; i++) {
                if (salida[i] != entrada[i]) {
                    return false;
                }
            }
            return true;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PrototipoHopfield();
        });
    }
}
