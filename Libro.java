public class Libro extends Publicacion implements Reservable {
    private Genero genero; 
    private boolean reservado;

    public Libro(String titulo, String autor, Genero genero, int año, boolean reservado) {
        super(titulo, autor, año);
        this.genero = genero;
        this.reservado = reservado;
    }

    public String getTitulo() {
        return super.getTitulo(); 
    }

    public String getAutor() {
        return super.getAutor(); 
    }

    public Genero getGenero() {
        return genero;
    }

    public String getGeneroString() {
        return genero.name(); 
    }

    public int getAño() {
        return super.getAño(); 
    }

    @Override
    public boolean isReservado() {
        return reservado;
    }

    @Override
    public void setReservado(boolean reservado) {
        this.reservado = reservado;
    }

    @Override
    public String toString() {
        return "Título: " + getTitulo() + ", Autor: " + getAutor() + ", Género: " + getGeneroString() + 
               ", Año: " + getAño() + ", Reservado: " + (reservado ? "Sí" : "No");
    }
}
