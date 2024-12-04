import java.io.*;
import java.util.*;

public class GestorLibros {
    private List<Libro> libros;
    public GestorLibros(String archivo) throws IOException {
        libros = new ArrayList<>();
        cargarLibrosDesdeArchivo();
    }

    public void agregarLibro(Libro libro) {
        libros.add(libro);
        guardarLibrosEnArchivo(); 
    }

    public void eliminarLibro(String titulo) {
        libros.removeIf(libro -> libro.getTitulo().equalsIgnoreCase(titulo));
        guardarLibrosEnArchivo();
    }

    
    public List<Libro> buscarLibroPorTitulo(String termino) {
        List<Libro> resultados = new ArrayList<>();
        for (Libro libro : libros) {
            if (libro.getTitulo().toLowerCase().contains(termino.toLowerCase())) {
                resultados.add(libro);
            }
        }
        return resultados;
    }

    
    public List<Libro> listarLibrosOrdenados() {
        libros.sort(Comparator.comparing(Libro::getTitulo));
        return libros;
    }

    
    public boolean reservarLibro(String titulo) {
        for (Libro libro : libros) {
            if (libro.getTitulo().equalsIgnoreCase(titulo)) {
                if (!libro.isReservado()) {
                    libro.setReservado(true); 
                    guardarLibrosEnArchivo(); 
                    return true; 
                } else {
                    return false; 
                }
            }
        }
        return false; 
    }

    
    private void cargarLibrosDesdeArchivo() throws IOException {
        File file = new File("Libros.txt"); 
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String linea;
                while ((linea = reader.readLine()) != null) {
                    String[] partes = linea.split(",");
                    if (partes.length == 5) {
                        String titulo = partes[0];
                        String autor = partes[1];
                        Genero genero;
                        try {
                            genero = Genero.valueOf(partes[2].toUpperCase()); 
                        } catch (IllegalArgumentException e) {
                            System.err.println("Género no válido: " + partes[2]);
                            continue;
                        }
                        int año = Integer.parseInt(partes[3]);
                        boolean reservado = Boolean.parseBoolean(partes[4]);
                        libros.add(new Libro(titulo, autor, genero, año, reservado));
                    }
                }
            }
        }
    }

    public void guardarLibrosEnArchivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Libros.txt"))) {
            for (Libro libro : libros) {
                writer.write(libro.getTitulo() + "," + libro.getAutor() + "," + libro.getGenero() + ","
                        + libro.getAño() + "," + libro.isReservado());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar los libros en el archivo: " + e.getMessage());
        }
    }

}