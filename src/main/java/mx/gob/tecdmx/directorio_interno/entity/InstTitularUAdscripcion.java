package mx.gob.tecdmx.directorio_interno.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "inst_titular_u_adscripcion", schema = "public")
public class InstTitularUAdscripcion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "n_id_titular_area", unique = true, nullable = false)
	int  id;
  
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="n_id_u_adscripcion_detalle", referencedColumnName="n_id_u_adscripcion_detalle") 
	InstUAdscripcion  idUnAdscripcion;
  
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="n_id_empleado_puesto_area", referencedColumnName="n_id_empleado_puesto_area") 
	InstEmpleadoPuestoArea  idEmpleadoPuestoArea;
  
	@Column(name = "fecha_inicio")
	Date  fechaInicio;
  
	@Column(name = "fecha_conclusion")
	Date  fechaConclusion;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public InstUAdscripcion getIdUnAdscripcion() {
		return idUnAdscripcion;
	}

	public void setIdUnAdscripcion(InstUAdscripcion idUnAdscripcion) {
		this.idUnAdscripcion = idUnAdscripcion;
	}

	public InstEmpleadoPuestoArea getIdEmpleadoPuestoArea() {
		return idEmpleadoPuestoArea;
	}

	public void setIdEmpleadoPuestoArea(InstEmpleadoPuestoArea idEmpleadoPuestoArea) {
		this.idEmpleadoPuestoArea = idEmpleadoPuestoArea;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaConclusion() {
		return fechaConclusion;
	}

	public void setFechaConclusion(Date fechaConclusion) {
		this.fechaConclusion = fechaConclusion;
	}

	
	
}