package com.angelchacon.kinalapp.config;

import com.angelchacon.kinalapp.service.IClienteService;
import com.angelchacon.kinalapp.service.IDetalleVentaService;
import com.angelchacon.kinalapp.service.IProductoService;
import com.angelchacon.kinalapp.service.IVentaService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class DataLoader implements CommandLineRunner {

    private final IVentaService ventaService;
    private final IProductoService productoService;
    private final IClienteService clienteService;
    private final IDetalleVentaService detalleService;

    public DataLoader(IVentaService v, IProductoService p, IClienteService c, IDetalleVentaService d) {
        this.ventaService = v;
        this.productoService = p;
        this.clienteService = c;
        this.detalleService = d;
    }

    @Override
    public void run(String... args) throws Exception {
        // 1. Verificamos qué tenemos en la DB
        long conteoClientes = clienteService.listarTodos().size();
        boolean hayProductos = !productoService.listarTodos().isEmpty();
        boolean hayVentas = !ventaService.listarTodos().isEmpty();

        System.out.println("Estado de la DB: Clientes: " + conteoClientes);

        // 2. SOLO CREAR PRODUCTOS SI NO HAY
        if (!hayProductos) {
            System.out.println("Creando productos...");
            // ... (aquí va tu código de crear 1000 productos)
        }

        // 3. SOLO CREAR VENTAS SI NO HAY
        if (!hayVentas && conteoClientes > 0 && hayProductos) {
            System.out.println("Creando ventas vinculadas a los clientes existentes...");
            // ... (aquí va tu código para crear las 1000 ventas)
        }
    }
}
 // Solo dos llaves al final: una para el método y otra para la clase.