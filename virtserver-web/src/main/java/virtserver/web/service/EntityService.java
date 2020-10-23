package virtserver.web.service;

import io.vavr.control.Either;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import virtserver.web.dto.Id;


@Transactional
public interface EntityService<T, R> {

    Either<HttpStatus, Id> create(T zone);

    Either<HttpStatus, Id> update(T zone, Integer id);

    @Transactional(readOnly = true)
    Either<HttpStatus, R> get(Integer id);

    void delete(Integer id);
}
