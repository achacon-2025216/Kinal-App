package com.angelchacon.kinalapp.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "ventas")
public class Venta {
    
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<DetalleVenta> detalles;

    @Id
    @Column(name = "codigo_venta")
    private Integer codigoVenta;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "fecha_venta", nullable = false)
    private LocalDate fechaVenta;

    @Column(nullable = false)
    private BigDecimal total;

    @Column
    private Integer estado;

    // RELACION CON CLIENTE
    @ManyToOne
    @JoinColumn(name = "clientes_dpi_cliente", referencedColumnName = "dpi_cliente")
    private Cliente cliente;

    // RELACION CON USUARIO
    @ManyToOne
    @JoinColumn(name = "usuario_codigo_usuario", referencedColumnName = "codigo")
    private Usuario usuario;


    public Venta() {}

    public Venta(Integer codigoVenta, LocalDate fechaVenta, BigDecimal total, Integer estado, Cliente cliente, Usuario usuario) {
        this.codigoVenta = codigoVenta;
        this.fechaVenta = fechaVenta;
        this.total = total;
        this.estado = estado;
        this.cliente = cliente;
        this.usuario = usuario;
    }

    // GETTERS Y SETTERS


    public Integer getCodigoVenta() {
        return codigoVenta;
    }

    public void setCodigoVenta(Integer codigoVenta) {
        this.codigoVenta = codigoVenta;
    }

    public LocalDate getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDate fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}