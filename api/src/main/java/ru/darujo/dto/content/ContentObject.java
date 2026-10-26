package ru.darujo.dto.content;

import java.util.List;

public class ContentObject implements ContentObjectInterface {
    public ContentObject(List<? extends ContentElement> contentInter) {
        this.content = contentInter;
    }

    public ContentObject() {
    }

    List<? extends ContentElement> content;

    public int getCount() {
        return content.stream().mapToInt(ContentElement::getCount).sum();
    }

    public List<?> getContent() {
        return content;
    }

}
