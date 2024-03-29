package entities;

import java.io.Serializable;
import java.util.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "Movimiento")
public class Movimiento implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idMovimiento")
	private Integer id;

	@Temporal(TemporalType.DATE)
	private Date fecha;

	@Column(name = "monto")
	private double monto;

	@Column(name = "concepto")
	private String concepto;

	@Enumerated
	private TipoMovimiento tipo;

	@ManyToOne
	@JoinColumn
	private Cuenta origen;

	@ManyToOne
	@JoinColumn
	private Cuenta destino;

	@ManyToOne
	@JoinColumn(referencedColumnName = "id")
	private Categoria categoria;

	public Movimiento() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public double getMonto() {
		return monto;
	}

	public void setMonto(double monto) {
		this.monto = monto;
	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public TipoMovimiento getTipo() {
		return tipo;
	}

	public void setTipo(TipoMovimiento tipo) {
		this.tipo = tipo;
	}

	public Cuenta getOrigen() {
		return origen;
	}

	public void setOrigen(Cuenta origen) {
		this.origen = origen;
	}

	public Cuenta getDestino() {
		return destino;
	}

	public void setDestino(Cuenta destino) {
		this.destino = destino;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	@SuppressWarnings("unchecked")
	public static List<Movimiento> getAll() {
		EntityManager em = Persistence.createEntityManagerFactory("persistencia").createEntityManager();
		String consulta = "SELECT m FROM Movimiento m";
		Query query = em.createQuery(consulta);
		return (List<Movimiento>) query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public static List<Movimiento> getMovimientosByCategoriaByMes(int anio, int mes, int idCategoria) {
		EntityManager em = Persistence.createEntityManagerFactory("persistencia").createEntityManager();
		Query query = em.createQuery(
				"SELECT m FROM Movimiento m WHERE (m.categoria.id = :idCategoria AND FUNCTION('MONTH', m.fecha) = :mes AND FUNCTION('YEAR', m.fecha) = :anio) ORDER BY m.fecha DESC");
		query.setParameter("idCategoria", idCategoria);
		query.setParameter("mes",Categoria.obtenerMesCorrecto(mes));
		query.setParameter("anio",Categoria.obtenerAnioCorrecto(anio));
		List<Movimiento> movimientos = query.getResultList();
		return movimientos;
	}
	
	public static double getMontoTotalFromMovimientos(List<Movimiento> movimientos) {
		double suma = 0;
		for (Movimiento movimiento: movimientos) {
			suma += movimiento.monto;
		}
		return suma;
	}

}