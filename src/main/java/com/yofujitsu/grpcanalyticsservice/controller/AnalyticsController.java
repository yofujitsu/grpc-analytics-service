package com.yofujitsu.grpcanalyticsservice.controller;

import com.yofujitsu.grpcanalyticsservice.dto.SummaryDto;
import com.yofujitsu.grpcanalyticsservice.entity.data.ValueType;
import com.yofujitsu.grpcanalyticsservice.entity.summary.Summary;
import com.yofujitsu.grpcanalyticsservice.entity.summary.SummaryType;
import com.yofujitsu.grpcanalyticsservice.mapper.SummaryMapper;
import com.yofujitsu.grpcanalyticsservice.service.summary.SummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final SummaryService summaryService;
    private final SummaryMapper summaryMapper;

    @GetMapping("/summary/{heroId}")
    public SummaryDto getSummary
            (@PathVariable long heroId,
                                 @RequestParam(value = "vt", required = false)
                                 Set<ValueType> valueTypes,
                                 @RequestParam(value = "st", required = false)
                                 Set<SummaryType> summaryTypes
            ) {
        Summary summary = summaryService.get(heroId, valueTypes, summaryTypes);
        return summaryMapper.toDto(summary);
    }
}
