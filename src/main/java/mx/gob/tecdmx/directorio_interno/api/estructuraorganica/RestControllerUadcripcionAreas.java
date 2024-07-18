package mx.gob.tecdmx.directorio_interno.api.estructuraorganica;

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
public class RestControllerUadcripcionAreas {
	
	@Autowired
	ServiceUadcripcionAreas serviceUadcripcionAreas;

	@CrossOrigin()
	@RequestMapping(method = RequestMethod.POST, path = "/create-uadscripcion-areas", produces = "application/json")
	@ResponseBody
	public ResponseEntity<String> createUAdscripcionAreas(@RequestBody PayloadUadcripcionAreas payload) {
		MetodosUtils utils = new MetodosUtils();
		DTOResponse response = new DTOResponse();
		serviceUadcripcionAreas.createUadcripcionAreas(payload, response);
		return ResponseEntity.ok().header(null).body(utils.objectToJson(response));
	}

	
}
