package virt.server.service;

import io.vavr.control.Either;
import org.springframework.http.HttpStatus;
import virt.server.dto.Identifier;

import java.util.List;


/**
 * Entity service to perform CRUD operations
 *
 * @author Lukas.Oscipovsky
 */
public interface EntityService<T> {

    /*
     * Convert request DTO to entity and store. Return created id from entity
     */
    Identifier create(T zone);

    /*
     * Convert request DTO to entity and update based on id. Return either update id or HttpStatus
     */
    Either<HttpStatus, Identifier> update(T zone, Long id);

    /*
     * Return entity by given id converted to response DTO or HttpStatus
     */
    Either<HttpStatus, T> get(Long id);

    /*
     * Return all entities converted to reponse DTO list
     */
    List<T> getAll();

    /*
     * Delete entity based on id
     */
    Either<HttpStatus, Identifier> delete(Long id);
}
