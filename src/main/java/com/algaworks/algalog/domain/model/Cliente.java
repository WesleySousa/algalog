package com.algaworks.algalog.domain.model;

import lombok.Data;

@Data
public class Cliente {
	private Long id;
	private String nome;
	private String telefone;
	private String email;
}
