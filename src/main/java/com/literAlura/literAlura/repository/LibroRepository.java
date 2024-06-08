package com.literAlura.literAlura.repository;

import com.literAlura.literAlura.model.Autor;
import com.literAlura.literAlura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro,Long> {

    @Query("SELECT a FROM Libro l JOIN l.autor a")
    List<Autor> librosPorAutores();

    @Query("SELECT a FROM Libro l JOIN l.autor a WHERE a.fechaMuerte > :fecha AND a.fechaNacimiento < :fecha")
    List<Autor> autorPorFecha(int fecha);

    @Query("SELECT l FROM Libro l WHERE l.idiomas = :idioma")
    List<Libro> librosPorIdiomas(String idioma);
}
