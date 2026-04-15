package com.angelchacon.kinalapp.service;

import com.angelchacon.kinalapp.entity.Venta;
import com.angelchacon.kinalapp.entity.Cliente;
import com.angelchacon.kinalapp.entity.Usuario;
import com.angelchacon.kinalapp.repository.VentaRepository;
import com.angelchacon.kinalapp.repository.ClienteRepository;
import com.angelchacon.kinalapp.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VentaService implements IVentaService {

    private final VentaRepository ventaRepository;
    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;

    public VentaService(VentaRepository ventaRepository,
                        ClienteRepository clienteRepository,
                        UsuarioRepository usuarioRepository) {

        this.ventaRepository = ventaRepository;
        this.clienteRepository = clienteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<Venta> listarTodos() {
        return ventaRepository.findAll();
    }

    @Override
    public Venta guardar(Venta venta) {
        // 1. Validar que la venta traiga un cliente y un usuario
        if (venta.getCliente() == null || venta.getCliente().getDpiCliente() == null) {
            throw new RuntimeException("Error: Debe seleccionar un cliente.");
        }
        if (venta.getUsuario() == null || venta.getUsuario().getCodigo() == null) {
            throw new RuntimeException("Error: Debe seleccionar un usuario.");
        }

        // 2. Buscar los objetos reales en la base de datos
        Cliente cliente = clienteRepository.findById(venta.getCliente().getDpiCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Usuario usuario = usuarioRepository.findById(venta.getUsuario().getCodigo())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 3. Asignarlos a la venta
        venta.setCliente(cliente);
        venta.setUsuario(usuario);

        return ventaRepository.save(venta);
    }

    @Override
    public Optional<Venta> buscarPorCodigo(Integer codigo) {
        return ventaRepository.findById(codigo);
    }

    @Override
    public void eliminar(Integer codigo) {
        ventaRepository.deleteById(codigo);
    }
}