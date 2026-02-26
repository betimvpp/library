package com.livraria.repository;

import com.livraria.model.Venda;

import java.util.List;
import java.util.Optional;

public interface VendaRepository {

    Venda salvar(Venda venda);

    Optional<Venda> buscarPorId(int id);

    List<Venda> listarPorLivraria(int idLivraria);

    List<Venda> listarPorConsumidor(int idConsumidor);
}
