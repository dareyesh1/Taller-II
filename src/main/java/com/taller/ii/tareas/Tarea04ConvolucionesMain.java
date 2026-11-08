package com.taller.ii.tareas;


import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Tarea04ConvolucionesMain {

    private static final String RUTA_ORIGINAL =
            "src/main/resources/img/m4.jpg";

    private static final String RUTA_DESTINO =
            "src/main/resources/img/";

    // =====================================================
    // CLASE QUE REPRESENTA UN FILTRO
    // =====================================================

    public static class Filtro {

        private final String nombre;
        private final int[][] matriz;
        private final int divisor;
        private final int brillo;

        public Filtro(String nombre, int[][] matriz,
                      int divisor, int brillo) {

            this.nombre = nombre;
            this.matriz = matriz;
            this.divisor = divisor;
            this.brillo = brillo;
        }

        public String getNombre() {
            return nombre;
        }

        public int[][] getMatriz() {
            return matriz;
        }

        public int getDivisor() {
            return divisor;
        }

        public int getBrillo() {
            return brillo;
        }
    }

    // =====================================================
    // GENERADORES DE MATRICES
    // =====================================================

    // 1. Matriz de promedio
    private static int[][] crearPromedio(int n) {

        int[][] matriz = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matriz[i][j] = 1;
            }
        }

        return matriz;
    }

    // 2. Matriz gaussiana mediante coeficientes binomiales
    private static int[][] crearGaussiana(int n) {

        int[] valores;

        if (n == 7) {
            valores = new int[]{
                    1, 6, 15, 20, 15, 6, 1
            };
        } else {
            valores = new int[]{
                    1, 8, 28, 56, 70,
                    56, 28, 8, 1
            };
        }

        int[][] matriz = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {

                matriz[i][j] = valores[i] * valores[j];

            }
        }

        return matriz;
    }

    // 3. Matriz de enfoque
    private static int[][] crearEnfoque(int n) {

        int[][] matriz = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {

                matriz[i][j] = -1;

            }
        }

        matriz[n / 2][n / 2] = n * n;

        return matriz;
    }

    // 4. Matriz Laplaciana
    private static int[][] crearLaplaciano(int n) {

        int[][] matriz = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {

                matriz[i][j] = -1;

            }
        }

        matriz[n / 2][n / 2] = (n * n) - 1;

        return matriz;
    }

    // 5. Matriz de relieve
    private static int[][] crearRelieve(int n) {

        int[][] matriz = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {

                if (i < n / 2 && j < n / 2) {

                    matriz[i][j] = -1;

                } else if (i > n / 2 && j > n / 2) {

                    matriz[i][j] = 1;

                } else {

                    matriz[i][j] = 0;

                }
            }
        }

        return matriz;
    }

    // 6. Matriz para detectar bordes verticales
    private static int[][] crearBordesVerticales(int n) {

        int[][] matriz = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {

                if (j < n / 2) {

                    matriz[i][j] = -1;

                } else if (j > n / 2) {

                    matriz[i][j] = 1;

                } else {

                    matriz[i][j] = 0;

                }
            }
        }

        return matriz;
    }

    // =====================================================
    // DEFINICIÓN DE LOS 10 FILTROS
    // =====================================================

    public static Filtro filtroPromedio7() {

        return new Filtro(
                "Promedio 7x7",
                crearPromedio(7),
                49,
                0
        );
    }

    public static Filtro filtroGaussiano7() {

        return new Filtro(
                "Gaussiano 7x7",
                crearGaussiana(7),
                4096,
                0
        );
    }

    public static Filtro filtroEnfoque7() {

        return new Filtro(
                "Enfoque 7x7",
                crearEnfoque(7),
                1,
                0
        );
    }

    public static Filtro filtroLaplaciano7() {

        return new Filtro(
                "Laplaciano 7x7",
                crearLaplaciano(7),
                1,
                128
        );
    }

    public static Filtro filtroRelieve7() {

        return new Filtro(
                "Relieve 7x7",
                crearRelieve(7),
                1,
                128
        );
    }

    public static Filtro filtroPromedio9() {

        return new Filtro(
                "Promedio 9x9",
                crearPromedio(9),
                81,
                0
        );
    }

    public static Filtro filtroGaussiano9() {

        return new Filtro(
                "Gaussiano 9x9",
                crearGaussiana(9),
                65536,
                0
        );
    }

    public static Filtro filtroEnfoque9() {

        return new Filtro(
                "Enfoque 9x9",
                crearEnfoque(9),
                1,
                0
        );
    }

    public static Filtro filtroLaplaciano9() {

        return new Filtro(
                "Laplaciano 9x9",
                crearLaplaciano(9),
                1,
                128
        );
    }

    public static Filtro filtroBordes9() {

        return new Filtro(
                "Bordes verticales 9x9",
                crearBordesVerticales(9),
                1,
                128
        );
    }

    // =====================================================
    // APLICAR CONVOLUCIÓN
    // =====================================================

    public static BufferedImage aplicarFiltro(
            BufferedImage original,
            Filtro filtro) {

        int ancho = original.getWidth();
        int alto = original.getHeight();

        BufferedImage resultado = new BufferedImage(
                ancho,
                alto,
                BufferedImage.TYPE_INT_RGB
        );

        int[][] matriz = filtro.getMatriz();

        int dimension = matriz.length;
        int radio = dimension / 2;

        int divisor = filtro.getDivisor();
        int brillo = filtro.getBrillo();

        for (int x = 0; x < ancho; x++) {

            for (int y = 0; y < alto; y++) {

                int r1 = 0;
                int g1 = 0;
                int b1 = 0;

                for (int i = -radio; i <= radio; i++) {

                    for (int j = -radio; j <= radio; j++) {

                        int px = reflejar(x + i, ancho);
                        int py = reflejar(y + j, alto);

                        Color color = new Color(
                                original.getRGB(px, py)
                        );

                        int r = color.getRed();
                        int g = color.getGreen();
                        int b = color.getBlue();

                        int coeficiente =
                                matriz[i + radio][j + radio];

                        r1 += r * coeficiente;
                        g1 += g * coeficiente;
                        b1 += b * coeficiente;

                    }
                }

                int rfinal = limitar(
                        r1 / divisor + brillo
                );

                int gfinal = limitar(
                        g1 / divisor + brillo
                );

                int bfinal = limitar(
                        b1 / divisor + brillo
                );

                int pixel = (rfinal << 16)
                        | (gfinal << 8)
                        | bfinal;

                resultado.setRGB(x, y, pixel);

            }
        }

        return resultado;
    }

    // =====================================================
    // CONTROL DE BORDES
    // =====================================================

    private static int reflejar(int posicion, int limite) {

        if (limite <= 1) {
            return 0;
        }

        while (posicion < 0 || posicion >= limite) {

            if (posicion < 0) {
                posicion = -posicion - 1;
            }

            if (posicion >= limite) {
                posicion = 2 * limite - posicion - 1;
            }
        }

        return posicion;
    }

    // =====================================================
    // LIMITAR VALORES RGB
    // =====================================================

    private static int limitar(int valor) {

        return Math.max(0, Math.min(255, valor));

    }

    // =====================================================
    // CARGAR IMAGEN
    // =====================================================

    public static BufferedImage cargarImagen(String ruta)
            throws IOException {

        return ImageIO.read(new File(ruta));

    }

    // =====================================================
    // GUARDAR IMAGEN
    // =====================================================

    public static void guardarImagen(
            BufferedImage imagen,
            String ruta) throws IOException {

        ImageIO.write(
                imagen,
                "jpg",
                new File(ruta)
        );

    }

    // =====================================================
    // MÉTODO GENERAL PARA INTERFAZ GRÁFICA
    // =====================================================

    public static void ejecutarFiltro(
            String rutaEntrada,
            String rutaSalida,
            Filtro filtro) throws IOException {

        BufferedImage original = cargarImagen(rutaEntrada);

        BufferedImage resultado =
                aplicarFiltro(original, filtro);

        guardarImagen(resultado, rutaSalida);

    }

    // =====================================================
    // MÉTODOS INDEPENDIENTES PARA LA INTERFAZ GRÁFICA
    // =====================================================

    public static void aplicarPromedio7() throws IOException {
        ejecutarFiltro(RUTA_ORIGINAL,
                RUTA_DESTINO + "promedio7.jpg",
                filtroPromedio7());
    }

    public static void aplicarGaussiano7() throws IOException {
        ejecutarFiltro(RUTA_ORIGINAL,
                RUTA_DESTINO + "gaussiano7.jpg",
                filtroGaussiano7());
    }

    public static void aplicarEnfoque7() throws IOException {
        ejecutarFiltro(RUTA_ORIGINAL,
                RUTA_DESTINO + "enfoque7.jpg",
                filtroEnfoque7());
    }

    public static void aplicarLaplaciano7() throws IOException {
        ejecutarFiltro(RUTA_ORIGINAL,
                RUTA_DESTINO + "laplaciano7.jpg",
                filtroLaplaciano7());
    }

    public static void aplicarRelieve7() throws IOException {
        ejecutarFiltro(RUTA_ORIGINAL,
                RUTA_DESTINO + "relieve7.jpg",
                filtroRelieve7());
    }

    public static void aplicarPromedio9() throws IOException {
        ejecutarFiltro(RUTA_ORIGINAL,
                RUTA_DESTINO + "promedio9.jpg",
                filtroPromedio9());
    }

    public static void aplicarGaussiano9() throws IOException {
        ejecutarFiltro(RUTA_ORIGINAL,
                RUTA_DESTINO + "gaussiano9.jpg",
                filtroGaussiano9());
    }

    public static void aplicarEnfoque9() throws IOException {
        ejecutarFiltro(RUTA_ORIGINAL,
                RUTA_DESTINO + "enfoque9.jpg",
                filtroEnfoque9());
    }

    public static void aplicarLaplaciano9() throws IOException {
        ejecutarFiltro(RUTA_ORIGINAL,
                RUTA_DESTINO + "laplaciano9.jpg",
                filtroLaplaciano9());
    }

    public static void aplicarBordes9() throws IOException {
        ejecutarFiltro(RUTA_ORIGINAL,
                RUTA_DESTINO + "bordes9.jpg",
                filtroBordes9());
    }

    // =====================================================
    // MAIN PARA PROBAR LOS 10 FILTROS
    // =====================================================

    public static void main(String[] args) {

        try {

            aplicarPromedio7();
            aplicarGaussiano7();
            aplicarEnfoque7();
            aplicarLaplaciano7();
            aplicarRelieve7();

            aplicarPromedio9();
            aplicarGaussiano9();
            aplicarEnfoque9();
            aplicarLaplaciano9();
            aplicarBordes9();

            System.out.println(
                    "Los 10 filtros se aplicaron correctamente."
            );

        } catch (Exception e) {

            e.printStackTrace();

        }
    }
}
