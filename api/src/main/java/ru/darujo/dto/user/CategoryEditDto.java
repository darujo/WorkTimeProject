package ru.darujo.dto.user;

public class CategoryEditDto {

    private Long id;
    private String name;
    private Float amount;
    private Float amountFact;

    @SuppressWarnings("unused")
    public CategoryEditDto() {
    }

    public CategoryEditDto(Long id, String name, Float amount, Float amountFact) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.amountFact = amountFact;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Float getAmount() {
        return amount;
    }

    public Float getAmountFact() {
        return amountFact;
    }
}
