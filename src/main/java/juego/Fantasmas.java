package juego;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.Timer;

/**
 *
 * @author Felipe Herrera
 */
public class Fantasmas {

    // Atributos
    int fan_x; //posicion fantasma en x
    int fan_y; //posicion fantasma en y
    int imag;

    Timer timer;
    Random aleatorio; //El fantasma debe moverse de forma aleatoria
    int direccion; // Controlara la direccion de nuestro fantasma va a ir de 0 a 3
    int mov_x, mov_y; // Movimiento en eje x/y
    
    

    // Constructor
    public Fantasmas(int x, int y, int imagen) {
        this.fan_x = x;
        this.fan_y = y;
        this.imag = imagen;
        this.aleatorio = new Random(); //Damos memoria al random
        Juego.mat[fan_x][fan_y] = imag; //Asignamos la imagen del fantasma
        direccion = aleatorio.nextInt(4); //Asignamos la direccion de movimiento
        
        this.moverFantasma(); //Llamada al metodo moverFantasma
    } // fin constructor

    /**
     * Este metodo se encargara, del movimiento de los fantasmas
     */
    public void moverFantasma() {

        //Creamos un Timer para las banderas
        timer = new Timer(200, new ActionListener() { // El 200 controla la velocidad 
            
            public void actionPerformed(ActionEvent e) {
                /*
                 * Validacion de choque (pared y/o fantasmas) modifica direccion.
                 * Tenemos que verificar en todas las direcciones si hay un muro u objeto
                 * para cambiar la direccion de nuestro fantasma
                 */
                //----------------------------------------------------------------------------------------
                // ARRIBA
                if (direccion == 0) {
                    // Movimiento ARRIBA
                    if (Juego.mat[fan_x][fan_y - 1] == 0 || Juego.mat[fan_x][fan_y - 1] == 1) {
                        
                        Juego.mat[fan_x][fan_y] = Juego.matAux[fan_x][fan_y]; //Pinta el paso del fantasmas
                        fan_y -= 1;
                        Juego.mat[fan_x][fan_y] = imag; //Agregamos el fantasma en la nueva pos
                        Juego.pintarMatriz();
                    }
                    // Validacion de obstaculos en el camino = CAMBIA DIRECCION
                    if (fan_y > 0 && Juego.mat[fan_x][fan_y - 1] == 2) {
                        direccion = aleatorio.nextInt(4);
                    }
                    // Validacion de FANTASMA en el camino = CAMBIA DIRECCION
                    if (Juego.mat[fan_x][fan_y - 1] == 70 || Juego.mat[fan_x][fan_y - 1] == 80 || Juego.mat[fan_x][fan_y - 1] == 90) {
                        direccion = aleatorio.nextInt(4);
                    }
                    
                }
                //--------------------------------------------------------------------------------------
                // ABAJO
                if (direccion == 1) {
                    // Moviemiento ABAJO
                    if (Juego.mat[fan_x][fan_y + 1] == 0 || Juego.mat[fan_x][fan_y + 1] == 1) {
                        
                        Juego.mat[fan_x][fan_y] = Juego.matAux[fan_x][fan_y]; //Pinta el paso del fantasmas
                        fan_y += 1;
                        Juego.mat[fan_x][fan_y] = imag; //Agregamos el fantasma en la nueva pos
                        Juego.pintarMatriz();
                    }
                    // Validacion de obstaculos en el camino = CAMBIA DIRECCION
                    if (fan_y > 0 && Juego.mat[fan_x][fan_y + 1] == 2) {
                        direccion = aleatorio.nextInt(4);
                    }
                    // Validacion de FANTASMA en el camino = CAMBIA DIRECCION
                    if (Juego.mat[fan_x][fan_y + 1] == 70 || Juego.mat[fan_x][fan_y + 1] == 80 || Juego.mat[fan_x][fan_y + 1] == 90) {
                        direccion = aleatorio.nextInt(4);
                    }
                    
                }
                //-----------------------------------------------------------------------------------
                // DERECHA
                if (direccion == 2) {
                    //Moviemiento DERECHA
                    if (Juego.mat[fan_x + 1][fan_y] == 0 || Juego.mat[fan_x + 1][fan_y] == 1) {
                        
                        Juego.mat[fan_x][fan_y] = Juego.matAux[fan_x][fan_y]; //Pinta el paso del fantasmas
                        fan_x += 1;
                        Juego.mat[fan_x][fan_y] = imag; //Agregamos el fantasma en la nueva pos
                        Juego.pintarMatriz();
                    }
                    // Validacion de obstaculos en el camino = CAMBIA DIRECCION
                    if (fan_x > 0 && Juego.mat[fan_x + 1][fan_y] == 2) {
                        direccion = aleatorio.nextInt(4);
                    }
                    // Validacion de FANTASMA en el camino = CAMBIA DIRECCION
                    if (Juego.mat[fan_x + 1][fan_y] == 70 || Juego.mat[fan_x + 1][fan_y] == 80 || Juego.mat[fan_x + 1][fan_y] == 90) {
                        direccion = aleatorio.nextInt(4);
                    }
                    
                }
                //-----------------------------------------------------------------------------------
                // IZQUIERDA
                if (direccion == 3) {
                    //Movimiento IZQUIERDA
                    if (Juego.mat[fan_x - 1][fan_y] == 0 || Juego.mat[fan_x - 1][fan_y] == 1) {
                        
                        Juego.mat[fan_x][fan_y] = Juego.matAux[fan_x][fan_y]; //Pinta el paso del fantasmas
                        fan_x -= 1;
                        Juego.mat[fan_x][fan_y] = imag; //Agregamos el fantasma en la nueva pos
                        Juego.pintarMatriz();
                    }
                    // Validacion de MURO en el camino = CAMBIA DIRECCION
                    if (fan_x > 0 && Juego.mat[fan_x - 1][fan_y] == 2) {
                        direccion = aleatorio.nextInt(4);
                    }
                    // Validacion de FANTASMA en el camino = CAMBIA DIRECCION
                    if (Juego.mat[fan_x - 1][fan_y] == 70 || Juego.mat[fan_x - 1][fan_y] == 80 || Juego.mat[fan_x - 1][fan_y] == 90) {
                        direccion = aleatorio.nextInt(4);
                    }
                    
                }
                

            }
        });
        
        timer.start(); //Iniciamos el timer
        
    }// fin metodo moverFantasma

    
}
