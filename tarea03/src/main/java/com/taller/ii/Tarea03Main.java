package com.taller.ii;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Tarea03Main {

    private static final String imagenOriginal = "src/main/resources/img/m4.jpg";

    private static final String destino = "src/main/resources/img/Recolor";

    private static final int ORIGINAL = 0;
    private static final int GRIS = 1;
    private static final int SEPIA = 2;
    private static final int ACLARADO = 3;
    private static final int BLANCO_NEGRO_1 = 4;
    private static final int BLANCO_NEGRO_2 = 5;
    private static final int BLANCO_NEGRO_3 = 6;
    private static final int GRIS_OSCURO = 7;
    private static final int AZUL_OSCURO = 8;
    private static final int NARANJA_OSCURO = 9;
    private static final int GRIS_MEDIO = 10;
    private static final int AMARILLO_OSCURO = 11;
    private static final int AZUL = 12;
    private static final int VERDE = 13;
    private static final int GRIS_CLARO_1 = 14;
    private static final int AZUL_CLARO_1 = 15;
    private static final int NARANJA_CLARO = 16;
    private static final int GRIS_CLARO_2 = 17;
    private static final int AMARILLO_CLARO = 18;
    private static final int AZUL_CLARO_2 = 19;
    private static final int VERDE_CLARO = 20;

    private static final String[] NOMBRES = {

            "01_Original",
            "02_EscalaGrises",
            "03_Sepia",
            "04_Aclarado",
            "05_BlancoNegro1",
            "06_BlancoNegro2",
            "07_BlancoNegro3",

            "08_GrisOscuro",
            "09_AzulOscuro",
            "10_NaranjaOscuro",
            "11_GrisMedio",
            "12_AmarilloOscuro",
            "13_Azul",
            "14_Verde",

            "15_GrisClaro1",
            "16_AzulClaro1",
            "17_NaranjaClaro",
            "18_GrisClaro2",
            "19_AmarilloClaro",
            "20_AzulClaro2",
            "21_VerdeClaro"
    };

    public static void main(String[] args) {
        File original = new File(imagenOriginal);

        File carpetaSalida = new File(destino);

        try{

            BufferedImage imagen = ImageIO.read(original);

            int ancho = imagen.getWidth();
            int alto = imagen.getHeight();

            BufferedImage imagen2 = new BufferedImage(
                    ancho, alto, BufferedImage.TYPE_INT_RGB
            );

            for(int filtro =0; filtro<NOMBRES.length; filtro++){

                aplicarFiltro(imagen, imagen2, filtro);

                String nombreArchivo = NOMBRES[filtro] + ".jpg";

                File destino = new File(carpetaSalida, nombreArchivo);

                ImageIO.write(imagen2, "jpg", destino);
            }


        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private static void aplicarFiltro(
            BufferedImage original, BufferedImage destino, int tipoFiltro
    ){

        int ancho = original.getWidth();
        int alto = original.getHeight();

        for(int x =0; x<ancho; x++){
            for (int y=0; y<alto; y++){
                int pixel = original.getRGB(x,y);

                //Extraemos los componentes
                int r = (pixel >> 16) & 0xFF;
                int g = (pixel >> 8) & 0xFF;
                int b = (pixel) & 0xFF;

                int pixel2 = transformarPixel(
                        r,g,b, tipoFiltro
                );


                destino.setRGB(x,y, pixel2);
            }

        }

    }

    public static int transformarPixel(int r, int g, int b, int tipoFiltro){
        int gris = calcularGris(r, g, b);

        int r2 = r;
        int g2= g;
        int b2 = b;

        switch (tipoFiltro){
            case ORIGINAL:
                r2 = r;
                g2 = g;
                b2 = b;
                break;

            case GRIS:
                r2 = gris;
                g2 = gris;
                b2 = gris;
                break;

            case SEPIA:
                r2 = (int) (gris * 1.07);
                g2 = gris;
                b2 = (int) (gris * 0.80);

                break;

            case ACLARADO:
                r2 = aclarar(r);
                g2 = aclarar(g);
                b2 = aclarar(b);

                break;


            case BLANCO_NEGRO_1:

                r2 = g2 = b2 = umbral(gris, 128);

                break;



            case BLANCO_NEGRO_2:

                r2 = g2 = b2 = umbral(gris, 170);

                break;


            case BLANCO_NEGRO_3:

                r2 = g2 = b2 = umbral(gris, 90);

                break;


            case GRIS_OSCURO:

                gris = oscurecer(gris, 0.55);

                r2 = g2 = b2 = gris;

                break;



            case AZUL_OSCURO:

                r2 = (int) (gris * 0.30);
                g2 = (int) (gris * 0.55);
                b2 = (int) (gris * 0.90);

                break;


            case NARANJA_OSCURO:

                r2 = (int) (gris * 0.90);
                g2 = (int) (gris * 0.50);
                b2 = (int) (gris * 0.25);

                break;



            case GRIS_MEDIO:

                gris = oscurecer(gris, 0.75);

                r2 = g2 = b2 = gris;

                break;



            case AMARILLO_OSCURO:

                r2 = (int) (gris * 0.90);
                g2 = (int) (gris * 0.80);
                b2 = (int) (gris * 0.25);

                break;



            case AZUL:

                r2 = (int) (gris * 0.45);
                g2 = (int) (gris * 0.70);
                b2 = gris;

                break;



            case VERDE:

                r2 = (int) (gris * 0.40);
                g2 = gris;
                b2 = (int) (gris * 0.40);

                break;



            case GRIS_CLARO_1:

                gris = aclarar(gris);

                r2 = g2 = b2 = gris;

                break;



            case AZUL_CLARO_1:

                r2 = (int) (gris * 0.60);
                g2 = (int) (gris * 0.80);
                b2 = aclarar(gris);

                break;



            case NARANJA_CLARO:

                r2 = aclarar(gris);
                g2 = (int) (gris * 0.75);
                b2 = (int) (gris * 0.45);

                break;



            case GRIS_CLARO_2:

                gris = aclarar(gris);

                r2 = g2 = b2 = gris;

                break;



            case AMARILLO_CLARO:

                r2 = aclarar(gris);
                g2 = aclarar((int) (gris * 0.85));
                b2 = (int) (gris * 0.50);

                break;



            case AZUL_CLARO_2:

                r2 = (int) (gris * 0.70);
                g2 = aclarar(gris);
                b2 = aclarar(gris);

                break;


            // 21. Verde claro
            case VERDE_CLARO:

                r2 = (int) (gris * 0.65);
                g2 = aclarar(gris);
                b2 = (int) (gris * 0.60);

                break;
        }

        // Limitar componentes RGB
        r2 = limitar(r2);
        g2 = limitar(g2);
        b2 = limitar(b2);

        // Reconstruir píxel utilizando desplazamientos de bits
        return (r2 << 16) | (g2 << 8) | b2;
    }



    private static int calcularGris(
            int r,
            int g,
            int b
    ) {

        return (int) Math.round(
                r * 0.299
                        + g * 0.587
                        + b * 0.114
        );
    }



    private static int aclarar(int valor) {

        return (int) Math.round(
                valor + (255 - valor) * 0.65
        );
    }



    private static int oscurecer(
            int valor,
            double factor
    ) {

        return (int) Math.round(
                valor * factor
        );
    }


    /*
     * Genera blanco o negro según un umbral.
     */
    private static int umbral(
            int gris,
            int limite
    ) {

        return gris >= limite ? 255 : 0;
    }



    //Limita que no se salgan de 255
    private static int limitar(int valor) {

        return Math.max(
                0,
                Math.min(255, valor)
        );
    }

}