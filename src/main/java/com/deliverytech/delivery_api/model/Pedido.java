package com.deliverytech.delivery_api.model;

import com.deliverytech.delivery_api.enums.StatusPedido;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "pedido")
public class Pedido {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "endereco_entrega")
    private String enderecoEntrega;

    @Column(name = "numero_pedido")
    private String numeroPedido;

    @Column(name = "taxa_entrega")
    private BigDecimal taxaEntrega;

    @Column(name = "valor_total")
    private BigDecimal valorTotal;

    @Column(name = "data_pedido")
    private LocalDateTime dataPedido;

    @Enumerated(EnumType.STRING)
    private StatusPedido status;

    @PrePersist
    public void prePersist(){
        this.dataPedido = LocalDateTime.now();
        this.status = StatusPedido.PENDENTE;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    @JsonIgnore
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurante_id")
    @JsonIgnore
    private Restaurante restaurante;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ItemPedido> itens = new ArrayList<>();
}
