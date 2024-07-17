package mx.gob.tecdmx.directorio_interno.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import mx.gob.tecdmx.directorio_interno.entity.InstCatAreas;
import mx.gob.tecdmx.directorio_interno.entity.InstCatPuestos;
import mx.gob.tecdmx.directorio_interno.entity.InstEmpleado;
import mx.gob.tecdmx.directorio_interno.entity.InstEmpleadoPuestoArea;



@Repository
public interface InstEmpleadoPuestoAreaRepository extends CrudRepository<InstEmpleadoPuestoArea, Integer> {

	List<InstEmpleadoPuestoArea> findByIdNumEmpleado(InstEmpleado idEmpleado);
	Optional<InstEmpleadoPuestoArea> findByIdNumEmpleadoAndIdCatAreaAndIdPuesto(InstEmpleado idEmpleado, InstCatAreas  idCatArea, InstCatPuestos  idPuesto);
  
	
}