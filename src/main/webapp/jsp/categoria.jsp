<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Mi Chauchera</title>
<link rel="stylesheet" href="./css/styleCategoria.css">
<script
	src="https://code.iconify.design/iconify-icon/1.0.7/iconify-icon.min.js"></script>

</head>

<body>
	<c:choose>
		<c:when test="${empty sessionScope.mes}">
			<c:set var="mes" value="-1" />
			<!-- Si el mes en la sesión está vacío, asigna -1 -->
		</c:when>
		<c:otherwise>
			<c:set var="mes" value="${sessionScope.mes}" />
			<!-- Si el mes en la sesión no está vacío, utiliza el valor de la sesión -->
		</c:otherwise>
	</c:choose>
	<header class="header_pagina_principal">
		<button class="boton boton2 usuario_shortcut">
			<img src="https://picsum.photos/30" alt="imagen de usuario">
			<p>Usuario</p>
		</button>
		<div>
			<a href="javascript:history.back()"> <iconify-icon class="icono"
					icon="carbon:view-filled" width="18"></iconify-icon> <span>Dashboard</span>
			</a>
		</div>
	</header>

	<div class="contenedor_form">
		<c:set var="categoria" value="${requestScope.categoria}" />
		<div class="encabezado">
			<h2> ${categoria.nombre}</h2>
			<p class='label_tipo'> ${categoria.tipo }</p>
			<c:choose>
				<c:when test="${categoria.tipo=='TRANSFERENCIA'}">
				</c:when>
				<c:otherwise>
					<p class='label_total'> ${total}</p>
				</c:otherwise>
			</c:choose>
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
								<p>${movimiento.origen.nombre}> ${movimiento.destino.nombre}</p>
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
</body>

</html>