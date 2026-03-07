package com.taskmanager.taskmanager.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskSummary {

    private long total;
    private Map<String, Long> byStatus;
    private Map<String, Long> byPriority;
    private long overdue;
}
