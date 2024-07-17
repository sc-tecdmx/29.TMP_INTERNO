package mx.gob.tecdmx.directorio_interno.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import mx.gob.tecdmx.directorio_interno.entity.InstEmpleado;



@Repository
public interface InstEmpleadoRepository extends CrudRepository<InstEmpleado, Integer> {
  Optional<InstEmpleado> findByIdUsuario(int usuario);

Optional<InstEmpleado> findByEmailInst(String email);

Optional<InstEmpleado> findByEmailPers(String emailPers);
	
}