package com.uidea.rrhh.dao;

import com.uidea.rrhh.model.Funcionario;

import java.util.List;
import java.util.Optional;

public interface FuncionarioDAO {
    List<Funcionario> findAll();
    Optional<Funcionario> findById(int id);
    Optional<Funcionario> findByNumeroIdentificacion(String numeroIdentificacion);
    Funcionario create(Funcionario funcionario);
    void update(Funcionario funcionario);
    void delete(int id);
}


