package app.service;

import app.dto.UsuarioDTO;

import app.exceptions.ResourceNotFoundException;
import app.model.FichaUsuario;
import app.model.Rol;
import app.model.Usuario;
import app.repository.FichaUsuarioRepository;
import app.repository.RolRepository;
import app.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService implements UserDetailsService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private FichaUsuarioRepository fichaUsuarioRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private static final Logger LOGGER = LoggerFactory.getLogger(UsuarioService.class);


    public Page<UsuarioDTO> listarUsuarios(int pag, int tam, String campoOrdenacion, String direccionOrdenacion) {
        Sort.Direction direccion = direccionOrdenacion.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pag, tam, Sort.by(direccion, campoOrdenacion));
        Page<Usuario> usuariosPage = usuarioRepository.findAll(pageable);

        return usuariosPage.map(usuario -> {
            UsuarioDTO dto = new UsuarioDTO();
            dto.setId(usuario.getId());
            dto.setNickname(usuario.getNickname());
            dto.setContrasena(usuario.getContrasena());
            dto.setFechaRegistro(usuario.getFechaRegistro());
            dto.setFichaUsuarioId(dto.getFichaUsuarioId());

            if (usuario.getFichaUsuario() != null) {
                dto.setFichaUsuarioId(usuario.getFichaUsuario().getId());
            }

            if (usuario.getRol() != null) {                                       // imprescindible verificar si el rol es nulo
                dto.setFichaUsuarioId(usuario.getFichaUsuario().getId());
                dto.setRolID(usuario.getRol().getId());
                dto.setNombreRol(usuario.getRol().getNombreRol());
            } else {
                dto.setRolID(3);
                dto.setNombreRol("Usuario");
            }

            return dto;
        });
    }

    public Page<UsuarioDTO> buscarUsuarioPorRolId(int pag, int tam, String campoOrdenacion, String direccionOrdenacion, Integer rolId) {
        Sort.Direction direccion = direccionOrdenacion.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pag, tam, Sort.by(direccion, campoOrdenacion));
        Page<Usuario> usuariosPage = usuarioRepository.findByRolId(rolId, pageable);
        List<UsuarioDTO> usuarioDTOs = usuariosPage.getContent().stream()
                .map(usuario -> new UsuarioDTO(usuario.getId(), usuario.getNickname(), usuario.getContrasena(),
                        usuario.getRol().getId(), usuario.getRol().getNombreRol(),
                        usuario.getFichaUsuario() != null ? usuario.getFichaUsuario().getId() : null,
                        usuario.getFechaRegistro()))
                .collect(Collectors.toList());

        return new PageImpl<>(usuarioDTOs, pageable, usuariosPage.getTotalElements());
    }

    public Optional<UsuarioDTO> buscarUsuarioPorNickName(String nickname) {
        return usuarioRepository.findByNickname(nickname).map(usuario -> new UsuarioDTO(
                usuario.getId(),
                usuario.getNickname(),
                usuario.getContrasena(),
                usuario.getRol() != null ? usuario.getRol().getId() : null,
                usuario.getRol() != null ? usuario.getRol().getNombreRol() : null,
                usuario.getFichaUsuario() != null ? usuario.getFichaUsuario().getId() : null,
                usuario.getFechaRegistro()
        ));
    }

    // necesario para el paquete de seguridad
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByNickname(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().getNombreRol());         // convertir el único rol del usuario en una autoridad

        return new org.springframework.security.core.userdetails.User(                                                  // crear y devolver el UserDetails con el rol único
                usuario.getNickname(),
                usuario.getContrasena(),
                Collections.singletonList(authority)                                                                    // usa singletonList para un solo rol
        );
    }

    public Page<UsuarioDTO> buscarUsuariosPorFecha(LocalDateTime fechaFiltro, int page, int size, String sortField, String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        return usuarioRepository.findByFechaRegistroAfter(fechaFiltro, pageable).map(usuario -> {
            UsuarioDTO dto = new UsuarioDTO();
            dto.setId(usuario.getId());
            dto.setNickname(usuario.getNickname());
            dto.setContrasena(usuario.getContrasena());
            dto.setFechaRegistro(usuario.getFechaRegistro());
            if (usuario.getRol() != null) {
                dto.setFichaUsuarioId(usuario.getFichaUsuario().getId());
                dto.setRolID(usuario.getRol().getId());
                dto.setNombreRol(usuario.getRol().getNombreRol());
            } else {
                dto.setRolID(null);
                dto.setNombreRol(null);
            }
            return dto;
        });
    }

    @Transactional
    public Usuario crearUsuario(UsuarioDTO usuarioDTO) {
        Optional<Rol> rolOptional = rolRepository.findById(usuarioDTO.getRolID());
        if (rolOptional.isEmpty()) {
           usuarioDTO.setRolID(3);
        }
        Rol rol = rolOptional.get();

        Usuario usuario = new Usuario();
        usuario.setNickname(usuarioDTO.getNickname());
        usuario.setContrasena(passwordEncoder.encode(usuarioDTO.getContrasena()));
        usuario.setFechaRegistro(usuarioDTO.getFechaRegistro());
        usuario.setRol(rol); // Asocia el rol
        sincronizarCreacionEntidades(usuario);
        return usuarioRepository.save(usuario);
    }

    // sobrecarga para crear usuarios usando un objeto Usuario en lugar de un DTO
    @Transactional
    public Usuario crearUsuario (Usuario usuario){
        return usuarioRepository.save(usuario);
    }

    private void sincronizarCreacionEntidades(Usuario usuario){
        FichaUsuario fichaUsuario = FichaUsuario.builder()
                .nickname(usuario.getNickname())
                .password(usuario.getContrasena())
                .correoElectronico(usuario.getNickname() + "@emptymail.emp")
                .nombre("  ")                                                     // valores predeterminados
                .apellidos("   ")
                .telefono("   ")
                .direccion("   ")
                .score(0L)
                .fechaRegistro(LocalDateTime.now())
                .build();
        usuario.setFichaUsuario(fichaUsuario);                                  // vincular ambos objetos
        fichaUsuario.setUsuario(usuario);

        fichaUsuarioRepository.save(fichaUsuario);                                // guardar ambas entidades (al ser transaccional, se guardan o fallan ambas)
    }


    public boolean existsByNickname(String nickname) {
        return usuarioRepository.existsByNickname(nickname);
    }

    public Usuario obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    @Transactional
    public Usuario actualizarUsuario(Long id, UsuarioDTO datosActualizados) throws ResourceNotFoundException {
        Usuario usuarioExistente = obtenerUsuarioPorId(id);
        usuarioExistente.setNickname(Optional.ofNullable(datosActualizados.getNickname()).orElse(usuarioExistente.getNickname()));      // asignar valores del DTO o mantener los existentes

        if (datosActualizados.getContrasena() != null) {
            boolean contrasenaCoincide = datosActualizados.getContrasena().equals(usuarioExistente.getContrasena());

            if (!contrasenaCoincide) {                                                   // sólo recodificar si la nueva contraseña no coincide con la existente
                usuarioExistente.setContrasena(passwordEncoder.encode(datosActualizados.getContrasena()));
            }
        }

        if (datosActualizados.getRolID() != null) {                         // actualizar el rol si `rolID` en el DTO no es nulo
            Rol rol = obtenerRolPorId(datosActualizados.getRolID());
            usuarioExistente.setRol(rol);
        }

        usuarioExistente.getFichaUsuario().setPassword(usuarioExistente.getContrasena());            // actualizar la ficha de usuario si `fichaUsuarioId` en el DTO no es nulo
        usuarioExistente.getFichaUsuario().setNickname(usuarioExistente.getNickname());

        return usuarioRepository.save(usuarioExistente);                                     // guardar y retorna el usuario actualizado
    }

    public Rol obtenerRolPorId(Integer rolId) throws ResourceNotFoundException {
        return rolRepository.findById(rolId)
                .orElseThrow(() -> new ResourceNotFoundException("Rol con ID " + rolId + " no encontrado"));
    }

    public void borrarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    public UsuarioDTO convertirADTO(Usuario usuario) {
        Integer rolID = usuario.getRol() != null ? usuario.getRol().getId() : null;
        String nombreRol = usuario.getRol() != null ? usuario.getRol().getNombreRol() : null;
        Long fichaUsuarioId = usuario.getFichaUsuario() != null ? usuario.getFichaUsuario().getId() : null;

        return new UsuarioDTO(
                usuario.getId(),
                usuario.getNickname(),
                usuario.getContrasena(),
                rolID,
                nombreRol,
                fichaUsuarioId,
                usuario.getFechaRegistro()
        );
    }
}