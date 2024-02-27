package controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import entities.Categoria;
import entities.Cuenta;
import entities.Movimiento;

@WebServlet("/DashboardController")
public class DashboardController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
    	ruteador(request, response);
    }


	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ruteador(request, response);
	}
	
	private void ruteador(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String ruta = (request.getParameter("ruta")!=null)? request.getParameter("ruta"):"ver";
		
		switch(ruta) {
		case "ver":
			verDashboard(request, response);
			break;
		case "verCategoria":
			verCategoria(request, response);
			break;
		}
		
	}
	
	private void verMovimientos(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}

	private void verCategoria(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		if (session.getAttribute("mes") == null || session.getAttribute("anio") == null) {
		    session.setAttribute("mes", -1);
		    session.setAttribute("anio", -1);
		}
		int mes = Integer.parseInt(session.getAttribute("mes").toString());
		int anio = Integer.parseInt(session.getAttribute("anio").toString()); 
		int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
		Categoria categoria = Categoria.getById(idCategoria);
		List<Movimiento> movimientos = Movimiento.getMovimientosByCategoriaByMes(anio, mes, idCategoria);
		double total = Movimiento.getMontoTotalFromMovimientos(movimientos);
		request.setAttribute("categoria", categoria);
		request.setAttribute("movimientos", movimientos);
		request.setAttribute("total", total);
		request.getRequestDispatcher("/jsp/categoria.jsp").forward(request, response);
	}

	private void verCuenta(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}

	private void verDashboard(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		if (session.getAttribute("mes") == null || session.getAttribute("anio") == null) {
		    session.setAttribute("mes", -1);
		    session.setAttribute("anio", -1);
		} 
		int mes = Integer.parseInt(session.getAttribute("mes").toString());
		int anio = Integer.parseInt(session.getAttribute("anio").toString()); 
		List<Cuenta> cuentas = Cuenta.getSumarized();
		List<Categoria> categorias = Categoria.getSumarizedByDate(mes, anio);
		List<Movimiento> movimientos = Movimiento.getAll();
	
		request.setAttribute("cuentas", cuentas);
		request.setAttribute("movimientos", movimientos);
		request.setAttribute("categorias", categorias);
		request.getRequestDispatcher("/jsp/dashboard.jsp").forward(request, response);
	}

	

    

}