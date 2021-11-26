package io.github.cwireset.tcc.repository;

import io.github.cwireset.tcc.domain.Anuncio;
import io.github.cwireset.tcc.domain.Imovel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnuncioRepository extends CrudRepository<Anuncio,Integer> {

    Anuncio findByImovelIdAndImovelAtivoIsTrue(Long idImovel);
    List<Anuncio> findAll();

    List<Anuncio> findByAtivoIsTrue();
    Page<Anuncio> findByAtivoIsTrue(Pageable pageable);
    List<Anuncio> findByAnuncianteIdAndAtivoIsTrue(Long id);
    Page<Anuncio> findByAnuncianteIdAndAtivoIsTrue(Long id, Pageable pageable);
    Anuncio findByImovelIdAndAtivoIsTrue(Long id);
    Optional<Anuncio> findByIdAndAtivoIsTrue(Long id);

}
