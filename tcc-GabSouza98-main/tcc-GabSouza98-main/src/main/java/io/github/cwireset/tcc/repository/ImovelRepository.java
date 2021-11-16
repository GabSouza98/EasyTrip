package io.github.cwireset.tcc.repository;

import io.github.cwireset.tcc.domain.Imovel;
import io.github.cwireset.tcc.domain.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImovelRepository extends CrudRepository<Imovel,Integer> {

    List<Imovel> findAll();
    List<Imovel> findByAtivoIsTrue();
    Page<Imovel> findByAtivoIsTrue(Pageable pageable);

    Optional<Imovel> findByIdAndAtivoIsTrue(Long id);
    List<Imovel> findByProprietarioIdAndAtivoIsTrue(Long id);
    Page<Imovel> findByProprietarioIdAndAtivoIsTrue(Long id, Pageable pageable);

}
