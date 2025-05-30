package com.example.golf.service;


import java.util.List;

public interface BaseService<T, ID> {
    <Req, Res> Res save(Req request, Class<Res> responseType);
    <Req, Res> String saveAll(List<Req> request);
    <Req, Res> Res update(ID id, Req request, Class<Res> responseType);
    void delete(ID id);
    <Res> Res getById(ID id, Class<Res> responseType);
    <Res> List<Res> findAll(Class<Res> responseType);
    T findById(ID id);
    void save(T entity);

}
