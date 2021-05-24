package com.algaworks.algalog.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.algaworks.algalog.api.model.OcorreciaModel;
import com.algaworks.algalog.domain.model.Ocorrencia;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class OcorrenciaAssembler {

	private ModelMapper modelMapper;

	public OcorreciaModel toModel(Ocorrencia ocorrencia) {
		return modelMapper.map(ocorrencia, OcorreciaModel.class);
	}

}
