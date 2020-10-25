package virt.server.service.impl;

import io.vavr.control.Either;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import virt.server.aspect.Logging;
import virt.server.data.api.repository.Dao;
import virt.server.dto.Identifier;
import virt.server.entities.AbstractEntity;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
public abstract class AbstractEntityService<T, R extends AbstractEntity> {

    private final ModelMapper modelMapper;

    private final Class<T> dtoType;
    private final Class<R> entityType;

    public AbstractEntityService(final ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.dtoType = ((Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
        this.entityType = ((Class<R>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1]);
    }

    @Logging
    public Identifier create(final T t) {
        final R r = this.modelMapper.map(t, this.entityType);

        this.getRepository().save(r);

        return new Identifier(r.getId());
    }

    @Logging
    public Either<HttpStatus, Identifier> update(final T t, final Long id) {
        final R r = this.modelMapper.map(t, this.entityType);

        r.setId(id);

        return this.getRepository().update(r).<Either<HttpStatus, Identifier>>map(d -> Either.right(new Identifier(d.getId())))
                .orElseGet(() -> Either.left(HttpStatus.NOT_FOUND));
    }

    @Logging
    public Either<HttpStatus, T> get(final Long id) {
        final Optional<R> device = this.getRepository().get(id);

        return device.<Either<HttpStatus, T>>map(d -> Either.right(this.modelMapper.map(d, this.dtoType)))
                .orElseGet(() -> Either.left(HttpStatus.NOT_FOUND));
    }

    @Logging
    public List<T> getAll() {
        final List<R> devices = this.getRepository().getAll();
        return devices.stream().map(d -> this.modelMapper.map(d, this.dtoType)).collect(Collectors.toList());
    }

    @Logging
    public Either<HttpStatus, Identifier> delete(final Long id) {
        final boolean deleted = this.getRepository().delete(id);

        return deleted ? Either.right(new Identifier(id)) : Either.left(HttpStatus.NOT_FOUND);
    }

    protected abstract Dao<R> getRepository();
}
