package ru.darujo.integration;

import org.springframework.stereotype.Component;


@Component
public class BasketServiceIntegration {
//    private WebClient webClientBasket;
//
//    @Autowired
//    public void setWebClientBasket(WebClient webClientBasket) {
//        this.webClientBasket = webClientBasket;
//    }
//
//    public BasketDto getBasket() {
//        return webClientBasket.get()
//                .retrieve()
//                .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
//                        clientResponse -> Mono.error(new ResourceNotFoundException("Корзина не найдена")))
//                .bodyToMono(BasketDto.class)
//                .block();
//    }
//
//    public void clearBasket() {
//        webClientBasket
//                .get()
//                .uri("/clear")
//                .retrieve()
//                .toBodilessEntity()
//                .block();
//    }
}
