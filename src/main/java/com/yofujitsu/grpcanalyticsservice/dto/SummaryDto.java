package com.yofujitsu.grpcanalyticsservice.dto;

import com.yofujitsu.grpcanalyticsservice.entity.summary.Summary;
import com.yofujitsu.grpcanalyticsservice.entity.data.ValueType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class SummaryDto {

    private Long heroId;
    private Map<ValueType, List<Summary.SummaryEntry>> values;

}
