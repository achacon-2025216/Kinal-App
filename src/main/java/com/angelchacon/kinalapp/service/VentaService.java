package com.angelchacon.kinalapp.service;

import com.angelchacon.kinalapp.entity.Venta;
import com.angelchacon.kinalapp.entity.Cliente;
import com.angelchacon.kinalapp.entity.Usuario;
import com.angelchacon.kinalapp.repository.VentaRepository;
import com.angelchacon.kinalapp.repository.ClienteRepository;
import com.angelchacon.kinalapp.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
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
    @Transactional(readOnly = true)
    public List<Venta> listarTodos() {
        return ventaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Venta> buscarVentas(String termino) {
        return ventaRepository.buscarPorClienteOCodigo(termino);
    }

    @Override
    public Venta guardar(Venta venta) {
        if (venta.getCliente() == null || venta.getCliente().getDpiCliente() == null) {
            throw new RuntimeException("Error: Debe seleccionar un cliente.");
        }
        if (venta.getUsuario() == null || venta.getUsuario().getCodigo() == null) {
            throw new RuntimeException("Error: Debe seleccionar un usuario.");
        }

        Cliente cliente = clienteRepository.findById(venta.getCliente().getDpiCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Usuario usuario = usuarioRepository.findById(venta.getUsuario().getCodigo())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        venta.setCliente(cliente);
        venta.setUsuario(usuario);

        return ventaRepository.save(venta);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Venta> buscarPorCodigo(Integer codigo) {
        return ventaRepository.findById(codigo);
    }

    @Override
    public void eliminar(Integer codigo) {
        ventaRepository.deleteById(codigo);
    }
}