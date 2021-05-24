package com.algaworks.algalog.api.model.input;

import javax.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class OcorrenciaInput {

	@NotBlank
	private String descricao;

}
