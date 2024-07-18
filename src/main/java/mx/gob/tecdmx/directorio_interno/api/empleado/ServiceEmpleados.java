package mx.gob.tecdmx.directorio_interno.api.empleado;

import java.util.Optional;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.gob.tecdmx.directorio_interno.entity.InstCatAreas;
import mx.gob.tecdmx.directorio_interno.entity.InstCatPuestos;
import mx.gob.tecdmx.directorio_interno.entity.InstCatSexo;
import mx.gob.tecdmx.directorio_interno.entity.InstEmpleado;
import mx.gob.tecdmx.directorio_interno.entity.InstEmpleadoPuestoArea;
import mx.gob.tecdmx.directorio_interno.entity.InstLogEmpleado;
import mx.gob.tecdmx.directorio_interno.entity.InstTitularUAdscripcion;
import mx.gob.tecdmx.directorio_interno.repository.InstCatAreasRepository;
import mx.gob.tecdmx.directorio_interno.repository.InstCatPuestosRepository;
import mx.gob.tecdmx.directorio_interno.repository.InstCatSexoRepository;
import mx.gob.tecdmx.directorio_interno.repository.InstEmpleadoPuestoAreaRepository;
import mx.gob.tecdmx.directorio_interno.repository.InstEmpleadoRepository;
import mx.gob.tecdmx.directorio_interno.repository.InstLogEmpleadoRepository;
import mx.gob.tecdmx.directorio_interno.repository.InstTitularUAdscripcionRepository;
import mx.gob.tecdmx.directorio_interno.repository.InstUAdscripcionRepository;
import mx.gob.tecdmx.directorio_interno.utils.DTOResponse;
import mx.gob.tecdmx.directorio_interno.utils.MetodosUtils;


@Service
public class ServiceEmpleados {

	@Autowired
	InstCatAreasRepository instCatAreasRepository;

	@Autowired
	InstCatPuestosRepository instCatPuestosRepository;

	@Autowired
	InstCatSexoRepository instCatSexoRepository;

	@Autowired
	InstEmpleadoPuestoAreaRepository instEmpleadoPuestoRepository;

	@Autowired
	InstEmpleadoRepository instEmpleadoRepository;

	@Autowired
	InstTitularUAdscripcionRepository instTitularUAdscripcionRepository;

	@Autowired
	InstUAdscripcionRepository instUAdscripcionRepository;
	
	@Autowired
	InstLogEmpleadoRepository instLogEmpleadoRepository;

	
	@Autowired
	InstEmpleadoPuestoAreaRepository InstEmpleadoPuestoAreaRepository;


	public DTOResponse createEmpleadoV2(PayloadEmpleados payload, DTOResponse response, Authentication auth) {
		
//		VOUsuario usuarioVO = (VOUsuario) auth.getDetails();
//		Optional<SegOrgLogSesion> sesionExist =segOrgLogSesionRepository.findById(Integer.parseInt(usuarioVO.getIdSession()));
		
		
		MetodosUtils utils = new MetodosUtils();

		Optional<InstEmpleado> empleadoExist = instEmpleadoRepository.findById(payload.getIdNumEmpleado());
		if (empleadoExist.isPresent()) {
			response.setMessage("Ya existe un registro con el número de empleado proporcionado");
			response.setStatus("Fail");
			return response;
		}
		if (payload.getEmailInst() != null) {
			Optional<InstEmpleado> empleadoExistEmail = instEmpleadoRepository.findByEmailInst(payload.getEmailInst());
			if (empleadoExistEmail.isPresent()) {
				response.setMessage("Ya existe un registro con el correo institucional proporcionado");
				response.setStatus("Fail");
				return response;
			}
		}
		if (payload.getEmailPers() != null) {
			Optional<InstEmpleado> empleadoExistEmail = instEmpleadoRepository.findByEmailPers(payload.getEmailPers());
			if (empleadoExistEmail.isPresent()) {
				response.setMessage("Ya existe un registro con el correo personal proporcionado");
				response.setStatus("Fail");
				return response;
			}
		}

		Optional<InstCatSexo> sexo = instCatSexoRepository.findBySexo(payload.getCodigoSexo());
		if (sexo.isPresent()) {
			Optional<InstCatPuestos> puesto = instCatPuestosRepository
					.findByDescNombramiento(payload.getCodigoPuesto());
			if (puesto.isPresent()) {
				Optional<InstCatAreas> area = instCatAreasRepository.findByAbrevArea(payload.getCodigoArea());
				if (area.isPresent()) {

					InstEmpleado empleado = new InstEmpleado();
					empleado.setId(payload.getIdNumEmpleado());
					empleado.setNombre(payload.getNombre());
					empleado.setApellido1(payload.getApellido1());
					empleado.setApellido2(payload.getApellido2());
					empleado.setIdSexo(sexo.get());
					empleado.setEmailPers(payload.getEmailPers());
					empleado.setEmailInst(payload.getEmailInst());
					empleado.setTelPers(payload.getTelPers());
					empleado.setTelInst(payload.getTelInst());
					empleado.setCurp(payload.getCurp());
					empleado.setRfc(payload.getRfc());
					empleado.setActivo(true);
					empleado.setPathFotografia(payload.getPathFotografia());
					InstEmpleado empleadoStored = instEmpleadoRepository.save(empleado);

					InstEmpleadoPuestoArea empleadoPuestoArea = new InstEmpleadoPuestoArea();
					empleadoPuestoArea.setIdNumEmpleado(empleadoStored);
					empleadoPuestoArea.setIdCatArea(area.get());
					empleadoPuestoArea.setIdPuesto(puesto.get());
					empleadoPuestoArea.setFechaAlta(utils.formatDate(payload.getFechaAltaEmpleado()));
					empleadoPuestoArea.setFechaConclusion(null);
					empleadoPuestoArea.setTipoEstructura("O"); //O organizacional F funcional
					empleadoPuestoArea.setActivo(true);

					instEmpleadoPuestoRepository.save(empleadoPuestoArea);
					
					//almacena el Log del empleado
					InstLogEmpleado logEmpleado = new InstLogEmpleado();
					logEmpleado.setIdNumEmpleado(empleadoStored);
					//logEmpleado.setSessionId(sesionExist.get());
					logEmpleado.setBitacora("creado");
					instLogEmpleadoRepository.save(logEmpleado);
					
					
					response.setMessage("El empleado se ha creado correctamente");
					response.setStatus("Success");

					if (payload.isEsTitular()) {
						InstTitularUAdscripcion titular = new InstTitularUAdscripcion();
						titular.setIdUnAdscripcion(area.get().getIdUnAdscripcion());
						titular.setIdEmpleadoPuestoArea(empleadoPuestoArea);
						titular.setFechaInicio(utils.formatDate(payload.getFechainicioTitular()));
						titular.setFechaConclusion(null);

						instTitularUAdscripcionRepository.save(titular);
					}

				} else {
					response.setMessage("El código de area es incorrecto");
					response.setStatus("Fail");
				}
			} else {
				response.setMessage("El código de puesto es incorrecto");
				response.setStatus("Fail");
			}
		} else {
			response.setMessage("El código de sexo es incorrecto");
			response.setStatus("Fail");
		}
		return response;
	}

}
