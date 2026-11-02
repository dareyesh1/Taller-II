package com.taller.ii;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ClonarImagen {
    public static void main(String[] args) {
        System.out.println("Clonando imagen... ");

        File imagenOriginal = new File("src/main/resources/img/m4.jpg");
        File imagenClon = new File("src/main/resources/img/m4_clon.jpg");

        int mascara = 0xFF; //Para escribir en binario 0b111

        try{

            BufferedImage imagenEntrada = ImageIO.read(imagenOriginal); //Este es el Buffer de entrada

            int ancho = imagenEntrada.getWidth();
            int alto = imagenEntrada.getHeight();

            BufferedImage imagenSalida = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB); //Este es el buffer de salida

            for(int x = 0; x<ancho; x++){
                for(int y = 0; y < alto; y++){
                    int pixel = imagenEntrada.getRGB(x, y); //Saca los colores RGB

                    int r = ((pixel >> 16) & mascara); //Extrae y mueve el color rojo
                    int g = ((pixel >> 8) & mascara); //Extrae y vueve el color verde
                    int b = ((pixel >> 0) & mascara); //Extrae y mueve el color azul

                    //Se ejecutan operaciones
                    int pixel2 = (r<<16) | (g<<8) | (b<<8); // Cominar de vuelta los componentes de color

                    imagenSalida.setRGB(x,y, pixel2); // Establecer los nuevos pixeles en la imagen nueva



                }
            }

            //Se guarda la imagen en un archivo digital
            ImageIO.write(imagenSalida, "jpg", imagenClon);
            System.out.println(System.currentTimeMillis());
            System.out.println("Imagen clonada");

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
