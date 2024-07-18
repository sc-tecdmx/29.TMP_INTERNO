package mx.gob.tecdmx.directorio_interno.api.empleado;

import java.util.List;

public class DTOResponseEmpleado {
	String nombre;
	String apellido1;
	String apellido2;
	List<PerfilDTO> perfiles;
	String correo;
	String pathFotografia;
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido1() {
		return apellido1;
	}
	public void setApellido1(String apellido1) {
		this.apellido1 = apellido1;
	}
	public String getApellido2() {
		return apellido2;
	}
	public void setApellido2(String apellido2) {
		this.apellido2 = apellido2;
	}
	public List<PerfilDTO> getPerfiles() {
		return perfiles;
	}
	public void setPerfiles(List<PerfilDTO> perfiles) {
		this.perfiles = perfiles;
	}
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	public String getPathFotografia() {
		return pathFotografia;
	}
	public void setPathFotografia(String pathFotografia) {
		this.pathFotografia = pathFotografia;
	}
	
	
}
