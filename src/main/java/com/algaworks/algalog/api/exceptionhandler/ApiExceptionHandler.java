package com.algaworks.algalog.api.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.algaworks.algalog.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algalog.domain.exception.NegocioException;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	private MessageSource messageSource;

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<Campo> campos = new ArrayList<>();

		for (ObjectError error : ex.getBindingResult().getAllErrors()) {

			String nome = ((FieldError) error).getField();
			String mensagem = messageSource.getMessage(error, LocaleContextHolder.getLocale());

			campos.add(new Campo(nome, mensagem));
		}
		var problema = new Problema();
		problema.setStatus(status.value());
		problema.setDataHora(OffsetDateTime.now());
		problema.setTitulo("Um ou mais campos estão inválidos. Faça o preenchimento correto!");
		problema.setCampos(campos);

		return handleExceptionInternal(ex, problema, headers, status, request);
	}

	@ExceptionHandler(NegocioException.class)
	public ResponseEntity<Object> handleNegocio(NegocioException negocioException, WebRequest request) {
		var httpStatus = HttpStatus.BAD_REQUEST;
		var problema = new Problema();
		problema.setStatus(httpStatus.value());
		problema.setDataHora(OffsetDateTime.now());
		problema.setTitulo(negocioException.getMessage());

		return handleExceptionInternal(negocioException, problema, new HttpHeaders(), httpStatus, request);
	}

	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	public ResponseEntity<Object> handleEntidadeNaoEncontrada(NegocioException negocioException, WebRequest request) {
		var httpStatus = HttpStatus.NOT_FOUND;
		var problema = new Problema();
		problema.setStatus(httpStatus.value());
		problema.setDataHora(OffsetDateTime.now());
		problema.setTitulo(negocioException.getMessage());

		return handleExceptionInternal(negocioException, problema, new HttpHeaders(), httpStatus, request);
	}

}
