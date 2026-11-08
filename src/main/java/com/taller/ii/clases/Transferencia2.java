package com.taller.ii.clases;

import com.taller.ii.util.ImagenUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class Transferencia2 {

    private static final String RUTA_ENTRADA =
            "src/main/resources/img/m4.jpg";

    private static final String RUTA_SALIDA =
            "src/main/resources/img/m4Bits.jpg";

    /**
     * Genera el negativo de una imagen.
     */
    public static BufferedImage crearNegativo(
            BufferedImage original
    ) {

        validarImagen(original);

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

                int rNegativo = 255 - r;
                int gNegativo = 255 - g;
                int bNegativo = 255 - b;

                int pixelResultado =
                        ImagenUtils.empaquetarRGB(
                                rNegativo,
                                gNegativo,
                                bNegativo
                        );

                resultado.setRGB(
                        x,
                        y,
                        pixelResultado
                );
            }
        }

        return resultado;
    }

    /**
     * Reduce la profundidad de color de una imagen.
     *
     * niveles:
     * 2   = blanco y negro
     * 4   = 4 niveles por canal
     * 16  = 16 niveles por canal
     * 256 = profundidad completa
     */
    public static BufferedImage reducirProfundidad(
            BufferedImage original,
            int niveles
    ) {

        validarImagen(original);

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

                r = nuevaProfundidad(r, niveles);
                g = nuevaProfundidad(g, niveles);
                b = nuevaProfundidad(b, niveles);

                int pixelResultado =
                        ImagenUtils.empaquetarRGB(r, g, b);

                resultado.setRGB(
                        x,
                        y,
                        pixelResultado
                );
            }
        }

        return resultado;
    }

    /**
     * Cuantiza un canal RGB al número de niveles indicado.
     */
    public static int nuevaProfundidad(
            int original,
            int niveles
    ) {

        if (niveles < 2 || niveles > 256) {
            throw new IllegalArgumentException(
                    "Los niveles deben estar entre 2 y 256."
            );
        }

        original = ImagenUtils.limitar(original);

        int indice = (int) Math.round(
                original * (niveles - 1) / 255.0
        );

        return (int) Math.round(
                indice * 255.0 / (niveles - 1)
        );
    }

    /**
     * Versión útil para generar una imagen de niveles
     * de gris a partir de una imagen RGB.
     */
    public static BufferedImage imagenNiveles(
            BufferedImage original,
            int niveles
    ) {

        validarImagen(original);

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

                int gris = (r + g + b) / 3;

                gris = nuevaProfundidad(
                        gris,
                        niveles
                );

                int pixelGris =
                        ImagenUtils.empaquetarRGB(
                                gris,
                                gris,
                                gris
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

    private static void validarImagen(
            BufferedImage imagen
    ) {

        if (imagen == null) {
            throw new IllegalArgumentException(
                    "La imagen no puede ser null."
            );
        }
    }

    /**
     * Ejecuta el ejercicio de negativo.
     */
    public static void main(String[] args) {

        try {

            BufferedImage original =
                    ImagenUtils.cargarImagen(RUTA_ENTRADA);

            BufferedImage resultado =
                    crearNegativo(original);

            ImagenUtils.guardarImagen(
                    resultado,
                    RUTA_SALIDA
            );

            System.out.println(
                    "Imagen negativa generada correctamente."
            );

        } catch (IOException e) {

            System.err.println(
                    "Error: " + e.getMessage()
            );
        }
    }
}