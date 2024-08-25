package com.yofujitsu.grpcanalyticsservice.mapper;

import com.yofujitsu.grpcanalyticsservice.dto.SummaryDto;
import com.yofujitsu.grpcanalyticsservice.entity.summary.Summary;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SummaryMapper extends Mappable<Summary, SummaryDto>{
}
