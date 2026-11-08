package com.taller.ii.clases;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
public class Convoluciones {
    static File original = new File("src/main/resources/img/m4.jpg");
    static File destino = new File("src/main/resources/img/m4Convolucion.jpg");
    static int[] matriz;
    static void main() {

        try {
            matriz = new int[]{
                    0, 1, 0,
                    1, -4, 1,
                    0, 1, 0
            };
            BufferedImage bi1 = ImageIO.read(original);
            int ancho = bi1.getWidth();
            int alto = bi1.getHeight();
            BufferedImage bi2 = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
            for(int x = 1; x< ancho-1; x++){
                for(int  y =1; y<alto-1; y++){
                    int r1 =0;
                    int g1 =0;
                    int b1 =0;
                    int indice =0;
                    for(int i = -1; i<=1; i++){
                        for(int j = -1; j<=1; j++){
                            int pixel1 = bi1.getRGB(x+i,y+j);
                            Color color = new Color(pixel1);
                            int r = color.getRed();
                            int g = color.getGreen();
                            int b = color.getBlue();

                            r1 += r*matriz[indice];
                            g1 += g*matriz[indice];
                            b1 += b*matriz[indice];
                            indice++;
                        }
                    }
                    int rfinal = (int)Math.min(255, Math.max(0, r1));
                    int gfinal = (int)Math.min(255, Math.max(0, g1));
                    int bfinal = (int)Math.min(255, Math.max(0, b1));
                    int pixel2 = (rfinal<< 16) | (gfinal<<8) | bfinal;
                    bi2.setRGB(x,y,pixel2);
                }
            }
            ImageIO.write(bi2, "jpg", destino);
            System.out.println("ok");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}