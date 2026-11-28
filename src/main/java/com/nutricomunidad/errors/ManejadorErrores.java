package com.nutricomunidad.errors;

import com.nutricomunidad.dto.Mensaje;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * Manejo centralizado de errores. Todas las respuestas de error tienen la forma
 * {"mensaje": "..."} y nunca exponen detalles internos (SQL, trazas, etc.).
 */
@RestControllerAdvice
public class ManejadorErrores {

    private static final Logger log = LoggerFactory.getLogger(ManejadorErrores.class);

    private static final String FK_VIOLADA = "23503";
    private static final String NO_NULO = "23502";
    private static final String CHECK_VIOLADO = "23514";

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Mensaje> manejar(ResponseStatusException e) {
        return responder(HttpStatus.valueOf(e.getStatusCode().value()), e.getReason());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Mensaje> jsonInvalido() {
        return responder(HttpStatus.BAD_REQUEST,
                "El cuerpo de la petición no es un JSON válido o tiene tipos de dato incorrectos");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Mensaje> datosInvalidos(MethodArgumentNotValidException e) {
        String mensaje = e.getBindingResult().getAllErrors().stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("Datos inválidos");
        return responder(HttpStatus.BAD_REQUEST, mensaje);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<Mensaje> parametrosInvalidos(HandlerMethodValidationException e) {
        String mensaje = e.getAllErrors().stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("Parámetros inválidos");
        return responder(HttpStatus.BAD_REQUEST, mensaje);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Mensaje> tipoInvalido(MethodArgumentTypeMismatchException e) {
        return responder(HttpStatus.BAD_REQUEST, "El parámetro '" + e.getName() + "' tiene un formato inválido");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Mensaje> parametroFaltante(MissingServletRequestParameterException e) {
        return responder(HttpStatus.BAD_REQUEST, "Falta el parámetro '" + e.getParameterName() + "'");
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Mensaje> rutaNoEncontrada() {
        return responder(HttpStatus.NOT_FOUND, "La ruta solicitada no existe");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Mensaje> metodoNoPermitido(HttpRequestMethodNotSupportedException e) {
        return responder(HttpStatus.METHOD_NOT_ALLOWED, "El método " + e.getMethod() + " no está permitido en esta ruta");
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<Mensaje> tipoContenidoNoSoportado() {
        return responder(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "El Content-Type debe ser application/json");
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Mensaje> duplicado() {
        return responder(HttpStatus.CONFLICT, "Ya existe un registro con esos datos");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Mensaje> integridad(DataIntegrityViolationException e) {
        String estadoSql = estadoSql(e);
        if (FK_VIOLADA.equals(estadoSql)) {
            return responder(HttpStatus.CONFLICT,
                    "No se puede completar la operación: el registro está relacionado con otros registros");
        }
        if (NO_NULO.equals(estadoSql) || CHECK_VIOLADO.equals(estadoSql)) {
            return responder(HttpStatus.BAD_REQUEST, "Uno o más valores no cumplen las restricciones de la base de datos");
        }
        return responder(HttpStatus.CONFLICT, "La operación viola una restricción de integridad de los datos");
    }

    @ExceptionHandler(DataAccessResourceFailureException.class)
    public ResponseEntity<Mensaje> baseNoDisponible(DataAccessResourceFailureException e) {
        log.error("No fue posible conectar con la base de datos", e);
        return responder(HttpStatus.SERVICE_UNAVAILABLE, "La base de datos no está disponible, intenta más tarde");
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Mensaje> errorBaseDatos(DataAccessException e) {
        log.error("Error de acceso a datos", e);
        return responder(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error al acceder a la base de datos");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Mensaje> errorInesperado(Exception e) {
        log.error("Error inesperado", e);
        return responder(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado en el servidor");
    }

    private static ResponseEntity<Mensaje> responder(HttpStatus estado, String mensaje) {
        return ResponseEntity.status(estado).body(new Mensaje(mensaje));
    }

    private static String estadoSql(Throwable e) {
        Throwable causa = NestedExceptionUtils.getMostSpecificCause(e);
        return causa instanceof SQLException sql ? sql.getSQLState() : null;
    }
}
