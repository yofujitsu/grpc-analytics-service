package com.yofujitsu.grpcanalyticsservice.repository;

import com.yofujitsu.grpcanalyticsservice.entity.data.Data;
import com.yofujitsu.grpcanalyticsservice.entity.data.ValueType;
import com.yofujitsu.grpcanalyticsservice.entity.summary.Summary;
import com.yofujitsu.grpcanalyticsservice.entity.summary.SummaryType;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;


public interface SummaryRepository {
    Optional<Summary> get(
            Long heroId,
            Set<ValueType> valueTypes,
            Set<SummaryType> summaryTypes
    );

    void handle(Data data);
}
