package com.taller.ii.tareas;

import com.taller.ii.util.ImagenUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Tarea03Main {

    private static final String RUTA_ENTRADA =
            "src/main/resources/img/m4.jpg";

    private static final String CARPETA_SALIDA =
            "src/main/resources/img/Recolor";


    public enum FiltroRecolor {

        ORIGINAL(0, "Original"),
        GRIS(1, "Escala de grises"),
        SEPIA(2, "Sepia"),
        ACLARADO(3, "Aclarado"),

        BLANCO_NEGRO_1(4, "Blanco y negro 1"),
        BLANCO_NEGRO_2(5, "Blanco y negro 2"),
        BLANCO_NEGRO_3(6, "Blanco y negro 3"),

        GRIS_OSCURO(7, "Gris oscuro"),
        AZUL_OSCURO(8, "Azul oscuro"),
        NARANJA_OSCURO(9, "Naranja oscuro"),
        GRIS_MEDIO(10, "Gris medio"),
        AMARILLO_OSCURO(11, "Amarillo oscuro"),

        AZUL(12, "Azul"),
        VERDE(13, "Verde"),

        GRIS_CLARO_1(14, "Gris claro 1"),
        AZUL_CLARO_1(15, "Azul claro 1"),
        NARANJA_CLARO(16, "Naranja claro"),
        GRIS_CLARO_2(17, "Gris claro 2"),
        AMARILLO_CLARO(18, "Amarillo claro"),
        AZUL_CLARO_2(19, "Azul claro 2"),
        VERDE_CLARO(20, "Verde claro");

        private final int codigo;
        private final String nombreVisible;

        FiltroRecolor(
                int codigo,
                String nombreVisible
        ) {

            this.codigo = codigo;
            this.nombreVisible = nombreVisible;
        }

        public int getCodigo() {
            return codigo;
        }

        public String getNombreVisible() {
            return nombreVisible;
        }

        @Override
        public String toString() {
            return codigo + " - " + nombreVisible;
        }
    }


    public static BufferedImage aplicarFiltro(
            BufferedImage original,
            FiltroRecolor filtro
    ) {

        if (original == null || filtro == null) {
            throw new IllegalArgumentException(
                    "La imagen y el filtro son obligatorios."
            );
        }

        BufferedImage resultado = new BufferedImage(
                original.getWidth(),
                original.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );

        for (int y = 0; y < original.getHeight(); y++) {

            for (int x = 0; x < original.getWidth(); x++) {

                int pixel = original.getRGB(x, y);

                int r = (pixel >> 16) & 0xFF;
                int g = (pixel >> 8) & 0xFF;
                int b = pixel & 0xFF;

                int nuevoPixel = transformarPixel(
                        r,
                        g,
                        b,
                        filtro.codigo
                );

                resultado.setRGB(
                        x,
                        y,
                        nuevoPixel
                );
            }
        }

        return resultado;
    }

    public static BufferedImage aplicarFiltro(
            BufferedImage original,
            int codigoFiltro
    ) {

        FiltroRecolor filtro = obtenerFiltro(codigoFiltro);

        return aplicarFiltro(
                original,
                filtro
        );
    }


    public static FiltroRecolor obtenerFiltro(
            int codigo
    ) {

        for (FiltroRecolor filtro :
                FiltroRecolor.values()) {

            if (filtro.codigo == codigo) {
                return filtro;
            }
        }

        throw new IllegalArgumentException(
                "Código de filtro inválido: " + codigo
        );
    }


    public static String[] obtenerNombresFiltros() {

        FiltroRecolor[] filtros =
                FiltroRecolor.values();

        String[] nombres = new String[filtros.length];

        for (int i = 0; i < filtros.length; i++) {
            nombres[i] = filtros[i].toString();
        }

        return nombres;
    }


    public static int transformarPixel(
            int r,
            int g,
            int b,
            int tipoFiltro
    ) {

        FiltroRecolor filtro =
                obtenerFiltro(tipoFiltro);

        int gris = calcularGris(r, g, b);

        int r2 = r;
        int g2 = g;
        int b2 = b;

        switch (filtro) {

            case ORIGINAL:

                r2 = r;
                g2 = g;
                b2 = b;

                break;

            case GRIS:

                r2 = g2 = b2 = gris;

                break;

            case SEPIA:

                r2 = (int) (gris * 1.07);
                g2 = gris;
                b2 = (int) (gris * 0.80);

                break;

            case ACLARADO:

                r2 = aclarar(r);
                g2 = aclarar(g);
                b2 = aclarar(b);

                break;

            case BLANCO_NEGRO_1:

                r2 = g2 = b2 = umbral(gris, 128);

                break;

            case BLANCO_NEGRO_2:

                r2 = g2 = b2 = umbral(gris, 170);

                break;

            case BLANCO_NEGRO_3:

                r2 = g2 = b2 = umbral(gris, 90);

                break;

            case GRIS_OSCURO:

                gris = oscurecer(gris, 0.55);

                r2 = g2 = b2 = gris;

                break;

            case AZUL_OSCURO:

                r2 = (int) (gris * 0.30);
                g2 = (int) (gris * 0.55);
                b2 = (int) (gris * 0.90);

                break;

            case NARANJA_OSCURO:

                r2 = (int) (gris * 0.90);
                g2 = (int) (gris * 0.50);
                b2 = (int) (gris * 0.25);

                break;

            case GRIS_MEDIO:

                gris = oscurecer(gris, 0.75);

                r2 = g2 = b2 = gris;

                break;

            case AMARILLO_OSCURO:

                r2 = (int) (gris * 0.90);
                g2 = (int) (gris * 0.80);
                b2 = (int) (gris * 0.25);

                break;

            case AZUL:

                r2 = (int) (gris * 0.45);
                g2 = (int) (gris * 0.70);
                b2 = gris;

                break;

            case VERDE:

                r2 = (int) (gris * 0.40);
                g2 = gris;
                b2 = (int) (gris * 0.40);

                break;

            case GRIS_CLARO_1:

                gris = aclararPorcentaje(gris, 0.40);

                r2 = g2 = b2 = gris;

                break;

            case AZUL_CLARO_1:

                r2 = (int) (gris * 0.60);
                g2 = (int) (gris * 0.80);
                b2 = aclarar(gris);

                break;

            case NARANJA_CLARO:

                r2 = aclarar(gris);
                g2 = (int) (gris * 0.75);
                b2 = (int) (gris * 0.45);

                break;

            case GRIS_CLARO_2:

                gris = aclararPorcentaje(gris, 0.65);

                r2 = g2 = b2 = gris;

                break;

            case AMARILLO_CLARO:

                r2 = aclarar(gris);
                g2 = aclarar((int) (gris * 0.85));
                b2 = (int) (gris * 0.50);

                break;

            case AZUL_CLARO_2:

                r2 = (int) (gris * 0.70);
                g2 = aclarar(gris);
                b2 = aclarar(gris);

                break;

            case VERDE_CLARO:

                r2 = (int) (gris * 0.65);
                g2 = aclarar(gris);
                b2 = (int) (gris * 0.60);

                break;
        }

        r2 = limitar(r2);
        g2 = limitar(g2);
        b2 = limitar(b2);

        return (r2 << 16)
                | (g2 << 8)
                | b2;
    }

    private static int calcularGris(
            int r,
            int g,
            int b
    ) {

        return limitar(
                (int) Math.round(
                        r * 0.299
                                + g * 0.587
                                + b * 0.114
                )
        );
    }


    private static int aclarar(int valor) {

        return aclararPorcentaje(valor, 0.65);
    }

    private static int aclararPorcentaje(
            int valor,
            double porcentaje
    ) {

        return limitar(
                (int) Math.round(
                        valor
                                + (255 - valor) * porcentaje
                )
        );
    }

    private static int oscurecer(
            int valor,
            double factor
    ) {

        return limitar(
                (int) Math.round(valor * factor)
        );
    }

    private static int umbral(
            int gris,
            int limite
    ) {

        return gris >= limite ? 255 : 0;
    }

    private static int limitar(int valor) {

        return Math.max(
                0,
                Math.min(255, valor)
        );
    }

    public static void generarTodas()
            throws IOException {

        BufferedImage original =
                ImagenUtils.cargarImagen(RUTA_ENTRADA);

        File carpeta = new File(CARPETA_SALIDA);

        if (!carpeta.exists() && !carpeta.mkdirs()) {
            throw new IOException(
                    "No se pudo crear la carpeta Recolor."
            );
        }

        for (FiltroRecolor filtro :
                FiltroRecolor.values()) {

            BufferedImage resultado =
                    aplicarFiltro(original, filtro);

            String nombreArchivo =
                    String.format(
                            "%02d_%s.jpg",
                            filtro.codigo + 1,
                            filtro.name()
                    );

            File destino =
                    new File(carpeta, nombreArchivo);

            ImagenUtils.guardarImagen(
                    resultado,
                    destino.getAbsolutePath()
            );

            System.out.println(
                    "Filtro generado: "
                            + filtro.nombreVisible
            );
        }
    }

    public static void main(String[] args) {

        try {

            generarTodas();

            System.out.println(
                    "Los 21 filtros Recolor fueron generados."
            );

        } catch (IOException e) {

            System.err.println(
                    "Error al generar Recolor: "
                            + e.getMessage()
            );
        }
    }
}