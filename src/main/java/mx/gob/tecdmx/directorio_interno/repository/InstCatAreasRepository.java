package mx.gob.tecdmx.directorio_interno.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import mx.gob.tecdmx.directorio_interno.entity.InstCatAreas;


@Repository
public interface InstCatAreasRepository extends CrudRepository<InstCatAreas, Integer> {
	Optional<InstCatAreas> findByAbrevArea(String area);
	
}