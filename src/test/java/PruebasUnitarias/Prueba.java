package PruebasUnitarias;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import entities.*;

public class Prueba {

	public static void main(String[] args) {
		EntityManager em = Persistence.createEntityManagerFactory("persistencia").createEntityManager();
		em.getTransaction().begin();
		em.getTransaction().commit();
	}

}
