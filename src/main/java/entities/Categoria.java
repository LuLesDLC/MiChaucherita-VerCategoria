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
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.TypedQuery;


@Entity
@Table(name="Categoria")
public class Categoria implements Serializable {

    private static final long serialVersionUID = 1L;

	/**
     * Default constructor
     */
    public Categoria() {
    }

    
    public Categoria(Integer id, String nombre, TipoMovimiento tipo) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.tipo = tipo;
	}


	public Categoria(String nombre, TipoMovimiento tipo) {
		super();
		this.nombre = nombre;
		this.tipo = tipo;

	}


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
    private Integer id;

    @Column
    private String nombre;

    @Enumerated
    private TipoMovimiento tipo;
    

    
    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public TipoMovimiento getTipo() {
		return tipo;
	}

	public void setTipo(TipoMovimiento tipo) {
		this.tipo = tipo;
	}

	public double Total(int mes) {
		return obtenerSumaMovimientosPorCategoriaYMes(this.id,mes);
	}






    public static Categoria getById(int idCategoria) {
    	EntityManager em = Persistence.createEntityManagerFactory("persistencia").createEntityManager();
    	return em.find(Categoria.class, idCategoria);
    }


    public static List<Categoria> getSumarizedByMonth(int mes) {
        EntityManager entityManager = Persistence.createEntityManagerFactory("persistencia").createEntityManager();
        Query query = entityManager.createQuery("SELECT DISTINCT m.categoria FROM Movimiento m WHERE FUNCTION('MONTH', m.fecha) = :mes");
        query.setParameter("mes", obtenerMes(mes));
        List<Categoria> categorias = (List<Categoria>) query.getResultList();
        return categorias;
    }
    @SuppressWarnings("unchecked")
	public static List<Categoria> getAllOfIngresoType() {
    	EntityManager em = Persistence.createEntityManagerFactory("persistencia").createEntityManager();
    	String consultaJPQL = "SELECT t FROM Categoria t WHERE t.tipo= :mitipo";
    	Query query =  em.createQuery(consultaJPQL);
    	query.setParameter("mitipo", TipoMovimiento.INGRESO);
    	
    	return (List<Categoria>)query.getResultList();
    	
    }


    private static double obtenerSumaMovimientosPorCategoriaYMes(int idCategoria, int mes) {
        EntityManager entityManager = Persistence.createEntityManagerFactory("persistencia").createEntityManager();
        TypedQuery<Double> query = entityManager.createQuery(
                "SELECT SUM(m.monto) FROM Movimiento m " +
                        "WHERE m.categoria.id = :idCategoria " +
                        "AND FUNCTION('MONTH', m.fecha) = :mes",
                Double.class);
        query.setParameter("idCategoria", idCategoria);
        query.setParameter("mes", obtenerMes(mes));
        Double sumaMovimientos = query.getSingleResult();
        return sumaMovimientos != null ? sumaMovimientos : 0.0;
    }
    
    public static int obtenerMes(int mes) {
        if (mes != -1) {
        	return mes;
        }
        Calendar calendar = Calendar.getInstance();
        int mesActual = calendar.get(Calendar.MONTH) + 1; 
        return mesActual;
    }

}