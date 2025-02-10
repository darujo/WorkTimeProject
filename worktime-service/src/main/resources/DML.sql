
INSERT INTO productrepo.product
(price, title)
values
    (999.99, 'Продукт №1'),
    (1100  , 'Продукт №2'),
    (300   , 'Продукт №3');
select * from productrepo.product ;

INSERT INTO productrepo.buyer
("name")
values
    ('Покупатель № 1'),
    ('Покупатель № 2'),
    ('Покупатель № 3'),
    ('Покупатель № 4')
;
select * from productrepo.buyer  ;

INSERT INTO productrepo.cheque
(datatime, buyer_id)
VALUES('2022-11-23 10:34:06', 1);

INSERT INTO productrepo.chequeline
(price, quantity, cheque_id, product_id)
values
    (100, 10, 1, 1),
    (300,  5, 1, 2);


INSERT INTO productrepo.cheque
(datatime, buyer_id)
VALUES('2022-11-23 10:54:06', 2);

INSERT INTO productrepo.chequeline
(price, quantity, cheque_id, product_id)
values
    (500, 10, 2, 3),
    (200,  5, 2, 2);

INSERT INTO productrepo.cheque
(datatime, buyer_id)
VALUES('2022-11-23 10:34:06', 1);

INSERT INTO productrepo.chequeline
(price, quantity, cheque_id, product_id)
values
    (120, 10, 3, 1),
    (300,  5, 3, 3);

select * from productrepo.cheque ;

select * from productrepo.chequeline ;

