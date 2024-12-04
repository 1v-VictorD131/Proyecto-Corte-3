import javax.swing.table.AbstractTableModel;
import java.util.List;

public class LibroTableModel extends AbstractTableModel {
    private List<Libro> libros;
    private final String[] columnas = {"Título", "Autor", "Género", "Año", "Reservado"};

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
    public String getColumnName(int columnIndex) {
        return columnas[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Libro libro = libros.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return libro.getTitulo();
            case 1:
                return libro.getAutor();
            case 2:
                return libro.getGenero();
            case 3:
                return libro.getAño();
            case 4:
                return libro.isReservado() ? "Sí" : "No";
            default:
                return null;
        }
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
        fireTableDataChanged();
    }
}