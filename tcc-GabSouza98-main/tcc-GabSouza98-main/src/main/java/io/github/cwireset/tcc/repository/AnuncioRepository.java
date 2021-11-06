package io.github.cwireset.tcc.repository;

import io.github.cwireset.tcc.domain.Anuncio;
import io.github.cwireset.tcc.domain.Imovel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnuncioRepository extends CrudRepository<Anuncio,Integer> {

    Anuncio findByImovelId(Long idImovel);
    List<Anuncio> findAll();
    List<Anuncio> findByAtivoIsTrue();
    List<Anuncio> findByAnuncianteIdAndAtivoIsTrue(Long id);
    Anuncio findByImovelIdAndAtivoIsTrue(Long id);
    Optional<Anuncio> findById(Long id);
}
