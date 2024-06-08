package com.literAlura.literAlura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(unique = true)
    private String titulo;
    private String idiomas;
    private Integer descargas;
    @OneToOne(mappedBy = "libro", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Autor autor;

    public Libro(){}

    public Libro(DatosLibro datosLibro){
        this.titulo = datosLibro.titulo();
        this.idiomas = datosLibro.idiomas().toString();
        this.descargas = datosLibro.descargas();
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(String idiomas) {
        this.idiomas = idiomas;
    }

    public Integer getDescargas() {
        return descargas;
    }

    public void setDescargas(Integer descargas) {
        this.descargas = descargas;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        autor.setLibro(this);
        this.autor = autor;
    }

    @Override
    public String toString() {
        return "\n***********LIBRO***********\n" +
               "Titulo: " + titulo + "\n" +
               "Autor: " + autor.getNombre() + "\n" +
               "Idioma: " + idiomas + "\n" +
               "Descargas: " + descargas + "\n" +
               "***************************\n";
    }
}
