package ru.darujo.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import ru.darujo.converter.DayInfoConverter;
import ru.darujo.model.DayInfo;
import ru.darujo.repository.DayInfoRepository;
import ru.darujo.specifications.Specifications;
import ru.darujo.utils.calendar.days.DateInfoRepository;
import ru.darujo.utils.calendar.structure.DateInfo;
import ru.darujo.utils.calendar.structure.DayType;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j

public class DayInfoService extends DateInfoRepository {
    private final DayInfoRepository dayInfoRepository;

    public DayInfoService(DayInfoRepository dayInfoRepository) {
        this.dayInfoRepository = dayInfoRepository;
    }

    final private Map<Integer, Map<LocalDate, DayInfo>> yearDays = new ConcurrentHashMap<>();

    @Override
    public synchronized void add(LocalDate date, DayType dayType, String title) {
        Specification<DayInfo> sp = Specification.unrestricted();
        sp = Specifications.eq(sp, "date", date);

        if (!dayInfoRepository.exists(sp)) {
            if (!isWeekEnd(date) || !dayType.equals(DayType.WEEK_END)) {
                dayInfoRepository.save(new DayInfo(null, date, dayType.toString(), title));
                deleteMapDate(date);
            }
        }
    }

    public synchronized void addNew(DayInfo dayInfo) {
        DayInfo daySave = dayInfoRepository.findFirstByDate(dayInfo.getDate());
        if (daySave == null) {
            if (!isWeekEnd(dayInfo.getDate()) || !dayInfo.getType().equals(DayType.WEEK_END.toString())) {
                dayInfoRepository.save(dayInfo);
                deleteMapDate(dayInfo.getDate());
            }
        } else {
            if (isWeekEnd(dayInfo.getDate()) && dayInfo.getType().equals(DayType.WEEK_END.toString())) {
                dayInfoRepository.delete(daySave);
                deleteMapDate(dayInfo.getDate());
            } else {
                daySave.setType(dayInfo.getType());
                daySave.setTitle(dayInfo.getTitle());
                dayInfoRepository.save(daySave);
                deleteMapDate(dayInfo.getDate());
            }
        }


    }

    private void deleteMapDate(LocalDate date) {
        yearDays.remove(date.getYear());
    }

    @Override
    @Transactional
    public DateInfo getDateInfo(LocalDate date) {
        return DayInfoConverter.getDateInfo(getDayInfo(date));
    }

    @Transactional
    public DayInfo getDayInfo(LocalDate date) {
        Map<LocalDate, DayInfo> days = yearDays.get(date.getYear());
        if (days == null) {
            days = getDateInfoYear(date.getYear());
        }
        return days.get(date);

    }

    private synchronized Map<LocalDate, DayInfo> getDateInfoYear(Integer year) {
        Map<LocalDate, DayInfo> days = yearDays.get(year);
        if (days != null) {
            return days;
        }
        addYear.accept(year);
        Specification<DayInfo> sp = Specification.unrestricted();
        sp = Specifications.ge(sp, "date", LocalDate.of(year, 1, 1));
        sp = Specifications.le(sp, "date", LocalDate.of(year, 12, 1));
        List<DayInfo> dayInfoList = dayInfoRepository.findAll(sp, Sort.by("date"));
        if (dayInfoList.isEmpty()) {
            return new HashMap<>();
        }
        Map<LocalDate, DayInfo> dateInfoMap = new HashMap<>();
        dayInfoList.forEach(dayInfo -> dateInfoMap.put(
                dayInfo.getDate(),
                dayInfo));
        yearDays.put(year, dateInfoMap);
        return dateInfoMap;
    }
}
