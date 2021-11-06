package io.github.cwireset.tcc.repository;

import io.github.cwireset.tcc.domain.Imovel;
import io.github.cwireset.tcc.domain.Usuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImovelRepository extends CrudRepository<Imovel,Integer> {

    List<Imovel> findAll();
    Optional<Imovel> findById(Long id);
    List<Imovel> findByProprietarioId(Long id);

}
