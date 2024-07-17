package mx.gob.tecdmx.directorio_interno.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import mx.gob.tecdmx.directorio_interno.entity.InstCatPuestos;


@Repository
public interface InstCatPuestosRepository extends CrudRepository<InstCatPuestos, Integer> {
	
	Optional<InstCatPuestos> findByDescNombramiento(String puesto);

	Optional<InstCatPuestos> findById(InstCatPuestos idPuesto);
	
}