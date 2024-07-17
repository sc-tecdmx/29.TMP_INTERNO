package mx.gob.tecdmx.directorio_interno.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import mx.gob.tecdmx.directorio_interno.entity.InstUAdscripcion;


@Repository
public interface InstUAdscripcionRepository extends CrudRepository<InstUAdscripcion, Integer> {
  
	
}