/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package segunda_instancia_evaluativa.modelo;

/**
 *
 * @author fedee
 */
public class ValidacionApodo  {
    // Letras (incluye acentos) y espacios, 3 a 10 chars
    private static final String REGEX = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]{3,10}$";
    public static boolean esValido(String apodo) {
        return apodo != null && apodo.matches(REGEX);
    }
}
