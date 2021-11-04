package io.github.cwireset.tcc.repository;

import io.github.cwireset.tcc.domain.Usuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario,Integer> {

    Usuario findByEmailEquals(String email);
    Usuario findByCpfEquals(String cpf);
    List<Usuario> findAll();


}
