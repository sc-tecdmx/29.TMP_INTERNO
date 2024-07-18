package mx.gob.tecdmx.directorio_interno.api.estructuraorganica;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.gob.tecdmx.directorio_interno.entity.InstCatAreas;
import mx.gob.tecdmx.directorio_interno.entity.InstUAdscripcion;
import mx.gob.tecdmx.directorio_interno.repository.InstCatAreasRepository;
import mx.gob.tecdmx.directorio_interno.repository.InstUAdscripcionRepository;
import mx.gob.tecdmx.directorio_interno.utils.DTOResponse;


@Service
public class ServiceUadcripcionAreas {

	@Autowired
	InstCatAreasRepository instCatAreasRepository;
	@Autowired
	InstUAdscripcionRepository instUAdscripcionRepository;

	public boolean createUadcripcionAreas(PayloadUadcripcionAreas payload, InstUAdscripcion uAdscripcionParent, InstCatAreas areaParent) {
		InstUAdscripcion uAdscripcion = new InstUAdscripcion();
		if(payload.isUnidadAdscripcion) {
			uAdscripcion.setDescripcionUnidad(payload.getArea());
			uAdscripcion.setAbreviacionUnidad(payload.getAbreviatura());
			uAdscripcionParent = instUAdscripcionRepository.save(uAdscripcion);
		}
		InstCatAreas catArea = new InstCatAreas();
		catArea.setIdUnAdscripcion(uAdscripcionParent);
		catArea.setDescArea(payload.getArea());
		catArea.setAbrevArea(payload.getAbreviatura());
		catArea.setIdCatAreaPadre(areaParent);
		InstCatAreas catAreaStored = instCatAreasRepository.save(catArea);
		
		List<PayloadUadcripcionAreas> areasPayload = payload.getAreas();
		for(PayloadUadcripcionAreas areaPayload: areasPayload) {
			createUadcripcionAreas(areaPayload, uAdscripcionParent, catAreaStored);
		}
		return true;
	}

	public DTOResponse createUadcripcionAreas(PayloadUadcripcionAreas payload, DTOResponse response) {
		InstUAdscripcion uAdscripcion = new InstUAdscripcion();
		try {
			createUadcripcionAreas(payload, null, null);
			response.setMessage("Se han creado las unidades de adscripción y áreas satisfactoriamente");
			response.setStatus("OK");
			response.setData(true);
		}catch(Exception e) {
			response.setMessage(e+"");
			response.setStatus("OK");
			response.setData(true);
		}
		return response;
	}
	
	
}
