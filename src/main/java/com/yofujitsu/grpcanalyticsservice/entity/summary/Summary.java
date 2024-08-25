package com.yofujitsu.grpcanalyticsservice.entity.summary;

import com.yofujitsu.grpcanalyticsservice.entity.data.ValueType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class Summary {

    private Long heroId;
    private Map<ValueType, List<SummaryEntry>> values;

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public static class SummaryEntry {
        private SummaryType type;
        private double value;
        private long counter;
    }

    public Summary() {
        this.values = new HashMap<>();
    }

    public void addValue(ValueType valueType, SummaryEntry value) {
        if(values.containsKey(valueType)) {
            List<SummaryEntry> entries = new ArrayList<>(values.get(valueType));
            entries.add(value);
            values.put(valueType, entries);
        } else {
            values.put(valueType, List.of(value));
        }
    }
}
