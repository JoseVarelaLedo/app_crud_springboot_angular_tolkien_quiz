package app.service;

import app.dto.FichaUsuarioDTO;
import app.model.FichaUsuario;
import app.model.Rol;
import app.model.Usuario;
import app.repository.FichaUsuarioRepository;
import app.repository.RolRepository;
import app.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class FichaUsuarioService {

    @Autowired
    private FichaUsuarioRepository fichaUsuarioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private static final Logger LOGGER = LoggerFactory.getLogger(FichaUsuarioService.class);

    public Page<FichaUsuarioDTO> listarFichasDeUsuarios(int pag, int tam, String campoOrdenacion, String direccionOrdenacion, String filtro) {
        Sort.Direction direccion  = direccionOrdenacion.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(pag, tam, Sort.by(direccion, campoOrdenacion));      // crear el objeto Pageable

        Page<FichaUsuario> fichasUsuarioPage;                                   // convertir la página de fichas de usuario desde el repositorio
        if (filtro == null || filtro.trim().isEmpty()) {
            fichasUsuarioPage = fichaUsuarioRepository.findAll(pageable);
        }else{
            fichasUsuarioPage = fichaUsuarioRepository.findAllByFilter(filtro, pageable);
        }

        return fichasUsuarioPage.map(fichaUsuario -> {          // convertir la página de entidades FichaUsuario a DTOs
            FichaUsuarioDTO dto = new FichaUsuarioDTO();
            dto.setId(fichaUsuario.getId());
            dto.setNombre(fichaUsuario.getNombre());
            dto.setApellidos(fichaUsuario.getApellidos());
            dto.setTelefono(fichaUsuario.getTelefono());
            dto.setDireccion(fichaUsuario.getDireccion());
            dto.setCorreoElectronico(fichaUsuario.getCorreoElectronico());
            dto.setNickname(fichaUsuario.getNickname());
            dto.setPassword(fichaUsuario.getPassword());
            dto.setScore(fichaUsuario.getScore());
            dto.setFechaNacimiento(fichaUsuario.getFechaNacimiento());
            dto.setFechaRegistro(fichaUsuario.getFechaRegistro());
            return dto;
        });
    }

    @Transactional
    public FichaUsuario crearFichasDeUsuarios(FichaUsuarioDTO fichaUsuarioDTO) {
        FichaUsuario fichaUsuario = new FichaUsuario();
        fichaUsuario.setNombre(fichaUsuarioDTO.getNombre());
        fichaUsuario.setApellidos(fichaUsuarioDTO.getApellidos());
        fichaUsuario.setTelefono(fichaUsuarioDTO.getTelefono());
        fichaUsuario.setDireccion(fichaUsuarioDTO.getDireccion());
        fichaUsuario.setCorreoElectronico(fichaUsuarioDTO.getCorreoElectronico());
        fichaUsuario.setNickname(fichaUsuarioDTO.getNickname());
        fichaUsuario.setScore(fichaUsuarioDTO.getScore());
        fichaUsuario.setPassword(passwordEncoder.encode(fichaUsuarioDTO.getPassword()));
        fichaUsuario.setFechaNacimiento(fichaUsuarioDTO.getFechaNacimiento());
        sincronizarCreacionEntidades(fichaUsuario);
        return fichaUsuarioRepository.save(fichaUsuario);
    }

    @Transactional
    public FichaUsuario crearFichasDeUsuarios(FichaUsuario fichaUsuario){
        return fichaUsuarioRepository.save(fichaUsuario);
    }

    public void sincronizarCreacionEntidadesPublic(FichaUsuario fichaUsuario) {
        this.sincronizarCreacionEntidades(fichaUsuario);
    }

    private void sincronizarCreacionEntidades(FichaUsuario fichaUsuario){
        Usuario usuario = Usuario.builder()
                .nickname(fichaUsuario.getNickname())
                .contrasena(fichaUsuario.getPassword())
                .fechaRegistro(LocalDateTime.now())
                .build();

        Rol rolUsuario = rolRepository.findByNombreRol("Usuario")
                .orElseThrow(() -> new RuntimeException("Rol 'Usuario' no encontrado en la base de datos"));
        usuario.setRol(rolUsuario);

        fichaUsuario.setUsuario(usuario);                // vincular ambos objetos
        usuario.setFichaUsuario(fichaUsuario);

        usuarioRepository.save(usuario);   // guardar ambas entidades (al ser transaccional, se guardan o fallan ambas)
    }

    public FichaUsuario obtenerFichasDeUsuariosPorId(Long id) {
        return fichaUsuarioRepository.findById(id).orElse(null);
    }

    @Transactional
    public FichaUsuario actualizarFichasDeUsuarios(Long id, FichaUsuarioDTO fichaUsuarioDTO) {

        Optional<FichaUsuario> fichaUsuarioOptional = fichaUsuarioRepository.findById(id);  // verificar si la ficha de usuario existe
        if (!fichaUsuarioOptional.isPresent()) {
            throw new RuntimeException("Ficha de usuario no encontrada");
        }

        FichaUsuario fichaUsuario = fichaUsuarioOptional.get();

        fichaUsuario.setNombre(fichaUsuarioDTO.getNombre());            // actualizar los campos de la ficha de usuario
        fichaUsuario.setApellidos(fichaUsuarioDTO.getApellidos());
        fichaUsuario.setTelefono(fichaUsuarioDTO.getTelefono());
        fichaUsuario.setDireccion(fichaUsuarioDTO.getDireccion());
        fichaUsuario.setCorreoElectronico(fichaUsuarioDTO.getCorreoElectronico());
        fichaUsuario.setNickname(fichaUsuarioDTO.getNickname());
        fichaUsuario.setPassword(passwordEncoder.encode(fichaUsuarioDTO.getPassword()));
        fichaUsuario.setScore(fichaUsuarioDTO.getScore());
        fichaUsuario.setFechaNacimiento(fichaUsuarioDTO.getFechaNacimiento());
        fichaUsuario.setFechaRegistro(fichaUsuarioDTO.getFechaRegistro());

        fichaUsuario.getUsuario().setNickname(fichaUsuario.getNickname());
        fichaUsuario.getUsuario().setContrasena(fichaUsuario.getPassword());

        return fichaUsuarioRepository.save(fichaUsuario);        // guardar la ficha de usurio actualizada
    }

    public void borrarEmpleado(Long id) {
        fichaUsuarioRepository.deleteById(id);
    }
}