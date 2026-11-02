package com.taller.ii;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Transferencia2 {

    static  void imagenNiveles(){

    }
    static void main(String args[]) {
        File original = new File("src/main/resources/img/m4.jpg");
        File destino = new File("src/main/resources/img/m4Bits.jpg");

        try {
            BufferedImage bi1 = ImageIO.read(original);

            int ancho = bi1.getWidth();
            int alto = bi1.getHeight();

            BufferedImage bi2 = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

            for(int x = 0; x< ancho; x++){
                for(int  y =0; y<alto; y++){
                    int pixel1 = bi1.getRGB(x,y);

                    Color color = new Color(pixel1);

                    int r = color.getRed();
                    int g = color.getGreen();
                    int b = color.getBlue();


                    //int r1 = (r>>4) & 0x15;
                    //int g1 = (g>>4) & 0x115;
                    //int b1 = (b>>4) & 15;


                    //Para negativo

                    int r1 = 255-r;
                    int g1 = 255 -g;
                    int b1 = 255 -b ;

                    //int r2 = r1*255/15;
                    //int g2 = g1*255/15;
                    //int b2 = b1*255/15;

                    //r=nuevaProfundidad(r, 2);
                    //g=nuevaProfundidad(g, 2);
                    //b=nuevaProfundidad(b, 2);



                    //Contar los bits perdidos
                    //int r3 = Math.abs(r-r2);
                    //int g3 = Math.abs(g-g2);
                    //int b3 = Math.abs(b-b2);

                    //Garantizar solo dos colores
                    //r2 = b2= g2;

                    //int lumi = (int) ((r3*0.299) + (g3+0.587) + (b3*0.114));
                    int pixel2 = (r1<< 16) | (g1<<8) | b1;

                    bi2.setRGB(x,y,pixel2);
                }
            }

            ImageIO.write(bi2, "jpg", destino);
            System.out.println("ok");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //Si es menor que la mitad (0), si es mayor (255)
    private static int nuevaProfundidad(int original, int niveles){
        int valor = 0;
//        int valor2 =0;
//
//        if(niveles ==2){
//            valor = (original < 128 ? 0 :255);
//            valor2 = valor;
//        }
//
//        if(niveles == 4){
//            if(original <64){
//                valor = 0;
//            }
//
//            if(original >= 64 & original <128){
//                valor = 64;
//            }
//
//            if(original >= 128 & original <192){
//                valor = 128;
//            }
//            if(original >= 192 & original <255){
//                valor = 192;
//            }
//
//            valor2=(valor*255)/192;
//        }

        int n = 256/niveles;
        int s =  255 / (niveles -1);

        valor = (original/n)*s;
        return valor;

        //Generar un algoritmo diferente a este, para generar una imagen en blanco y negro
    }
}
