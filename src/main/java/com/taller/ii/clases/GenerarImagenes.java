package com.taller.ii.clases;

import com.taller.ii.util.ImagenUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class GenerarImagenes {

    private static final int ANCHO = 800;
    private static final int ALTO = 600;

    private static final String CARPETA =
            "src/main/resources/img";

    /**
     * Genera un gradiente según el tipo seleccionado.
     *
     * 1: Horizontal
     * 2: Vertical
     * 3: Diagonal
     * 4: Radial
     * 5: Arcoíris
     */
    public static BufferedImage generarGradiente(int tipo) {

        BufferedImage imagen = new BufferedImage(
                ANCHO,
                ALTO,
                BufferedImage.TYPE_INT_RGB
        );

        int centroX = ANCHO / 2;
        int centroY = ALTO / 2;

        double distanciaMaxima =
                Math.hypot(centroX, centroY);

        for (int y = 0; y < ALTO; y++) {

            for (int x = 0; x < ANCHO; x++) {

                int pixel;

                switch (tipo) {

                    case 1: {

                        double t = x / (ANCHO - 1.0);

                        pixel = mezclar(
                                0x0050FF,
                                0xFF1E3C,
                                t
                        );

                        break;
                    }

                    case 2: {

                        double t = y / (ALTO - 1.0);

                        pixel = mezclar(
                                0x050F3C,
                                0xFFD200,
                                t
                        );

                        break;
                    }

                    case 3: {

                        double tx = x / (ANCHO - 1.0);
                        double ty = y / (ALTO - 1.0);

                        double t = (tx + ty) / 2.0;

                        pixel = mezclar(
                                0x7D00D2,
                                0x00E6C8,
                                t
                        );

                        break;
                    }

                    case 4: {

                        double dx = x - centroX;
                        double dy = y - centroY;

                        double distancia =
                                Math.hypot(dx, dy);

                        double t =
                                distancia / distanciaMaxima;

                        // Suavizar transición.
                        t = t * t * (3.0 - 2.0 * t);

                        pixel = mezclar(
                                0xFFE664,
                                0x0F0F5A,
                                t
                        );

                        break;
                    }

                    case 5: {

                        double dx = x - centroX;
                        double dy = y - centroY;

                        double distancia =
                                Math.hypot(dx, dy);

                        double tono =
                                (Math.atan2(dy, dx) + Math.PI)
                                        / (2.0 * Math.PI);

                        if (tono >= 1.0) {
                            tono = 0.0;
                        }

                        double brillo =
                                1.0 - 0.30
                                        * (distancia / distanciaMaxima);

                        pixel = arcoiris(tono, brillo);

                        break;
                    }

                    default:

                        throw new IllegalArgumentException(
                                "Tipo de gradiente no válido: " + tipo
                        );
                }

                imagen.setRGB(x, y, pixel);
            }
        }

        return imagen;
    }

    /**
     * Genera y guarda los cinco gradientes.
     */
    public static void generarYGuardarGradientes()
            throws IOException {

        String[] nombres = {
                "gradiente_1_horizontal",
                "gradiente_2_vertical",
                "gradiente_3_diagonal",
                "gradiente_4_radial",
                "gradiente_5_arcoiris"
        };

        for (int i = 0; i < nombres.length; i++) {

            BufferedImage imagen =
                    generarGradiente(i + 1);

            String ruta =
                    CARPETA + "/" + nombres[i] + ".png";

            ImagenUtils.guardarImagen(
                    imagen,
                    ruta
            );

            System.out.println(
                    "Gradiente creado: " + ruta
            );
        }
    }

    /**
     * Mezcla dos colores RGB.
     */
    private static int mezclar(
            int color1,
            int color2,
            double t
    ) {

        int r1 = (color1 >> 16) & 0xFF;
        int g1 = (color1 >> 8) & 0xFF;
        int b1 = color1 & 0xFF;

        int r2 = (color2 >> 16) & 0xFF;
        int g2 = (color2 >> 8) & 0xFF;
        int b2 = color2 & 0xFF;

        int r = (int) Math.round(
                r1 + (r2 - r1) * t
        );

        int g = (int) Math.round(
                g1 + (g2 - g1) * t
        );

        int b = (int) Math.round(
                b1 + (b2 - b1) * t
        );

        return ImagenUtils.empaquetarRGB(r, g, b);
    }

    /**
     * Genera un color arcoíris mediante HSV.
     */
    private static int arcoiris(
            double tono,
            double brillo
    ) {

        double h = tono * 6.0;

        int sector = (int) Math.floor(h);

        double f = h - sector;

        double saturacion = 0.90;

        double p = brillo * (1.0 - saturacion);

        double q = brillo * (1.0 - saturacion * f);

        double t = brillo
                * (1.0 - saturacion * (1.0 - f));

        double rojo;
        double verde;
        double azul;

        switch (sector) {

            case 0:

                rojo = brillo;
                verde = t;
                azul = p;

                break;

            case 1:

                rojo = q;
                verde = brillo;
                azul = p;

                break;

            case 2:

                rojo = p;
                verde = brillo;
                azul = t;

                break;

            case 3:

                rojo = p;
                verde = q;
                azul = brillo;

                break;

            case 4:

                rojo = t;
                verde = p;
                azul = brillo;

                break;

            default:

                rojo = brillo;
                verde = p;
                azul = q;

                break;
        }

        int r = (int) Math.round(rojo * 255);
        int g = (int) Math.round(verde * 255);
        int b = (int) Math.round(azul * 255);

        return ImagenUtils.empaquetarRGB(r, g, b);
    }

    public static void main(String[] args) {

        try {

            generarYGuardarGradientes();

            System.out.println(
                    "Los cinco gradientes fueron generados."
            );

        } catch (IOException e) {

            System.err.println(
                    "Error al generar gradientes: "
                            + e.getMessage()
            );
        }
    }
}