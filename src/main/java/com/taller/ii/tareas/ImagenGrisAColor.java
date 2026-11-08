package com.taller.ii.tareas;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImagenGrisAColor {
    public static void main(String[] args) {

        File imagenOriginal = new File("src/main/resources/img/gris_256.png");

        File imagenColor = new File("src/main/resources/img/imagen_coloreada.png");


        int mascara = 0xFF;

        try {

            BufferedImage imagen = ImageIO.read(imagenOriginal);

            int ancho = imagen.getWidth();
            int alto = imagen.getHeight();

            BufferedImage imagenSalida = new BufferedImage(
                    ancho, alto, BufferedImage.TYPE_INT_RGB
            );

            for(int x =0; x<ancho; x++){
                for (int y =0; y<alto; y++){
                    int pixel = imagen.getRGB(x,y);

                    int gris = pixel & mascara;

                    int r = gris/8;
                    int g = gris /4;
                    int b = gris;

                    int pixel2 = (r<<16) | (g<<8) | b;

                    imagenSalida.setRGB(x,y, pixel2);
                }
            }

            ImageIO.write(imagenSalida, "png", imagenColor);





        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
