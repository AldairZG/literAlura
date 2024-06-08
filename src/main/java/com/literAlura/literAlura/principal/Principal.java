package com.literAlura.literAlura.principal;

import com.literAlura.literAlura.model.*;
import com.literAlura.literAlura.repository.LibroRepository;
import com.literAlura.literAlura.service.ConsumoAPI;
import com.literAlura.literAlura.service.ConvierteDatos;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/?search=";
    private ConvierteDatos conversor = new ConvierteDatos();
    private String tituloLibro;
    private LibroRepository repositorio;
    private List<Libro> libros;

    public Principal(LibroRepository repository) {
        this.repositorio = repository;
    }

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
                case 2:
                    listarLibrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresPorFecha();
                    break;
                case 5:
                    listarLibrosIdioma();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }

    private Datos obtenerDatos() {
        System.out.println("Escribe el título del libro que deseas buscar");
        tituloLibro = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + tituloLibro.replace(" ","+"));
        var datosBusqueda = conversor.obtenerDatos(json, Datos.class);
        return datosBusqueda;
    }

    private DatosLibro obtenerDatosLibro(){
        Datos datosBusqueda = obtenerDatos();
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
        return libroBuscado.get();
    }

    private void buscarLibrosTitulo() {
        DatosLibro datosLibro = obtenerDatosLibro();
        Libro libro = new Libro(datosLibro);
        Autor autor = new Autor(datosLibro.autor().get(0));
        libro.setAutor(autor);
        try{
            repositorio.save(libro);
        }catch (DataIntegrityViolationException e){
            System.out.println("Libro registrado anteriormente");
        }

    }

    private void listarLibrosRegistrados() {
        libros = repositorio.findAll();
        libros.stream()
                .forEach(System.out::println);
    }

    private void listarAutoresRegistrados(){
        List<Autor> autorBuscado = repositorio.librosPorAutores();
        autorBuscado.forEach(a ->
                System.out.println("\n***********AUTOR***********\n" +
                                   "Nombre: " + a.getNombre() + "\n" +
                                   "Fecha de nacimiento: " + a.getFechaNacimiento() + "\n" +
                                   "Fecha de fallecimiento: " + a.getFechaMuerte() + "\n" +
                                   "Libros: " + a.getLibro().getTitulo() + "\n" +
                                   "***************************\n"));
    }

    private void listarAutoresPorFecha(){
        System.out.println("Ingresa el año del autor vivo que desea buscar");
        var fecha = teclado.nextInt();
        List<Autor> autorBuscado = repositorio.autorPorFecha(fecha);
        autorBuscado.forEach(a ->
                System.out.println("\n***********AUTOR***********\n" +
                        "Nombre: " + a.getNombre() + "\n" +
                        "Fecha de nacimiento: " + a.getFechaNacimiento() + "\n" +
                        "Fecha de fallecimiento: " + a.getFechaMuerte() + "\n" +
                        "Libros: " + a.getLibro().getTitulo() + "\n" +
                        "***************************\n"));
    }

    private void listarLibrosIdioma(){
        System.out.println("Ingrese el idioma para buscar los libros:\n" +
                           "es - español\n" +
                           "en - ingles\n" +
                           "fr - frances\n" +
                           "pt - portugués ");
        String idioma = teclado.nextLine();
        if(Objects.equals(idioma, "es") || Objects.equals(idioma, "en") ||
                Objects.equals(idioma, "fr") || Objects.equals(idioma, "pt")){
            libros = repositorio.librosPorIdiomas("[" + idioma + "]");
            libros.stream()
                    .forEach(System.out::println);
        }else {

            System.out.println("Opción Invalida");
        }
    }
}
