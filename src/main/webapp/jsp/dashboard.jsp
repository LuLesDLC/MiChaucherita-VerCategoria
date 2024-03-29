<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Mi Chauchera</title>
<link rel="stylesheet" href="./css/style.css">
<script
	src="https://code.iconify.design/iconify-icon/1.0.7/iconify-icon.min.js"></script>

</head>

<body>

	<c:choose>
		<c:when test="${empty sessionScope.mes}">
			<c:set var="mes" value="-1" />
			<c:set var="anio" value="-1" />
			<!-- Si el mes y el año en la sesión están vacíos, asigna -1 -->
		</c:when>
		<c:otherwise>
			<c:set var="mes" value="${sessionScope.mes}" />
			<c:set var="anio" value="${sessionScope.anio}" />
			<!-- Si el mes en la sesión no está vacío, utiliza el valor de la sesión -->
		</c:otherwise>
	</c:choose>

	<header class="header_pagina_principal">
		<button class="boton boton2 usuario_shortcut">
			<img src="https://picsum.photos/30" alt="imagen de usuario">
			<p>Usuario</p>
		</button>
		<div>
			<a href=""> <iconify-icon class="icono" icon="carbon:view-filled"
					width="18"></iconify-icon> <span>Dashboard</span>
			</a> <a href=""> <iconify-icon class="icono"
					icon="carbon:view-filled" width="18"></iconify-icon> <span>Configuración</span>
			</a>
		</div>
	</header>

	<!-- dashboard de cartera digital -->
	<main class="main_pagina_principal">
		<div class="contenedor_default dash_head">
			<header class="header_contenedor">
				<h3>Balance Total</h3>
				<button class="boton boton2 color_inactivo_boton">
					<iconify-icon class="icono" icon="carbon:view-filled" width="18"></iconify-icon>
				</button>
			</header>

			<main class="balance">
				<iconify-icon class="icono" icon="cryptocurrency:usd"
					style="color: white;" width="32"></iconify-icon>
				<h1>3000.00</h1>
			</main>
		</div>

		<div class="contenedor-resumen">
			<div class="contenedor_default">
				<!-- CUENTAS -->
				<header class="header_contenedor">
					<h3>Cuentas</h3>
				</header>


				<main class="cuentas">

					<c:forEach items="${cuentas}" var="cuenta">

						<div class="card-cuenta">
							<div>
								<div class=" cuenta_icono">
									<iconify-icon class="icono" icon="mdi:bank" width="32"></iconify-icon>
								</div>
								<h4>${cuenta.nombre}</h4>
								<p>${cuenta.total}$</p>
							</div>
							<div class="botones_transacciones">

								<a
									href="RegistrarMovimientosController?ruta=nuevoingreso&idCuenta=${cuenta.id}">
									<iconify-icon icon="ph:arrow-down-bold" width="18"></iconify-icon>
									<span class="texto_boton">Depositar</span>
								</a> <a href=""> <iconify-icon icon="ph:arrow-up-bold"
										width="18"></iconify-icon> <span class="texto_boton">Retirar</span>
								</a> <a href=""> <iconify-icon icon="mingcute:transfer-fill"
										width="18"></iconify-icon> <span class="texto_boton">Transferir</span>
								</a>

							</div>
						</div>
					</c:forEach>

				</main>

			</div>

			<!-- CATEGORIAS -->
			<div class="contenedor_default">

				<header class="header_contenedor">
					<h3>Resumen por categoría</h3>
				</header>

				<div>
					<input type="month" id="fechaFiltro" name="fechaFiltro"
						placeholder="yyyy-mm">
					<button onclick="extraerDatos()">Buscar</button>
					<script>
						function extraerDatos() {
							// Obtener el valor del campo de entrada
							var valorInput = document
									.getElementById("fechaFiltro").value;

							var regex = /^(19|20)\d{2}-(0[1-9]|1[0-2])$/; // Año entre 1900 y 2099, mes entre 01 y 12
							if (!regex.test(valorInput)) {
								alert("El formato de la fecha no es válido (yyyy-mm)");
								return; // Detener la ejecución si el formato es incorrecto
							}

							// Extraer el año y el mes
							var anioFiltrado = valorInput.substring(0, 4);
							var mesFiltrado = valorInput.substring(5, 7);

							// Crear un formulario dinámicamente
							var form = document.createElement('form');
							form.method = 'POST';
							form.action = 'DashboardController';

							// Crear campos ocultos para enviar los datos
							var mesField = document.createElement('input');
							mesField.type = 'hidden';
							mesField.name = 'mes';
							mesField.value = mesFiltrado;
							form.appendChild(mesField);

							var anioField = document.createElement('input');
							anioField.type = 'hidden';
							anioField.name = 'anio';
							anioField.value = anioFiltrado;
							form.appendChild(anioField);

							// Agregar el formulario al cuerpo del documento y enviarlo
							document.body.appendChild(form);
							form.submit();
						}
					</script>
				</div>

				<c:if test="${not empty categoriasIngreso}">

					<div>
						<h4>Categorías de Ingreso</h4>

					</div>

					<main class="clasificacion">
						<c:set var="total" value="0.0" />
						<c:set var="total_cat" value="0.0" />

						<c:forEach items="${categoriasIngreso}" var="categoria">
							<div class="card-cuenta">
								<div>
									<div class=" cuenta_icono">
										<iconify-icon class="icono" icon="mdi:bank" width="32"></iconify-icon>
									</div>
									<h4>${categoria.nombre}</h4>
									<c:set var="total_cat"
										value="${categoria.getMontoTotalMes(mes, anio)}" />

									<p>${total_cat}$</p>
								</div>
								<div class="botones_transacciones">

									<a
										href="DashboardController?ruta=verCategoria&idCategoria=${categoria.id}">
										<iconify-icon icon="ph:arrow-down-bold" width="18"></iconify-icon>
										<span class="texto_boton">Movimientos</span>
									</a>

								</div>
							</div>
							<c:set var="total" value="${total + total_cat}" />

						</c:forEach>

					</main>
					<h5>Total de ingresos: $ ${total}</h5>
					<br>
				</c:if>
				<c:if test="${not empty categoriasEgreso }">
					<div>
						<h4>Categorías de Egreso</h4>

					</div>

					<main class="clasificacion">


						<c:set var="total" value="0.0" />
						<c:set var="total_cat" value="0.0" />
						<c:forEach items="${categoriasEgreso}" var="categoria">
							<div class="card-cuenta">
								<div>
									<div class=" cuenta_icono">
										<iconify-icon class="icono" icon="mdi:bank" width="32"></iconify-icon>
									</div>
									<h4>${categoria.nombre}</h4>
									<c:set var="total_cat"
										value="${categoria.getMontoTotalMes(mes, anio)}" />
									<p>${total_cat}$</p>
								</div>
								<div class="botones_transacciones">

									<a
										href="DashboardController?ruta=verCategoria&idCategoria=${categoria.id}">
										<iconify-icon icon="ph:arrow-up-bold" width="18"></iconify-icon>
										<span class="texto_boton">Movimientos</span>
									</a>

								</div>
							</div>
							<c:set var="total" value="${total + total_cat}" />

						</c:forEach>
					</main>
					<h5>Total de Egresos: $ ${total}</h5>
<br>
				</c:if>
				<c:if test="${not empty categoriasTransferencia }">

					<div>
						<h4>Categorías de Transferencia</h4>

					</div>

					<main class="clasificacion">

						<c:set var="total" value="0.0" />
						<c:set var="total_cat" value="0.0" />
						<c:forEach items="${categoriasTransferencia}" var="categoria">
							<div class="card-cuenta">
								<div>
									<div class=" cuenta_icono">
										<iconify-icon class="icono" icon="mdi:bank" width="32"></iconify-icon>
									</div>
									<h4>${categoria.nombre}</h4>
									<c:set var="total_cat"
										value="${categoria.getMontoTotalMes(mes, anio)}" />
									<p>${total_cat}$</p>
								</div>
								<div class="botones_transacciones">

									<a
										href="DashboardController?ruta=verCategoria&idCategoria=${categoria.id}">
										<iconify-icon icon="mingcute:transfer-fill" width="18"></iconify-icon>
										<span class="texto_boton">Movimientos</span>
									</a>

								</div>
							</div>
							<c:set var="total" value="${total + total_cat}" />

						</c:forEach>
					</main>
					<h5>Total de Transferencia: $ ${total}</h5>
<br>
				</c:if>
			</div>

		</div>

		<!-- TRANSACCIONES RECIENTES -->
		<div class="contenedor_default grid_abajo">

			<header class="header_contenedor">
				<h3>Movimientos</h3>
			</header>

			<main id="transacciones" class="mostrar">
				<div class="transaccion">
					<div class="transaccion_icono"></div>
					<div class="id_transaccion">
						<p>ID</p>
					</div>
					<div class="transaccion_info">
						<p>Fecha</p>
					</div>

					<div class="cuenta">
						<p>Cuenta</p>
					</div>

					<div class="concepto">
						<p>Concepto</p>
					</div>

					<div class="transaccion_monto">
						<h4>Monto</h4>
					</div>
				</div>

				<c:forEach items="${movimientos}" var="movimiento">

					<div class="transaccion">
						<div class="transaccion_icono">
							<c:choose>
								<c:when test="${movimiento.tipo=='EGRESO'}">
									<c:set var="color" value="retiro_color" />
									<c:set var="icono" value="ph:arrow-up-bold" />
									<br />
								</c:when>
								<c:when test="${movimiento.tipo=='TRANSFERENCIA'}">
									<c:set var="color" value="transferencia_color" />
									<c:set var="icono" value="mingcute:transfer-fill" />
									<br />
								</c:when>
								<c:otherwise>
									<c:set var="color" value="deposito_color" />
									<c:set var="icono" value="ph:arrow-down-bold" />
									<br />
								</c:otherwise>
							</c:choose>
							<iconify-icon class="icono ${color}" icon="${icono}" width="18"></iconify-icon>
						</div>
						<div class="id_transaccion">${movimiento.id}</div>
						<div class="transaccion_info">
							<p>${movimiento.fecha}</p>
						</div>

						<div class="cuenta">
							<div class="cuenta_icono">
								<iconify-icon class="icono" icon="mdi:bank" width="20"></iconify-icon>
							</div>
							<c:choose>
								<c:when test="${movimiento.tipo=='TRANSFERENCIA'}">
									<p>${movimiento.origen.nombre}>
										${movimiento.destino.nombre}</p>
								</c:when>
								<c:when test="${not empty movimiento.origen}">
									<p>${movimiento.origen.nombre}</p>
								</c:when>
								<c:otherwise>
									<p>${movimiento.destino.nombre}</p>
								</c:otherwise>
							</c:choose>
						</div>

						<div class="concepto">
							<p>${movimiento.concepto}</p>
						</div>

						<div class="transaccion_monto">
							<h4>${movimiento.monto}$</h4>
						</div>
					</div>
				</c:forEach>
			</main>
		</div>
	</main>
</body>

</html>