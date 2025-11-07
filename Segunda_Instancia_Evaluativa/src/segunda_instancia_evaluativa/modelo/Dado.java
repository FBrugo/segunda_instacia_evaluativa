package segunda_instancia_evaluativa.modelo;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.util.Random;
/**
 *
 * @author carlo
 */
public class Dado {
   
    private static final Random RNG = new Random();
   
    public int tirar() { return RNG.nextInt(6) + 1; } 
}
