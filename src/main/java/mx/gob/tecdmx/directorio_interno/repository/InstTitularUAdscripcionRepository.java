package mx.gob.tecdmx.directorio_interno.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import mx.gob.tecdmx.directorio_interno.entity.InstTitularUAdscripcion;


@Repository
public interface InstTitularUAdscripcionRepository extends CrudRepository<InstTitularUAdscripcion, Integer> {
  
	
}