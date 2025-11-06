package segunda_instancia_evaluativa;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.util.Random;
/**
 *
 * @author carlo
 */
public class TrampasEngine {
    
    private final Random rng = new Random();
    private final double probDadoCargado; // ej. 0.30
    private final double probConfundir;   // ej. 0.30

    public TrampasEngine(double probDadoCargado, double probConfundir) {
        this.probDadoCargado = probDadoCargado;
        this.probConfundir = probConfundir;
    }

    public int tirarConDadoCargado(int valorNormal) {
        return rng.nextDouble() < probDadoCargado ? 6 : valorNormal;
    }

    public int confundirValor(int valorNormal) {
        return rng.nextDouble() < probConfundir ? Math.max(1, valorNormal - 1) : valorNormal;
    }
}
