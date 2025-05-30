package com.example.golf.service.impl;

import com.example.golf.dtos.event.request.EventForUserRequest;
import com.example.golf.dtos.event.response.EventResponse;
import com.example.golf.exception.AppException;
import com.example.golf.enums.ErrorResponse;
import com.example.golf.service.BaseService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public abstract class BaseServiceImpl<T, ID> implements BaseService<T, ID> {
    protected final JpaRepository<T, ID> repository;
    public BaseServiceImpl(JpaRepository<T, ID> repository ) {
        this.repository = repository;
    }

    @Override
    public <Req, Res> Res save(Req request, Class<Res> responseType) {
        T entity = convertToEntity(request);
        repository.save(entity);
        return convertToResponse(entity, responseType);
    }

    public <Req, Res> String saveAll(List<Req> request) {
        List<T> list = request.stream()
                .map(this::convertToEntity)
                .toList();
        repository.saveAll(list);
        return "Success";
    }

    @Override
    public <Req, Res> Res update(ID id, Req request, Class<Res> responseType) {
        Optional<T> optionalEntity = repository.findById(id);
        if (optionalEntity.isEmpty())
            throw new AppException(ErrorResponse.ENTITY_NOT_EXISTED);
        T entity = convertToEntity(request);
        repository.save(entity);
        return convertToResponse(entity, responseType);
    }

    @Override
    public void delete(ID id) {
        repository.deleteById(id);
    }

    @Override
    public <Res> Res getById(ID id, Class<Res> responseType) {
        T entity = repository.findById(id)
                .orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));
        return convertToResponse(entity, responseType);
    }
    @Override
    public T findById(ID id) {
        return repository.findById(id)
                .orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));
    }

    @Override
    public <Res> List<Res> findAll(Class<Res> responseType) {
        return repository.findAll().stream()
                .map(entity -> convertToResponse(entity, responseType))
                .toList();
    }

    @Override
    public void save(T entity) {
        repository.save(entity);
    }
    protected abstract <Req> T convertToEntity(Req request);
    protected abstract <Res> Res convertToResponse(T entity, Class<Res> responseType);


}
