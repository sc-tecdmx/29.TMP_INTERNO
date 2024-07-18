package mx.gob.tecdmx.directorio_interno.api.empleado;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import mx.gob.tecdmx.directorio_interno.utils.DTOResponse;
import mx.gob.tecdmx.directorio_interno.utils.MetodosUtils;


@RestController
@RequestMapping(path = "/api/seguridad")
public class RestControllerEmpleados {
	
	@Autowired
	ServiceEmpleados serviceEmpleados;

	@CrossOrigin()
	@RequestMapping(method = RequestMethod.POST, path = "/create-empleado", produces = "application/json")
	@ResponseBody
	public ResponseEntity<String> createEmpleado(@RequestBody PayloadEmpleados payload, Authentication auth) {
		MetodosUtils utils = new MetodosUtils();
		DTOResponse response = new DTOResponse();
		serviceEmpleados.createEmpleadoV2(payload, response, auth);
		return ResponseEntity.ok().header(null).body(utils.objectToJson(response));
	}
	
	
}
