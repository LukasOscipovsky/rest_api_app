package virt.server.service;

import io.vavr.control.Either;
import org.springframework.http.HttpStatus;
import virt.server.dto.Id;

import java.util.List;


public interface EntityService<T> {

    Id create(T zone);

    Either<HttpStatus, Id> update(T zone, Long id);

    Either<HttpStatus, T> get(Long id);

    List<T> getAll();

    Either<HttpStatus, Id> delete(Long id);
}
