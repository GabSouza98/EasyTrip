package io.github.cwireset.tcc.externalAPI;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "post",url = "https://some-random-api.ml/img/dog" )
public interface ClientFeign {

    @GetMapping
    PostDTO buscarLink();

}
