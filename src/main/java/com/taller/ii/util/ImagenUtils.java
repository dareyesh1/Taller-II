package com.taller.ii.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public final class ImagenUtils {

    private ImagenUtils() {

    }


    public static BufferedImage cargarImagen(String ruta)
            throws IOException {

        File archivo = new File(ruta);

        if (!archivo.exists()) {
            throw new IOException(
                    "No existe el archivo: " + archivo.getAbsolutePath()
            );
        }

        BufferedImage imagen = ImageIO.read(archivo);

        if (imagen == null) {
            throw new IOException(
                    "El archivo no contiene una imagen válida: " + ruta
            );
        }

        return imagen;
    }


    public static void crearDirectorio(String ruta)
            throws IOException {

        File directorio = new File(ruta);

        if (!directorio.exists() && !directorio.mkdirs()) {
            throw new IOException(
                    "No se pudo crear el directorio: "
                            + directorio.getAbsolutePath()
            );
        }
    }


    public static int limitar(int valor) {

        return Math.max(0, Math.min(255, valor));
    }


    public static int empaquetarRGB(int r, int g, int b) {

        r = limitar(r);
        g = limitar(g);
        b = limitar(b);

        return (r << 16) | (g << 8) | b;
    }


    public static int luminancia(int r, int g, int b) {

        return limitar(
                (int) Math.round(
                        0.299 * r
                                + 0.587 * g
                                + 0.114 * b
                )
        );
    }


    public static String obtenerFormato(String ruta) {

        String nombre = new File(ruta)
                .getName()
                .toLowerCase();

        if (nombre.endsWith(".png")) {
            return "png";
        }

        if (nombre.endsWith(".jpg")
                || nombre.endsWith(".jpeg")) {
            return "jpg";
        }

        throw new IllegalArgumentException(
                "Formato no compatible: " + ruta
        );
    }


    public static void guardarImagen(
            BufferedImage imagen,
            String ruta
    ) throws IOException {

        File archivo = new File(ruta);

        File directorio = archivo.getParentFile();

        if (directorio != null) {
            crearDirectorio(directorio.getPath());
        }

        String formato = obtenerFormato(ruta);

        boolean guardada = ImageIO.write(
                imagen,
                formato,
                archivo
        );

        if (!guardada) {
            throw new IOException(
                    "No existe un escritor compatible con "
                            + formato
            );
        }
    }
}
