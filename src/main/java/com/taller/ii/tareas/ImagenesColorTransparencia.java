package com.taller.ii.tareas;

import com.taller.ii.util.ImagenUtils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImagenesColorTransparencia {

    private static final String RUTA_ENTRADA =
            "src/main/resources/img/m4.jpg";

    private static final Color[] COLORES = {
            new Color(128, 128, 128), // Gris
            new Color(40, 90, 220),   // Azul
            new Color(220, 110, 40),  // Naranja
            new Color(40, 190, 220),  // Cian
            new Color(220, 210, 40),  // Amarillo
            new Color(60, 180, 60)    // Verde
    };


    public static BufferedImage aplicarTinte(
            BufferedImage original,
            Color color,
            int intensidad,
            int alfa
    ) {

        if (original == null || color == null) {
            throw new IllegalArgumentException(
                    "La imagen y el color son obligatorios."
            );
        }

        validarRango(intensidad, "intensidad");
        validarRango(alfa, "alfa");

        int ancho = original.getWidth();
        int alto = original.getHeight();

        BufferedImage resultado = new BufferedImage(
                ancho,
                alto,
                BufferedImage.TYPE_INT_ARGB
        );

        int fr = color.getRed();
        int fg = color.getGreen();
        int fb = color.getBlue();

        for (int y = 0; y < alto; y++) {

            for (int x = 0; x < ancho; x++) {

                int pixel = original.getRGB(x, y);

                int r = (pixel >> 16) & 0xFF;
                int g = (pixel >> 8) & 0xFF;
                int b = pixel & 0xFF;

                int nr = mezclarCanal(
                        r,
                        fr,
                        intensidad
                );

                int ng = mezclarCanal(
                        g,
                        fg,
                        intensidad
                );

                int nb = mezclarCanal(
                        b,
                        fb,
                        intensidad
                );

                int pixelResultado =
                        (alfa << 24)
                                | (nr << 16)
                                | (ng << 8)
                                | nb;

                resultado.setRGB(
                        x,
                        y,
                        pixelResultado
                );
            }
        }

        return resultado;
    }


    private static int mezclarCanal(
            int original,
            int filtro,
            int intensidad
    ) {

        return (
                original * (255 - intensidad)
                        + filtro * intensidad
        ) / 255;
    }

    private static void validarRango(
            int valor,
            String nombre
    ) {

        if (valor < 0 || valor > 255) {
            throw new IllegalArgumentException(
                    nombre + " debe estar entre 0 y 255."
            );
        }
    }

    public static void generarTodas()
            throws IOException {

        BufferedImage original =
                ImagenUtils.cargarImagen(RUTA_ENTRADA);

        int intensidad = 100;
        int alfa = 180;

        for (int i = 0; i < COLORES.length; i++) {

            BufferedImage resultado =
                    aplicarTinte(
                            original,
                            COLORES[i],
                            intensidad,
                            alfa
                    );

            String ruta =
                    "src/main/resources/img/color_"
                            + (i + 1)
                            + ".png";

            ImagenUtils.guardarImagen(
                    resultado,
                    ruta
            );

            System.out.println(
                    "Imagen generada: " + ruta
            );
        }
    }

    public static void main(String[] args) {

        try {

            generarTodas();

            System.out.println(
                    "Imágenes con transparencia generadas."
            );

        } catch (IOException e) {

            System.err.println(
                    "Error: " + e.getMessage()
            );
        }
    }
}