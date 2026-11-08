package com.taller.ii.gui;

import com.taller.ii.clases.ClonarImagen;
import com.taller.ii.clases.GenerarImagenes;
import com.taller.ii.clases.Transferencia2;
import com.taller.ii.tareas.EscalaDeGrises;
import com.taller.ii.tareas.ImagenesColorTransparencia;
import com.taller.ii.tareas.ImagenGrisAColor;
import com.taller.ii.tareas.Tarea03Main;
import com.taller.ii.tareas.Tarea04ConvolucionesMain;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class TallerIIApp extends Application {

    // =====================================================
    // ESTADO DE LA APLICACIÓN
    // =====================================================

    private BufferedImage imagenOriginal;
    private BufferedImage imagenResultado;

    private File archivoOriginal;

    private ImageView vistaOriginal;
    private ImageView vistaResultado;

    private ComboBox<Tarea> selectorTarea;
    private ComboBox<String> selectorOpcion;

    private ColorPicker selectorColor;

    private Slider sliderIntensidad;
    private Slider sliderAlfa;

    private Label etiquetaEstado;
    private Label etiquetaInformacion;

    private Button botonAplicar;
    private Button botonGuardar;

    private ProgressIndicator progreso;

    // =====================================================
    // TAREAS DISPONIBLES
    // =====================================================

    private enum Tarea {

        CLONAR("Clonar imagen"),

        GRADIENTES("Generar gradientes"),

        NEGATIVO("Imagen negativa"),

        PROFUNDIDAD("Reducir profundidad de color"),

        GRISES("Escala de grises"),

        TINTE("Colores y transparencia"),

        GRIS_COLOR("Convertir imagen gris a color"),

        RECOLOR("Filtros Recolor de Word"),

        CONVOLUCIONES("Filtros de convolución"),

        LAPLACIANO("Laplaciano 3x3");

        private final String nombre;

        Tarea(String nombre) {
            this.nombre = nombre;
        }

        @Override
        public String toString() {
            return nombre;
        }
    }

    // =====================================================
    // INICIO DE JAVA FX
    // =====================================================

    @Override
    public void start(Stage stage) {

        stage.setTitle("TallerII - Procesamiento de imágenes");

        selectorTarea = new ComboBox<>();
        selectorTarea.getItems().addAll(Tarea.values());
        selectorTarea.setValue(Tarea.CLONAR);
        selectorTarea.setMaxWidth(Double.MAX_VALUE);

        selectorOpcion = new ComboBox<>();
        selectorOpcion.setMaxWidth(Double.MAX_VALUE);

        selectorColor = new ColorPicker(Color.ORANGE);
        selectorColor.setMaxWidth(Double.MAX_VALUE);

        sliderIntensidad = crearSlider(0, 255, 100);
        sliderAlfa = crearSlider(0, 255, 180);

        vistaOriginal = crearImageView();
        vistaResultado = crearImageView();

        etiquetaEstado = new Label("Carga una imagen para comenzar.");
        etiquetaInformacion = new Label("Sin imagen cargada.");

        botonAplicar = new Button("Aplicar filtro");
        botonGuardar = new Button("Guardar resultado");

        botonAplicar.setMaxWidth(Double.MAX_VALUE);
        botonGuardar.setMaxWidth(Double.MAX_VALUE);

        botonGuardar.setDisable(true);

        progreso = new ProgressIndicator();
        progreso.setVisible(false);
        progreso.setMaxSize(45, 45);

        selectorTarea.setOnAction(e -> actualizarOpciones());

        botonAplicar.setOnAction(e -> aplicarFiltro());

        botonGuardar.setOnAction(e -> guardarResultado());

        actualizarOpciones();

        BorderPane raiz = new BorderPane();

        raiz.setTop(crearBarraSuperior(stage));
        raiz.setLeft(crearPanelControles(stage));
        raiz.setCenter(crearPanelImagenes());
        raiz.setBottom(crearBarraEstado());

        Scene escena = new Scene(raiz, 1250, 780);

        escena.getStylesheets().add(
                getClass().getResource("estilos.css") == null
                        ? ""
                        : getClass().getResource("estilos.css").toExternalForm()
        );

        stage.setMinWidth(950);
        stage.setMinHeight(650);
        stage.setScene(escena);
        stage.show();
    }

    // =====================================================
    // BARRA SUPERIOR
    // =====================================================

    private HBox crearBarraSuperior(Stage stage) {

        Label titulo = new Label("TallerII");
        titulo.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;"
        );

        Label subtitulo = new Label("Laboratorio de procesamiento digital de imágenes");
        subtitulo.setStyle("-fx-text-fill: #dbeafe;");

        VBox textos = new VBox(4, titulo, subtitulo);

        Button cargar = new Button("Cargar imagen");
        cargar.setOnAction(e -> cargarImagen(stage));

        Button restablecer = new Button("Restablecer");
        restablecer.setOnAction(e -> restablecerImagen());

        Region espacio = new Region();
        HBox.setHgrow(espacio, Priority.ALWAYS);

        HBox barra = new HBox(
                18,
                textos,
                espacio,
                cargar,
                restablecer
        );

        barra.setAlignment(Pos.CENTER_LEFT);
        barra.setPadding(new Insets(15, 20, 15, 20));

        barra.setStyle(
                "-fx-background-color: #172554;"
        );

        return barra;
    }

    // =====================================================
    // PANEL DE CONTROLES
    // =====================================================

    private VBox crearPanelControles(Stage stage) {

        Label titulo = new Label("Configuración");
        titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label tareaLabel = new Label("Ejercicio:");
        Label opcionLabel = new Label("Filtro / Parámetro:");

        VBox panel = new VBox(
                12,
                titulo,
                tareaLabel,
                selectorTarea,
                opcionLabel,
                selectorOpcion,
                new Separator(),
                new Label("Color del tinte:"),
                selectorColor,
                new Label("Intensidad del tinte:"),
                sliderIntensidad,
                new Label("Transparencia (alfa):"),
                sliderAlfa,
                new Separator(),
                botonAplicar,
                botonGuardar,
                progreso
        );

        panel.setPadding(new Insets(20));
        panel.setPrefWidth(280);
        panel.setMinWidth(250);

        panel.setStyle(
                "-fx-background-color: #f1f5f9;"
        );

        VBox.setVgrow(progreso, Priority.NEVER);

        return panel;
    }

    // =====================================================
    // PANEL DE IMÁGENES
    // =====================================================

    private VBox crearPanelImagenes() {

        Label tituloOriginal = new Label("Imagen original");
        Label tituloResultado = new Label("Resultado");

        tituloOriginal.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        tituloResultado.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        VBox panelOriginal = new VBox(
                10,
                tituloOriginal,
                crearMarcoImagen(vistaOriginal)
        );

        VBox panelResultado = new VBox(
                10,
                tituloResultado,
                crearMarcoImagen(vistaResultado)
        );

        HBox paneles = new HBox(
                15,
                panelOriginal,
                panelResultado
        );

        paneles.setPadding(new Insets(20));
        paneles.setAlignment(Pos.CENTER);

        HBox.setHgrow(panelOriginal, Priority.ALWAYS);
        HBox.setHgrow(panelResultado, Priority.ALWAYS);

        VBox contenedor = new VBox(paneles);

        VBox.setVgrow(paneles, Priority.ALWAYS);

        return contenedor;
    }

    private StackPane crearMarcoImagen(ImageView imagen) {

        StackPane marco = new StackPane(imagen);

        marco.setMinSize(300, 300);
        marco.setPrefSize(500, 600);

        marco.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #cbd5e1;" +
                        "-fx-border-radius: 6;" +
                        "-fx-background-radius: 6;"
        );

        return marco;
    }

    private HBox crearBarraEstado() {

        HBox barra = new HBox(
                15,
                etiquetaEstado,
                new Separator(),
                etiquetaInformacion
        );

        barra.setAlignment(Pos.CENTER_LEFT);
        barra.setPadding(new Insets(10, 15, 10, 15));

        barra.setStyle(
                "-fx-background-color: #e2e8f0;"
        );

        return barra;
    }

    // =====================================================
    // COMPONENTES AUXILIARES
    // =====================================================

    private ImageView crearImageView() {

        ImageView imagen = new ImageView();

        imagen.setPreserveRatio(true);
        imagen.setSmooth(true);
        imagen.setFitWidth(500);
        imagen.setFitHeight(600);

        return imagen;
    }

    private Slider crearSlider(double min, double max, double valor) {

        Slider slider = new Slider(min, max, valor);

        slider.setShowTickLabels(true);
        slider.setShowTickMarks(false);
        slider.setMajorTickUnit(50);
        slider.setBlockIncrement(10);

        return slider;
    }

    // =====================================================
    // CARGAR IMAGEN
    // =====================================================

    private void cargarImagen(Stage stage) {

        FileChooser selectorArchivo = new FileChooser();

        selectorArchivo.setTitle("Seleccionar imagen");

        selectorArchivo.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(
                        "Imágenes",
                        "*.png",
                        "*.jpg",
                        "*.jpeg",
                        "*.bmp"
                ),
                new FileChooser.ExtensionFilter(
                        "Todos los archivos",
                        "*.*"
                )
        );

        File archivo = selectorArchivo.showOpenDialog(stage);

        if (archivo == null) {
            return;
        }

        try {

            BufferedImage imagen = ImageIO.read(archivo);

            if (imagen == null) {
                mostrarError("No se pudo leer la imagen seleccionada.");
                return;
            }

            imagenOriginal = imagen;
            imagenResultado = null;
            archivoOriginal = archivo;

            mostrarImagenOriginal();

            vistaResultado.setImage(null);

            botonGuardar.setDisable(true);

            etiquetaEstado.setText("Imagen cargada correctamente.");

            etiquetaInformacion.setText(
                    archivo.getName()
                            + " | "
                            + imagen.getWidth()
                            + " × "
                            + imagen.getHeight()
            );

        } catch (IOException ex) {

            mostrarError("Error al cargar imagen: " + ex.getMessage());
        }
    }

    private void mostrarImagenOriginal() {

        if (imagenOriginal == null) {
            return;
        }

        Image imagenFX = SwingFXUtils.toFXImage(imagenOriginal, null);

        vistaOriginal.setImage(imagenFX);
    }

    // =====================================================
    // ACTUALIZAR OPCIONES SEGÚN TAREA
    // =====================================================

    private void actualizarOpciones() {

        selectorOpcion.getItems().clear();

        Tarea tarea = selectorTarea.getValue();

        if (tarea == null) {
            return;
        }

        switch (tarea) {

            case GRADIENTES:

                selectorOpcion.getItems().addAll(
                        "1 - Gradiente horizontal",
                        "2 - Gradiente vertical",
                        "3 - Gradiente diagonal",
                        "4 - Gradiente radial",
                        "5 - Gradiente arcoíris"
                );

                break;

            case PROFUNDIDAD:
            case GRISES:

                selectorOpcion.getItems().addAll(
                        "256 niveles",
                        "128 niveles",
                        "64 niveles",
                        "32 niveles",
                        "16 niveles",
                        "2 niveles"
                );

                break;

            case RECOLOR:

                for (Tarea03Main.FiltroRecolor filtro :
                        Tarea03Main.FiltroRecolor.values()) {

                    selectorOpcion.getItems().add(
                            filtro.getCodigo()
                                    + " - "
                                    + filtro.getNombreVisible()
                    );
                }

                break;

            case CONVOLUCIONES:

                List<Tarea04ConvolucionesMain.Filtro> filtros =
                        Tarea04ConvolucionesMain.obtenerFiltros();

                for (Tarea04ConvolucionesMain.Filtro filtro : filtros) {
                    selectorOpcion.getItems().add(filtro.getNombre());
                }

                break;

            default:

                selectorOpcion.getItems().add("Predeterminado");

                break;
        }

        selectorOpcion.getSelectionModel().selectFirst();
    }

    // =====================================================
    // APLICAR FILTRO
    // =====================================================

    private void aplicarFiltro() {

        Tarea tarea = selectorTarea.getValue();

        if (tarea == null) {
            mostrarError("Selecciona un ejercicio.");
            return;
        }

        if (tarea != Tarea.GRADIENTES && imagenOriginal == null) {
            mostrarError("Primero debes cargar una imagen.");
            return;
        }

        if (selectorOpcion.getValue() == null) {
            mostrarError("Selecciona una opción.");
            return;
        }

        // Copiamos las referencias para el proceso en segundo plano.
        BufferedImage entrada = imagenOriginal;
        String opcion = selectorOpcion.getValue();

        Tarea tareaSeleccionada = tarea;

        int intensidad = (int) sliderIntensidad.getValue();
        int alfa = (int) sliderAlfa.getValue();

        java.awt.Color color = new java.awt.Color(
                (float) selectorColor.getValue().getRed(),
                (float) selectorColor.getValue().getGreen(),
                (float) selectorColor.getValue().getBlue(),
                (float) selectorColor.getValue().getOpacity()
        );

        botonAplicar.setDisable(true);
        botonGuardar.setDisable(true);

        progreso.setVisible(true);

        etiquetaEstado.setText("Procesando imagen...");

        Task<BufferedImage> tareaJavaFX = new Task<>() {

            @Override
            protected BufferedImage call() throws Exception {

                return procesarImagen(
                        tareaSeleccionada,
                        entrada,
                        opcion,
                        color,
                        intensidad,
                        alfa
                );
            }
        };

        tareaJavaFX.setOnSucceeded(e -> {

            imagenResultado = tareaJavaFX.getValue();

            vistaResultado.setImage(
                    SwingFXUtils.toFXImage(imagenResultado, null)
            );

            botonAplicar.setDisable(false);
            botonGuardar.setDisable(false);

            progreso.setVisible(false);

            etiquetaEstado.setText("Proceso completado correctamente.");

        });

        tareaJavaFX.setOnFailed(e -> {

            botonAplicar.setDisable(false);
            botonGuardar.setDisable(true);

            progreso.setVisible(false);

            Throwable error = tareaJavaFX.getException();

            mostrarError(
                    error == null
                            ? "Error desconocido."
                            : error.getMessage()
            );
        });

        Thread hilo = new Thread(tareaJavaFX);

        hilo.setDaemon(true);
        hilo.start();
    }

    // =====================================================
    // PROCESAMIENTO DE IMÁGENES
    // =====================================================

    private BufferedImage procesarImagen(
            Tarea tarea,
            BufferedImage original,
            String opcion,
            java.awt.Color color,
            int intensidad,
            int alfa
    ) throws Exception {

        switch (tarea) {

            case CLONAR:

                return ClonarImagen.clonar(original);

            case GRADIENTES:

                int tipoGradiente = Integer.parseInt(
                        opcion.substring(0, 1)
                );

                return GenerarImagenes.generarGradiente(tipoGradiente);

            case NEGATIVO:

                return Transferencia2.crearNegativo(original);

            case PROFUNDIDAD:

                int niveles = obtenerNiveles(opcion);

                return Transferencia2.reducirProfundidad(
                        original,
                        niveles
                );

            case GRISES:

                niveles = obtenerNiveles(opcion);

                return EscalaDeGrises.convertir(
                        original,
                        niveles
                );

            case TINTE:

                return ImagenesColorTransparencia.aplicarTinte(
                        original,
                        color,
                        intensidad,
                        alfa
                );

            case GRIS_COLOR:

                return ImagenGrisAColor.colorear(original);

            case RECOLOR:

                int codigoFiltro = Integer.parseInt(
                        opcion.split(" - ")[0]
                );

                return Tarea03Main.aplicarFiltro(
                        original,
                        codigoFiltro
                );

            case CONVOLUCIONES:

                for (Tarea04ConvolucionesMain.Filtro filtro :
                        Tarea04ConvolucionesMain.obtenerFiltros()) {

                    if (filtro.getNombre().equals(opcion)) {

                        return Tarea04ConvolucionesMain.aplicarFiltro(
                                original,
                                filtro
                        );
                    }
                }

                throw new IllegalArgumentException(
                        "No se encontró el filtro de convolución."
                );

            case LAPLACIANO:

                int[][] kernel = {
                        { 0, -1,  0 },
                        {-1,  4, -1 },
                        { 0, -1,  0 }
                };

                return com.taller.ii.clases.Convoluciones.aplicarKernel(
                        original,
                        kernel,
                        1,
                        128
                );

            default:

                throw new IllegalArgumentException(
                        "La tarea seleccionada no está implementada."
                );
        }
    }

    private int obtenerNiveles(String opcion) {

        return Integer.parseInt(
                opcion.split(" ")[0]
        );
    }

    // =====================================================
    // GUARDAR RESULTADO
    // =====================================================

    private void guardarResultado() {

        if (imagenResultado == null) {
            mostrarError("No hay ningún resultado para guardar.");
            return;
        }

        FileChooser selectorArchivo = new FileChooser();

        selectorArchivo.setTitle("Guardar imagen procesada");

        selectorArchivo.setInitialFileName("resultado.png");

        selectorArchivo.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(
                        "Imagen PNG",
                        "*.png"
                ),
                new FileChooser.ExtensionFilter(
                        "Imagen JPEG",
                        "*.jpg",
                        "*.jpeg"
                )
        );

        File archivo = selectorArchivo.showSaveDialog(
                vistaResultado.getScene().getWindow()
        );

        if (archivo == null) {
            return;
        }

        try {

            String nombre = archivo.getName().toLowerCase();

            String formato;

            if (nombre.endsWith(".jpg") || nombre.endsWith(".jpeg")) {
                formato = "jpg";
            } else {
                formato = "png";

                if (!nombre.endsWith(".png")) {
                    archivo = new File(archivo.getAbsolutePath() + ".png");
                }
            }

            BufferedImage imagenGuardar = imagenResultado;

            // JPEG no admite transparencia.
            if (formato.equals("jpg")
                    && imagenGuardar.getType() == BufferedImage.TYPE_INT_ARGB) {

                BufferedImage rgb = new BufferedImage(
                        imagenGuardar.getWidth(),
                        imagenGuardar.getHeight(),
                        BufferedImage.TYPE_INT_RGB
                );

                java.awt.Graphics2D g = rgb.createGraphics();

                g.setColor(java.awt.Color.WHITE);

                g.fillRect(
                        0,
                        0,
                        rgb.getWidth(),
                        rgb.getHeight()
                );

                g.drawImage(imagenGuardar, 0, 0, null);

                g.dispose();

                imagenGuardar = rgb;
            }

            ImageIO.write(imagenGuardar, formato, archivo);

            etiquetaEstado.setText(
                    "Imagen guardada: " + archivo.getName()
            );

        } catch (IOException ex) {

            mostrarError(
                    "No se pudo guardar la imagen: "
                            + ex.getMessage()
            );
        }
    }

    // =====================================================
    // RESTABLECER
    // =====================================================

    private void restablecerImagen() {

        if (imagenOriginal == null) {
            mostrarError("No hay una imagen cargada.");
            return;
        }

        imagenResultado = null;

        vistaResultado.setImage(null);

        botonGuardar.setDisable(true);

        mostrarImagenOriginal();

        etiquetaEstado.setText(
                "Imagen original restaurada."
        );
    }

    // =====================================================
    // MENSAJES
    // =====================================================

    private void mostrarError(String mensaje) {

        Alert alerta = new Alert(Alert.AlertType.ERROR);

        alerta.setTitle("TallerII");
        alerta.setHeaderText("Se produjo un problema");
        alerta.setContentText(mensaje);

        alerta.showAndWait();

        etiquetaEstado.setText("Error: " + mensaje);
    }

    // =====================================================
    // MAIN
    // =====================================================

    public static void main(String[] args) {

        launch(args);
    }
}