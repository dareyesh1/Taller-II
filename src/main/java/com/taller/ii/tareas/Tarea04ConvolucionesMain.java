package com.taller.ii.tareas;

import com.taller.ii.clases.Convoluciones;
import com.taller.ii.util.ImagenUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Tarea04ConvolucionesMain {

    private static final String RUTA_ORIGINAL =
            "src/main/resources/img/m4.jpg";

    private static final String RUTA_DESTINO =
            "src/main/resources/img/Convoluciones";


    public static class Filtro {

        private final String nombre;
        private final int[][] matriz;
        private final int divisor;
        private final int brillo;

        public Filtro(
                String nombre,
                int[][] matriz,
                int divisor,
                int brillo
        ) {

            if (nombre == null || matriz == null) {
                throw new IllegalArgumentException(
                        "El nombre y la matriz son obligatorios."
                );
            }

            this.nombre = nombre;
            this.matriz = copiarMatriz(matriz);
            this.divisor = divisor;
            this.brillo = brillo;
        }

        public String getNombre() {
            return nombre;
        }


        public int[][] getMatriz() {
            return copiarMatriz(matriz);
        }

        public int getDivisor() {
            return divisor;
        }

        public int getBrillo() {
            return brillo;
        }

        @Override
        public String toString() {
            return nombre;
        }
    }

    private static int[][] copiarMatriz(
            int[][] original
    ) {

        int[][] copia = new int[original.length][];

        for (int i = 0; i < original.length; i++) {
            copia[i] = original[i].clone();
        }

        return copia;
    }

    private static int[][] crearPromedio(int n) {

        int[][] matriz = new int[n][n];

        for (int i = 0; i < n; i++) {

            for (int j = 0; j < n; j++) {
                matriz[i][j] = 1;
            }
        }

        return matriz;
    }

    private static int[][] crearGaussiana(int n) {

        int[] valores;

        if (n == 7) {

            valores = new int[]{
                    1, 6, 15, 20, 15, 6, 1
            };

        } else if (n == 9) {

            valores = new int[]{
                    1, 8, 28, 56, 70,
                    56, 28, 8, 1
            };

        } else {

            throw new IllegalArgumentException(
                    "La matriz gaussiana debe ser 7x7 o 9x9."
            );
        }

        int[][] matriz = new int[n][n];

        for (int i = 0; i < n; i++) {

            for (int j = 0; j < n; j++) {

                matriz[i][j] =
                        valores[i] * valores[j];
            }
        }

        return matriz;
    }

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

    public static List<Filtro> obtenerFiltros() {

        List<Filtro> filtros = new ArrayList<>();

        filtros.add(filtroPromedio7());
        filtros.add(filtroGaussiano7());
        filtros.add(filtroEnfoque7());
        filtros.add(filtroLaplaciano7());
        filtros.add(filtroRelieve7());

        filtros.add(filtroPromedio9());
        filtros.add(filtroGaussiano9());
        filtros.add(filtroEnfoque9());
        filtros.add(filtroLaplaciano9());
        filtros.add(filtroBordes9());

        return filtros;
    }


    public static BufferedImage aplicarFiltro(
            BufferedImage original,
            Filtro filtro
    ) {

        if (original == null || filtro == null) {
            throw new IllegalArgumentException(
                    "La imagen y el filtro son obligatorios."
            );
        }

        return Convoluciones.aplicarKernel(
                original,
                filtro.getMatriz(),
                filtro.getDivisor(),
                filtro.getBrillo()
        );
    }


    public static BufferedImage cargarImagen(
            String ruta
    ) throws IOException {

        return ImagenUtils.cargarImagen(ruta);
    }

    public static void guardarImagen(
            BufferedImage imagen,
            String ruta
    ) throws IOException {

        ImagenUtils.guardarImagen(
                imagen,
                ruta
        );
    }

    public static void ejecutarFiltro(
            String rutaEntrada,
            String rutaSalida,
            Filtro filtro
    ) throws IOException {

        BufferedImage original =
                cargarImagen(rutaEntrada);

        BufferedImage resultado =
                aplicarFiltro(original, filtro);

        guardarImagen(
                resultado,
                rutaSalida
        );
    }


    public static void generarTodas()
            throws IOException {

        ImagenUtils.crearDirectorio(RUTA_DESTINO);

        for (Filtro filtro : obtenerFiltros()) {

            BufferedImage original =
                    cargarImagen(RUTA_ORIGINAL);

            BufferedImage resultado =
                    aplicarFiltro(original, filtro);

            String nombre =
                    filtro.getNombre()
                            .toLowerCase()
                            .replace(" ", "_")
                            .replace("x", "");

            String ruta =
                    RUTA_DESTINO + "/" + nombre + ".jpg";

            guardarImagen(
                    resultado,
                    ruta
            );

            System.out.println(
                    "Filtro generado: " + filtro.getNombre()
            );
        }
    }

    public static void main(String[] args) {

        try {

            generarTodas();

            System.out.println(
                    "Los diez filtros fueron generados."
            );

        } catch (IOException e) {

            System.err.println(
                    "Error al generar convoluciones: "
                            + e.getMessage()
            );
        }
    }
}