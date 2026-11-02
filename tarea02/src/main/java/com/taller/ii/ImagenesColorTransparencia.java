package com.taller.ii;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImagenesColorTransparencia {
    public static void main(String[] args) {
        File imagenOriginal = new File("src/main/resources/img/m4.jpg");

        int[][] colores = {
            {128, 128, 128}, // Gris
            {40, 90, 220},   // Azul
            {220, 110, 40},  // Naranja
            {40, 190, 220},  // Cian
            {220, 210, 40},  // Amarillo
            {60, 180, 60} //Verde
        };

        int intensidadFiltro =100;


        int alfa = 180; //Este se usa para la transparencia

        int mascara = 0xFF;

        try{
            BufferedImage original = ImageIO.read(imagenOriginal);

            int ancho = original.getWidth();
            int alto = original.getHeight();

            BufferedImage[] imagenes = new BufferedImage[colores.length];

            for (int i = 0; i< colores.length; i++){
                imagenes[i] = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
            }

            for (int x =0; x< ancho; x++){
                for (int y = 0; y< alto; y++){
                    int pixel = original.getRGB(x, y);

                    int r = (pixel >> 16) & mascara;
                    int g = (pixel >> 8) & mascara;
                    int b = pixel & mascara;

                    //Aplica cada filtro de color
                    for(int i=0; i< colores.length; i++){
                        int fr = colores[i][0];
                        int fg = colores[i][0];
                        int fb = colores[i][0];

                        int nr = (r*(255- intensidadFiltro)
                                + fr*intensidadFiltro
                                )/ 255;
                        int ng = (
                                g * (255 - intensidadFiltro)
                                        + fg * intensidadFiltro
                        ) / 255;

                        int nb = (
                                b * (255 - intensidadFiltro)
                                        + fb * intensidadFiltro
                        ) / 255;

                        int pixel2 = (alfa <<24) | (nr<<16) | (ng<<8) | (nb);

                        imagenes[i].setRGB(x,y, pixel2);
                    }

                }
            }

            for (int i=0; i< imagenes.length; i++){
                String nombre = "src/main/resources/img/color_" + (i+1)+ ".png";
                File imagenSalida = new File(nombre);

                ImageIO.write(imagenes[i], "png", imagenSalida);
            }

            System.out.println("Imagenes generadas con exito");

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
