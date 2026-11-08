package com.taller.ii.clases;

import com.taller.ii.util.ImagenUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ClonarImagen {

    private static final String RUTA_ENTRADA =
            "src/main/resources/img/m4.jpg";

    private static final String RUTA_SALIDA =
            "src/main/resources/img/m4_clon.jpg";


    public static BufferedImage clonar(
            BufferedImage imagenEntrada
    ) {

        if (imagenEntrada == null) {
            throw new IllegalArgumentException(
                    "La imagen de entrada no puede ser null."
            );
        }

        int ancho = imagenEntrada.getWidth();
        int alto = imagenEntrada.getHeight();

        BufferedImage imagenSalida = new BufferedImage(
                ancho,
                alto,
                BufferedImage.TYPE_INT_RGB
        );

        int mascara = 0xFF;

        for (int y = 0; y < alto; y++) {

            for (int x = 0; x < ancho; x++) {

                int pixel = imagenEntrada.getRGB(x, y);

                // Extraer los componentes RGB.
                int r = (pixel >> 16) & mascara;
                int g = (pixel >> 8) & mascara;
                int b = pixel & mascara;

                // Reconstruir correctamente el pixel.
                int pixel2 =
                        (r << 16)
                                | (g << 8)
                                | b;

                imagenSalida.setRGB(
                        x,
                        y,
                        pixel2
                );
            }
        }

        return imagenSalida;
    }


    public static void main(String[] args) {

        System.out.println("Clonando imagen...");

        try {

            BufferedImage imagenEntrada =
                    ImagenUtils.cargarImagen(RUTA_ENTRADA);

            BufferedImage imagenSalida =
                    clonar(imagenEntrada);

            ImagenUtils.guardarImagen(
                    imagenSalida,
                    RUTA_SALIDA
            );

            System.out.println(
                    "Imagen clonada correctamente."
            );

            System.out.println(
                    "Dimensiones: "
                            + imagenSalida.getWidth()
                            + " × "
                            + imagenSalida.getHeight()
            );

        } catch (IOException e) {

            System.err.println(
                    "Error al clonar la imagen: "
                            + e.getMessage()
            );
        }
    }
}