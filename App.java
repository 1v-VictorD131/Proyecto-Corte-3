import javax.swing.*;
import java.io.IOException;

public class App {
    public static void main(String[] args) {
        try {
            
            GestorLibros gestorLibros = new GestorLibros("Libros.txt");

            
            new InterfazGrafica(gestorLibros);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los libros desde el archivo.");
        }
    }
}