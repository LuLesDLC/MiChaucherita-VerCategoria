package entities;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

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

    public static Categoria getById(int idCategoria) {
    	EntityManager em = Persistence.createEntityManagerFactory("persistencia").createEntityManager();
    	return em.find(Categoria.class, idCategoria);
    }


    public static List<Categoria> getSumarizedByDate(int mes, int anio) {
        EntityManager entityManager = Persistence.createEntityManagerFactory("persistencia").createEntityManager();
        Query query = entityManager.createQuery("SELECT DISTINCT m.categoria FROM Movimiento m WHERE (FUNCTION('MONTH', m.fecha) = :mes AND FUNCTION('YEAR', m.fecha) = :anio)");
        query.setParameter("mes", obtenerMesCorrecto(mes));
        query.setParameter("anio", obtenerAnioCorrecto(anio));
        List<Categoria> categorias = (List<Categoria>) query.getResultList();
        return categorias;
    }
    
    public static List<Categoria> getCategoriasPorTipo(List<Categoria> categorias, TipoMovimiento tipo){
    	List<Categoria> categoriasIngreso = new ArrayList();
    	for(Categoria categoria: categorias) {
        	if(categoria.getTipo() == tipo) {
        		categoriasIngreso.add(categoria);
        	}
        }
    	return categoriasIngreso;
    }

    
    public static List<Categoria> getByTipo(List<Categoria> categorias, int tipo){
    	
    	return null;
    }
    
    public static int obtenerMesCorrecto(int mes) {
        if (mes != -1) {
        	return mes;
        }
        Calendar calendar = Calendar.getInstance();
        int mesActual = calendar.get(Calendar.MONTH) + 1; 
        return mesActual;
    }
   
    public static int obtenerAnioCorrecto(int anio) {
        if (anio != -1) {
        	return anio;
        }
        Calendar calendar = Calendar.getInstance();
        int anioActual = calendar.get(Calendar.YEAR); 
        return anioActual;
	}

	@SuppressWarnings("unchecked")
	public static List<Categoria> getAllOfIngresoType() {
    	EntityManager em = Persistence.createEntityManagerFactory("persistencia").createEntityManager();
    	String consultaJPQL = "SELECT t FROM Categoria t WHERE t.tipo= :mitipo";
    	Query query =  em.createQuery(consultaJPQL);
    	query.setParameter("mitipo", TipoMovimiento.INGRESO);
    	
    	return (List<Categoria>)query.getResultList();
    	
    }

    public double getMontoTotalMes(int mes, int anio) {
    	if (this.tipo == TipoMovimiento.TRANSFERENCIA) {
    		return 0;
    	} else {
            EntityManager entityManager = Persistence.createEntityManagerFactory("persistencia").createEntityManager();
            TypedQuery<Double> query = entityManager.createQuery(
                    "SELECT SUM(m.monto) FROM Movimiento m " +
                            "WHERE m.categoria.id = :idCategoria " +
                            "AND FUNCTION('MONTH', m.fecha) = :mes " +
                            "AND FUNCTION('YEAR', m.fecha) = :anio",
                    Double.class);
            query.setParameter("idCategoria", this.id);
            query.setParameter("mes", obtenerMesCorrecto(mes));
            query.setParameter("anio", obtenerAnioCorrecto(anio));
            Double sumaMovimientos = query.getSingleResult();
            return sumaMovimientos != null ? sumaMovimientos : 0.0;
    	}
    }

}