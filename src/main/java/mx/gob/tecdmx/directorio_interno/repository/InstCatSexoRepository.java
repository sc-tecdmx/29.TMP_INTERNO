package mx.gob.tecdmx.directorio_interno.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import mx.gob.tecdmx.directorio_interno.entity.InstCatSexo;


@Repository
public interface InstCatSexoRepository extends CrudRepository<InstCatSexo, Integer> {
  Optional<InstCatSexo> findBySexo(String sexo);
	
}