package mx.gob.tecdmx.directorio_interno.api.empleado;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import mx.gob.tecdmx.directorio_interno.entity.InstUAdscripcion;
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
					empleadoPuestoArea.setTipoEstructura("O"); // O organizacional F funcional
					empleadoPuestoArea.setActivo(true);

					instEmpleadoPuestoRepository.save(empleadoPuestoArea);
					
					// almacena el Log del empleado
					InstLogEmpleado logEmpleado = new InstLogEmpleado();
					logEmpleado.setIdNumEmpleado(empleadoStored);
					//logEmpleado.setSessionId(sesion);
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

	public DTOResponse editarEmpleado(int numEmpleado, PayloaEditardEmpleados payload, DTOResponse response,
			Authentication auth) {
//		VOUsuario usuarioVO = (VOUsuario) auth.getDetails();
//		Optional<SegOrgLogSesion> sesionExist =segOrgLogSesionRepository.findById(Integer.parseInt(usuarioVO.getIdSession()));

		MetodosUtils utils = new MetodosUtils();

		Optional<InstEmpleado> empleadoExist = instEmpleadoRepository.findById(numEmpleado);
		if (empleadoExist.isPresent()) {

			if (payload.getEmailPers() != null) {
				Optional<InstEmpleado> empleadoExistEmail = instEmpleadoRepository
						.findByEmailPers(payload.getEmailPers());
				if (empleadoExistEmail.isPresent() && !(empleadoExistEmail.get().getId() == numEmpleado)) {
					response.setMessage("Ya existe un registro con el correo personal proporcionado");
					response.setStatus("Fail");
					return response;
				}
			}

			Optional<InstCatSexo> sexo = instCatSexoRepository.findBySexo(payload.getCodigoSexo());
			if (sexo.isPresent()) {

				InstEmpleado empleado = empleadoExist.get();
				empleado.setNombre(payload.getNombre());
				empleado.setApellido1(payload.getApellido1());
				empleado.setApellido2(payload.getApellido2());
				empleado.setIdSexo(sexo.get());
				empleado.setEmailPers(payload.getEmailPers());
				empleado.setTelPers(payload.getTelPers());
				empleado.setTelInst(payload.getTelInst());
				empleado.setCurp(payload.getCurp());
				empleado.setRfc(payload.getRfc());
				empleado.setPathFotografia(payload.getPathFotografia());
				InstEmpleado empleadoStored = instEmpleadoRepository.save(empleado);

				// almacena el Log del empleado
				InstLogEmpleado logEmpleado = new InstLogEmpleado();
				Optional<InstEmpleado> empleadoLog = instEmpleadoRepository
						.findByEmailInst(empleadoExist.get().getEmailInst());
				logEmpleado.setIdNumEmpleado(empleadoLog.get());
				// logEmpleado.setSessionId(sesionExist.get());
				logEmpleado.setBitacora("editado");
				instLogEmpleadoRepository.save(logEmpleado);

				response.setMessage("Se han editado los datos correctamente");
				response.setStatus("Success");

				response.setData(payload);

			} else {
				response.setMessage("El código de sexo es incorrecto");
				response.setStatus("Fail");
			}
		}
		return response;
	}

	public DTOResponse eliminarEmpleado(int idEmpleado, DTOResponse response, Authentication auth) {
//			VOUsuario usuarioVO = (VOUsuario) auth.getDetails();
//			Optional<SegOrgLogSesion> sesionExist =segOrgLogSesionRepository.findById(Integer.parseInt(usuarioVO.getIdSession()));

		Optional<InstEmpleado> empleadoExist = instEmpleadoRepository.findById(idEmpleado);

		if (!empleadoExist.isPresent()) {
			response.setMessage("El usuario no existe");
			response.setStatus("Fail");

		} else {

			List<InstEmpleadoPuestoArea> empleadoPuestosAreas = instEmpleadoPuestoRepository
					.findByIdNumEmpleado(empleadoExist.get());
			if (empleadoPuestosAreas.size() < 0) {
				response.setMessage("No se pudo obtener el area y puesto del usuario");
				response.setStatus("Fail");
			} else {

				empleadoExist.get().setActivo(false);
				instEmpleadoRepository.save(empleadoExist.get());
				for (InstEmpleadoPuestoArea empleadoPuestoArea : empleadoPuestosAreas) {
					empleadoPuestoArea.setActivo(false);
					instEmpleadoPuestoRepository.save(empleadoPuestoArea);
				}

				// almacena el Log del empleado
				InstLogEmpleado logEmpleado = new InstLogEmpleado();
				Optional<InstEmpleado> empleadoLog = instEmpleadoRepository
						.findByEmailInst(empleadoExist.get().getEmailInst());
				logEmpleado.setIdNumEmpleado(empleadoLog.get());
				// logEmpleado.setSessionId(sesionExist.get());
				logEmpleado.setBitacora("eliminado");
				instLogEmpleadoRepository.save(logEmpleado);

				response.setMessage("Empleado eliminado");
				response.setStatus("success");
			}

		}

		return response;

	}

	public DTOResponse consultarEmpleado(int idEmpleado, DTOResponse response) {
		DTOResponseEmpleado resp = new DTOResponseEmpleado();
		List<PerfilDTO> perfiles = new ArrayList<PerfilDTO>();
		Optional<InstEmpleado> empleadoExist = instEmpleadoRepository.findById(idEmpleado);
		if (!empleadoExist.isPresent()) {
			response.setMessage("El empleado no existe");
			response.setStatus("Fail");

		} else {
			if (!empleadoExist.get().isActivo()) {
				response.setMessage("El empleado no existe");
				response.setStatus("Fail");
				return response;
			}
			List<InstEmpleadoPuestoArea> empleadoPuesto = instEmpleadoPuestoRepository
					.findByIdNumEmpleado(empleadoExist.get());
			if (empleadoPuesto.size()<=0) {
				response.setMessage("No se pudo obtener areas y puestos del usuario");
				response.setStatus("Fail");
			} else {
				resp.setNombre(empleadoExist.get().getNombre());
				resp.setApellido1(empleadoExist.get().getApellido1());
				resp.setApellido2(empleadoExist.get().getApellido2());
				resp.setCorreo(empleadoExist.get().getEmailInst());
				resp.setPathFotografia(empleadoExist.get().getPathFotografia());

				for (InstEmpleadoPuestoArea empleadoPuestoArea : empleadoPuesto) {
					PerfilDTO perfil = new PerfilDTO();
					// puesto
					Optional<InstCatPuestos> puesto = instCatPuestosRepository
							.findById(empleadoPuestoArea.getIdPuesto().getId());
					perfil.setPuesto(puesto.get().getDescNombramiento());
					// area
					Optional<InstCatAreas> area = instCatAreasRepository
							.findById(empleadoPuestoArea.getIdCatArea().getId());
					perfil.setArea(area.get().getDescArea());
					// UA
					Optional<InstUAdscripcion> unidadAds = instUAdscripcionRepository
							.findById(area.get().getIdUnAdscripcion().getId());
					perfil.setUnidadAdscripcion(unidadAds.get().getDescripcionUnidad());

					perfiles.add(perfil);

				}
				resp.setPerfiles(perfiles);
				response.setMessage("Información del Empleado");
				response.setStatus("success");
				response.setData(resp);

			}

		}

		return response;

		// TODO Auto-generated method stub

	}
	
	public DTOResponse createPerfil(PayloadPerfiles payload, DTOResponse response) {
		
		Optional<InstEmpleado> empleado = instEmpleadoRepository.findById(payload.getIdNumEmpleado());
		Optional<InstCatAreas> area = instCatAreasRepository.findById(payload.getIdCatArea());
		Optional<InstCatPuestos> puesto = instCatPuestosRepository.findById(payload.getIdCatPuesto());
		
		if(empleado.isPresent()&&area.isPresent()&&puesto.isPresent()) {
			InstEmpleadoPuestoArea perfil = new InstEmpleadoPuestoArea();
			
			perfil.setIdNumEmpleado(empleado.get());
			perfil.setIdCatArea(area.get());
			perfil.setIdPuesto(puesto.get());
			perfil.setFechaConclusion(null);
			perfil.setFechaAlta(new Date());
			perfil.setTipoEstructura(payload.getTipoEstructura());
			perfil.setActivo(true);
			
			InstEmpleadoPuestoAreaRepository.save(perfil);
			
			response.setMessage("El perfil se ha creado satisfactoriamente");
			response.setStatus("Success");
			
			return response;
			
		}
		return null;
	}
	
}
