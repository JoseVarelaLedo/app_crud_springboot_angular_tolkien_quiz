package app.service;

import app.model.Rol;
import app.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolService {
    @Autowired
    private RolRepository rolRepository;

    public List<Rol> listarTodosLosRoles(){
        return rolRepository.findAll();
    }
}
