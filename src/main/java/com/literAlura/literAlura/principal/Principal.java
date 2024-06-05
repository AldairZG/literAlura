package com.literAlura.literAlura.principal;

import com.literAlura.literAlura.model.DatosLibro;
import com.literAlura.literAlura.service.ConsumoAPI;
import com.literAlura.literAlura.service.ConvierteDatos;
import com.literAlura.literAlura.service.Datos;

import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/?search=";
    private ConvierteDatos conversor = new ConvierteDatos();
    private String tituloLibro;

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    ------------------------------------------------
                    1 - Buscar libro por título
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en determinado año
                    5 - Listar libros por idioma
                                 
                    0 - Salir
                    ------------------------------------------------
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibrosTitulo();
                    break;
//                case 2:
//                    listarLibrosRegistrados();
//                    break;
//                case 3:
//                    listarAutoresRegistrados();
//                    break;
//                case 4:
//                    listarAutoresPorAño();
//                    break;
//                case 5:
//                    listarLibrosIdioma();
//                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }

    private Datos obtenerDatosLibro() {
        System.out.println("Escribe el título del libro que deseas buscar");
        tituloLibro = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + tituloLibro.replace(" ","+"));
        var datosBusqueda = conversor.obtenerDatos(json, Datos.class);
        return datosBusqueda;
    }


    private void buscarLibrosTitulo() {
        Datos datosBusqueda = obtenerDatosLibro();
        Optional<DatosLibro> libroBuscado = datosBusqueda.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
                .findFirst();
        if (libroBuscado.isPresent()){
            System.out.println("Titulo: " + libroBuscado.get().titulo()  + "\n" +
                               "Autor: " + libroBuscado.get().autor().get(0).nombre()   + "\n" +
                               "Idioma: " + libroBuscado.get().idiomas().get(0) + "\n" +
                               "Número de descargas: " + libroBuscado.get().descargas());
        }else {
            System.out.println("Libro no encontrado");
        }
    }
}
