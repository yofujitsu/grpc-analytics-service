package com.yofujitsu.grpcanalyticsservice.service.summary;

import com.yofujitsu.grpcanalyticsservice.entity.data.Data;
import com.yofujitsu.grpcanalyticsservice.entity.data.ValueType;
import com.yofujitsu.grpcanalyticsservice.entity.summary.Summary;
import com.yofujitsu.grpcanalyticsservice.entity.summary.SummaryType;

import java.util.Set;

public interface SummaryService {
    Summary get(long heroId, Set<ValueType> valueTypes, Set<SummaryType> summaryTypes);

    void handle(Data data);
}
