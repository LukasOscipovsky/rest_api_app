package virtserver.web.service;

import io.vavr.control.Either;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface EntityService<T> {

    Either<HttpStatus, Integer> create(T zone);

    Either<HttpStatus, Integer> update(T zone);

    @Transactional(readOnly = true)
    Either<HttpStatus, T> get(Integer id);

    HttpStatus delete(Integer id);
}
