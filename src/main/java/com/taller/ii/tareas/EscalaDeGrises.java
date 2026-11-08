package com.taller.ii.tareas;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class EscalaDeGrises {
    public static void main(String[] args)  {

        File imagenOriginal= new File("src/main/resources/img/m4.jpg"); //Entrada
        int[] niveles = {256, 128, 64, 32, 16, 2};
        //File imageClon= new File("img/ClonLinuxGris.jpg");//Resultado
        int mascara=0xFF; //0x(Hexadecimal) y el valor de la mascara
        try {
            BufferedImage imagen= ImageIO.read(imagenOriginal);
            int ancho= imagen.getWidth();
            int alto= imagen.getHeight();

            //BufferedImage imagen2 =new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

            BufferedImage[] imagenes = new BufferedImage[niveles.length];
            for(int i =0; i< niveles.length; i++){
                imagenes[i] = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
            }
            for(int x=0; x< ancho; x++){
                for (int y =0; y<alto; y++){

                    int pixel = imagen.getRGB(x,y);

                    int r = (pixel >> 16)&mascara;
                    int g = (pixel >> 8) & mascara;
                    int b = pixel & mascara;

                    //Aqui se calcula el gris original
                    int gris = (r+g+b)/3;

                    for(int i=0; i<niveles.length; i++){

                        int n = niveles[i];

                        int nivel = (gris* (n-1) + 127)/255;

                        int grisActual= (nivel * 255 + (n-1)/2) / (n-1);

                        int pixel2 = (grisActual << 16) | (grisActual << 8) | grisActual;

                        imagenes[i].setRGB(x,y, pixel2);
                    }
                }
            }

            for(int i = 0; i< niveles.length; i++){
                String nombre = "src/main/resources/img/gris_" + niveles[i] + ".png";

                File imagenSalida = new File(nombre);

                boolean guardada = ImageIO.write(
                        imagenes[i],
                        "png",
                        imagenSalida
                );
            }
            System.out.println("Imagenes creadas con exito");

        }catch(Exception e) {
            System.out.println("Error al clonar la imagen "+ e.getMessage());
        }
    }
}
