package com.taller.ii.clases;

import com.taller.ii.util.ImagenUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class Convoluciones {

    private static final String RUTA_ENTRADA =
            "src/main/resources/img/m4.jpg";

    private static final String RUTA_SALIDA =
            "src/main/resources/img/m4Convolucion.jpg";


    public static BufferedImage aplicarKernel(
            BufferedImage original,
            int[][] kernel,
            int divisor,
            int brillo
    ) {

        validarParametros(
                original,
                kernel,
                divisor
        );

        int ancho = original.getWidth();
        int alto = original.getHeight();

        int dimension = kernel.length;
        int radio = dimension / 2;

        BufferedImage resultado = new BufferedImage(
                ancho,
                alto,
                BufferedImage.TYPE_INT_RGB
        );


        int[] entrada = original.getRGB(
                0,
                0,
                ancho,
                alto,
                null,
                0,
                ancho
        );

        int[] salida = new int[entrada.length];

        for (int y = 0; y < alto; y++) {

            for (int x = 0; x < ancho; x++) {

                int sumaR = 0;
                int sumaG = 0;
                int sumaB = 0;

                for (int ky = 0; ky < dimension; ky++) {

                    for (int kx = 0; kx < dimension; kx++) {

                        int px = reflejar(
                                x + kx - radio,
                                ancho
                        );

                        int py = reflejar(
                                y + ky - radio,
                                alto
                        );

                        int pixel = entrada[
                                py * ancho + px
                                ];

                        int r = (pixel >> 16) & 0xFF;
                        int g = (pixel >> 8) & 0xFF;
                        int b = pixel & 0xFF;

                        int coeficiente = kernel[ky][kx];

                        sumaR += r * coeficiente;
                        sumaG += g * coeficiente;
                        sumaB += b * coeficiente;
                    }
                }

                int r = limitar(
                        sumaR / divisor + brillo
                );

                int g = limitar(
                        sumaG / divisor + brillo
                );

                int b = limitar(
                        sumaB / divisor + brillo
                );

                salida[y * ancho + x] =
                        ImagenUtils.empaquetarRGB(r, g, b);
            }
        }

        resultado.setRGB(
                0,
                0,
                ancho,
                alto,
                salida,
                0,
                ancho
        );

        return resultado;
    }


    private static int reflejar(
            int posicion,
            int limite
    ) {

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

    private static int limitar(int valor) {

        return Math.max(
                0,
                Math.min(255, valor)
        );
    }

    private static void validarParametros(
            BufferedImage original,
            int[][] kernel,
            int divisor
    ) {

        if (original == null) {
            throw new IllegalArgumentException(
                    "La imagen no puede ser null."
            );
        }

        if (kernel == null || kernel.length == 0) {
            throw new IllegalArgumentException(
                    "El kernel no puede estar vacío."
            );
        }

        if (divisor == 0) {
            throw new IllegalArgumentException(
                    "El divisor no puede ser cero."
            );
        }

        int dimension = kernel.length;

        if (dimension % 2 == 0) {
            throw new IllegalArgumentException(
                    "El kernel debe tener una dimensión impar."
            );
        }

        for (int[] fila : kernel) {

            if (fila == null || fila.length != dimension) {
                throw new IllegalArgumentException(
                        "El kernel debe ser cuadrado."
                );
            }
        }
    }

    public static void main(String[] args) {

        try {

            BufferedImage original =
                    ImagenUtils.cargarImagen(RUTA_ENTRADA);

            int[][] kernel = {
                    {0, -1, 0},
                    {-1, 4, -1},
                    {0, -1, 0}
            };

            BufferedImage resultado =
                    aplicarKernel(
                            original,
                            kernel,
                            1,
                            128
                    );

            ImagenUtils.guardarImagen(
                    resultado,
                    RUTA_SALIDA
            );

            System.out.println(
                    "Convolución aplicada correctamente."
            );

        } catch (IOException e) {

            System.err.println(
                    "Error de convolución: " + e.getMessage()
            );
        }
    }
}