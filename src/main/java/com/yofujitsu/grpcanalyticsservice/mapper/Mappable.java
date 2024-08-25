package com.yofujitsu.grpcanalyticsservice.mapper;

public interface Mappable<E, D> {

    D toDto(E entity);
}
