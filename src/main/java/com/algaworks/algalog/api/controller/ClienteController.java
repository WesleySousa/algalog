package com.algaworks.algalog.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algalog.api.assembler.ClienteAssembler;
import com.algaworks.algalog.api.model.ClienteModel;
import com.algaworks.algalog.api.model.input.ClienteInput;
import com.algaworks.algalog.domain.repository.ClienteRepository;
import com.algaworks.algalog.domain.service.CatalogoClienteService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/clientes")
public class ClienteController {

	private ClienteRepository clienteRepository;
	private CatalogoClienteService catalogoClienteService;
	private ClienteAssembler clienteAssembler;

	@GetMapping
	public List<ClienteModel> listar() {
		return clienteAssembler.toCollectionModel(clienteRepository.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<ClienteModel> buscarPorId(@PathVariable Long id) {		
		return clienteRepository.findById(id)
				.map(cliente -> ResponseEntity.ok(clienteAssembler.toModel(cliente)))
				.orElse(ResponseEntity.notFound().build());
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public ClienteModel adicionar(@Valid @RequestBody ClienteInput clienteInput) {
		var novoCliente = clienteAssembler.toEntity(clienteInput);
		return clienteAssembler.toModel(catalogoClienteService.salvar(novoCliente));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ClienteModel> atualizar(@PathVariable Long id, @Valid @RequestBody ClienteInput clienteInput) {
		if (!clienteRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		var cliente = clienteAssembler.toEntity(clienteInput);
		cliente.setId(id);
		cliente = catalogoClienteService.salvar(cliente);
		return ResponseEntity.ok(clienteAssembler.toModel(cliente));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluir(@PathVariable Long id) {
		if (!clienteRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		catalogoClienteService.excluir(id);
		return ResponseEntity.noContent().build();
	}
}
