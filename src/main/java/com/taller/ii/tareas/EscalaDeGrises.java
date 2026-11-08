package com.taller.ii.tareas;

import com.taller.ii.util.ImagenUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class EscalaDeGrises {

    private static final String RUTA_ENTRADA =
            "src/main/resources/img/m4.jpg";

    private static final int[] NIVELES = {
            256, 128, 64, 32, 16, 2
    };

    /**
     * Convierte una imagen a escala de grises
     * con el número de niveles especificado.
     */
    public static BufferedImage convertir(
            BufferedImage original,
            int niveles
    ) {

        if (original == null) {
            throw new IllegalArgumentException(
                    "La imagen no puede ser null."
            );
        }

        if (niveles < 2 || niveles > 256) {
            throw new IllegalArgumentException(
                    "Los niveles deben estar entre 2 y 256."
            );
        }

        int ancho = original.getWidth();
        int alto = original.getHeight();

        BufferedImage resultado = new BufferedImage(
                ancho,
                alto,
                BufferedImage.TYPE_INT_RGB
        );

        for (int y = 0; y < alto; y++) {

            for (int x = 0; x < ancho; x++) {

                int pixel = original.getRGB(x, y);

                int r = (pixel >> 16) & 0xFF;
                int g = (pixel >> 8) & 0xFF;
                int b = pixel & 0xFF;

                // Se conserva el promedio simple del ejercicio.
                int gris = (r + g + b) / 3;

                // Cuantización de niveles.
                int nivel = (gris * (niveles - 1) + 127) / 255;

                int grisActual =
                        (nivel * 255 + (niveles - 1) / 2)
                                / (niveles - 1);

                int pixelGris =
                        ImagenUtils.empaquetarRGB(
                                grisActual,
                                grisActual,
                                grisActual
                        );

                resultado.setRGB(
                        x,
                        y,
                        pixelGris
                );
            }
        }

        return resultado;
    }

    public static void generarTodas()
            throws IOException {

        BufferedImage original =
                ImagenUtils.cargarImagen(RUTA_ENTRADA);

        for (int nivel : NIVELES) {

            BufferedImage resultado =
                    convertir(original, nivel);

            String ruta =
                    "src/main/resources/img/gris_"
                            + nivel
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
                    "Imágenes en escala de grises generadas."
            );

        } catch (IOException e) {

            System.err.println(
                    "Error al generar escala de grises: "
                            + e.getMessage()
            );
        }
    }
}