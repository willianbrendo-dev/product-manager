package com.wb.product_manager.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
/**
 * Anotações do Lombok para reduzir código repetitivo:
 * @Data: Cria automaticamente os métodos getters, setters, toString, equals e hashCode.
 * @NoArgsConstructor: Cria um construtor vazio (sem argumentos), que é exigido pelo JPA.
 * @AllArgsConstructor: Cria um construtor com todos os campos da classe como argumentos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    /**
     * @GeneratedValue: Define a estratégia de geração da chave primária.
     * GenerationType.IDENTITY: Diz ao banco de dados que ele é responsável por
     * gerar e incrementar o valor do ID automaticamente. É a forma mais comum
     * para bancos como PostgreSQL e MySQL.
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    /**
     * @Column: Usado para customizar o mapeamento da coluna.
     * unique = true: Garante que não haverá dois produtos com o mesmo nome.
     * nullable = false: Torna o preenchimento deste campo obrigatório.
     */
    @Column(unique = true, nullable = false)
    private String name;

    /**
     * nullable = false: O preço também é obrigatório.
     * precision = 10, scale = 2: Define a precisão do número.
     * `precision` é o total de dígitos e `scale` é o número de dígitos após a vírgula.
     * Ex: 12345678.90
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price; // Usamos BigDecimal para dinheiro, pois evita problemas de arredondamento.

    @Column(nullable = false)
    private Integer quantity;

}
