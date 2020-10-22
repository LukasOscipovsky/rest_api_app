package virtserver.web.service;

import io.vavr.control.Either;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface EntityService<T, R> {

    Either<HttpStatus, Integer> create(R zone);

    Either<HttpStatus, Integer> update(R zone);

    @Transactional(readOnly = true)
    Either<HttpStatus, T> get(Integer id);

    HttpStatus delete(Integer id);
}
