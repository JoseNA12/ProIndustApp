-- phpMyAdmin SQL Dump
-- version 4.7.7
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost:3306
-- Tiempo de generación: 14-06-2018 a las 23:03:13
-- Versión del servidor: 10.2.12-MariaDB
-- Versión de PHP: 7.0.26

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `id5850606_proindustappbd`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Actividad`
--

CREATE TABLE `Actividad` (
  `idActividad` int(11) NOT NULL,
  `tipo` varchar(20) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Volcado de datos para la tabla `Actividad`
--

INSERT INTO `Actividad` (`idActividad`, `tipo`) VALUES
(1, 'PRODUCTIVA'),
(2, 'COLABORATIVA'),
(3, 'IMPRODUCTIVA');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Colaborador`
--

CREATE TABLE `Colaborador` (
  `idColaborador` int(11) NOT NULL,
  `pseudonimo` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  `descripcion` varchar(75) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Volcado de datos para la tabla `Colaborador`
--

INSERT INTO `Colaborador` (`idColaborador`, `pseudonimo`, `descripcion`) VALUES
(12, 'Colaborador 1', 'Descripción'),
(13, 'Colaborador 2', 'Descripción'),
(14, 'Colaborador 3', 'Descripción'),
(15, 'Colaborador 4', 'Descripción'),
(16, 'Colaborador 5', 'Descripción'),
(17, 'Colaborador 6', 'Descripción'),
(18, 'Colaborador 7', 'Descripción'),
(19, 'Colaborador 8', 'Descripción'),
(21, 'Colaborador prueba', 'descripcion');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `HorasLibres`
--

CREATE TABLE `HorasLibres` (
  `idHorasLibres` int(11) NOT NULL,
  `idProyecto` int(11) NOT NULL,
  `horaInicio` time NOT NULL,
  `horaFinal` time NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Volcado de datos para la tabla `HorasLibres`
--

INSERT INTO `HorasLibres` (`idHorasLibres`, `idProyecto`, `horaInicio`, `horaFinal`) VALUES
(23, 23, '12:00:00', '02:05:00'),
(24, 23, '12:25:00', '01:08:00'),
(25, 24, '23:59:00', '02:59:00');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Muestreo`
--

CREATE TABLE `Muestreo` (
  `idMuestreo` int(11) NOT NULL,
  `idProyecto` int(11) NOT NULL,
  `fechaInicio` date NOT NULL,
  `lapsoInicial` int(11) NOT NULL,
  `lapsoFinal` int(11) NOT NULL,
  `horaObservacion` time NOT NULL,
  `tiempoRecorrido` int(11) NOT NULL COMMENT 'minutos',
  `estado` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `descripcion` varchar(500) COLLATE utf8_unicode_ci NOT NULL,
  `cantObservRegistradas` int(11) NOT NULL,
  `cantObservRequeridas` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Volcado de datos para la tabla `Muestreo`
--

INSERT INTO `Muestreo` (`idMuestreo`, `idProyecto`, `fechaInicio`, `lapsoInicial`, `lapsoFinal`, `horaObservacion`, `tiempoRecorrido`, `estado`, `descripcion`, `cantObservRegistradas`, `cantObservRequeridas`) VALUES
(24, 26, '2018-06-04', 1, 2, '08:14:59', 1, 'EN CURSO', '', 1, 30);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Observacion`
--

CREATE TABLE `Observacion` (
  `idObservacion` int(11) NOT NULL,
  `comentario` varchar(500) COLLATE utf8_unicode_ci NOT NULL,
  `temperatura` int(11) NOT NULL,
  `humedad` int(11) NOT NULL,
  `fecha` date NOT NULL,
  `hora` time NOT NULL,
  `idUsuario` int(11) NOT NULL,
  `idColaborador` int(11) NOT NULL,
  `idOperacion` int(11) NOT NULL,
  `idTarea` int(11) NOT NULL,
  `idMuestreo` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Volcado de datos para la tabla `Observacion`
--

INSERT INTO `Observacion` (`idObservacion`, `comentario`, `temperatura`, `humedad`, `fecha`, `hora`, `idUsuario`, `idColaborador`, `idOperacion`, `idTarea`, `idMuestreo`) VALUES
(137, '', 24, 64, '2018-06-04', '08:12:33', 48, 21, 22, 22, 24),
(138, '', 25, 60, '2018-06-04', '08:22:07', 50, 21, 22, 22, 24);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Operacion`
--

CREATE TABLE `Operacion` (
  `idOperacion` int(11) NOT NULL,
  `nombre` varchar(25) COLLATE utf8_unicode_ci NOT NULL,
  `descripcion` longtext COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Volcado de datos para la tabla `Operacion`
--

INSERT INTO `Operacion` (`idOperacion`, `nombre`, `descripcion`) VALUES
(22, 'Operación 1', 'Descripción'),
(23, 'Operación 2', 'Descripción'),
(24, 'Operación 3', 'Descripción'),
(25, 'Operación 4', 'Descripción'),
(26, 'Operación 5', 'Descripción'),
(27, 'Operación 6', 'Descripción');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `OperacionTarea`
--

CREATE TABLE `OperacionTarea` (
  `idOperacionTarea` int(11) NOT NULL,
  `idOperacion` int(11) NOT NULL,
  `idTarea` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Volcado de datos para la tabla `OperacionTarea`
--

INSERT INTO `OperacionTarea` (`idOperacionTarea`, `idOperacion`, `idTarea`) VALUES
(33, 22, 19),
(34, 22, 20),
(36, 22, 22),
(37, 22, 23),
(38, 23, 23),
(39, 23, 24),
(40, 23, 25),
(41, 23, 26),
(43, 26, 25);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Proyecto`
--

CREATE TABLE `Proyecto` (
  `idProyecto` int(11) NOT NULL,
  `nombre` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `descripcion` varchar(120) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Volcado de datos para la tabla `Proyecto`
--

INSERT INTO `Proyecto` (`idProyecto`, `nombre`, `descripcion`) VALUES
(23, 'Proyecto 1', 'Descripción'),
(24, 'Proyecto 2', 'Descripción del proyecto 2'),
(25, 'Proyecto 3', 'Descripción'),
(26, 'Proyecto 4', 'Descripción');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ProyectoColaborador`
--

CREATE TABLE `ProyectoColaborador` (
  `idProyectoColaborador` int(11) NOT NULL,
  `idProyecto` int(11) NOT NULL,
  `idColaborador` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Volcado de datos para la tabla `ProyectoColaborador`
--

INSERT INTO `ProyectoColaborador` (`idProyectoColaborador`, `idProyecto`, `idColaborador`) VALUES
(37, 23, 12),
(38, 23, 13),
(40, 23, 15),
(42, 23, 16),
(43, 26, 21);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ProyectoOperacion`
--

CREATE TABLE `ProyectoOperacion` (
  `idProyectoOperacion` int(11) NOT NULL,
  `idProyecto` int(11) NOT NULL,
  `idOperacion` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Volcado de datos para la tabla `ProyectoOperacion`
--

INSERT INTO `ProyectoOperacion` (`idProyectoOperacion`, `idProyecto`, `idOperacion`) VALUES
(21, 23, 22),
(22, 23, 23),
(23, 24, 25),
(25, 25, 22),
(26, 26, 22);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ProyectoUsuario`
--

CREATE TABLE `ProyectoUsuario` (
  `idProyectoUsuario` int(11) NOT NULL,
  `idProyecto` int(11) NOT NULL,
  `idUsuario` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Volcado de datos para la tabla `ProyectoUsuario`
--

INSERT INTO `ProyectoUsuario` (`idProyectoUsuario`, `idProyecto`, `idUsuario`) VALUES
(17, 23, 50),
(19, 25, 50),
(20, 23, 55),
(21, 26, 50);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `RolUsuario`
--

CREATE TABLE `RolUsuario` (
  `idRolUsuario` int(11) NOT NULL,
  `rol` varchar(30) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Volcado de datos para la tabla `RolUsuario`
--

INSERT INTO `RolUsuario` (`idRolUsuario`, `rol`) VALUES
(1, 'ADMINISTRADOR'),
(2, 'ANALISTA');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Tarea`
--

CREATE TABLE `Tarea` (
  `idTarea` int(11) NOT NULL,
  `nombre` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `descripcion` varchar(120) COLLATE utf8_unicode_ci DEFAULT NULL,
  `idActividad` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Volcado de datos para la tabla `Tarea`
--

INSERT INTO `Tarea` (`idTarea`, `nombre`, `descripcion`, `idActividad`) VALUES
(19, 'Tarea 1', 'Descripción ', 1),
(20, 'Tarea 2', 'Descripción', 2),
(21, 'Tarea 3', 'Descripción ', 3),
(22, 'Tarea 4', 'Descripción', 2),
(23, 'Tarea 5', 'Descripción', 1),
(24, 'Tarea 6', 'Descripción', 3),
(25, 'Tarea 7', 'Descripción', 3),
(26, 'Tarea 8', 'Descripción', 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `TipoMuestreo`
--

CREATE TABLE `TipoMuestreo` (
  `idTipoMuestreo` int(11) NOT NULL,
  `nombre` varchar(20) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Volcado de datos para la tabla `TipoMuestreo`
--

INSERT INTO `TipoMuestreo` (`idTipoMuestreo`, `nombre`) VALUES
(1, 'PRELIMINAR'),
(2, 'DEFINITIVO');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Usuario`
--

CREATE TABLE `Usuario` (
  `idUsuario` int(11) NOT NULL,
  `nombre` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  `apellidos` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  `idRolUsuario` int(11) NOT NULL,
  `nombreUsuario` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `correoElectronico` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  `contrasenia` varchar(20) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Volcado de datos para la tabla `Usuario`
--

INSERT INTO `Usuario` (`idUsuario`, `nombre`, `apellidos`, `idRolUsuario`, `nombreUsuario`, `correoElectronico`, `contrasenia`) VALUES
(48, 'nombre admin', 'apellido admin', 1, 'Admin', 'correo@admin.com', '123'),
(50, 'Nombre Usuario', 'apellidos usuario', 2, 'Usuario1', 'usuario@correo.com', '123'),
(55, 'Nombre usuario', 'Apellidos usuario', 2, 'Usuario2', 'usuario@correo.com', '123'),
(56, 'Nombre usuario', 'Apellidos usuario', 2, 'Usuario3', 'usuario@correo.com', '123'),
(57, 'Nombre usuario', 'Apellidos usuario', 2, 'Usuario4', 'usuario@correo.com', '123');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `Actividad`
--
ALTER TABLE `Actividad`
  ADD PRIMARY KEY (`idActividad`);

--
-- Indices de la tabla `Colaborador`
--
ALTER TABLE `Colaborador`
  ADD PRIMARY KEY (`idColaborador`);

--
-- Indices de la tabla `HorasLibres`
--
ALTER TABLE `HorasLibres`
  ADD PRIMARY KEY (`idHorasLibres`),
  ADD KEY `HorasLibres_fk_Proyecto` (`idProyecto`);

--
-- Indices de la tabla `Muestreo`
--
ALTER TABLE `Muestreo`
  ADD PRIMARY KEY (`idMuestreo`),
  ADD KEY `Muestreo_fk_Proyecto` (`idProyecto`);

--
-- Indices de la tabla `Observacion`
--
ALTER TABLE `Observacion`
  ADD PRIMARY KEY (`idObservacion`),
  ADD KEY `Observacion_fk_Colaborador` (`idColaborador`),
  ADD KEY `Observacion_fk_Tarea` (`idTarea`),
  ADD KEY `Observacion_fk_Usuario` (`idUsuario`),
  ADD KEY `Observacion_fk_Operacion` (`idOperacion`),
  ADD KEY `Observacion_fk_Muestreo` (`idMuestreo`);

--
-- Indices de la tabla `Operacion`
--
ALTER TABLE `Operacion`
  ADD PRIMARY KEY (`idOperacion`);

--
-- Indices de la tabla `OperacionTarea`
--
ALTER TABLE `OperacionTarea`
  ADD PRIMARY KEY (`idOperacionTarea`),
  ADD KEY `OperacionTarea_fk_Operacion` (`idOperacion`),
  ADD KEY `OperacionTarea_fk_Tarea` (`idTarea`);

--
-- Indices de la tabla `Proyecto`
--
ALTER TABLE `Proyecto`
  ADD PRIMARY KEY (`idProyecto`);

--
-- Indices de la tabla `ProyectoColaborador`
--
ALTER TABLE `ProyectoColaborador`
  ADD PRIMARY KEY (`idProyectoColaborador`),
  ADD KEY `ProyectoColaborador_fk_Proyecto` (`idProyecto`),
  ADD KEY `ProyectoColaborador_fk_Colaborador` (`idColaborador`);

--
-- Indices de la tabla `ProyectoOperacion`
--
ALTER TABLE `ProyectoOperacion`
  ADD PRIMARY KEY (`idProyectoOperacion`),
  ADD KEY `ProyectoOperacion_fk_Proyecto` (`idProyecto`),
  ADD KEY `ProyectoOperacion_fk_Operacion` (`idOperacion`);

--
-- Indices de la tabla `ProyectoUsuario`
--
ALTER TABLE `ProyectoUsuario`
  ADD PRIMARY KEY (`idProyectoUsuario`),
  ADD KEY `ProyectoUsuario_fk_Proyecto` (`idProyecto`),
  ADD KEY `ProyectoUsuario_fk_Usuario` (`idUsuario`);

--
-- Indices de la tabla `RolUsuario`
--
ALTER TABLE `RolUsuario`
  ADD PRIMARY KEY (`idRolUsuario`);

--
-- Indices de la tabla `Tarea`
--
ALTER TABLE `Tarea`
  ADD PRIMARY KEY (`idTarea`),
  ADD KEY `Tarea_fk_Actividad` (`idActividad`);

--
-- Indices de la tabla `TipoMuestreo`
--
ALTER TABLE `TipoMuestreo`
  ADD PRIMARY KEY (`idTipoMuestreo`);

--
-- Indices de la tabla `Usuario`
--
ALTER TABLE `Usuario`
  ADD PRIMARY KEY (`idUsuario`),
  ADD KEY `Usuario_fk_RolUsuario` (`idRolUsuario`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `Actividad`
--
ALTER TABLE `Actividad`
  MODIFY `idActividad` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `Colaborador`
--
ALTER TABLE `Colaborador`
  MODIFY `idColaborador` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;

--
-- AUTO_INCREMENT de la tabla `HorasLibres`
--
ALTER TABLE `HorasLibres`
  MODIFY `idHorasLibres` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

--
-- AUTO_INCREMENT de la tabla `Muestreo`
--
ALTER TABLE `Muestreo`
  MODIFY `idMuestreo` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- AUTO_INCREMENT de la tabla `Observacion`
--
ALTER TABLE `Observacion`
  MODIFY `idObservacion` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=139;

--
-- AUTO_INCREMENT de la tabla `Operacion`
--
ALTER TABLE `Operacion`
  MODIFY `idOperacion` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;

--
-- AUTO_INCREMENT de la tabla `OperacionTarea`
--
ALTER TABLE `OperacionTarea`
  MODIFY `idOperacionTarea` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=44;

--
-- AUTO_INCREMENT de la tabla `Proyecto`
--
ALTER TABLE `Proyecto`
  MODIFY `idProyecto` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=42;

--
-- AUTO_INCREMENT de la tabla `ProyectoColaborador`
--
ALTER TABLE `ProyectoColaborador`
  MODIFY `idProyectoColaborador` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=44;

--
-- AUTO_INCREMENT de la tabla `ProyectoOperacion`
--
ALTER TABLE `ProyectoOperacion`
  MODIFY `idProyectoOperacion` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=27;

--
-- AUTO_INCREMENT de la tabla `ProyectoUsuario`
--
ALTER TABLE `ProyectoUsuario`
  MODIFY `idProyectoUsuario` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;

--
-- AUTO_INCREMENT de la tabla `RolUsuario`
--
ALTER TABLE `RolUsuario`
  MODIFY `idRolUsuario` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `Tarea`
--
ALTER TABLE `Tarea`
  MODIFY `idTarea` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;

--
-- AUTO_INCREMENT de la tabla `TipoMuestreo`
--
ALTER TABLE `TipoMuestreo`
  MODIFY `idTipoMuestreo` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `Usuario`
--
ALTER TABLE `Usuario`
  MODIFY `idUsuario` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=60;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `HorasLibres`
--
ALTER TABLE `HorasLibres`
  ADD CONSTRAINT `HorasLibres_fk_Proyecto` FOREIGN KEY (`idProyecto`) REFERENCES `Proyecto` (`idProyecto`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `Muestreo`
--
ALTER TABLE `Muestreo`
  ADD CONSTRAINT `Muestreo_fk_Proyecto` FOREIGN KEY (`idProyecto`) REFERENCES `Proyecto` (`idProyecto`);

--
-- Filtros para la tabla `Observacion`
--
ALTER TABLE `Observacion`
  ADD CONSTRAINT `Observacion_fk_Colaborador` FOREIGN KEY (`idColaborador`) REFERENCES `Colaborador` (`idColaborador`),
  ADD CONSTRAINT `Observacion_fk_Muestreo` FOREIGN KEY (`idMuestreo`) REFERENCES `Muestreo` (`idMuestreo`),
  ADD CONSTRAINT `Observacion_fk_Operacion` FOREIGN KEY (`idOperacion`) REFERENCES `Operacion` (`idOperacion`),
  ADD CONSTRAINT `Observacion_fk_Tarea` FOREIGN KEY (`idTarea`) REFERENCES `Tarea` (`idTarea`),
  ADD CONSTRAINT `Observacion_fk_Usuario` FOREIGN KEY (`idUsuario`) REFERENCES `Usuario` (`idUsuario`);

--
-- Filtros para la tabla `OperacionTarea`
--
ALTER TABLE `OperacionTarea`
  ADD CONSTRAINT `OperacionTarea_fk_Operacion` FOREIGN KEY (`idOperacion`) REFERENCES `Operacion` (`idOperacion`),
  ADD CONSTRAINT `OperacionTarea_fk_Tarea` FOREIGN KEY (`idTarea`) REFERENCES `Tarea` (`idTarea`);

--
-- Filtros para la tabla `ProyectoColaborador`
--
ALTER TABLE `ProyectoColaborador`
  ADD CONSTRAINT `ProyectoColaborador_fk_Colaborador` FOREIGN KEY (`idColaborador`) REFERENCES `Colaborador` (`idColaborador`),
  ADD CONSTRAINT `ProyectoColaborador_fk_Proyecto` FOREIGN KEY (`idProyecto`) REFERENCES `Proyecto` (`idProyecto`);

--
-- Filtros para la tabla `ProyectoOperacion`
--
ALTER TABLE `ProyectoOperacion`
  ADD CONSTRAINT `ProyectoOperacion_fk_Operacion` FOREIGN KEY (`idOperacion`) REFERENCES `Operacion` (`idOperacion`),
  ADD CONSTRAINT `ProyectoOperacion_fk_Proyecto` FOREIGN KEY (`idProyecto`) REFERENCES `Proyecto` (`idProyecto`);

--
-- Filtros para la tabla `ProyectoUsuario`
--
ALTER TABLE `ProyectoUsuario`
  ADD CONSTRAINT `ProyectoUsuario_fk_Proyecto` FOREIGN KEY (`idProyecto`) REFERENCES `Proyecto` (`idProyecto`),
  ADD CONSTRAINT `ProyectoUsuario_fk_Usuario` FOREIGN KEY (`idUsuario`) REFERENCES `Usuario` (`idUsuario`);

--
-- Filtros para la tabla `Tarea`
--
ALTER TABLE `Tarea`
  ADD CONSTRAINT `Tarea_fk_Actividad` FOREIGN KEY (`idActividad`) REFERENCES `Actividad` (`idActividad`);

--
-- Filtros para la tabla `Usuario`
--
ALTER TABLE `Usuario`
  ADD CONSTRAINT `Usuario_fk_RolUsuario` FOREIGN KEY (`idRolUsuario`) REFERENCES `RolUsuario` (`idRolUsuario`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
