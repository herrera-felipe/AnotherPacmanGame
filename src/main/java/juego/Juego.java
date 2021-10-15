package juego;

import java.awt.*;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

/**
 *
 * @author Felipe Herrera
 */
public class Juego {

    //Atributos
    static JFrame ventana;

    //Presentacion
    JPanel panelPresentacion;
    JButton iniciar;
    JLabel fondoPresentacion;
    ImageIcon imagenFondoPres;

    //Menu
    JPanel panelMenu;
    JButton botones[];
    JLabel fondoMenu;
    ImageIcon imagenFondoMenu;

    //PanelJuego
    static JPanel panelJuego;
    JLabel fondoJuego;
    ImageIcon imagenFondoJuego;

    static int mat[][]; //Matirz de enteros donde haremos cambios 
    static JLabel matriz[][];

    //Pacman
    int pos_x; //posicion de pacman en X
    int pos_y; //posicion de pacman en Y
    
    //Fantasmas
    Fantasmas fantasma1, fantasma2, fantasma3;
    static int matAux[][];

    //Banderas se usaran para mover a pacman
    int abajo, arriba, derecha, izquierda;
    Timer timer; //Timer para las banderas

    //Datos
    String jugador;
    JLabel nameJugador; //Mostrara el nombre del jugador
    int puntos;
    JLabel score; //Muestra puntaje del juego actual

//------------------------------------------------------------------------    
    /**
     * CONSTRUCTOR DEL JUEGO
     *
     */
    public Juego() {
        // CREAMOS LA VENTANA
        ventana = new JFrame("PACMAN");
        ventana.setSize(700, 700);
        ventana.setLayout(null);
        ventana.setLocationRelativeTo(null); //Ubica Ventana Principal en el medio
        ventana.setResizable(false); //Deshabilita Maximizar
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Cierra al dar clic en la x

        //-------------------------------------------------------------------
        // PRESENTACION
        panelPresentacion = new JPanel();
        panelPresentacion.setLayout(null);
        panelPresentacion.setBounds(0, 0, ventana.getWidth(), ventana.getHeight()); //Tendra mismo tamaño que la ventana
        panelPresentacion.setVisible(true);

        // AGREGAMOS A LA VENTANA
        ventana.add(panelPresentacion);

        //-------------------------------------------------------------------
        //Boton INICIAR
        iniciar = new JButton("Iniciar");
        iniciar.setBounds(ventana.getWidth() - 130, 20, 100, 30); //Tamaño Boton
        iniciar.setVisible(true);
        iniciar.setBackground(Color.ORANGE);
        iniciar.setFont(new Font("Arial", Font.BOLD, 15));

        // AGREGAMOS A LA PRESENTACION
        panelPresentacion.add(iniciar, 0);

        //--------------------------------------------------------------------
        // FONDO PRESENTACION
        fondoPresentacion = new JLabel();
        fondoPresentacion.setBounds(0, 0, ventana.getWidth(), ventana.getHeight());
        imagenFondoPres = new ImageIcon("imagenes/fondoPresentacion-01.png");
        imagenFondoPres = new ImageIcon(imagenFondoPres.getImage().getScaledInstance(ventana.getWidth(), ventana.getHeight(), Image.SCALE_DEFAULT));
        fondoPresentacion.setIcon(imagenFondoPres); //Se agrega la imagen al Jlabel
        fondoPresentacion.setVisible(true);

        // AGREGAMOS A LA PRESENTACION
        panelPresentacion.add(fondoPresentacion, 0);

        //--------------------------------------------------------------------
        // MENU
        botones = new JButton[5]; //Vector de 5 botones

        for (int i = 0; i < botones.length; i++) {
            botones[i] = new JButton();
        }

        //--------------------------------------------------------------------
        // EVENTO BOTON INICIAR
        iniciar.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println("Iniciar");
                //Llamada a al metodo menu y eventos
                menu();
                eventoMenu();
            }
        });

        //-------------------------------------------------------------------
        // JUEGO
        // DAMOS MEMORIA A LA MATRIZ
        mat = new int[15][15];
        matriz = new JLabel[15][15];
        matAux = new int[15][15];
       
        // ASIGANMOS EL TABLERO (NIVEL) A LA MATRIZ
        mat = tablero(1);
        
        // RECORREMOS LA MATRIZ
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat.length; j++) {
                matriz[i][j] = new JLabel();
                matAux[i][j] = mat[i][j]; //Transferimos los datos para que nos quede igual la matris al paso del fantasma
            }
        }
        
        //--------------------------------------------------------------------
        // PACMAN _ COORDENDAS X,Y
        // POSICION INICIAL
        pos_x = 1;
        pos_y = 1;

        mat[pos_x][pos_y] = 30;
        
        //----------------------------------------------------------------------
        // BANDERAS INICIALIZADAS EN CERO
        abajo = 0;
        arriba = 0;
        izquierda = 0;
        derecha = 0;

        //----------------------------------------------------------------------
        ventana.setVisible(true); //Siempre va al final 
    } // Fin Constructor Juego

//--------------------------------------------------------------------------
    /**
     * Metodo Menu
     */
    public void menu() {

        panelPresentacion.setVisible(false); //Cambiamos de "Capa" ocultamos panel presentacion

        // PANEL MENU
        panelMenu = new JPanel();
        panelMenu.setLayout(null);
        panelMenu.setBounds(0, 0, ventana.getWidth(), ventana.getHeight()); //Tendra mismo tamaño que la ventana
        panelMenu.setVisible(true);

        //--------------------------------------------------------------------
        // FONDO MENU
        fondoMenu = new JLabel();
        fondoMenu.setBounds(0, 0, ventana.getWidth(), ventana.getHeight());
        imagenFondoMenu = new ImageIcon("imagenes/FondoMenu-01.png");
        imagenFondoMenu = new ImageIcon(imagenFondoMenu.getImage().getScaledInstance(ventana.getWidth(), ventana.getHeight(), Image.SCALE_DEFAULT));
        fondoMenu.setIcon(imagenFondoMenu); //Se agrega la imagen al Jlabel
        fondoMenu.setVisible(true);

        // AGREGAMOS AL PANEL MENU
        panelMenu.add(fondoMenu, 0);

        // AGREGAMOS LOS BOTONES
        botones[0].setText("JUGAR");
        botones[1].setText("Crear Nivel");
        botones[2].setText("Score");
        botones[3].setText("Cargar Nivel");
        botones[4].setText("SALIR");

        //-------------------------------------------------------------------
        for (int i = 0; i < botones.length; i++) {
            botones[i].setBounds(ventana.getWidth() - (200 + 50), (i + 1) * 50, 200, 40); // (i+1)*50 es para que los botones no queden uno encima del otro
            botones[i].setVisible(true);
            botones[i].setBackground(Color.ORANGE);
            botones[i].setFont(new Font("Arial", Font.BOLD, 15));
            panelMenu.add(botones[i], 0); //Agrega los botones al Panel Menu
        }

        ventana.add(panelMenu);

    } // Fin menu

//--------------------------------------------------------------------------
    /**
     * EVENTOS DEL MENU
     */
    public void eventoMenu() {

        // BOTON JUGAR
        botones[0].addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println("JUGAR");

                //Pedir nombre
                jugador = JOptionPane.showInputDialog(ventana, "Nombre del jugador", "Escribe aqui");
                //validacion de datos para que el usuario ingrese nombre si o si
                while (jugador == null || jugador.compareTo("Escribe aqui") == 0 || jugador.compareTo("") == 0) {
                    jugador = JOptionPane.showInputDialog(ventana, "Debes ingresar usuario", "Escribe aqui");
                }

                //Llamada a al juego
                jugar();
            }
        });

        // BOTON CREAR TABLERO
        botones[1].addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println("Crear Nivel");
                //Llamada a al juego
            }
        });

        // BOTON SCORE
        botones[2].addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println("Score");
                //Llamada a al metodo
            }
        });

        // BOTON CARGAR TABLERO
        botones[3].addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println("Cargar Nivel");
                //Llamada a al metodo
            }
        });

        // BOTON SALIR
        botones[4].addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println("SALIR");
                //Llamada a la accion salir
                System.exit(0);
            }
        });

    }//Fin eventoMenu

//--------------------------------------------------------------------------
    /**
     * Metodo JUGAR
     */
    public void jugar() {

        panelMenu.setVisible(false); //Cambiamos de "Capa" ocultamos panel Menu

        // PANEL JUEGO
        panelJuego = new JPanel(); // Crea el nuevo panel
        panelJuego.setLayout(null);
        panelJuego.setBounds(0, 0, ventana.getWidth(), ventana.getHeight()); //Tendra mismo tamaño que la ventana
        panelJuego.setVisible(true);

        //--------------------------------------------------------------------
        // FONDO PANEL JUEGO
        fondoJuego = new JLabel();
        fondoJuego.setBounds(0, 0, ventana.getWidth(), ventana.getHeight());
        imagenFondoJuego = new ImageIcon("imagenes/fondoJuegoNegro-01.png");
        imagenFondoJuego = new ImageIcon(imagenFondoJuego.getImage().getScaledInstance(ventana.getWidth(), ventana.getHeight(), Image.SCALE_DEFAULT));
        fondoJuego.setIcon(imagenFondoJuego); //Se agrega la imagen al Jlabel
        fondoJuego.setVisible(true); //se hace visible el JLabel

        // AGREGAMOS EL FONDO AL PANEL JUEGO
        panelJuego.add(fondoJuego, 0);

        //For anidado para recorrer el ciclo o matriz
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat.length; j++) {
                matriz[i][j].setIcon(new ImageIcon("imagenes/" + mat[i][j] + ".png")); //Agregamos las imagenes (suelo y comida que corresponden a 0 y 1)
                matriz[i][j].setBounds(120 + (i * 30), 120 + (j * 30), 30, 30); // tamaño del panel 200(+i*30), tamaño imagenes30*30
                matriz[i][j].setVisible(true); //Hacemos visible la matriz

                panelJuego.add(matriz[i][j], 0); //añadimos la matriz al panel de juego
            }
        }
        //-----------------------------------------------------------------
        // AGREGAMOS NOMBRE
        nameJugador = new JLabel("Jugador: " + jugador); //damos memoria
        nameJugador.setBounds(20, 20, 150, 30); //Damos medidas
        nameJugador.setForeground(Color.ORANGE);
        nameJugador.setFont(new Font("Arial", Font.BOLD, 18));
        nameJugador.setVisible(true); //Hacemos visible

        panelJuego.add(nameJugador, 0); //Añadimos al panel de juego

        // AGREGAMOS SCORE
        puntos = 0;
        score = new JLabel("Score: " + puntos);
        score.setBounds(ventana.getWidth() - (150 + 20), 20, 150, 30);
        score.setForeground(Color.ORANGE);
        score.setFont(new Font("Arial", Font.BOLD, 18));
        score.setVisible(true);

        panelJuego.add(score, 0); //Añadimos al panel de juego

        //----------------------------------------------------------------
        // METODO PARA MOVER A PACMAN
        moverPacman();
        
        // AGREGAMOS FANTASMAS 
        fantasma1 = new Fantasmas(12, 13, 70);
        fantasma2 = new Fantasmas(13, 13, 90);
        fantasma3 = new Fantasmas(13, 12, 80);

        // AGREGAMOS A LA VENTANA
        ventana.add(panelJuego);
    }//Fin Jugar

//-------------------------------------------------------------------------------    
    /**
     * Pinta la matriz cada vez que salga un juego
     */
    public static void pintarMatriz() {

        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat.length; j++) {
                matriz[i][j].setIcon(new ImageIcon("imagenes/" + mat[i][j] + ".png")); //Agregamos las imagenes (suelo y comida que corresponden a 0 y 1)
                matriz[i][j].setBounds(120 + (i * 30), 120 + (j * 30), 30, 30); // tamaño del panel 200(+i*30), tamaño imagenes30*30
                matriz[i][j].setVisible(true); //Hacemos visible la matriz

                panelJuego.add(matriz[i][j], 0); //añadimos la matriz al panel de juego
            }
        }

    }

//-------------------------------------------------------------------------------
    /**
     * Este metodo se encargara del movimiento del personaje Pacman
     */
    public void moverPacman() {

        //Creamos un Timer para as banderas
        timer = new Timer(200, new ActionListener() { // El 200 controla la velocidad de pacman
            @Override
            public void actionPerformed(ActionEvent e) {

                //Si es hacia ARRIBA que me verifique
                if (arriba == 1 && (mat[pos_x][pos_y - 1] == 1 || mat[pos_x][pos_y - 1] == 0)) {

                    //Si la posicion es igual a 1 ( 1 = "comida")
                    if (mat[pos_x][pos_y - 1] == 1) {
                        puntos = puntos + 5; //cuando come aumentamos los puntos
                        score.setText("Score: " + puntos);
                    }

                    mat[pos_x][pos_y] = 0; //Limpia por donde va pasando
                    matAux[pos_x][pos_y] = mat[pos_x][pos_y];
                    pos_y = pos_y - 1; //Mueve arriba
                    mat[pos_x][pos_y] = 33; //Agrega personaje en la nueva pos

                    pintarMatriz(); //Pintamos matriz
                }

                //Si es hacia ABAJO que me verifique
                if (abajo == 1 && (mat[pos_x][pos_y + 1] == 1 || mat[pos_x][pos_y + 1] == 0)) {

                    //Si la posicion es igual a 1 ( 1 = comida)
                    if (mat[pos_x][pos_y + 1] == 1) {
                        puntos = puntos + 5; //cuando come aumentamos los puntos
                        score.setText("Score: " + puntos);
                    }

                    mat[pos_x][pos_y] = 0; //Limpia por donde va pasando
                    matAux[pos_x][pos_y] = mat[pos_x][pos_y];
                    pos_y = pos_y + 1; //Mueve arriba
                    mat[pos_x][pos_y] = 34; //Agrega personaje en la nueva pos

                    pintarMatriz(); //Pintamos matriz
                }

                //Si es hacia la DERECHA que me verifique
                if (derecha == 1 && (mat[pos_x + 1][pos_y] == 1 || mat[pos_x + 1][pos_y] == 0)) {

                    //Si la posicion es igual a 1 ( 1 = comida)
                    if (mat[pos_x + 1][pos_y] == 1) {
                        puntos = puntos + 5; //cuando come aumentamos los puntos
                        score.setText("Score: " + puntos);
                    }

                    mat[pos_x][pos_y] = 0; //Limpia por donde va pasando
                    matAux[pos_x][pos_y] = mat[pos_x][pos_y];
                    pos_x = pos_x + 1; //Mueve arribax  
                    mat[pos_x][pos_y] = 31; //Agrega personaje en la nueva pos

                    pintarMatriz(); //Pintamos matriz
                }

                //Si es hacia la IZQUIERDA que me verifique
                if (izquierda == 1 && (mat[pos_x - 1][pos_y] == 1 || mat[pos_x - 1][pos_y] == 0)) {

                    //Si la posicion es igual a 1 ( 1 = comida)
                    if (mat[pos_x - 1][pos_y] == 1) {

                        puntos = puntos + 5; //cuando come aumentamos los puntos
                        score.setText("Score: " + puntos);
                    }

                    mat[pos_x][pos_y] = 0; //Limpia por donde va pasando
                    matAux[pos_x][pos_y] = mat[pos_x][pos_y];
                    pos_x = pos_x - 1; //Mueve arriba
                    mat[pos_x][pos_y] = 32; //Agrega personaje en la nueva pos

                    pintarMatriz(); //Pintamos matriz
                }

                //------------------------------------------------------------
                // FINALIZA JUEGO Al GANAR O COMER TODO
                int encontrado = 0;

                for (int i = 0; i < mat.length && encontrado == 0; i++) {
                    for (int j = 0; j < mat.length && encontrado == 0; j++) {
                        if (mat[i][j] == 1) {
                            encontrado = 1;
                        }
                    }
                }
                
                if (encontrado == 0) {
                    
                    JOptionPane.showMessageDialog(ventana, "Felicitaciones, Ganaste!!");

                    panelJuego.setVisible(false); //OcultaJuego
                    panelMenu.setVisible(true); //Muestra Menu

                    timer.stop(); //Detenemos el timer
                    
                }
                
                //LLAMADA AL METODO MATAR PACMAN
                matarPacman();

            }
        });

        timer.start(); //Cuando llama el metodo Mover arrancara el timer

        //Añadimos el evento de teclado: respondera a nuestras teclas
        ventana.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            //Evento cuando una tecla se mantiene presionada
            @Override
            public void keyPressed(KeyEvent e) {
                // Si evento(e) es igual al evento tecla(UP flecha arriba)
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    System.out.println("Tecla hacia ARRIBA");

                    //pos_y - 1 es mover arriba en el eje Y
                    if (mat[pos_x][pos_y - 1] == 1 || mat[pos_x][pos_y - 1] == 0) {
                        //bandera arriba
                        arriba = 1;
                        abajo = 0;
                        izquierda = 0;
                        derecha = 0;
                    }

                }

                // Si evento(e) es igual al evento tecla(DOWN flecha abajo)
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    System.out.println("Tecla hacia ABAJO");

                    //pos_y + 1 es mover abajo en el eje Y
                    if (mat[pos_x][pos_y + 1] == 1 || mat[pos_x][pos_y + 1] == 0) {
                        //bandera abajo
                        arriba = 0;
                        abajo = 1;
                        izquierda = 0;
                        derecha = 0;
                    }

                }

                // Si evento(e) es igual al evento tecla(RIGHT flecha Derecha)
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    System.out.println("Tecla hacia DERECHA");

                    //Si la posicion es igual a 1 (comida) o igual a espacio en blanco
                    if (mat[pos_x + 1][pos_y] == 1 || mat[pos_x + 1][pos_y] == 0) {
                        //bandera derecha
                        arriba = 0;
                        abajo = 0;
                        izquierda = 0;
                        derecha = 1;
                    }

                }

                // Si evento(e) es igual al evento tecla(LEFT flecha Izquierda)
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    System.out.println("Tecla hacia IZQUIERDA");

                    if (mat[pos_x - 1][pos_y] == 1 || mat[pos_x - 1][pos_y] == 0) {
                        //bandera arriba
                        arriba = 0;
                        abajo = 0;
                        izquierda = 1;
                        derecha = 0;
                    }

                }
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            //Evento cuando suelta la tecla
            @Override
            public void keyReleased(KeyEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }

    //------------------------------------------------------------------------------------------------------------------------
    /**
     * Matar Pacman
     */
    public void matarPacman() {
        
        // SI esta pacman abajo
        if(mat[pos_x][pos_y + 1] == 70 || mat[pos_x][pos_y + 1] == 80 || mat[pos_x][pos_y + 1] == 90){
            
            fantasma1.timer.stop(); //De esta forma matamos a pacman
            fantasma2.timer.stop();
            fantasma3.timer.stop();
            
            JOptionPane.showMessageDialog(ventana, "ESTAS MUERTO!");
            panelJuego.setVisible(false);
            panelMenu.setVisible(true);
            timer.stop();
            
        }
        //-----------------------------------------------------------------------------------------------------------------
        //SI esta pacman arriba
        if(mat[pos_x][pos_y - 1] == 70 || mat[pos_x][pos_y - 1] == 80 || mat[pos_x][pos_y - 1] == 90){
            
            fantasma1.timer.stop(); //De esta forma matamos a pacman
            fantasma2.timer.stop();
            fantasma3.timer.stop();
            
            JOptionPane.showMessageDialog(ventana, "ESTAS MUERTO!");
            panelJuego.setVisible(false);
            panelMenu.setVisible(true);
            timer.stop();
        }
        //-----------------------------------------------------------------------------------------------------------------
        //Si esta pacman a la izquierda
        if(mat[pos_x - 1][pos_y] == 70 || mat[pos_x - 1][pos_y] == 80 || mat[pos_x - 1][pos_y] == 90){
            
            fantasma1.timer.stop(); //De esta forma matamos a pacman
            fantasma2.timer.stop();
            fantasma3.timer.stop();
            
            JOptionPane.showMessageDialog(ventana, "ESTAS MUERTO!");
            panelJuego.setVisible(false);
            panelMenu.setVisible(true);
            timer.stop();
        }
        //------------------------------------------------------------------------------------------------------------------
        //SI esta pacman a la derecha
         if(mat[pos_x + 1][pos_y] == 70 || mat[pos_x + 1][pos_y] == 80 || mat[pos_x + 1][pos_y] == 90){
            
            fantasma1.timer.stop(); //De esta forma matamos a pacman
            fantasma2.timer.stop();
            fantasma3.timer.stop();
            
            JOptionPane.showMessageDialog(ventana, "ESTAS MUERTO!");
            panelJuego.setVisible(false);
            panelMenu.setVisible(true);
            timer.stop();
        }
    }
    
//--------------------------------------------------------------------------------
    /*
    * Este metodo llamara la matriz
     */
    public int[][] tablero(int opcion) {

        int[][] aux1 = new int[15][15];

        if (opcion == 1) {
            int aux[][] = {
                //0  1  2  3  4  5  6  7  8  9  10 11 12 13 14
                {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
                {2, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 2},
                {2, 1, 2, 2, 1, 2, 1, 2, 1, 2, 2, 1, 2, 1, 2},
                {2, 1, 2, 1, 1, 2, 1, 1, 1, 1, 2, 1, 2, 1, 2},
                {2, 1, 1, 1, 2, 2, 2, 1, 2, 1, 1, 1, 1, 1, 2},
                {2, 1, 2, 1, 1, 1, 1, 1, 2, 2, 2, 1, 2, 2, 2},
                {2, 1, 2, 2, 1, 2, 2, 1, 1, 2, 2, 1, 1, 1, 2},
                {2, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 1, 2, 1, 2},
                {2, 2, 2, 1, 2, 1, 2, 2, 2, 1, 2, 1, 2, 1, 2},
                {2, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
                {2, 1, 2, 1, 1, 1, 2, 2, 2, 1, 2, 1, 2, 1, 2},
                {2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 2, 1, 2},
                {2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2},
                {2, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
                {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},};
            return aux;
        }

        if (opcion == 2) {
            int aux[][] = {
                //0  1  2  3  4  5  6  7  8  9  10 11 12 13 14
                {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},//0
                {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},//1
                {2, 1, 2, 1, 2, 1, 2, 2, 2, 1, 2, 1, 2, 1, 2},//2
                {2, 1, 2, 1, 2, 1, 1, 2, 1, 1, 2, 1, 2, 1, 2},//3
                {2, 1, 2, 1, 2, 2, 1, 2, 1, 2, 2, 1, 2, 1, 2},//4
                {2, 1, 2, 1, 2, 1, 1, 1, 1, 1, 2, 1, 2, 1, 2},//5
                {2, 1, 2, 1, 2, 1, 2, 2, 2, 1, 2, 1, 2, 1, 2},//6
                {2, 1, 1, 1, 1, 1, 2, 2, 2, 1, 1, 1, 1, 1, 2},//7
                {2, 1, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 2, 1, 2},//8
                {2, 1, 1, 1, 2, 1, 2, 2, 2, 1, 2, 1, 1, 1, 2},//9
                {2, 2, 2, 1, 1, 1, 1, 2, 1, 1, 1, 1, 2, 2, 2},//10
                {2, 1, 1, 1, 2, 2, 1, 2, 1, 2, 2, 1, 1, 1, 2},//11
                {2, 1, 2, 2, 2, 2, 1, 2, 1, 2, 2, 2, 2, 1, 2},//12
                {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},//13
                {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},//14
            };
            return aux;
        }

        if (opcion == 3) {
            int aux[][] = {
                {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
                {2, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 2},
                {2, 1, 2, 2, 1, 2, 1, 2, 1, 2, 2, 1, 2, 1, 2},
                {2, 1, 2, 1, 1, 2, 1, 1, 1, 1, 2, 1, 2, 1, 2},
                {2, 1, 1, 1, 2, 2, 2, 1, 2, 1, 1, 1, 1, 1, 2},
                {2, 1, 2, 1, 1, 1, 1, 1, 2, 2, 2, 1, 2, 2, 2},
                {2, 1, 2, 2, 1, 2, 2, 1, 1, 2, 2, 1, 1, 1, 2},
                {2, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 1, 2, 1, 2},
                {2, 2, 2, 1, 2, 1, 2, 2, 2, 1, 2, 1, 2, 1, 2},
                {2, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
                {2, 1, 2, 1, 1, 1, 2, 2, 2, 1, 2, 1, 2, 1, 2},
                {2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 2, 1, 2},
                {2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2},
                {2, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
                {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},};
            return aux;
        }

        return aux1;
    }

    
}
