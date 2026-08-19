package ru.darujo.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.*;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.util.Assert;
import ru.darujo.dto.workperiod.GetList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EmberPageImpl<R, T extends GetList<R>> extends RepresentationModel<EmberPageImpl<R, T>> implements Page<R> {
    @Getter
    private final Pageable pageable;

    private Integer totalPages;

    //    @Override
    public @NonNull <U> Page<U> map(@NonNull Function<? super R, ? extends U> converter) {
        return new PageImpl<>(getConvertedContent(converter), getPageable(), totalPages);
    }

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public EmberPageImpl(
            @JsonProperty("_embedded") T content,
            @JsonProperty("page") EmberPageDto page,
            @JsonProperty("_links") Map<String, LinkDto> links) {

        this(content == null ? new ArrayList<>() : content.getList(), PageRequest.of(page.getNumber() == null ? 0 : page.getNumber(), page.getSize() != null && page.getSize() >= 1 ? page.getSize() : 1),
                page.getTotalElements() != null ? page.getTotalElements() : 0);
        this.totalPages = page.getTotalPages();
        links.forEach((name, linkDto) -> add(Link.of(linkDto.getHref(), LinkRelation.of(name))));


    }


    private final Long totalElements;

    @Override
    public long getTotalElements() {
        return totalElements;
    }

    private final List<R> content;

    public EmberPageImpl(List<R> content, Pageable pageable, Long totalElements) {
        this.pageable = pageable;
        this.totalElements = pageable.toOptional().filter(it -> !content.isEmpty()) //
                .filter(it -> it.getOffset() + it.getPageSize() > totalElements)//
                .map(it -> it.getOffset() + content.size())//
                .orElse(totalElements);
        this.content = content;
//        super(content, pageable, total);
    }

    protected <U> List<U> getConvertedContent(Function<? super R, ? extends U> converter) {

        Assert.notNull(converter, "Function must not be null");

        return this.content.stream().map(converter).collect(Collectors.toList());
    }

    @Override
    public int getTotalPages() {
        return totalPages;
    }

    @Override
    public int getNumber() {
        return 0;
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public int getNumberOfElements() {
        return 0;
    }

    @Override
    public @NonNull List<R> getContent() {
        return content;
    }

    @Override
    public boolean hasContent() {
        return false;
    }

    @Override
    public @NonNull Sort getSort() {
        return Sort.unsorted();
    }

    @Override
    public boolean isFirst() {
        return getNumber() == 0;
    }

    @Override
    public boolean isLast() {
        return getNumber() == totalPages;
    }

    @Override
    public boolean hasNext() {
        return getNumber() + 1 < getTotalPages();
    }

    @Override
    public boolean hasPrevious() {
        return getNumber() > 0;
    }

    @Override
    public @NonNull Pageable nextPageable() {
        return hasNext() ? pageable.next() : Pageable.unpaged();
    }


    @Override
    public @NonNull Pageable previousPageable() {
        return hasPrevious() ? pageable.previousOrFirst() : Pageable.unpaged();
    }


    @Override
    public @NonNull Iterator<R> iterator() {
        return content.iterator();
    }
}
