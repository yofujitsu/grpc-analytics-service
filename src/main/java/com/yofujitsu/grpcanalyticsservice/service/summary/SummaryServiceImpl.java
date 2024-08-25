package com.yofujitsu.grpcanalyticsservice.service.summary;

import com.yofujitsu.grpcanalyticsservice.entity.data.Data;
import com.yofujitsu.grpcanalyticsservice.entity.data.ValueType;
import com.yofujitsu.grpcanalyticsservice.entity.summary.Summary;
import com.yofujitsu.grpcanalyticsservice.entity.summary.SummaryType;
import com.yofujitsu.grpcanalyticsservice.exception.HeroNotFoundException;
import com.yofujitsu.grpcanalyticsservice.repository.SummaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class SummaryServiceImpl implements SummaryService {

    private final SummaryRepository summaryRepository;


    @Override
    public Summary get(long heroId, Set<ValueType> valueTypes, Set<SummaryType> summaryTypes) {
        return summaryRepository.get(heroId,
                valueTypes == null ? Set.of(ValueType.values()) : valueTypes,
                summaryTypes == null ? Set.of(SummaryType.values()) : summaryTypes)
                .orElseThrow(() -> new HeroNotFoundException("Not found hero: " + heroId));
    }

    @Override
    public void handle(Data data) {
        summaryRepository.handle(data);
    }
}
