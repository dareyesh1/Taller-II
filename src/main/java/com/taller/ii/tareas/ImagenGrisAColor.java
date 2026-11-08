package com.taller.ii.tareas;

import com.taller.ii.util.ImagenUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImagenGrisAColor {

    private static final String RUTA_ENTRADA =
            "src/main/resources/img/gris_256.png";

    private static final String RUTA_SALIDA =
            "src/main/resources/img/imagen_coloreada.png";


    public static BufferedImage colorear(
            BufferedImage original
    ) {

        if (original == null) {
            throw new IllegalArgumentException(
                    "La imagen no puede ser null."
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

                int gris = pixel & 0xFF;

                int r = gris / 8;
                int g = gris / 4;
                int b = gris;

                int pixelColor =
                        ImagenUtils.empaquetarRGB(
                                r,
                                g,
                                b
                        );

                resultado.setRGB(
                        x,
                        y,
                        pixelColor
                );
            }
        }

        return resultado;
    }

    public static void main(String[] args) {

        try {

            BufferedImage original =
                    ImagenUtils.cargarImagen(RUTA_ENTRADA);

            BufferedImage resultado =
                    colorear(original);

            ImagenUtils.guardarImagen(
                    resultado,
                    RUTA_SALIDA
            );

            System.out.println(
                    "Imagen pseudocoloreada correctamente."
            );

        } catch (IOException e) {

            System.err.println(
                    "Error al colorear: " + e.getMessage()
            );
        }
    }
}