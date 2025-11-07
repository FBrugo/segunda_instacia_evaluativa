package segunda_instancia_evaluativa.modelo;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author carlo
 */
public class TrampasLog {
    
    private final List<String> eventos = new ArrayList<>();
   
    public void log(String texto) { eventos.add(texto); }
    
    public List<String> getEventos() { return eventos; }
    
    public void mostrar() {
        System.out.println("\n--- REGISTRO DE TRAMPAS ---");
        if (eventos.isEmpty()) { System.out.println("(sin eventos)"); return; }
        for (String e : eventos) System.out.println("• " + e);
    }
}
