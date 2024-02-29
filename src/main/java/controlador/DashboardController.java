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
import entities.TipoMovimiento;

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

	private void verDashboard(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		if (session.getAttribute("mes") == null || session.getAttribute("anio") == null) {
		    session.setAttribute("mes", -1);
		    session.setAttribute("anio", -1);
		}else {
			session.setAttribute("mes", request.getParameter("mes"));
		    session.setAttribute("anio", request.getParameter("anio"));
		}
		
		int mes = Integer.parseInt(session.getAttribute("mes").toString());
		int anio = Integer.parseInt(session.getAttribute("anio").toString()); 
		List<Cuenta> cuentas = Cuenta.getSumarized();
		List<Categoria> categorias = Categoria.getSumarizedByDate(mes, anio);
		List<Movimiento> movimientos = Movimiento.getAll();


		List<Categoria> categoriasIngreso = Categoria.getCategoriasPorTipo(categorias, TipoMovimiento.INGRESO);
		List<Categoria> categoriasEgreso = Categoria.getCategoriasPorTipo(categorias, TipoMovimiento.EGRESO);
		List<Categoria> categoriasTransferencia = Categoria.getCategoriasPorTipo(categorias, TipoMovimiento.TRANSFERENCIA);
		
		request.setAttribute("cuentas", cuentas);
		request.setAttribute("movimientos", movimientos);
		request.setAttribute("categoriasIngreso", categoriasIngreso);
		request.setAttribute("categoriasEgreso", categoriasEgreso);
		request.setAttribute("categoriasTransferencia", categoriasTransferencia);
		request.getRequestDispatcher("/jsp/dashboard.jsp").forward(request, response);
	}

	

    

}