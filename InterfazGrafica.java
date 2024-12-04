import javax.swing.*;
import java.awt.*;
import javax.swing.table.AbstractTableModel;
import java.util.Arrays;
import java.util.List;

public class InterfazGrafica {
    private GestorLibros gestorLibros;
    private JFrame frame;
    private JTextField campoBusqueda;
    private JTable tablaLibros;
    private LibroTableModel tableModel;

    public InterfazGrafica(GestorLibros gestorLibros) {
        this.gestorLibros = gestorLibros;
        inicializarInterfaz();
    }

    private void inicializarInterfaz() {
        frame = new JFrame("Biblioteca Virtual");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(235, 235, 235));  

        Font fuente = new Font("Serif", Font.PLAIN, 16);  

        JPanel panelSuperior = new JPanel(new BorderLayout());
        JLabel etiquetaBusqueda = new JLabel("Buscar libro: ");
        etiquetaBusqueda.setFont(fuente);
        etiquetaBusqueda.setForeground(new Color(0, 102, 204));  
        campoBusqueda = new JTextField();
        campoBusqueda.setFont(fuente);
        panelSuperior.add(etiquetaBusqueda, BorderLayout.WEST);
        panelSuperior.add(campoBusqueda, BorderLayout.CENTER);

        // Crear tabla
        tableModel = new LibroTableModel(gestorLibros.listarLibrosOrdenados());
        tablaLibros = new JTable(tableModel);
        tablaLibros.setFont(fuente);
        tablaLibros.setBackground(new Color(255, 255, 255));  
        tablaLibros.setFillsViewportHeight(true);
        tablaLibros.setCellSelectionEnabled(false);  
        tablaLibros.getTableHeader().setBackground(new Color(0, 102, 204));
        JScrollPane scrollPane = new JScrollPane(tablaLibros);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel();
        panelInferior.setBackground(new Color(235, 235, 235));
        panelInferior.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton botonAgregar = new JButton("Agregar Libro");
        botonAgregar.setFont(fuente);
        botonAgregar.setBackground(new Color(0, 204, 102));  
        botonAgregar.setForeground(Color.WHITE);
        botonAgregar.setFocusPainted(false);

        JButton botonEliminar = new JButton("Eliminar Libro");
        botonEliminar.setFont(fuente);
        botonEliminar.setBackground(new Color(255, 69, 0));  
        botonEliminar.setForeground(Color.WHITE);
        botonEliminar.setFocusPainted(false);

        JButton botonReservar = new JButton("Reservar");
        botonReservar.setFont(fuente);
        botonReservar.setBackground(new Color(0, 102, 204));  
        botonReservar.setForeground(Color.WHITE);
        botonReservar.setFocusPainted(false);

        panelInferior.add(botonAgregar);
        panelInferior.add(botonEliminar);
        panelInferior.add(botonReservar);

        frame.add(panelSuperior, BorderLayout.NORTH);
        frame.add(panelInferior, BorderLayout.SOUTH);

        campoBusqueda.addActionListener(e -> buscarLibros());
        botonAgregar.addActionListener(e -> mostrarFormularioAgregar());
        botonEliminar.addActionListener(e -> eliminarLibro());
        botonReservar.addActionListener(e -> mostrarMenuReservar());

        actualizarVistaLibros();
        frame.setVisible(true);
    }

    private void actualizarVistaLibros() {
        tableModel.actualizarLibros(gestorLibros.listarLibrosOrdenados() );
    }

    private void buscarLibros() {
        String termino = campoBusqueda.getText();
        List<Libro> resultados = gestorLibros.buscarLibroPorTitulo(termino);
        tableModel.actualizarLibros(resultados);
    }

    private void mostrarFormularioAgregar() {
        JTextField campoTitulo = new JTextField();
        JTextField campoAutor = new JTextField();
        String[] generos = Arrays.stream(Genero.values()).map(Genero::name).toArray(String[]::new); 
        JComboBox<String> campoGenero = new JComboBox<>(generos); 
        JTextField campoAnio = new JTextField();
        
        Object[] mensaje = {
            "Título:", campoTitulo,
            "Autor:", campoAutor,
            "Género:", campoGenero,
            "Año:", campoAnio
        };
    
        int opcion = JOptionPane.showConfirmDialog(frame, mensaje, "Agregar Libro", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            String titulo = campoTitulo.getText();
            String autor = campoAutor.getText();
            Genero genero = Genero.valueOf((String) campoGenero.getSelectedItem());
            int anio = Integer.parseInt(campoAnio.getText());
            Libro nuevoLibro = new Libro(titulo, autor, genero, anio, false);
            gestorLibros.agregarLibro(nuevoLibro);    
            actualizarVistaLibros(); 
        }
    }

    private void eliminarLibro() {
        int filaSeleccionada = tablaLibros.getSelectedRow();
        if (filaSeleccionada != -1) {
            Libro libroSeleccionado = tableModel.getLibroEnFila(filaSeleccionada);
            gestorLibros.eliminarLibro(libroSeleccionado.getTitulo()); 
            actualizarVistaLibros();
        } else {
            JOptionPane.showMessageDialog(frame, "Por favor, selecciona un libro para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarMenuReservar() {
        List<Libro> libros = gestorLibros.listarLibrosOrdenados();
        String[] opciones = new String[libros.size()];
        
        for (int i = 0; i < libros.size(); i++) {
            Libro libro = libros.get(i);
            opciones[i] = libro.getTitulo() + " - " + (libro.isReservado() ? "Reservado" : "Disponible");
        }

        String seleccion = (String) JOptionPane.showInputDialog(frame, "Seleccione un libro para reservar:",
                "Reservar Libro", JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);

        if (seleccion != null) {
            
            for (Libro libro : libros) {
                if (seleccion.startsWith(libro.getTitulo())) {
                    libro.setReservado(!libro.isReservado()); 
                    break;
                }
            }
            
            gestorLibros.guardarLibrosEnArchivo();
            actualizarVistaLibros();
        }
    }

    private class LibroTableModel extends AbstractTableModel {
        private List<Libro> libros;
        private String[] columnas = {"Título", "Autor", "Género", "Año", "Reservado"};

        public LibroTableModel(List<Libro> libros) {
            this.libros = libros;
        }

        @Override
        public int getRowCount() {
            return libros.size();
        }

        @Override
        public int getColumnCount() {
            return columnas.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Libro libro = libros.get(rowIndex);
            switch (columnIndex) {
                case 0: return libro.getTitulo();
                case 1: return libro.getAutor();
                case 2: return libro.getGenero();
                case 3: return libro.getAño();
                case 4: return libro.isReservado() ? "Sí" : "No";
                default: return null;
            }
        }

        @Override
        public String getColumnName(int column) {
            return columnas[column];
        }

        public void actualizarLibros(List<Libro> nuevosLibros) {
            this.libros = nuevosLibros;
            fireTableDataChanged();
        }

        public Libro getLibroEnFila(int fila) {
            return libros.get(fila);
        }
    }
}