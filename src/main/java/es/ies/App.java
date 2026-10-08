package es.ies;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// Mi clase Application de la que debe heredar cualquier APP con main en javafx
//es la clase que permite ejecutar una aplicacion JavaFX
public class App extends Application
{
    //Atributos privados de la clase  que vamos a usar dentro del "void" respectivo
    private ComboBox<String> comboPrioridad ; 
    private ListView<String> listaTareas ; 
    private Button botonCompletar ; 
    private Button botonEliminar ; 
    private Label mensaje,estado ; 
    private TextField campoTarea;
    /**
     * En el flujo de trabajo  de JavaFX, se llama automaticamente a este metodo start
     * En este metodo contruimos toda la interfaz
     * @see javafx.application.Application#start(javafx.stage.Stage);
     */

    public void start(Stage stage){

        //Creamos  una etiqueta y se modifica su estilo de via CSS
        Label titulo = new Label("Gestor de Tareas");//Label lo que hace es crear un texto o tambien puede contener un icono y tras eso modificarlo
        titulo.setStyle("-fx-font-size: 22px ; -fx-font-weight: bold");

        //Creamos un TextField para introducir el texto de la tarea
         campoTarea =  new TextField();// Es un campo de texto editable 

        //El metodo  setPromptText es un metodo que muestra el texto cuando el
        // TextField esta vacio
        campoTarea.setPromptText("Escribe una tarea");

        //Permitir que este TextField  pueda crecer horizontalmente cuando se redimensiona la ventana .
        // Se aplica a campoTarea y se hace siempre 
        HBox.setHgrow(campoTarea, Priority.ALWAYS);

        //Creamos un desplegable con  tres prioridades : baja , media y alta 
        //Queremos  que se quede  seleccionada la prioridad media 
        comboPrioridad =  new ComboBox<>();
        comboPrioridad.getItems().addAll("Baja","Media","Alta");
        comboPrioridad.setValue("Media");

        //Añadimos un boton  para añadir una tarea. Es el boton predeterminado de la  ventana
        Button botonAnadir =  new Button("Añadir tarea");
        botonAnadir.setDefaultButton(true);// Si pulsamos en el teclado enter tambien funcionaria al igual que un click

        //Creamos un contenedor horizontal para colocar los elementos unos tras otros cumpliendo con las restricciones anteriores
        //Representa el espaciado de elementos 
        HBox formulario = new HBox(10,
            new Label("Nueva tarea :"),
            campoTarea,
            new Label ("Prioridad :"),
            comboPrioridad,
            botonAnadir
        );
        
        //Alineao los elementos hacia  la izquierda  y centrado verticalmente 
        formulario.setAlignment(Pos.CENTER_LEFT);


        //Creamos una lista donde se van a mostrar las tareas creadas 
         listaTareas = new ListView<>();

         //Ancho que va a tener el componente de la lista de tareas 
         listaTareas.setPrefHeight(280);

         //Añadimos  una lista de botones 
         botonCompletar = new Button("Marcar como completada");
         botonEliminar = new Button ("Eliminar");
         Button botonLimpiar = new Button("Limpiar Todas");
         
         //Define un contenedor horizontal para poner todos los botones 
         HBox acciones =  new HBox(10,
            botonCompletar,
            botonLimpiar,
            botonEliminar
         );

         //Lo alineo al centro y a la izquierda
         acciones.setAlignment(Pos.CENTER_LEFT);
         
         //Defino labels para mostrar  informacion
         mensaje = new Label();
         mensaje.setWrapText(true);

         estado = new Label();

         //Defino mi elemento raiz de la ventana donde voy a ir apilando (verticalmente )
         //cada uno de los graficos de la ventana 
         VBox raiz = new VBox(14,
            titulo,
            formulario,
            new Label("Lista de Tareas"),
            listaTareas,
            acciones ,
            mensaje ,
            estado
         );

         //Añadimos 20 pixeles del margen inferior 
         raiz.setPadding(new Insets(20));

         //Partimos de la lista crezca  verticamente si crece la ventana 
         VBox.setVgrow(listaTareas, Priority.ALWAYS);


        //Añadimos eventos a los botones 
        botonAnadir.setOnAction(e -> anadirTarea());//Cuando pulsamos el boton se ejecuta el  codigo  del metodo anadirTarea
        botonEliminar.setOnAction(e -> eliminarContenido());//Cuando pulsamos el boton se elimina la tarea seleccionada
        botonCompletar.setOnAction(e -> tareaCompletada());
        botonLimpiar.setOnAction(e -> LimpiarCompletadas());

        //Necesito una escena para que se muestre y llamar al metodo stage.show()
        Scene scene =  new Scene(raiz, 720, 500);
        stage.setTitle("Gestor de Tareas JavaFX");//Le pongo el titulo a la ventana  
        stage.setScene(scene);//Asigno el contenido a la ventana 
        stage.show();//Muestro la ventana con un show 

        };

        //Metodo que se ejecuta al pulsar un boton botonAnadir
        //Realiza una validacion sobre el campo de datos de entrada  de la tarea 
        //Se añade al ListView(listaTareas)
        //Se muestra un mensaje y se actualiza el estado de la aplicacion
        private void anadirTarea(){
            String texto =  campoTarea.getText().trim();

            if(texto.isEmpty()){
                mostrarMensaje("Introduce una tarea . No se permite tareas vacias ");
                return ;
            }
            String prioridad = comboPrioridad.getValue();
            String tarea =  "["+ prioridad+"]"+texto ;
            listaTareas.getItems().add(tarea);//Añadimos la tarea al ListView 
            
            listaTareas.getSelectionModel().selectLast();
            listaTareas.scrollTo(listaTareas.getItems().size()-1);

            campoTarea.clear();//Limpiamos el TextField de  la tarea
            comboPrioridad.setValue("Media");//Volvemos a  dejar la prioridad en Media en la lista desplegable
            mostrarMensaje("Tarea añadida");//Mostramos un mensaje de tarea añadida correctamente 
            campoTarea.requestFocus();//Devolvemos el foco al textField

        }
        public void eliminarContenido(){

            //Averiguamos que numero de fila esta seleccionada por el usuario
            int seleccionada = listaTareas.getSelectionModel().getSelectedIndex();

            //Si el usuario no ha seleccionado ninguna tarea ,le decimos que selccione alguna

            if(seleccionada == -1){
                mostrarMensaje("Selecciona una de la lista para poder eliminarla");
                return ;
            }

            listaTareas.getItems().remove(seleccionada);
            mostrarMensaje("Tarea eliminada correctamente");
        }

        public void tareaCompletada(){
            int seleccionada = listaTareas.getSelectionModel().getSelectedIndex();

            if(seleccionada == -1){
                mostrarMensaje("Selecciona una de la lista para poder completarla");
                return ;
            }
            String tareaActual =  listaTareas.getItems().get(seleccionada);

            if(tareaActual.startsWith("[COMPLETADA]")){
                mostrarMensaje("Esta tarea ya esta completada , selecciona otra");
                return ;
            }
            String tareaCompletada = "[COMPLETADA]"+tareaActual;

            listaTareas.getItems().set(seleccionada, tareaCompletada);
            listaTareas.getSelectionModel().select(seleccionada);

            mostrarMensaje("Tarea marcada como completada");

        }

        public void LimpiarCompletadas(){
            int seleccionada = listaTareas.getSelectionModel().getSelectedIndex();
            String tareaActual =  listaTareas.getItems().get(seleccionada);
            String tareaCompletada = "[COMPLETADA]"+tareaActual;

            if(tareaCompletada.startsWith(tareaCompletada)){
              listaTareas.getItems().remove(tareaCompletada);
            }
        }

     /**
     * Hacer el boton para marcar tareas como completadas
     * Hacer el boton para eliminar tareas
     * Hacer el boton para limpiar la lista de tareas COMPLETADAS
     * 
     * Tambien podemos poner que las tareas sin completar se pongan en rojo y las completadas en verde
     * el boton de limpiar la lista podria necesitar confirmacion de que se quiere eliminar todas las tareas completadas
     */

        
        /**
         * Metodo que muestra en el interfaz el texto pasado como parametro
         * @param text
        */
        private void mostrarMensaje(String text){
            this.mensaje.setText(text);

        }

    public static void main( String[] args )
    {
        launch();
        }
}
