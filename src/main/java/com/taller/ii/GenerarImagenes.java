
package com.taller.ii;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.imageio.ImageIO;

public class GenerarImagenes{

    private static final int ancho = 800;
    private static final int alto = 600;

    private static final Path carpeta =
            Paths.get("src/main/resources/img");

    public static void main(String[] args) {

        try {

            crearGradiente("gradiente_1_horizontal", 1);
            crearGradiente("gradiente_2_vertical", 2);
            crearGradiente("gradiente_3_diagonal", 3);
            crearGradiente("gradiente_4_radial", 4);
            crearGradiente("gradiente_5_arcoiris", 5);

            System.out.println("Las cinco imagenes fueron generadas.");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void crearGradiente(String nombre, int tipo)
            throws Exception {


        BufferedImage imagen = new BufferedImage(
                ancho, alto, BufferedImage.TYPE_INT_RGB
        );

        int centroX = ancho / 2;
        int centroY = alto/ 2;

        double distanciaMaxima =
                Math.hypot(centroX, centroY);

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {

                int pixel;

                switch (tipo) {

                    // 1. Gradiente horizontal: azul a rojo
                    case 1: {
                        double t = x / (ancho - 1.0);

                        pixel = mezclar(
                                0x0050FF,
                                0xFF1E3C,
                                t
                        );
                        break;
                    }

                    // 2. Gradiente vertical: azul oscuro a amarillo
                    case 2: {
                        double t = y / (alto - 1.0);

                        pixel = mezclar(
                                0x050F3C,
                                0xFFD200,
                                t
                        );
                        break;
                    }

                    // 3. Gradiente diagonal: violeta a turquesa
                    case 3: {
                        double tx = x / (ancho - 1.0);
                        double ty = y / (alto - 1.0);

                        double t = (tx + ty) / 2.0;

                        pixel = mezclar(
                                0x7D00D2,
                                0x00E6C8,
                                t
                        );
                        break;
                    }

                    // 4. Gradiente radial: amarillo a azul oscuro
                    case 4: {
                        double dx = x - centroX;
                        double dy = y - centroY;

                        double distancia = Math.hypot(dx, dy);
                        double t = distancia / distanciaMaxima;

                        // Suavizar la transición del gradiente
                        t = t * t * (3.0 - 2.0 * t);

                        pixel = mezclar(
                                0xFFE664,
                                0x0F0F5A,
                                t
                        );
                        break;
                    }

                    // 5. Gradiente radial multicolor
                    case 5: {
                        double dx = x - centroX;
                        double dy = y - centroY;

                        double distancia = Math.hypot(dx, dy);

                        double tono =
                                (Math.atan2(dy, dx) + Math.PI)
                                        / (2.0 * Math.PI);


                        if (tono >= 1.0) {
                            tono = 0.0;
                        }

                        double brillo =
                                1.0 - 0.30 * (
                                        distancia / distanciaMaxima
                                );

                        pixel = arcoiris(tono, brillo);
                        break;
                    }

                    default:
                        throw new IllegalArgumentException(
                                "Tipo de gradiente no válido"
                        );
                }


                imagen.setRGB(x, y, pixel);
            }
        }

        File destino = carpeta.resolve(nombre + ".jpg").toFile();

        if (!ImageIO.write(imagen, "png", destino)) {
            throw new IllegalStateException(
                    "No se pudo guardar " + nombre
            );
        }

        System.out.println("Imagen creada: " + destino.getPath());

}


    private static int mezclar(int color1, int color2, double t) {

        // Extraer rojo, verde y azul usando desplazamientos de bits
        int r1 = (color1 >> 16) & 0xFF;
        int g1 = (color1 >> 8) & 0xFF;
        int b1 = color1 & 0xFF;

        int r2 = (color2 >> 16) & 0xFF;
        int g2 = (color2 >> 8) & 0xFF;
        int b2 = color2 & 0xFF;


        int r = (int) Math.round(r1 + (r2 - r1) * t);
        int g = (int) Math.round(g1 + (g2 - g1) * t);
        int b = (int) Math.round(b1 + (b2 - b1) * t);


        return (r << 16) | (g << 8) | b;
    }


    private static int arcoiris(double tono, double brillo) {

        double h = tono * 6.0;
        int sector = (int) Math.floor(h);
        double f = h - sector;

        double saturacion = 0.90;

        double p = brillo * (1.0 - saturacion);
        double q = brillo * (1.0 - saturacion * f);
        double t = brillo * (1.0 - saturacion * (1.0 - f));

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

        // Empaquetar el RGB usando desplazamientos de bits
        return (r << 16) | (g << 8) | b;
    }
}
